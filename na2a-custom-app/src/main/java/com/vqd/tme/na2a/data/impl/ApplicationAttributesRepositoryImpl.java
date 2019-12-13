package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.P360ToApplicationAttributesAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationAttributesRepository;
import com.vqd.tme.na2a.model.p360.P360Attribute;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationAttributesRepositoryImpl implements ApplicationAttributesRepository {

    private static final String FIELDS = String.join(",",
            "VariantAttribute.Identifier",
                        "VariantAttributeLang.Name",
                        "VariantAttributeValue.Value"
                    );

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    private final P360ToApplicationAttributesAdapter adapter;

    @Override
    public List<P360Attribute> findByApplication(String applicationId) {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantAttribute/byItems?items={applicationId}"
                + "&fields=" + FIELDS;

        log.trace("Application attributes request: {}", Json.of(url));
        GetResponse response = restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId(applicationId));

        log.trace("Application attributes response: {}", Json.of(response));
        List<P360Attribute> attributes = adapter.convert(response);
        attributes.stream().forEach(attribute -> attribute.setApplicationId(applicationId));
        return attributes;
    }
}
