package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureGroup;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RowToStructureGroupAdapter implements Adapter<GetResponse.Row, P360StructureGroup> {

    private static final int ROW_COUNT = 3;

    @Override
    public P360StructureGroup convert(GetResponse.Row row) {
        if (row.getValues() != null && row.getValues().size() == ROW_COUNT) {
            return P360StructureGroup.builder()
                    .parentIdentifier(CastUtils.asString(row.getValues().get(0)))
                    .identifier(CastUtils.asString(row.getValues().get(1)))
                    .name(CastUtils.asString(row.getValues().get(2)))
                    .build();
        }
        return null;
    }
}
