package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.P360ToHomologationGenerationCodeAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationHomologationGenerationCodeRepository;
import com.vqd.tme.na2a.model.p360.P360HomologationGenerationCode;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationHomologationGenerationCodeRepositoryImpl implements ApplicationHomologationGenerationCodeRepository {

    private static final String FIELDS = String.join(",",
            "VariantHomologationGenerationCode.TMECode",
            "VariantHomologationGenerationCode.TMEFrom",
            "VariantHomologationGenerationCode.TMETo");

    private static final String BASE_PATH = "/rest/V2.0/list/Variant/VariantHomologation/";

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    private final P360ToHomologationGenerationCodeAdapter adapter;

    @Override
    public List<P360HomologationGenerationCode> findByApplication(String applicationId) {
        String url = properties.getServer()
                + BASE_PATH
                + "byItems?items={applicationId}"
                + "&fields=" + FIELDS
                + "&includeLabels=true&formatData=true";

        log.trace("Request {}", Json.of(url));
        GetResponse response = restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId(applicationId));
        log.trace("Response {}", Json.of(response));

        return adapter.convert(response);
    }
}
