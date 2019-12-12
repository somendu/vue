package com.vqd.tme.na2a.partlinking.controller;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.partlinking.model.PartLinkingVariant;
import com.vqd.tme.na2a.partlinking.model.PartLinkingVariantList;
import com.vqd.tme.na2a.partlinking.service.PartLinkingVariantService;
import com.vqd.tme.na2a.support.CastUtils;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partlinking")
public class VariantReferencesController {
  private final PartLinkingVariantService svc;

  @GetMapping("/variants")
  public List<PartLinkingVariant> getVariants(
      @RequestParam(name="ids") String _variantIds) {
    List<String> variantIds = CastUtils.parameterToList(_variantIds);
    return svc.get(variantIds);
  }

  @PutMapping("/variants/{variantId}")
  public void updateVariant(
          @PathVariable String variantId,
          @RequestBody PartLinkingVariant body) throws CouldNotSaveException {
    body.setId(variantId);
    svc.update(body);
  }

  @PutMapping("/variants")
  public void updateVariants(
          @RequestBody PartLinkingVariantList body) throws CouldNotSaveException {

    for (PartLinkingVariant variant : body) {
      svc.update(variant);
    }
  }

  @GetMapping("/products")
  public List<PartLinkingVariant> getVariantsByProduct (
          @RequestParam(name = "ids") String _productIds) {

    List<String> productIds = Arrays.asList(_productIds.split(","));
    return svc.getByProducts(productIds);
  }

  @DeleteMapping("/variants")
  public void deletePart(@RequestParam String variantId,
                         @RequestParam String partId,
                         @RequestParam String partType) throws CouldNotSaveException {

    svc.delete(variantId, partId, partType);
  }
}
