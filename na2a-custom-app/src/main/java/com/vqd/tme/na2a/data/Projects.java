package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.KeyValue;

import java.util.Collection;
import java.util.List;

public interface Projects {
  /**
   * Gets TME model based on the variants that are linked to the development item/product
   *
   * @param
   * @return list of projects
   */
  List<KeyValue> get(Collection<String> projectNames, String organisation);
}
