package com.vqd.tme.na2a.controller;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.service.AccessoryTableService;
import com.vqd.tme.na2a.service.LocalizeAccessoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocalizeAccessoriesController {

  private final LocalizeAccessoriesService localizeAccessoriesService;
  private final AccessoryTableService accessoryTableService;

  @GetMapping("/api/localisation/{organisation}/projects/")
  public List<KeyValue> getLocalProjects(@PathVariable String organisation) {
    return localizeAccessoriesService.getKeyValues(organisation);
  }

  @GetMapping("/api/localisation/models")
  public List<KeyValue> getLocalModels(
          @RequestParam (name = "projectId") String projectId) {
    return localizeAccessoriesService.getKeyValues(projectId);
  }

  @GetMapping("/api/localisation/model")
  public LocalModel getLocalModel(
          @RequestParam (name= "modelId") String modelId) {
    return localizeAccessoriesService.getLocalModel(modelId);
  }

  @GetMapping("/api/localisation/vehicles")
  public ResponseVehicles getVehicles(@RequestParam(name = "modelId") String modelId) {
    return localizeAccessoriesService.getVehicles(modelId);
  }

  @GetMapping("/api/localisation/accessories")
  public ResponseVehicleAccessories getVehicleAccessories(
          @RequestParam(name = "brand") String brand,
          @RequestParam(name = "model") String model,
          @RequestParam(name = "tmeProject") String tmeProject) {

    return localizeAccessoriesService.getVehicleAccessories(brand, model, tmeProject);
  }

  @PostMapping("/api/localisation/update")
  public void updateAccessoryClassifications(@RequestBody() AccessoryUpdateRequest body) throws CouldNotSaveException {
    localizeAccessoriesService.updateAccessoryClassification(body);
  }

  @GetMapping("/api/localisation/tableinfo")
  public ResponseAccessoryTable getAccessoryTable() {
    return accessoryTableService.getAccessoryTable();
  }

  @GetMapping("/api/localisation/vehicleOptions")
  public List<KeyValue> getVehicleOptions(
          @RequestParam(name = "tmeProject") String tmeProject,
          @RequestParam(name = "optionType") String optionType) {
    return localizeAccessoriesService.getVehicleOptionsForProject(tmeProject,optionType);
  }
}
