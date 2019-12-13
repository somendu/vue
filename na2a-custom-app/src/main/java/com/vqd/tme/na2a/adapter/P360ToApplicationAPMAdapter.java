package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360ApplicationAPM;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

@Component
public class P360ToApplicationAPMAdapter implements Adapter<GetResponse, P360ApplicationAPM> {

    private static final int ROW_COUNT = 1;
    private static final int VALUE_COUNT = 3;

    @Override
    public P360ApplicationAPM convert(GetResponse response) {
        if (response.getRowCount() == ROW_COUNT) {
            GetResponse.Row row = response.getRows().get(0);
            if (row.getValues() != null && row.getValues().size() == VALUE_COUNT) {
                return P360ApplicationAPM.builder()
                        .tmeStandard(CastUtils.asStrings(row.getValues().get(0)))
                        .tmePackOffer(CastUtils.asStrings(row.getValues().get(1)))
                        .tmeDemo(CastUtils.asStrings(row.getValues().get(2)))
                        .build();
            }
        }

        return null;
    }
}
