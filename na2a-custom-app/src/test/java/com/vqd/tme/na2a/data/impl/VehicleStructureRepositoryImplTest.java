package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.VehicleStructureRepository;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleStructureRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;

    private VehicleStructureRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        testable = new VehicleStructureRepositoryImpl(properties, restTemplate);
    }

    @Test
    public void testFindByIdentifier() {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure=VehicleStructure"
                + "&query=StructureGroup.Identifier = \"ab-cd\""
                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";


        GetResponse.Row row = GetResponseBuilder.buildRow("1000@1", 1234, Lists.newArrayList("2000@1", "Project A", "Synonym", "3000@1"));

        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1 , 1);
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        GetResponse result = testable.findByIdentifier("ab-cd");

        assertNotNull(result);
        assertEquals(response, response);
    }
}
