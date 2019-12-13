package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.model.p360.P360EntryKey;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.service.EnumLookupService;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RowToProductAdapter implements Adapter<GetResponse.Row, Product> {

    public static final int FIELD_COUNT = 5;
    private final EnumLookupService enumLookupService;
    private final ObjectToP360EntryKeyAdapter entryKeyAdapter;

    @Override
    public Product convert(GetResponse.Row row) {
        List<Object> values = row.getValues();

        if(values.size() == FIELD_COUNT) {
            P360EnumEntry colour = getP360Colour(CastUtils.asMap(values.get(3)));
            P360EnumEntry material = getP360Material(CastUtils.asMap(values.get(4)));

            return Product.builder()
                    .id(CastUtils.asString(values.get(0)))
                    .productNumber(CastUtils.asString(values.get(1)))
                    .shortDescription(CastUtils.asString(values.get(2)))
                    .colour(Optional.ofNullable(colour)
                            .map(P360EnumEntry::getKey)
                            .map(o -> entryKeyAdapter.convert(o))
                            .orElse(null))
                    .material(Optional.ofNullable(material)
                            .map(P360EnumEntry::getKey)
                            .map(o -> entryKeyAdapter.convert(o))
                            .orElse(null))
                    .build();
        }
        return null;
    }

    private P360EnumEntry getP360Colour(Map<String, String> map) {
        if (map.containsKey("id")) {
            return enumLookupService.findColourById(map.get("id"));
        }
        return null;
    }

    private P360EnumEntry getP360Material(Map<String, String> map) {
        if (map.containsKey("id")) {
            return enumLookupService.findMaterialById(map.get("id"));
        }
        return null;
    }
}
