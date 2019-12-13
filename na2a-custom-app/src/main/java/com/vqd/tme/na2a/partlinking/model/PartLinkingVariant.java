package com.vqd.tme.na2a.partlinking.model;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PartLinkingVariant {
  private String id;
  private String commodity;
  private String summary;
  private String vehicleGeneration;
  private String name;
  private String description;
  private String status;
  private String interiorColours;
  private String trimColours;
  private String exteriorColours;
  private List<LinkedPart> parts;
  private String productColour;
  private String productMaterial;
  private String accessoryCategory;
  private String variantNo;
}
