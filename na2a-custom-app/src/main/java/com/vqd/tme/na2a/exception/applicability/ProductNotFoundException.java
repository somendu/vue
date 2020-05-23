package com.vqd.tme.na2a.exception.applicability;

public class ProductNotFoundException extends ApplicabilityException {
  private static final long serialVersionUID = 1L;

  public ProductNotFoundException(String productNumber) {
    super("Product not found: %s", productNumber);
  }

}
