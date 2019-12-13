package com.vqd.tme.na2a.p360;

import java.util.List;
import lombok.Data;

@Data
public class GetResponse {
  @Data
  public static class Row {
    @Data
    public static class RowObject {
      private String id;
      private Integer entityId;
    }

    @Data
    public static class Qualification {
      private RowObject structureGroupProxy;
      private RowObject structureProxy;
    }

    private RowObject object;
    private Qualification qualification;
    private List<Object> values;
  }

  private Integer totalSize;
  private Integer startIndex;
  private Integer pageSize;
  private Integer rowCount;
  private List<Row> rows;
}
