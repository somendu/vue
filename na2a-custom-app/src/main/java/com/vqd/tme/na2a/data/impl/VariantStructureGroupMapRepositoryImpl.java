package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.VariantStructureGroupMapRepository;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.support.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantStructureGroupMapRepositoryImpl implements VariantStructureGroupMapRepository {

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public GetResponse findStructureGroupIds(String items) {
        GetResponse clazzRes = restTemplate.getForObject(
                properties.getServer()
                        + "/rest/V2.0/list/Variant/VariantStructureGroupMap/byItems"
                        + "?items={items}"
                        + "&fields=VariantStructureGroupMap.StructureGroupProxy",
                GetResponse.class,
                items);

        log.trace("classifications raw response: {}", Json.of(clazzRes));

        return clazzRes;
    }

    public GetResponse findByApplicationId(String applicationId) {
        return restTemplate.getForObject(
                properties.getServer()
                        + "/rest/V2.0/list/Variant/VariantStructureGroupMap/bySearch?query=Variant.Id = {applicationId}"
                        + "&fields=VariantStructureGroupMap.StructureGroupProxy",
                GetResponse.class,
                applicationId);
    }
}
