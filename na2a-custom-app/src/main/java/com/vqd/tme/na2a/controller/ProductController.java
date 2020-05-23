package com.vqd.tme.na2a.controller;

import com.vqd.tme.na2a.exception.applicability.ApplicabilityException;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.model.GetProductsResult;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.model.ResponseVariant;
import com.vqd.tme.na2a.service.ProductService;
import com.vqd.tme.na2a.support.CastUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
  private final ProductService svc;

  @GetMapping("/api/products")
  public GetProductsResult getProducts(
      @RequestParam(name="productNumbers") String _productNumbers,
      @RequestParam(required = false) String localisation) throws ApplicabilityException {
    List<String> productNumbers = CastUtils.parameterToList(_productNumbers);
    return svc.get(productNumbers, localisation);
  }

  @PostMapping("/api/products")
  public ResponseVariant save(@RequestBody ResponseVariant variant) throws CouldNotSaveException, SavedWithErrorsException {
    return svc.save(variant);
  }

  @GetMapping("/api/products/variants")
  public GetProductsResult getProductsWithVariantIds(
          @RequestParam(name="variantIds") String _variantIds,
          @RequestParam(required = false) String localisation) throws ApplicabilityException {
    List<String> variantIds = Arrays.asList(_variantIds.split(","));

    GetProductsResult result = svc.getByVariantIds(variantIds, localisation);

    // filter out variant responses based on the variant IDs
    Product product = result.getDetails().iterator().next();
    result.getEntries().removeIf(application -> !variantIds.contains(application.getVariantIds().get(product.getId())));

    return result;
  }
}
