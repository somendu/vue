package com.vqd.tme.na2a.model.p360.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain=true)
public class UpdateArticleRequest {

    private List<Column> columns;
    private List<Row> rows;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {
        private String identifier;
    }

    @Data
    @Accessors(chain=true)
    public static class Row {

        private RowObject object;
        private List<Object> values;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RowObject {
            private String id;
        }

    }

}
