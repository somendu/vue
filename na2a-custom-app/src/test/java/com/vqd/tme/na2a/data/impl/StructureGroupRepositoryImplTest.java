package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToStructureGroupAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.StructureGroupRepository;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StructureGroupRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;
    private RowToStructureGroupAdapter structureGroupAdapter;

    private StructureGroupRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        testable = new StructureGroupRepositoryImpl(properties, restTemplate, structureGroupAdapter);
    }

    @Test
    public void findByIdentifier() {
        String structureName = "localVehicleStructure";
        P360Structure localVehicleStructure = P360Structure.builder().identifier(structureName).build();
        String identifier = "A0E68CF3-1D09-4595-923A-B3A60B56CEC3";

        String url = properties.getServer() + "/rest/V2.0/list/StructureGroup/bySearch?structure={structure}&query=StructureGroup.Identifier equalsic \"{identifier}\"";

        GetResponse.Row row = GetResponseBuilder.buildRow("487738@10000", 3000, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1 , 1);
        when(restTemplate.getForObject(url, GetResponse.class, structureName, identifier)).thenReturn(response);

        GetResponse result = testable.findByIdentifier(localVehicleStructure.getIdentifier(), identifier);

        assertNotNull(result);
        assertEquals(new Integer(1), result.getRowCount());
        assertEquals("487738@10000", result.getRows().get(0).getObject().getId());
    }
}
