package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.data.VariantStructureGroupMapRepository;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VariantStructureGroupMapService {

    private final VariantStructureGroupMapRepository structureGroupMapRepository;

    public GetResponse getStructureGroupMap(String items) {
        return structureGroupMapRepository.findStructureGroupIds(items);
    }

    public Set<String> getStructureGroupIds(GetResponse structureGroupMapResponse) {
        Set<String> structureGroupIds = structureGroupMapResponse.getRows().stream()
            .map(row -> row.getQualification() != null ? row.getQualification().getStructureGroupProxy() : null)
            .map(p -> p != null ? p.getId() : null)
            .filter(id -> id != null)
            .collect(Collectors.toSet());

        log.debug("occurring structure group ids: {}", structureGroupIds);
        return structureGroupIds;
    }
}
