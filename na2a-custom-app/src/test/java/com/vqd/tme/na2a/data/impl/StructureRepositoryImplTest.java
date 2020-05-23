package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToP360StructureAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.StructureRepository;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StructureRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;
    private RowToP360StructureAdapter structureAdapter;

    private StructureRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);
        structureAdapter = new RowToP360StructureAdapter();

        testable = new StructureRepositoryImpl(properties, restTemplate, structureAdapter);
    }

    @Test
    public void testFindAll() {
        String url = properties.getServer() + "/rest/V2.0/list/Structure/bySearch?fields=Structure.Identifier,StructureLang.Name(EN)";

        GetResponse.Row productStructureRow = GetResponseBuilder.buildRow("9000", 9000, Lists.newArrayList("productStructure", "Product Structure"));
        GetResponse.Row vehicleStructureRow = GetResponseBuilder.buildRow("9001", 9001, Lists.newArrayList("vehicleStructure", "Vehicle Structure"));
        GetResponse.Row localVehicleStructureRow = GetResponseBuilder.buildRow("9002", 9002, Lists.newArrayList("localVehicleStructure", "Local Vehicle Structure"));

        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(productStructureRow, vehicleStructureRow, localVehicleStructureRow), 3,3);
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        Map<P360StructureType, P360Structure> result = testable.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey(P360StructureType.PRODUCT_STRUCTURE));
        assertTrue(result.containsKey(P360StructureType.VEHICLE_STRUCTURE));
        assertTrue(result.containsKey(P360StructureType.LOCAL_VEHICLE_STRUCTURE));
    }

    @Test
    public void testFindNone() {
        String url = properties.getServer() + "/rest/V2.0/list/Structure/bySearch?fields=Structure.Identifier,StructureLang.Name(EN)";

        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0,0);
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        Map<P360StructureType, P360Structure> result = testable.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByParentIdentifier() {

        P360Structure structure = P360Structure.builder().objectId("localVehicleStructure").build();

        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure={structure}"
                + "&query=StructureGroup.ParentIdentifier = \"organisation|model\""
                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";

        List<GetResponse.Row> rows = Lists.newArrayList(
                GetResponseBuilder.buildRow("1@1", 1, Lists.newArrayList("id", "value", null))
        );
        GetResponse response = GetResponseBuilder.buildResponse(rows, 1, 1);

        when(restTemplate.getForObject(url, GetResponse.class, structure.getObjectId())).thenReturn(response);

        GetResponse result = testable.findByParentIdentifier("organisation|model", structure);

        assertNotNull(result);
        assertEquals(1, result.getRows().size());
        assertEquals("id", result.getRows().get(0).getValues().get(0));
        assertEquals("value", result.getRows().get(0).getValues().get(1));
        assertNull(result.getRows().get(0).getValues().get(2));
    }

    @Test
    public void findByIdentifier() {
        P360Structure structure = P360Structure.builder().objectId("localVehicleStructure").build();

        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure={structure}"
                + "&query=StructureGroup.Identifier = \"organisation|model\""
                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";

        List<GetResponse.Row> rows = Lists.newArrayList(
                GetResponseBuilder.buildRow("1@1", 1, Lists.newArrayList("id", "value", null))
        );
        GetResponse response = GetResponseBuilder.buildResponse(rows, 1, 1);

        when(restTemplate.getForObject(url, GetResponse.class, structure.getObjectId())).thenReturn(response);

        GetResponse result = testable.findByIdentifier("organisation|model", structure);

        assertNotNull(result);
        assertEquals(1, result.getRows().size());
        assertEquals("id", result.getRows().get(0).getValues().get(0));
        assertEquals("value", result.getRows().get(0).getValues().get(1));
        assertNull(result.getRows().get(0).getValues().get(2));
    }
}
