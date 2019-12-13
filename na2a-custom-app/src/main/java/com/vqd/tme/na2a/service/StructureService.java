package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.data.StructureRepository;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class StructureService {

    private final StructureRepository structureRepository;

    @Cacheable(cacheNames = "structureCache", key = "'product'", sync = true)
    public P360Structure getProductStructure() {
        return getP360Structure(P360StructureType.PRODUCT_STRUCTURE);
    }

    @Cacheable(cacheNames = "structureCache", key = "'vehicle'", sync = true)
    public P360Structure getVehicleStructure() {
        return getP360Structure(P360StructureType.VEHICLE_STRUCTURE);
    }

    @Cacheable(cacheNames = "structureCache", key = "'generation'", sync = true)
    public P360Structure getGenerationStructure() {
        return getP360Structure(P360StructureType.GENERATION_STRUCTURE);
    }

    @Cacheable(cacheNames = "structureCache", key = "'localVehicle'", sync = true)
    public P360Structure getLocalVehicleStructure() {
        return getP360Structure(P360StructureType.LOCAL_VEHICLE_STRUCTURE);
    }

    public GetResponse getByParentIdentifier (String identifier, P360StructureType type) {
        P360Structure structure = getStructureByType(type);
        return structureRepository.findByParentIdentifier(identifier, structure);
    }

    public GetResponse getByIdentifier (String identifier, P360StructureType type) {
        P360Structure structure = getStructureByType(type);
        return structureRepository.findByIdentifier(identifier, structure);
    }

    private P360Structure getStructureByType(P360StructureType type) {
        switch (type) {
            case PRODUCT_STRUCTURE:
                return getProductStructure();
            case VEHICLE_STRUCTURE:
                return getVehicleStructure();
            case LOCAL_VEHICLE_STRUCTURE:
                return getLocalVehicleStructure();
            case GENERATION_STRUCTURE:
                return getGenerationStructure();
        }
        return null;
    }

    private P360Structure getP360Structure(P360StructureType type) {
        Map<P360StructureType, P360Structure> structures = structureRepository.findAll();
        log.info(String.format("Getting structure of type %s", type));
        return structures.get(type);
    }

}
