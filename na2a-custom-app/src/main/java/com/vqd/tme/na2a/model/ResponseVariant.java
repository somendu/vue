package com.vqd.tme.na2a.model;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ResponseVariant {
  /**
   * Variant ID by Product ID
   */
  private Map<String, String> variantIds;

  /**
   * Variant No by Product ID
   */
  private Map<String, String> variantNos;
  private String shortDescription;
  private String segment;
  private boolean cancelled;
  private String cancelledReason;
  private ResponseModel model;
  private List<KeyValueCategory> vehicle;
  private List<KeyValueCategory> equipment;
  private List<KeyValue> exteriorColours;
  private List<KeyValue> interiorColours;
  private List<KeyValue> trimColours;
  private List<KeyValue> countries;
}
