package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360DesignReplacementOfEquipment;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class P350ToDesignReplacementOfEquipmentAdapter
        implements Adapter<GetResponse, List<P360DesignReplacementOfEquipment>> {

    private static final Integer VALUE_COUNT = 3;

    @Override
    public List<P360DesignReplacementOfEquipment> convert(GetResponse source) {
        if (source.getRowCount() == 0) {
            return Lists.newArrayList();
        }

        List<P360DesignReplacementOfEquipment> roes = Lists.newArrayList();
        source.getRows().forEach(row -> {
            if (isValid(row.getValues())) {
                List<Object> values = row.getValues();
                roes.add(P360DesignReplacementOfEquipment.builder()
                        .code(CastUtils.asString(values.get(0)))
                        .equipment(CastUtils.asString(values.get(1)))
                        .replacementFlagSpec(CastUtils.asBoolean(values.get(2)))
                        .build());
            }
        });
        return roes;
    }

    private boolean isValid(List<Object> values) {
        return values != null && values.size() == VALUE_COUNT;
    }

}
