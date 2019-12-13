package com.vqd.tme.na2a.service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToKeyValueAdapter;
import com.vqd.tme.na2a.data.ApplicationAPMRepository;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.data.CommodityRepository;
import com.vqd.tme.na2a.data.ProductRepository;
import com.vqd.tme.na2a.data.impl.StructureClassificationResolver;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.model.p360.*;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalizeAccessoriesService {

  private final ApplicationRepository applicationRepository;
  private final ProductRepository productRepository;
  private final StructureClassificationResolver classificationResolver;

  private final VehicleStructureService vehicleStructureService;
  private final CommodityRepository commodityRepository;
  private final ApplicationAPMRepository applicationAPMRepository;

  private final StructureService structureService;

  private final RowToKeyValueAdapter keyValueAdapter;

  public List<KeyValue> getKeyValues(String parentIdentifier) {
    List<KeyValue> result = Lists.newArrayList();

    GetResponse brandRes = structureService.getByParentIdentifier(parentIdentifier, P360StructureType.LOCAL_VEHICLE_STRUCTURE);

    brandRes.getRows().forEach(brandRow -> {
      KeyValue brand = keyValueAdapter.convert(brandRow);

      GetResponse localProjectRes = structureService.getByParentIdentifier(brand.getKey(), P360StructureType.LOCAL_VEHICLE_STRUCTURE);

      localProjectRes.getRows().forEach(row -> result.add(keyValueAdapter.convert(row)));

    });

    result.sort(Comparator.comparing(KeyValue::getValue));
    return result;
  }

  public LocalModel getLocalModel(String modelId) {
    GetResponse modelResponse = structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE);

    LocalModel model = new LocalModel();

    if(modelResponse.getRowCount() == 1) {
      KeyValue modelData = keyValueAdapter.convert(modelResponse.getRows().get(0));
      model.setModel(modelData);
      GetResponse configResponse = structureService.getByIdentifier(modelData.getParentKey(), P360StructureType.LOCAL_VEHICLE_STRUCTURE);
      if(configResponse.getRowCount() == 1) {
        KeyValue configData = keyValueAdapter.convert(configResponse.getRows().get(0));
        GetResponse projectResponse = structureService.getByIdentifier(configData.getParentKey(), P360StructureType.LOCAL_VEHICLE_STRUCTURE);
        if(projectResponse.getRowCount() == 1) {
          model.setProject(keyValueAdapter.convert(projectResponse.getRows().get(0)));
        }
      }
    }

    return model;
  }

  public ResponseVehicles getVehicles(String modelId) {
    ResponseVehicles vehicles = ResponseVehicles.builder().build();

    GetResponse modelResponse = structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE);
    if (modelResponse.getRowCount() == 1) {
      KeyValue model = keyValueAdapter.convert(modelResponse.getRows().get(0));

      GetResponse gradeResponse = structureService.getByParentIdentifier(model.getKey(), P360StructureType.LOCAL_VEHICLE_STRUCTURE);
      gradeResponse.getRows().forEach(row -> {
        KeyValue grade = keyValueAdapter.convert(row);
        vehicles.getVehicles().addAll(getVehicleCodes(grade));
      });
    }
    return vehicles;
  }

  private List<ResponseVehicle> getVehicleCodes(KeyValue grade) {
    List<ResponseVehicle> vehicles = Lists.newArrayList();
    GetResponse codeResponse = structureService.getByParentIdentifier(grade.getKey(), P360StructureType.LOCAL_VEHICLE_STRUCTURE);

    codeResponse.getRows().forEach(codeRow -> {
      KeyValue code = keyValueAdapter.convert(codeRow);
      vehicles.add(
              ResponseVehicle.builder()
                              .localGrade(grade.getValue())
                              .localCodeIdentifier(code.getKey())
                              .localCode(code.getValue())
                              .build());
    });
    return vehicles;
  }

  public ResponseVehicleAccessories getVehicleAccessories(String brand, String model, String tmeProject) {
    ResponseVehicleAccessories accessories = ResponseVehicleAccessories.builder().build();

    // First find the TME model in the vehicle structure based on the tmeProjectIdentifier
    KeyValue generalProject = vehicleStructureService.getByLocalProject(tmeProject);

    // With brand, brand and model, find the variants!!!
    List<P360Application> p360Applications = applicationRepository.findByBrandAndModelAndProject(brand, model, generalProject.getValue());

    p360Applications.forEach(p360Application -> {
      P360ApplicationAPM applicationAPM = applicationAPMRepository.findById(p360Application.getId());
      List<P360Classification> localVehicleClassifications = classificationResolver.resolve(p360Application.getId(), structureService.getLocalVehicleStructure());
      Product product = productRepository.findByApplicationId(p360Application.getId());
      if(product != null) {
        String commodity = Optional.ofNullable(commodityRepository.findByApplicationId(p360Application.getId())).orElse("");
        accessories.getAccessories().add(ResponseVehicleAccessory.builder()
                .applicationId(p360Application.getId())
                .applicationName(p360Application.getName())
                .interiorColourIds(p360Application.getInteriorColourIds())
                .exteriorColourIds(p360Application.getExteriorColourIds())
                .trimColourIds(p360Application.getTrimColourIds())
                .equipmentIds(p360Application.getExteriorColourIds())
                .classifications(localVehicleClassifications)
                .productId(product.getId())
                .productName(Optional.ofNullable(product.getShortDescription()).orElse(""))
                .commodity(commodity)
                .productColour(Optional.ofNullable(product.getColour()).map(P360EntryKey::getLabel).orElse(""))
                .productMaterial(Optional.ofNullable(product.getMaterial()).map(P360EntryKey::getLabel).orElse(""))
                .isStdFitPPO(product.getIsStdFitPPO() ? "Yes": "No")
                .isInPPO(p360Application.getIsInPPO() ? "Yes" : "No")
                .tmeStandard(Optional.ofNullable(applicationAPM).map(P360ApplicationAPM::getTmeStandard).orElse(null))
                .tmePackOffer(Optional.ofNullable(applicationAPM).map(P360ApplicationAPM::getTmePackOffer).orElse(null))
                .tmeDemo(Optional.ofNullable(applicationAPM).map(P360ApplicationAPM::getTmeDemo).orElse(null))
                .build());
      } else {
        log.warn(String.format("Vehicle accessories - skipped processing application with id %s. The application has no product linked to it.", p360Application.getId()));
      }
    });
    return accessories;
  }

  public void updateAccessoryClassification(AccessoryUpdateRequest updateRequest) throws CouldNotSaveException {
    List<P360Classification> vehicleClassifications = Lists.newArrayList();
    updateRequest.getVehicles().forEach(vehicle -> vehicleClassifications.add(P360Classification.builder()
            .identifier(vehicle.getLocalCodeIdentifier())
            .label(vehicle.getLocalCodeLabel())
            .build()));

    List<P360Classification> applicationClassifications = updateRequest.getAccessory().getClassifications();

    if (updateRequest.getAccessory().getAdd()) {
      vehicleClassifications.forEach(classification -> {
          if(!applicationClassifications.contains(classification)){
              applicationClassifications.add(classification);
          }
      });
    } else {
      vehicleClassifications.forEach(classification -> {
          applicationClassifications.removeIf(c -> c.equals(classification));
      });
    }

    applicationRepository.updateClassifications(updateRequest.getAccessory().getId(), structureService.getLocalVehicleStructure(), applicationClassifications);
  }

  public List<KeyValue> getVehicleOptionsForProject(String tmeProject, String node) {
    KeyValue generalProject = vehicleStructureService.getByLocalProject(tmeProject);

    GetResponse optionsResponse = structureService.getByParentIdentifier(generalProject.getKey(), P360StructureType.VEHICLE_STRUCTURE);

    KeyValue bodies = optionsResponse.getRows().stream()
            .map(row -> keyValueAdapter.convert(row))
            .collect(Collectors.toList())
            .stream()
            .filter(keyValue -> keyValue.getValue().equalsIgnoreCase(node))
            .findFirst()
            .orElse(null);

    if(bodies != null) {
      GetResponse bodyResponse = structureService.getByParentIdentifier(bodies.getKey(), P360StructureType.VEHICLE_STRUCTURE);

      return bodyResponse.getRows().stream()
              .map(row -> keyValueAdapter.convert(row))
              .collect(Collectors.toList());
    }

    return Lists.newArrayList();
  }
}
