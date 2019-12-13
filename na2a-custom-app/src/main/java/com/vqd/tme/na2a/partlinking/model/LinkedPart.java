package com.vqd.tme.na2a.partlinking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class LinkedPart {
  private String partNumber;
  private String partName;
  private String type;
  private BigDecimal quantity;
  private String colour;
  private String material;
  private Boolean knownInNPA;
}
