package com.vqd.tme.na2a.partlinking.persistence;

import com.vqd.tme.na2a.partlinking.model.VariantMetaData;
import java.util.Collection;
import java.util.Map;

public interface VariantMetaDataResolver {
  Map<String, VariantMetaData> resolve(Collection<String> variantIds);
}
