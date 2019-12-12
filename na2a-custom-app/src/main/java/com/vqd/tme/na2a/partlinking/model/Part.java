package com.vqd.tme.na2a.partlinking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part {
  private String partNumber;
  private String partName;
  private String colour;
  private String material;
  private Boolean knownInNPA;

  public Part(String partNumber, String partName, Boolean knownInNPA) {
    this.partNumber = partNumber;
    this.partName = partName;
    this.knownInNPA = knownInNPA;
  }
}
