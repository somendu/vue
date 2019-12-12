package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.config.TmeApiProperties;
import com.vqd.tme.na2a.data.TmeAuthenticationRepository;
import com.vqd.tme.na2a.model.tme.TmeAuthenticationToken;
import com.vqd.tme.na2a.model.tme.TmeLoginCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class TmeAuthenticationRepositoryImpl implements TmeAuthenticationRepository {

    @Autowired
    private TmeApiProperties tmeProp;
    private final RestTemplate restTemplate;

    @Autowired
    public TmeAuthenticationRepositoryImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .setConnectTimeout(10000)
                .setReadTimeout(10000)
                .build();
    }

    @Override
    public TmeAuthenticationToken getTmeAuthenticationToken() {
        TmeAuthenticationToken result = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TmeLoginCredentials> entity = new HttpEntity<>(tmeProp.getCredentials(), headers);

        result = restTemplate.postForObject(tmeProp.getAuthUrl(), entity, TmeAuthenticationToken.class);
        return result;
    }

}
