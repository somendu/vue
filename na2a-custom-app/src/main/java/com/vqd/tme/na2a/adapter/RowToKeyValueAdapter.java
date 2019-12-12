package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RowToKeyValueAdapter implements Adapter<GetResponse.Row, KeyValue> {

    @Override
    public KeyValue convert(GetResponse.Row row) {
        return new KeyValue(
                CastUtils.asString(Optional.ofNullable(row.getValues()).map(v -> v.get(0)).orElse(null)),
                CastUtils.asString(Optional.ofNullable(row.getValues()).map(v -> v.get(1)).orElse(null)),
                CastUtils.asStrings(Optional.ofNullable(row.getValues()).map(v -> v.get(2)).orElse(null)),
                CastUtils.asString(Optional.ofNullable(row.getValues()).map(v -> v.get(3)).orElse(null)));
    }
}
