package com.vqd.tme.na2a.partlinking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class VariantMetaData {
  private String commodity;
  private String summary;
  private String vehicleGeneration;
  private String name;
  private String description;
  private String status;
  private String interiorColours;
  private String trimColours;
  private String exteriorColours;
  private String accessoryCategory;
  private String accessoryMaterial;
  private String accessoryColour;
  private BigDecimal accessoryMass;
  private String equipmentSpecs;
  private String variantNo;
}
