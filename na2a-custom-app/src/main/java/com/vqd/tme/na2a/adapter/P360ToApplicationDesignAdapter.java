package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class P360ToApplicationDesignAdapter implements Adapter<GetResponse, P360ApplicationDesign> {

    private static final Integer ROW_COUNT = 1;
    private static final Integer VALUE_COUNT = 6;

    @Override
    public P360ApplicationDesign convert(GetResponse source) {
        if(source.getRowCount().equals(ROW_COUNT) && hasValidValueCount(source)) {
            List<Object> values = source.getRows().get(0).getValues();
            return P360ApplicationDesign.builder()
                    .lCoOrdinates(CastUtils.asBigDecimal(values.get(0)))
                    .lCoOrdinatesRounded(Optional.ofNullable(CastUtils.asInteger(values.get(1))).orElse(Integer.valueOf(0)))
                    .replacementAccessory(CastUtils.asBoolean(values.get(2)))
                    .incalculable(CastUtils.asBoolean(values.get(3)))
                    .deltaMass(CastUtils.asBigDecimal(values.get(4)))
                    .deltaMassRounded(Optional.ofNullable(CastUtils.asInteger(values.get(5))).orElse(Integer.valueOf(0)))
                    .build();
        }

        return P360ApplicationDesign.builder().build();
    }

    private boolean hasValidValueCount(GetResponse source) {
        return source.getRows().get(0).getValues() != null &&
                source.getRows().get(0).getValues().size() == VALUE_COUNT;
    }
}
