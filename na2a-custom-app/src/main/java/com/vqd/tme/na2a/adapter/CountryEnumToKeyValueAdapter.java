package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CountryEnumToKeyValueAdapter implements Adapter<P360Enum, List<KeyValue>> {

    private final CountryEntryToKeyValueAdapter countryEntryAdapter;

    @Override
    public List<KeyValue> convert(P360Enum p360Enum) {
        List<P360EnumEntry> enumEntries = Optional.ofNullable(p360Enum).map(P360Enum::getEntries).orElse(Lists.newArrayList());
        return enumEntries.stream()
                .map(entry -> countryEntryAdapter.convert(entry))
                .filter(keyValue -> keyValue != null)
                .collect(Collectors.toList());
    }
}
