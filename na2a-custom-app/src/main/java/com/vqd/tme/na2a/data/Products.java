package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.Product;

/**
 * Access to data of products
 *
 * @author edgardegraaff
 *
 */
public interface Products {
  /**
   * Gets product by ID
   *
   * @param productId
   * @return
   */
  Product get(String productId);

  /**
   * Gets parent product of the given variant
   * @param variantId the ID of the variant
   * @return the parent product
   */
  Product getForVariant(String variantId);
}
