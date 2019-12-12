package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.VehicleStructureRepository;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class VehicleStructureRepositoryImpl implements VehicleStructureRepository {

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public GetResponse findByIdentifier(String identifier) {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure=VehicleStructure"
                + "&query=" + String.format("StructureGroup.Identifier = \"%s\"", identifier)
                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";

        log.debug("get url: {}", url);

        return restTemplate.getForObject(url, GetResponse.class);
    }
}
