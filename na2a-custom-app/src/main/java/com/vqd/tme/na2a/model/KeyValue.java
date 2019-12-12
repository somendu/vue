package com.vqd.tme.na2a.model;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue {
  private String key;
  private String value;
  private Collection<String> katashiki;
  private String parentKey;

  public KeyValue(String key, String value) {
    this.key = key;
    this.value = value;
  }
}
