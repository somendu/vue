package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToStructureGroupAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.StructureGroupRepository;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureGroup;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StructureGroupRepositoryImpl implements StructureGroupRepository {

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    private final RowToStructureGroupAdapter structureGroupAdapter;

    @Override
    public GetResponse findByIdentifier(String structure, String identifier) {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch?structure={structure}&query=StructureGroup.Identifier equalsic \"{identifier}\"";

        return restTemplate.getForObject(url, GetResponse.class, structure, identifier);
    }

    @Override
    public List<P360StructureGroup> findByStructure(P360Structure structure) {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/byStructure?"
                + "structure=productStructure&fields=StructureGroup.ParentIdentifier, StructureGroup.Identifier, StructureGroupLang.Name(EN)"
                + "&pageSize=-1&cacheId=no-cache ";

        GetResponse response = restTemplate.getForObject(url, GetResponse.class);

        return response.getRows().stream()
                .map(row -> structureGroupAdapter.convert(row))
                .collect(Collectors.toList());
    }
}

