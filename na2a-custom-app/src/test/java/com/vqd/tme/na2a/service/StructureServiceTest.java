package com.vqd.tme.na2a.service;

import com.google.common.collect.Maps;
import com.vqd.tme.na2a.data.StructureRepository;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StructureServiceTest {

    private StructureRepository repository;
    private StructureService testable;

    @Before
    public void setUp() {
        repository = mock(StructureRepository.class);
        testable = new StructureService(repository);
    }

    @Test
    public void testGetProductStructure() {
        P360Structure productStructure = P360Structure.builder()
                .objectId("9000")
                .identifier("productStructure")
                .name("Product Structure")
                .type(P360StructureType.PRODUCT_STRUCTURE)
                .build();

        Map<P360StructureType, P360Structure> structures = Maps.newHashMap();
        structures.put(P360StructureType.PRODUCT_STRUCTURE,
                productStructure);

        when(repository.findAll()).thenReturn(structures);

        P360Structure result = testable.getProductStructure();

        assertNotNull(result);
        assertEquals(productStructure, result);
    }

    @Test
    public void testGetVehicleStructure() {
        P360Structure vehicleStructure = P360Structure.builder()
                .objectId("9001")
                .identifier("vehicleStructure")
                .name("Vehicle Structure")
                .type(P360StructureType.VEHICLE_STRUCTURE)
                .build();

        Map<P360StructureType, P360Structure> structures = Maps.newHashMap();
        structures.put(P360StructureType.VEHICLE_STRUCTURE,
                vehicleStructure);

        when(repository.findAll()).thenReturn(structures);

        P360Structure result = testable.getVehicleStructure();

        assertNotNull(result);
        assertEquals(vehicleStructure, result);
    }

    @Test
    public void testGetLocalVehicleStructure() {
        Map<P360StructureType, P360Structure> structures = Maps.newHashMap();
        P360Structure localVehicleStructure = P360Structure.builder()
                .objectId("9002")
                .identifier("localVehicleStructure")
                .name("Local Vehicle Structure")
                .type(P360StructureType.LOCAL_VEHICLE_STRUCTURE)
                .build();
        structures.put(P360StructureType.LOCAL_VEHICLE_STRUCTURE,
                localVehicleStructure);

        when(repository.findAll()).thenReturn(structures);

        P360Structure result = testable.getLocalVehicleStructure();

        assertNotNull(result);
        assertEquals(localVehicleStructure, result);
    }
}
