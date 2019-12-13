package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.EnumRepository;
import com.vqd.tme.na2a.model.p360.P360Enum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class EnumRepositoryImpl implements EnumRepository {

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public P360Enum findByType(String enumType) {
        String url = properties.getServer() +
                "/rest/V2.0/enum/{enumType}";

        return restTemplate.getForObject(url, P360Enum.class, enumType);
    }
}
