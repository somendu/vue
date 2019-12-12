package com.vqd.tme.na2a.service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.CountryEntryToKeyValueAdapter;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.data.Products;
import com.vqd.tme.na2a.data.Projects;
import com.vqd.tme.na2a.data.Variants;
import com.vqd.tme.na2a.exception.applicability.ApplicabilityException;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.ProductNotFoundException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.function.TriFunction;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.model.Variant.Options;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
  private final Products products;
  private final Variants variants;
  private final Projects projects;
  private final RefTableService refTables;
  private final LookupService lookups;
  private final EnumLookupService enumService;
  private final CountryEntryToKeyValueAdapter countryEntryToKeyValueAdapter;

  private final StructureService structureService;
  private final ApplicationRepository applicationRepository;

  /**
   * Get variants of given products (by ID), merging similar variants (for mass edit)
   *
   * @param productNumbers
   * @return
   * @throws ApplicabilityException
   */
  public GetProductsResult get(List<String> productNumbers, String organisation) throws ApplicabilityException {
    GetProductsResult result = new GetProductsResult();
    result.setDetails(new HashSet<>(productNumbers.size()));

    if (organisation != null) {
      result.setLocalProjects(new ArrayList<>());
    }

    /*
     * To combine variants having the same options selected, make a map
     * where key = what-is-selected, value = variants
     */
    Map<Variant.Options, List<Variant>> allEntries = new LinkedHashMap<>();
    for (String productNumber : productNumbers) {
      // add details header
      Product product = products.get(productNumber);
      if (product == null) {
        throw new ProductNotFoundException(productNumber);
      }

      result.getDetails().add(product);

      // get variants
      List<Variant> productVariants = variants.getForProduct(product.getId());

      for (Variant variant : productVariants) {
        if (variant.getOptions().getOrganisation() == null) {
          variant.setProduct(product);
          // merge
          allEntries.computeIfAbsent(
                  variant.getOptions(),
                  key -> new ArrayList<>(productNumbers.size()))
                  .add(variant);
        }
      }

      // TODO this should move to ProjectService
      if (organisation != null) {
        Set<String> projectNames = allEntries
            .keySet()
            .stream()
//            .map(Variant.Options::getModel)
            .map(Variant.Options::getGeneration)
            .filter(p -> p != null)
            .collect(Collectors.toSet());
        log.debug("occurring model names: {}", projectNames);

        List<KeyValue> localProjectNames = projects.get(projectNames, organisation);
        List<ResponseLocalProject> localProjects = new ArrayList<>(localProjectNames.size());

        for (KeyValue localProjectName : localProjectNames) {
          ResponseLocalProject localProject = new ResponseLocalProject()
              .setProject(localProjectName.getKey())
              .setProjectName(localProjectName.getValue());

          Variant localProjectVariant = null;

          for (Variant variant : productVariants) {
//            if (localProjectName.getValue().equals(variant.getOptions().getModel()) && organisation.equals(variant.getOptions().getOrganisation())) {
            if (localProjectName.getValue().equals(variant.getOptions().getGeneration()) && organisation.equals(variant.getOptions().getOrganisation())) {
              localProjectVariant = variant;
              break;
            }
          }

          if (localProjectVariant != null) {
            localProject.setVariantId(localProjectVariant.getId());
            localProject.setLocalProject(localProjectVariant.getOptions().getLocalProject());
            localProject.setLocalModel(localProjectVariant.getOptions().getLocalModel());
            localProject.setLocalGrade(localProjectVariant.getOptions().getLocalGrade());
            localProject.setLocalModelCode(localProjectVariant.getOptions().getLocalModelCode());
          } else {
            localProject.setLocalProject(Collections.emptyList());
            localProject.setLocalModel(Collections.emptyList());
            localProject.setLocalGrade(Collections.emptyList());
            localProject.setLocalModelCode(Collections.emptyList());
          }

          localProjects.add(localProject);
        }

        result.setLocalProjects(localProjects );
      }
    }

    result.setEntries(new ArrayList<>());
    for (Map.Entry<Variant.Options, List<Variant>> entry : allEntries.entrySet()) {
      ResponseVariant resVariant = new ResponseVariant()
          .setVariantIds(new HashMap<>())
          .setVariantNos(new HashMap<>());

      for (Variant variant : entry.getValue()) {
        resVariant.getVariantIds().put(variant.getProduct().getId(), variant.getId());
        resVariant.getVariantNos().put(variant.getProduct().getId(), variant.getVariantNo());

        // should be the same description for every variant, since options are similar
        resVariant.setShortDescription(variant.getShortDescription());
      }

      // resolve selected options
      resolve(entry.getKey(), resVariant);

      result.getEntries().add(resVariant);
    }

    return result;
  }

  public GetProductsResult getByVariantIds(List<String> variantIds, String localisation) throws ApplicabilityException {
    List<String> productIds = new ArrayList<>();

    for (String variantId : variantIds) {
      Product product = products.getForVariant(variantId);
      if (product != null) {
        String id = product.getId().substring(0, product.getId().lastIndexOf("@1"));
        productIds.add(id);
      } else {
        log.debug(String.format("No product found for variant ID '%s'", variantId));
      }
    }

    log.debug("Amount of product Ids: {}", productIds.size());

    return get(productIds, localisation);
  }

  /**
   * Resolves stored option keys into key / value (/category) objects
   *
   * @param source
   * @param target
   */
  public void resolve(Variant.Options source, ResponseVariant target) {
    // If a variant is retrieved that has no current status, set it to active
    target.setCancelled(VariantStatus.findByCode(source.getStatusCode()).map(status -> status.isCancelled()).orElse(false));
    target.setCancelledReason(source.getCancelledReason());

    target.setModel(resolveModel(source));

    Collection<TreeNode> vehicleOptions = null;
    Collection<TreeNode> equipmentOptions = null;

    if (target.getModel().getBrand() != null &&
        target.getModel().getModel() != null &&
        target.getModel().getProject() != null) {
      String brand = target.getModel().getBrand();
      String model = target.getModel().getModel().getKey();
      String project = target.getModel().getProject().getKey();

      vehicleOptions = refTables.getVehicleOptions(brand, model, project);
      equipmentOptions = refTables.getEquipmentOptions(brand, model, project);
    }

    List<KeyValueCategory> vehicle = new ArrayList<>();
    appendCategorizedOptions(source.getSubProjectIds(), vehicle, vehicleOptions);
    appendCategorizedOptions(source.getBodyTypeIds(), vehicle, vehicleOptions);
    appendCategorizedOptions(source.getEngineIds(), vehicle, vehicleOptions);
    appendCategorizedOptions(source.getTransmissionIds(), vehicle, vehicleOptions);
    appendCategorizedOptions(source.getProductionGradeIds(), vehicle, vehicleOptions);
    appendCategorizedOptions(source.getProductionLocationIds(), vehicle, vehicleOptions);
    appendCategorizedOption(source.getSteeringId(), vehicle, vehicleOptions);
    target.setVehicle(vehicle);

    List<KeyValueCategory> equipment = new ArrayList<>();
    appendCategorizedOptions(source.getFactoryEquipmentIds(), equipment, equipmentOptions);
    target.setEquipment(equipment);

    target.setExteriorColours(resolveOptions(source.getExteriorColourIds(), target.getModel(), refTables::listExteriorColours, lookups::getExteriorColours));
    target.setInteriorColours(resolveOptions(source.getInteriorColourIds(), target.getModel(), refTables::listInteriorColours, lookups::getInteriorColours));
    target.setTrimColours(resolveOptions(source.getTrimColourIds(), target.getModel(), refTables::listTrimColours, lookups::getTrimColours));

    List<KeyValue> countries = Lists.newArrayList();

    P360Enum countriesEnum = enumService.getTMECountryEnum();
    source.getCountries().forEach(countryCode ->
            countriesEnum.getEntries()
                    .stream()
                    .filter(entry -> entry.getKey().equals(countryCode))
                    .findFirst()
                    .map(e -> countryEntryToKeyValueAdapter.convert(e))
                    .map(keyValue -> countries.add(keyValue))
                    .orElse(false));


    target.setCountries(countries);
  }

  private ResponseModel resolveModel(Variant.Options source) {
    ResponseModel target = new ResponseModel();

    if (source != null) {
      if (source.getBrand() != null) {
        target.setBrand(source.getBrand());

        // brand.brand
        if (source.getModel() != null) {
          for (TreeNode model : refTables.listModels(source.getBrand())) {
            if (source.getModel().equals(model.getValue())) {
              target.setModel(new KeyValue(
                  model.getKey(),
                  model.getValue(),
                  model.getKatashiki(),
                      null));

              // brand.model
//              if (source.getModel() != null) {
              if (source.getGeneration() != null) {
                for (TreeNode variant : refTables.listProjects(source.getBrand(), model.getKey())) {
//                  if (source.getModel().equals(variant.getValue())) {
                  if (source.getGeneration().equals(variant.getValue())) {
                    target.setProject(new KeyValue(
                        variant.getKey(),
                        variant.getValue(),
                        variant.getKatashiki(),
                            null));

                    break;
                  }
                }
              }

              break;
            }
          }
        }
      }
    }

    if (target.getBrand() == null || target.getModel() == null || target.getProject() == null) {
      target.setAll(true);
    }

    return target;
  }

  private List<KeyValue> resolveOptions(
      List<String> selection, ResponseModel options,
      TriFunction<String, String, String, Collection<TreeNode>> treeSupplier,
      Supplier<Collection<KeyValue>> allOptionsSupplier) {
    List<KeyValue> result;

    if (selection == null || selection.isEmpty()) {
      result = Collections.emptyList();
    } else {
      result = new ArrayList<>(selection.size());

      String model = options.getModel() != null ? options.getModel().getKey() : null;
      String project = options.getProject() != null ? options.getProject().getKey() : null;
      if (options.getBrand() != null && model != null && project != null) {
        Collection<TreeNode> nodes = treeSupplier.apply(options.getBrand(), model, project);

        for (String value : selection) {

          for (TreeNode node : nodes) {
            if (node.getKey().equals(value)) {
              log.debug("resolved {} to {} ({})", value, node.getKey(), node.getValue());
              result.add(new KeyValue(node.getKey(), node.getValue(), node.getKatashiki(), null));
              break;
            }
          }
        }
      } else {
        Collection<KeyValue> allOptions = allOptionsSupplier.get();

        for (String value : selection) {
          for (KeyValue option : allOptions) {
            if (option.getKey().equals(value)) {
              log.debug("resolved {} to {}", value, option.getKey());
              result.add(option);
              break;
            }
          }
        }
      }
    }

    return result;
  }

  private void appendCategorizedOptions(
      List<String> values, List<KeyValueCategory> target,
      Collection<TreeNode> tree) {
    for (String value : values) {
      appendCategorizedOption(value, target, tree);
    }
  }

  private void appendCategorizedOption(String value, List<KeyValueCategory> target, Collection<TreeNode> tree) {

    if (value != null && tree != null) {
      KeyValueCategory kvc = null;

      for (TreeNode category : tree) {
        if (category.getChildren() != null) {
          for (TreeNode node : category.getChildren()) {
            if (node.getKey().equals(value)) {
              log.debug("resolved {} to {}/{} ({})", value, category.getValue(), node.getKey(), node.getValue());
              kvc = new KeyValueCategory(
                  node.getKey(),
                  node.getValue(),
                  category.getValue(),
                  node.getKatashiki());
              break;
            }
          }
        }

        if (kvc != null) {
          break;
        }
      }

      if (kvc == null) {
        kvc = new KeyValueCategory(
            value,
            value,
            "[unresolved category]",
            null);
      }

      target.add(kvc);
    }
  }

  public ResponseVariant save(ResponseVariant value) throws CouldNotSaveException, SavedWithErrorsException {
    Variant variant = toVariant(value);

    log.trace("save variant: {}", variant);

    for (Entry<String, String> entry : value.getVariantIds().entrySet()) {
      String productId = entry.getKey();
      Product product = null;
      String variantId = entry.getValue();

      if ("-1".equals(variantId)) {
        if (product == null) {
          product = products.get(productId);
        }
        variants.create(product.getProductNumber(), variant);
        log.debug("create variant {} for product {}", variant.getId(), productId);
        entry.setValue(variant.getId());

        classifyApplication(value, variant);

      } else if (variantId != null) {
        log.debug("update variant {}", variantId);
        variant.setId(variantId);
        variants.update(variant);
      }
    }

    return value;
  }

  private void classifyApplication(ResponseVariant value, Variant variant) throws CouldNotSaveException {
    if(value.getModel() != null && value.getModel().getProject() != null && StringUtils.isNotBlank(value.getModel().getProject().getKey())) {
      P360Classification classification = P360Classification.builder()
              .identifier(value.getModel().getProject().getKey())
              .label(value.getModel().getProject().getValue())
              .build();
      applicationRepository.updateClassifications(variant.getId(), structureService.getGenerationStructure(), Lists.newArrayList(classification));
      log.debug("classified variant {} to vehicle structure node {}", variant, classification.getLabel());
    } else {
      log.warn("Could not classify the variant {} to the vehicle structure due to unknown brand gen");
    }
  }

  private Variant toVariant(ResponseVariant value) {
    Variant variant = new Variant();

    variant.setShortDescription(VariantUtils.toString(value));
    variant.setKatashiki(collectAllKatashiki(value.getVehicle()));
    variant.setOptions(toVariantOptions(value));

    return variant;
  }

  private List<String> collectAllKatashiki(List<KeyValueCategory> options) {
    Set<String> values = null;

    if (options != null) {
      for (KeyValueCategory option : options) {
        if (option.getKatashiki() != null && !option.getKatashiki().isEmpty()) {
          if (values == null) {
            values = new HashSet<>();
          }
          values.addAll(option.getKatashiki());
        }
      }
    }

    return values != null ? new ArrayList<>(values) : null;
  }

  @AllArgsConstructor
  private static class VehicleOptionsMapping {
    /**
     * Consumer for adding a vehicle option label
     */
    final BiConsumer<Variant.Options, String> labels;
    /**
     * Consumer for adding a vehicle option id
     */
    final BiConsumer<Variant.Options, String> ids;
  }

  /**
   * Mapping indicating which kind of vehicle option should go where
   */
  private final static Map<String, VehicleOptionsMapping> VEHICLE_OPTIONS_MAPPING;

  static {
    VEHICLE_OPTIONS_MAPPING = new HashMap<>();

    VEHICLE_OPTIONS_MAPPING.put("Subprojects", new VehicleOptionsMapping(
        add(Variant.Options::getSubProjects, Variant.Options::setSubProjects),
        add(Variant.Options::getSubProjectIds, Variant.Options::setSubProjectIds)));
    VEHICLE_OPTIONS_MAPPING.put("Bodies", new VehicleOptionsMapping(
        add(Variant.Options::getBodyTypes, Variant.Options::setBodyTypes),
        add(Variant.Options::getBodyTypeIds, Variant.Options::setBodyTypeIds)));
    VEHICLE_OPTIONS_MAPPING.put("Engines", new VehicleOptionsMapping(
        add(Variant.Options::getEngines, Variant.Options::setEngines),
        add(Variant.Options::getEngineIds, Variant.Options::setEngineIds)));
    VEHICLE_OPTIONS_MAPPING.put("Transmissions", new VehicleOptionsMapping(
        add(Variant.Options::getTransmissions, Variant.Options::setTransmissions),
        add(Variant.Options::getTransmissionIds, Variant.Options::setTransmissionIds)));
    VEHICLE_OPTIONS_MAPPING.put("ProductionGrades", new VehicleOptionsMapping(
        add(Variant.Options::getProductionGrades, Variant.Options::setProductionGrades),
        add(Variant.Options::getProductionGradeIds, Variant.Options::setProductionGradeIds)));
    VEHICLE_OPTIONS_MAPPING.put("ProductionLocations", new VehicleOptionsMapping(
        add(Variant.Options::getProductionLocations, Variant.Options::setProductionLocations),
        add(Variant.Options::getProductionLocationIds, Variant.Options::setProductionLocationIds)));
    VEHICLE_OPTIONS_MAPPING.put("SteeringLevel", new VehicleOptionsMapping(
        Variant.Options::setSteering,
        Variant.Options::setSteeringId));
  }

  /**
   * Returns a add-to-list method that creates a new list in case of absence
   * (so that the caller can just use it without having to worry about the target collection
   * being null).
   *
   * @param getter is the method that provides the current list
   * @param setter is the method to set the current list
   * @return
   */
  private static <T, E> BiConsumer<T, E> add(Function<T, List<E>> getter, BiConsumer<T, List<E>> setter) {
    return (t, e) -> {
      List<E> list = getter.apply(t);
      if (list == null) {
        list = new ArrayList<>();
        setter.accept(t, list);
      }
      list.add(e);
    };
  }

  private Variant.Options toVariantOptions(ResponseVariant value) {
    Variant.Options options = new Variant.Options();

    options.setStatusCode(value.isCancelled() ? VariantStatus.WITHDRAWN.getCode() : VariantStatus.CREATED.getCode());
    options.setCancelledReason(value.getCancelledReason());

    ResponseModel model = value.getModel();
    if (model != null && !model.isAll()) {
      options.setBrand(model.getBrand());
      options.setModel(valueOf(model.getModel()));
      options.setGeneration(valueOf(model.getProject()));

      addVehicleOptions(value, options);
      addEquipmentOptions(value, options);
      addProjectSpecificColours(value, options);
      addCountries(value, options);
    } else {
      addColoursOfAllModels(value, options);
    }

    return options;
  }

  private void addCountries(ResponseVariant variant, Options options) {
    if(variant.getCountries() == null || variant.getCountries().isEmpty()) {
      return;
    }

    P360Enum countryEnum = enumService.getTMECountryEnum();

    variant.getCountries().forEach(country -> {
      if (countryEnum.getEntries().stream().anyMatch(entry -> entry.getKey().equals(country.getKey()))) {
        options.addCountry(country.getKey());
      }
    });
  }

  /**
   * Add vehicle option ids and labels, by matching their labels to all available options
   *
   * @param source
   * @param target
   */
  private void addVehicleOptions(ResponseVariant source, Options target) {
    if (source.getVehicle() == null || source.getVehicle().isEmpty()) {
      return;
    }

    String brand = target.getBrand();
    String model = source.getModel().getModel().getKey();
    String project = source.getModel().getProject().getKey();

    Set<String> selected = setOfKeysOf(source.getVehicle());
    Collection<TreeNode> vehicleOptions = refTables.getVehicleOptions(brand, model, project);

    // find each selected option
    for (TreeNode category : vehicleOptions) {
      String categoryKey = null;
      for (TreeNode node : category.getChildren()) {
        if (selected.contains(node.getKey())) {
          if (categoryKey == null) {
            categoryKey = StringUtils.substringAfter(category.getKey(), "|");
          }

          VehicleOptionsMapping consumer = VEHICLE_OPTIONS_MAPPING.get(categoryKey);
          if (consumer != null) {
            log.trace("matched category {} for option {}", categoryKey, node.getKey());
            consumer.ids.accept(target, node.getKey());
            consumer.labels.accept(target, node.getValue());
          } else {
            log.warn("unmapped category: {} for option {}", categoryKey, node.getKey());
          }
        }
      }
    }
  }

  /**
   * Add equipment option ids and labels, by matching their labels to all available options
   * @param source
   * @param target
   */
  private void addEquipmentOptions(ResponseVariant source, Options target) {
    if (source.getEquipment() == null || source.getEquipment().isEmpty()) {
      return;
    }

    Set<String> selected = setOfKeysOf(source.getEquipment());

    String brand = target.getBrand();
    String model = source.getModel().getModel().getKey();
    String project = source.getModel().getProject().getKey();

    Collection<TreeNode> equipmentOptions = refTables.getEquipmentOptions(brand, model, project);
    for (TreeNode category : equipmentOptions) {
      for (TreeNode node : category.getChildren()) {
        // match options by label
        if (selected.contains(node.getKey())) {
          String parent = Optional.ofNullable(category.getValue()).map(String::toUpperCase).orElse("UNRESOLVED PARENT");
          target.addFactoryEquipment(node.getKey(), String.format("%s: %s", parent, node.getValue()));
        }
      }
    }
  }

  /**
   * Handle selected colours where options are model specific
   * (key / value pairs coming from structure group)
   *
   * @param source
   * @param target
   */
  private void addProjectSpecificColours(ResponseVariant source, Options target) {
    Set<String> selected = colourKeysOf(source);

    if (selected.isEmpty()) {
      return;
    }

    String brand = target.getBrand();
    String model = source.getModel().getModel().getKey();
    String project = source.getModel().getProject().getKey();

    addColoursFromTreeNodes(target, selected, refTables.listExteriorColours(brand, model, project), "exterior");
    addColoursFromTreeNodes(target, selected, refTables.listInteriorColours(brand, model, project), "interior");
    addColoursFromTreeNodes(target, selected, refTables.listTrimColours(brand, model, project), "trim");
  }

  private void addColoursFromTreeNodes(Options target, Set<String> selected, Collection<TreeNode> availableColours, String type) {
    for (TreeNode colour : availableColours) {
      if (selected.contains(colour.getKey())) {
        target.addColour(colour.getKey(), colour.getValue(), type);
      }
    }
  }

  /**
   * Handle selected colours for where 'all models' is selected
   * (key / value pairs coming from lookup tables)
   *
   * @param source
   * @param target
   */
  private void addColoursOfAllModels(ResponseVariant source, Options target) {
    Set<String> selected = colourKeysOf(source);

    if (selected.isEmpty()) {
      return;
    }

    addColoursFromKeyValues(target, selected, lookups.getExteriorColours(), "exterior");
    addColoursFromKeyValues(target, selected, lookups.getInteriorColours(), "interior");
    addColoursFromKeyValues(target, selected, lookups.getTrimColours(), "trim");
  }

  private void addColoursFromKeyValues(Options target, Set<String> selected, List<KeyValue> availableColours, String type) {
    for (KeyValue colour : availableColours) {
      if (selected.contains(colour.getKey())) {
        target.addColour(colour.getKey(), colour.getValue(), type);
      }
    }
  }

  /**
   * Combines exterior, interior and trim colour keys
   *
   * @param value
   * @return
   */
  private static Set<String> colourKeysOf(ResponseVariant value) {
    Set<String> result = new HashSet<>();

    addKeysOf(value.getExteriorColours(), result);
    addKeysOf(value.getInteriorColours(), result);
    addKeysOf(value.getTrimColours(), result);

    return result;
  }

  private static void addKeysOf(List<KeyValue> values, Collection<String> target) {
    if (values == null) {
      return;
    }

    for (KeyValue value : values) {
      if (value != null) {
        String key = value.getKey();
        if (key != null) {
          target.add(key);
        }
      }
    }
  }

  private static String valueOf(KeyValue kv) {
    return kv != null ? kv.getValue() : null;
  }

  private static Set<String> setOfKeysOf(List<KeyValueCategory> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptySet();
    }

    Set<String> result = new HashSet<>();

    for (KeyValueCategory value : values) {
      if (value != null) {
        String key = value.getKey();
        if (key != null) {
          result.add(key);
        }
      }
    }

    return result;
  }
}
