package com.vqd.tme.na2a.controller;

import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.TreeNode;
import com.vqd.tme.na2a.service.LookupService;
import com.vqd.tme.na2a.service.RefTableService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefTableController {
  private final RefTableService svc;
  private final LookupService lkps;

  @GetMapping("/api/brands")
  public Collection<TreeNode> listBrands() {
    return svc.listBrands();
  }

  @GetMapping("/api/brands/{brand}/models")
  public Collection<TreeNode> listModels(
      @PathVariable String brand) {
    return svc.listModels(brand);
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects")
  public Collection<TreeNode> listProjects(
      @PathVariable String brand,
      @PathVariable String model) {
    return svc.listProjects(brand, model);
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects/{project}/vehicle-options")
  public Collection<TreeNode> getVehicleOptions(
      @PathVariable String brand,
      @PathVariable String model,
      @PathVariable String project) {
    return valuesAsKeys(svc.getVehicleOptions(brand, model, project));
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects/{project}/equipment-options")
  public Collection<TreeNode> getEquipmentOptions(
      @PathVariable String brand,
      @PathVariable String model,
      @PathVariable String project) {
    return valuesAsKeys(svc.getEquipmentOptions(brand, model, project));
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects/{project}/interior-colours")
  public Collection<TreeNode> listInteriorColours(
      @PathVariable String brand,
      @PathVariable String model,
      @PathVariable String project) {
    return valuesAsKeys(svc.listInteriorColours(brand, model, project));
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects/{project}/exterior-colours")
  public Collection<TreeNode> listExteriorColours(
      @PathVariable String brand,
      @PathVariable String model,
      @PathVariable String project) {
    return valuesAsKeys(svc.listExteriorColours(brand, model, project));
  }

  @GetMapping("/api/brands/{brand}/models/{model}/projects/{project}/trim-colours")
  public Collection<TreeNode> listTrimColours(
      @PathVariable String brand,
      @PathVariable String model,
      @PathVariable String project) {
    return valuesAsKeys(svc.listTrimColours(brand, model, project));
  }

  @GetMapping("/api/all/interior-colours")
  public List<KeyValue> listAllInteriorColours() {
    return lkps.getInteriorColours();
  }

  @GetMapping("/api/all/exterior-colours")
  public List<KeyValue> listAllExteriorColours() {
    return lkps.getExteriorColours();
  }

  @GetMapping("/api/all/trim-colours")
  public List<KeyValue> listAllTrimColours() {
    return lkps.getTrimColours();
  }

  @GetMapping("/api/cancelled-reasons")
  public List<KeyValue> getCancelledReasons() {
    return lkps.getCancelledReasons();
  }

  @GetMapping("/api/all/homologation-types")
  public List<KeyValue> getHomologatinoTypes () {
    return lkps.getHomologationTypes();
  }
  /**
   * Makes a deep copy in which the deepest level gets values set as keys
   *
   * @param tree
   * @return
   */
  private static Collection<TreeNode> valuesAsKeys(Collection<TreeNode> tree) {
    List<TreeNode> copy = new ArrayList<>(tree.size());

    for (TreeNode node : tree) {
      TreeNode n = new TreeNode(node);

      if (n.getChildren() != null) {
        n.setChildren(valuesAsKeys(n.getChildren()));
      } else {
        n.setKey(n.getKey());
      }
      copy.add(n);
    }

    return copy;
  }
}
