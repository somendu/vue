package com.vqd.tme.na2a.p360;

import java.util.List;
import lombok.Data;

@Data
public class UpdateItemResponse {
  @Data
  public static class Counters {
    private Integer errors;
    private Integer warnings;
    private Integer createdObjects;
    private Integer updatedObjects;
    private Integer objectsWithErrors;
    private Integer objectsWithWarnings;
  }

  @Data
  public static class Obj {
    private String id;
    private String label;
    private Integer entityId;
  }

  @Data
  public static class UpdatedObject {
    private Integer row;
    private Obj object;
    private List<String> status;
  }

  @Data
  public static class Entry {
    private Integer row;
    private String objectType;
    private Obj object;
    private String severity;
    private String category;
    private String propertyLabel;
    private String message;
    private String logDate;
    private String logTime;
  }

  private Counters counters;
  private List<Entry> entries;
  private List<UpdatedObject> objects;
}
