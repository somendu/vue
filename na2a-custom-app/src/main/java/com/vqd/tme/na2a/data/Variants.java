package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.model.Variant;

import java.util.List;

/**
 * Access to data for variants
 *
 * @author edgardegraaff
 *
 */
public interface Variants {
  /**
   * Returns all variants for a product
   *
   * @param productId
   * @return
   */
  List<Variant> getForProduct(String productId);

  /**
   * Creates a new variant for the given product. Will update variant.id
   *
   * @param productNumber
   * @param variant
   * @throws CouldNotSaveException
   */
  void create(String productNumber, Variant variant) throws CouldNotSaveException, SavedWithErrorsException;

  /**
   * Updates given variant
   *
   * @param variant
   * @throws CouldNotSaveException
   */
  void update(Variant variant) throws CouldNotSaveException, SavedWithErrorsException;
}
