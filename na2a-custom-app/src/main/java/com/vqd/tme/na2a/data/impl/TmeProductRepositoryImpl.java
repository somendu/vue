package com.vqd.tme.na2a.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vqd.tme.na2a.config.TmeApiProperties;
import com.vqd.tme.na2a.data.TmeProductRepository;
import com.vqd.tme.na2a.model.tme.TmePartsWrapper;
import com.vqd.tme.na2a.service.TmeAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class TmeProductRepositoryImpl implements TmeProductRepository {

    @Autowired
    private TmeApiProperties tmeProp;

    @Autowired
    private TmeAuthenticationService tmeAuthenticationService;

    private final RestTemplate restTemplate;
    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    @Autowired
    public TmeProductRepositoryImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .setConnectTimeout(10000)
                .setReadTimeout(10000)
                .build();
    }

    @Override
    public TmePartsWrapper searchParts(String partNumber) {
        String url = tmeProp.getProductUrl() + "/TME/parts?partNumber={partNumber}";
        log.debug("get url: {} partNumber: {}", url, partNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", tmeAuthenticationService.getTmeAuthenticationToken());
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        ResponseEntity<TmePartsWrapper> response = restTemplate.exchange(url, HttpMethod.GET, entity, TmePartsWrapper.class, partNumber);
        TmePartsWrapper wrapper = response.getBody();
        return wrapper;
    }

}
