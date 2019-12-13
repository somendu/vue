package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Attribute;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class P360ToApplicationAttributesAdapter implements Adapter<GetResponse, List<P360Attribute>> {

    private static final Integer VALUE_COUNT = 3;

    @Override
    public List<P360Attribute> convert(GetResponse response) {
        if (response.getRowCount() > 0) {
            List<P360Attribute> attributes = Lists.newArrayList();
            response.getRows().forEach(row -> {
                convertRow(attributes, row);
            });
            return attributes;
        }
        log.trace("No attributes where found!");
        return Lists.newArrayList();
    }

    private void convertRow(List<P360Attribute> attributes, GetResponse.Row row) {
        if(row.getValues() != null && row.getValues().size() == VALUE_COUNT) {
            attributes.add(
                    P360Attribute.builder()
                            .attributeId(CastUtils.asString(row.getValues().get(0)))
                            .name(CastUtils.asString(row.getValues().get(1)))
                            .value(CastUtils.asString(row.getValues().get(2)))
                            .build()
            );
        } else {
            log.warn("Invalid number of fields found in response for application attributes");
        }
    }
}
