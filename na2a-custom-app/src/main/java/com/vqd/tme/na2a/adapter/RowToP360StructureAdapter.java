package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import org.springframework.stereotype.Component;

@Component
public class RowToP360StructureAdapter implements Adapter<GetResponse.Row, P360Structure> {

    @Override
    public P360Structure convert(GetResponse.Row row) {

        if(row.getObject() == null || row.getValues().isEmpty() || row.getValues().size() > 2) {
            return null;
        }

        return P360Structure.builder()
                .objectId(row.getObject().getId())
                .identifier(String.valueOf(row.getValues().get(0)))
                .name(getName(row))
                .type(P360StructureType.findByName(getName(row)))
                .build();
    }

    private String getName(GetResponse.Row row) {
        return String.valueOf(row.getValues().get(1));
    }
}
