package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RowToP360ApplicationAdapter implements Adapter<GetResponse.Row, P360Application> {

    public static final int FIELD_COUNT = 13;

    @Override
    public P360Application convert(GetResponse.Row row) {
        if(row.getValues().size() == FIELD_COUNT) {
            return P360Application.builder()
                    .id(CastUtils.asString(row.getValues().get(0)))
                    .vehicleGeneration(CastUtils.asString(row.getValues().get(1)))
                    .name(Optional.ofNullable(CastUtils.asString(row.getValues().get(2))).orElse(""))
                    .shortDescription(Optional.ofNullable(CastUtils.asString(row.getValues().get(3))).orElse(""))
                    .status(CastUtils.asString(row.getValues().get(4)))
                    .interiorColourIds(CastUtils.asList(row.getValues().get(5)))
                    .interiorColours(CastUtils.asList(row.getValues().get(6)))
                    .exteriorColourIds(CastUtils.asList(row.getValues().get(7)))
                    .exteriorColours(CastUtils.asList(row.getValues().get(8)))
                    .trimColourIds(CastUtils.asList(row.getValues().get(9)))
                    .trimColours(CastUtils.asList(row.getValues().get(10)))
                    .equipmentIds(CastUtils.asList(row.getValues().get(11)))
                    .isInPPO(isInPPO(CastUtils.asStrings(row.getValues().get(12))))
                    .build();
        }
        return null;
    }

    private Boolean isInPPO(List<String> classifications) {
        return classifications != null &&
                !classifications.isEmpty() &&
                classifications.stream().filter(v -> StringUtils.isNotEmpty(v)).count() != 0;
    }
}
