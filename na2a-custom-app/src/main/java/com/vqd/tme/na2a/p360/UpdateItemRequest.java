package com.vqd.tme.na2a.p360;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class UpdateItemRequest {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Column {
    private String identifier;
  }

  @Data
  @Accessors(chain=true)
  public static class Row {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowObject {
      private String id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Qualification {
      private String referenceType;
      private String referencedSupplierAid;
    }

    private RowObject object;
    private Qualification qualification;
    private List<Object> values;
  }

  private List<Column> columns;
  private List<Row> rows;
}
