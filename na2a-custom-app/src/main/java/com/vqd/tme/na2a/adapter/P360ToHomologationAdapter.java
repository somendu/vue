package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360Homologation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class P360ToHomologationAdapter implements Adapter<Map<String, String>, P360Homologation> {

    @Override
    public P360Homologation convert(Map<String, String> source) {
        if (source == null || source.isEmpty()) {
            return P360Homologation.builder().build();
        }
        return P360Homologation.builder()
                .id(source.get("id"))
                .build();
    }

}
