package com.vqd.tme.na2a.util;

import com.vqd.tme.na2a.p360.GetResponse;

import java.util.List;

public final class GetResponseBuilder {

    private GetResponseBuilder() {
        super();
    }

    public static GetResponse.Row buildRow(String id, Integer entityId, List<Object> values) {
        GetResponse.Row.RowObject object = new GetResponse.Row.RowObject();
        object.setId(id);
        object.setEntityId(entityId);

        GetResponse.Row row = new GetResponse.Row();
        row.setObject(object);
        row.setValues(values);

        return row;
    }

    public static GetResponse buildResponse(List<GetResponse.Row> rows, int totalSize, int rowCount) {
        GetResponse response = new GetResponse();
        response.setRows(rows);
        response.setPageSize(100);
        response.setTotalSize(totalSize);
        response.setRowCount(rowCount);
        return response;
    }
}
