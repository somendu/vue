package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.P360ToApplicationAPMAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationAPMRepository;
import com.vqd.tme.na2a.model.p360.P360ApplicationAPM;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationAPMRepositoryImpl implements ApplicationAPMRepository {

    private static final String FIELDS = String.join(",", "VariantAPM.TMEStandardGrade", "VariantAPM.TMEPackOffer", "VariantAPM.TMEDemoConfiguration");

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;
    private final P360ToApplicationAPMAdapter adapter;

    @Override
    public P360ApplicationAPM findById(String applicationId) {
        String url = properties.getServer() +
                "/rest/V2.0/list/Variant/VariantAPM/byItems?items={applicationId}" +
                "&fields=" + FIELDS;

        log.trace("Fetch APM info request for id {}: {}", applicationId, Json.of(url));

        GetResponse response = restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId(applicationId));

        log.trace("Fetch APM info response for id {}: {}", applicationId, Json.of(response));

        return adapter.convert(response);
    }
}
