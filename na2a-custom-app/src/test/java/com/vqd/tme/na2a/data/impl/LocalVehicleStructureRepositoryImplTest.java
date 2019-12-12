//package com.vqd.tme.na2a.data.impl;
//
//import com.google.common.collect.Lists;
//import com.vqd.tme.na2a.config.InformaticaPimProperties;
//import com.vqd.tme.na2a.data.LocalVehicleStructureRepository;
//import com.vqd.tme.na2a.p360.GetResponse;
//import com.vqd.tme.na2a.util.GetResponseBuilder;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//public class LocalVehicleStructureRepositoryImplTest {
//
//    private InformaticaPimProperties properties;
//    private RestTemplate restTemplate;
//
//    private LocalVehicleStructureRepository testable;
//
//    @Before
//    public void setUp() {
//        properties = new InformaticaPimProperties();
//        properties.setServer("http://localhost:9090");
//
//        restTemplate = mock(RestTemplate.class);
//
//        testable = new LocalVehicleStructureRepositoryImpl(properties, restTemplate);
//    }
//
//    @Test
//    public void findByParentIdentifier() {
//
//        String url = properties.getServer()
//                + "/rest/V2.0/list/StructureGroup/bySearch"
//                + "?structure=localVehicleStructure"
//                + "&query=StructureGroup.ParentIdentifier = \"organisation|model\""
//                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";
//
//        List<GetResponse.Row> rows = Lists.newArrayList(
//            GetResponseBuilder.buildRow("1@1", 1, Lists.newArrayList("id", "value", null))
//        );
//        GetResponse response = GetResponseBuilder.buildResponse(rows, 1, 1);
//
//        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);
//
//        GetResponse result = testable.findByParentIdentifier("organisation|model");
//
//        assertNotNull(result);
//        assertEquals(1, result.getRows().size());
//        assertEquals("id", result.getRows().get(0).getValues().get(0));
//        assertEquals("value", result.getRows().get(0).getValues().get(1));
//        assertNull(result.getRows().get(0).getValues().get(2));
//    }
//
//    @Test
//    public void findByIdentifier() {
//        String url = properties.getServer()
//                + "/rest/V2.0/list/StructureGroup/bySearch"
//                + "?structure=localVehicleStructure"
//                + "&query=StructureGroup.Identifier = \"organisation|model\""
//                + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroupLang.Synonym(EN),StructureGroup.ParentIdentifier";
//
//        List<GetResponse.Row> rows = Lists.newArrayList(
//            GetResponseBuilder.buildRow("1@1", 1, Lists.newArrayList("id", "value", null))
//        );
//        GetResponse response = GetResponseBuilder.buildResponse(rows, 1, 1);
//
//        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);
//
//        GetResponse result = testable.findByIdentifier("organisation|model");
//
//        assertNotNull(result);
//        assertEquals(1, result.getRows().size());
//        assertEquals("id", result.getRows().get(0).getValues().get(0));
//        assertEquals("value", result.getRows().get(0).getValues().get(1));
//        assertNull(result.getRows().get(0).getValues().get(2));
//    }
//}
