package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360GenerationCode;
import com.vqd.tme.na2a.model.p360.P360HomologationGenerationCode;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class P360ToHomologationGenerationCodeAdapter implements Adapter<GetResponse, List<P360HomologationGenerationCode>> {

    private static final Integer VALUE_COUNT = 3;

    @Override
    public List<P360HomologationGenerationCode> convert(GetResponse source) {
        if (source.getRowCount() == 0) {
            return Lists.newArrayList();
        }

        List<P360HomologationGenerationCode> generationCodes = Lists.newArrayList();
        source.getRows().forEach(row -> {
            if (isValid(row.getValues())) {
                List<Object> values = row.getValues();
                Map<String, String> codeMap = CastUtils.asMap(values.get(0));
                generationCodes.add(P360HomologationGenerationCode.builder()
                        .code(P360GenerationCode.builder()
                                .id(codeMap.get("id"))
                                .label(codeMap.get("label"))
                                .build())
                        .from(CastUtils.asString(values.get(1)))
                        .to(CastUtils.asString(values.get(2)))
                        .build());
            }
        });

        // When a variant is created in pim somehow it returns an object with null values upon the first request of that GC
        // here a workaround the ignore that situation
        if (generationCodes.size() == 1) {
            P360HomologationGenerationCode gc = generationCodes.get(0);
            if (gc.getTo() == null && gc.getFrom() == null
                    && gc.getCode().getLabel() == null && gc.getCode().getId() == null) {
                generationCodes.remove(0);
            }
        }
        return generationCodes;
    }

    private boolean isValid(List<Object> values) {
        return values != null && values.size() == VALUE_COUNT;
    }
}
