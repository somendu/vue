package com.vqd.tme.na2a.support;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
  public static <T> T firstOf(Collection<T> coll) {
    if (coll != null) {
      Iterator<T> iter = coll.iterator();
      if (iter.hasNext()) {
        return iter.next();
      }
    }

    return null;
  }
}
