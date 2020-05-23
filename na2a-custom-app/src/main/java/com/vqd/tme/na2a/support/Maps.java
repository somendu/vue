package com.vqd.tme.na2a.support;

import java.util.HashMap;
import java.util.Map;

public class Maps {
  private Maps() {}

  public static Map<String, Object> of(Object... keyValuePairs) {
    Map<String, Object> map = new HashMap<>();

    for (int i = 0; i < keyValuePairs.length - 1; i += 2) {
      map.put(
          (String) keyValuePairs[i],
          keyValuePairs[i + 1]);
    }

    return map;
  }
}
