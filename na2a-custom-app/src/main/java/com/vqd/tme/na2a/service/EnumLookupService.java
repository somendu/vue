package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.data.EnumRepository;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnumLookupService {

    private final EnumRepository enumRepository;

    @Cacheable(cacheNames="enumLookupCache", key="'tmeColourEnum'", sync=true)
    public P360Enum getTMEColourEnum() {
        return enumRepository.findByType("Enum.TMEColours");
    }

    @Cacheable(cacheNames="enumLookupCache", key="'tmeMaterialEnum'", sync=true)
    public P360Enum getTMEMaterialEnum() {
        return enumRepository.findByType("Enum.TMEMaterial");
    }

    @Cacheable(cacheNames = "enumLookupCache", key = "'tmeTerritoryEnum'", sync = true)
    public P360Enum getTMECountryEnum() {
        return enumRepository.findByType("Enum.Territory");
    }

    public P360EnumEntry findColourById(String id) {
        P360Enum colourEnum = getTMEColourEnum();
        return findP360EnumEntry(id, colourEnum);
    }

    public P360EnumEntry findMaterialById(String id) {
        P360Enum materialEnum = getTMEMaterialEnum();
        return findP360EnumEntry(id, materialEnum);
    }

    private P360EnumEntry findP360EnumEntry(String id, P360Enum p360Enum) {
        List<P360EnumEntry> foundEntries = p360Enum.getEntries()
                .stream()
                .filter(entry -> CastUtils.asMap(entry.getKey()).get("id").equals(id))
                .collect(Collectors.toList());
        if (foundEntries != null && foundEntries.size() == 1) {
            return foundEntries.get(0);
        }
        return null;
    }

}
