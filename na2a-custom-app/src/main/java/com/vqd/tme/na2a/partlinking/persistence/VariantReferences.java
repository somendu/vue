package com.vqd.tme.na2a.partlinking.persistence;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.partlinking.model.VariantReference;
import java.util.List;

public interface VariantReferences {
  List<VariantReference> list(String variantId);
  void update(String variantId, List<VariantReference> references) throws CouldNotSaveException;
}
