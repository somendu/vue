package com.vqd.tme.na2a.partlinking.persistence.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ProductRepository;
import com.vqd.tme.na2a.data.StructureGroupRepository;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.model.p360.P360EntryKey;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureGroup;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.GetResponse.Row;
import com.vqd.tme.na2a.partlinking.model.VariantMetaData;
import com.vqd.tme.na2a.partlinking.persistence.VariantMetaDataResolver;
import com.vqd.tme.na2a.service.StructureService;
import com.vqd.tme.na2a.service.VariantStructureGroupMapService;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class P360VariantMetaDataResolver implements VariantMetaDataResolver {
  private static final String FIELDS = String.join(",",
          "Variant.TMEGeneration", // model
          "VariantLang.TMEApplication(EN)", // name, but used as description atm?
          "VariantLang.TMEDescriptionShort(EN)",
          "Variant.CurrentStatus",
          "VariantLang.TMEInteriorColours(EN)",
          "VariantLang.TMETrimColours(EN)",
          "VariantLang.TMEExteriorColours(EN)",
          "Variant.TMEAccessoryMass",
          "Variant.TMEEquipmentSpecs",
          "Variant.VariantNo");

  private final StructureService structureService;
  private final VariantStructureGroupMapService structureGroupMapService;
  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;
  private final ProductRepository productRepository;
  private final StructureGroupRepository structureGroupRepository;

  @Override
  public Map<String, VariantMetaData> resolve(Collection<String> variantIds) {
    if (variantIds == null || variantIds.isEmpty()) {
      return Collections.emptyMap();
    }

    P360Structure productStructure = structureService.getProductStructure();
    List<P360StructureGroup> structureGroups = structureGroupRepository.findByStructure(productStructure);

    Map<String, VariantMetaData> result = new HashMap<>();

    String items = variantIds.stream()
      .map(id -> VariantUtils.formatId(id))
      .collect(Collectors.joining(","));

    log.debug("resolving variants {}", items);

    /*
     * get descriptions
     */
    GetResponse descRes = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/Variant/byItems"
        + "?items={items}"
        + "&fields={fields}",
        GetResponse.class,
        items, FIELDS);

    log.trace("descriptions raw response: {}", Json.of(descRes));

    for (Row row : descRes.getRows()) {
      String variantId = StringUtils.substringBeforeLast(row.getObject().getId(), "@");
      String variantStatus = VariantStatus.findByCode(CastUtils.asString(row.getValues().get(3))).get().getValue();
      Product product = productRepository.findByApplicationId(variantId);

      result
      .computeIfAbsent(variantId, id -> new VariantMetaData())
              .setVehicleGeneration(CastUtils.asString(row.getValues().get(0)))
              .setSummary(CastUtils.asString(row.getValues().get(1)))
              .setDescription(CastUtils.asString(row.getValues().get(2)))
              .setStatus(variantStatus)
              .setInteriorColours(String.join(", ", CastUtils.asList(row.getValues().get(4))))
              .setTrimColours(String.join(", ", CastUtils.asList(row.getValues().get(5))))
              .setExteriorColours(String.join(", ", CastUtils.asList(row.getValues().get(6))))
              .setAccessoryMaterial(Optional.ofNullable(product.getMaterial()).map(P360EntryKey::getLabel).orElse(""))
              .setAccessoryColour(Optional.ofNullable(product.getColour()).map(P360EntryKey::getLabel).orElse(""))
              .setAccessoryMass(CastUtils.asBigDecimal(row.getValues().get(7)))
              .setEquipmentSpecs(String.join(",", CastUtils.asList(row.getValues().get(8))))
              .setVariantNo(CastUtils.asString(row.getValues().get(9)));
    }

    /*
     * get classifications of variants
     */
    GetResponse clazzRes = structureGroupMapService.getStructureGroupMap(items);
    Set<String> structureGroupIds = structureGroupMapService.getStructureGroupIds(clazzRes);

    if (!structureGroupIds.isEmpty()) {
      /*
       * Fetch all available structures - we are only interested in the product structure for the commodity
       * resolve names of referenced structure groups
       */
      Set<String> productStructureGroupIds = structureGroupIds.stream()
              .filter(id -> productStructure != null && id.endsWith(productStructure.getObjectId()))
              .collect(Collectors.toSet());

      // It is possible that a product isn't classified to a product structure. In this case the call can't be made
      if(productStructureGroupIds != null && !productStructureGroupIds.isEmpty()) {
        GetResponse sgRes = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/StructureGroup/byItems"
                        + "?items={items}"
                        + "&fields=StructureGroupLang.Name(EN),StructureGroup.ParentIdentifier",
                GetResponse.class,
                String.join(",", productStructureGroupIds));

        log.trace("structure groups raw response: {}", Json.of(sgRes));

        Map<String, KeyValue> structureGroupNames = new HashMap<>();
        for (Row row : sgRes.getRows()) {

          KeyValue kv = new KeyValue();
          kv.setValue(CastUtils.asString(row.getValues().get(0)));
          kv.setParentKey(CastUtils.asString(row.getValues().get(1)));

          structureGroupNames.put(row.getObject().getId(), kv);
        }
        log.debug("resolved structure group names: {}", structureGroupNames);



        /*
         * Mixin result
         */
        for (Row row : clazzRes.getRows()) {
          if (!isQualificationForStructure(productStructure, row)) {
            continue;
          }

          String variantId = StringUtils.substringBeforeLast(row.getObject().getId(), "@");
          KeyValue kv = structureGroupNames.get(row.getQualification().getStructureGroupProxy().getId());
          String name = kv.getValue();
          String commodityParentIdentifier = kv.getParentKey();

          String accessoryCategory = getAccessoryProduct(structureGroups, commodityParentIdentifier);

          result
                  .computeIfAbsent(variantId, id -> new VariantMetaData())
                  .setCommodity(name)
                  .setAccessoryCategory(accessoryCategory);
        }
      } else {
        log.warn("No product classifications found on item {}", items);
      }
    }

    return result;
  }

  private boolean isQualificationForStructure(P360Structure productStructure, Row row) {
    return row.getQualification() != null &&
            row.getQualification().getStructureGroupProxy() != null &&
            (row.getQualification().getStructureProxy() != null && row.getQualification().getStructureProxy().getId().equalsIgnoreCase(productStructure.getObjectId()));
  }

  private String getAccessoryProduct(List<P360StructureGroup> structureGroups, String parentIdentifier) {

      // get fist parent
      P360StructureGroup parent = getParentByIdentifier(structureGroups, parentIdentifier);

      if (parent != null) {
        // get Accessory product
        P360StructureGroup accessoryProductStructure = getParentByIdentifier(structureGroups, parent.getParentIdentifier());
        return accessoryProductStructure.getName();
      }

    return "";
  }

  private P360StructureGroup getParentByIdentifier (List<P360StructureGroup> structureGroups, String parentIdentifier) {
    return structureGroups.stream()
            .filter(structureGroup -> structureGroup.getParentIdentifier() != null)
            .filter(structureGroup -> structureGroup.getIdentifier().equals(parentIdentifier))
            .findAny()
            .orElse(null);
  }


}

