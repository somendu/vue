package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class P360ToApplicationHomologationAdapter implements Adapter<GetResponse, P360ApplicationHomologation> {

    private final static Integer ROW_COUNT = 1;
    private final static Integer VALUE_COUNT = 6;

    private final P360ToHomologationAdapter adapter;

    @Override
    public P360ApplicationHomologation convert(GetResponse source) {
        if (source.getRowCount().equals(ROW_COUNT) && hasValidValueCount(source.getRows().get(0))) {
            List<Object> values = source.getRows().get(0).getValues();
            return P360ApplicationHomologation.builder()
                    .wltp(CastUtils.asBoolean(values.get(0)))
                    .information(CastUtils.asString(values.get(1)))
                    .homologation(adapter.convert(CastUtils.asMap(values.get(2))))
                    .hubFitmentFlag(CastUtils.asBoolean(values.get(3)))
                    .contentApproval(CastUtils.asString(values.get(4)))
                    .deltaCDA(CastUtils.asBigDecimal(values.get(5)))
                    .build();
        }

        return P360ApplicationHomologation.builder().build();
    }

    private boolean hasValidValueCount(GetResponse.Row row) {
        return row.getValues() != null && row.getValues().size() == VALUE_COUNT;
    }
}
