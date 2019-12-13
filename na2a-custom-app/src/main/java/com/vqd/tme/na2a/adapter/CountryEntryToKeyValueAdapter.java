package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryEntryToKeyValueAdapter implements Adapter<P360EnumEntry, KeyValue> {

    @Override
    public KeyValue convert(P360EnumEntry p360EnumEntry) {
        return Optional.ofNullable(p360EnumEntry)
                .map(entry -> new KeyValue(CastUtils.asString(entry.getKey()), entry.getLabel()))
                .orElse(null);
    }
}
