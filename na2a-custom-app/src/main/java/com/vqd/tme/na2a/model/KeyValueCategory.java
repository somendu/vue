package com.vqd.tme.na2a.model;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueCategory {
  private String key;
  private String value;
  private String category;
  private Collection<String> katashiki;
}
