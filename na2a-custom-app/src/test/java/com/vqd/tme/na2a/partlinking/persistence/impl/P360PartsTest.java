package com.vqd.tme.na2a.partlinking.persistence.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.PartsFilter;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class P360PartsTest {

    private InformaticaPimProperties properties;
    private RestTemplate rest;

    private P360Parts testable;

    @Before
    public void setUp() throws Exception {
        this.properties = new InformaticaPimProperties();
        this.properties.setServer("http://localhost:8080");

        this.rest = mock(RestTemplate.class);

        this.testable = new P360Parts(this.properties, this.rest);
    }

    @Test
    public void testGetByEmptyIdentifier() {
        Map<String, Part> result = testable.get("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByEmptyIdentifiers() {
        Map<String, Part> result = testable.get(Lists.newArrayList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByNullIdentifiers() {
        List<String> identifiers = null;
        Map<String, Part> result = testable.get(identifiers);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByNullIdentifier() {
        String identifier = null;
        Map<String, Part> result = testable.get(identifier);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByIdentifier() {
        String identifier = "04130YZZCR";
        String url = properties.getServer() +
                "/rest/V2.0/list/Article/byIdentifiers?identifiers={identifiers}" +
                "&fields=Article.SupplierAID,ArticleLang.DescriptionShort(EN)," +
                "Article.TMEColour,Article.TMEMaterial,ArticleDesign.TMEKnownInNPA" +
                "&includeLabels=true&formatData=true";
        GetResponse.Row row = GetResponseBuilder.buildRow("60@1", 1000, Lists.newArrayList("04130YZZCR", "Part Name", "", "", ""));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        when(rest.getForObject(url, GetResponse.class, identifier)).thenReturn(response);

        Map<String, Part> result = testable.get(identifier);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(identifier));
        assertEquals(identifier, result.get(identifier).getPartNumber());
        assertEquals("Part Name", result.get(identifier).getPartName());
    }

    @Test
    public void testGetByIdentifiers() {
        List<String> identifiers = Lists.newArrayList("04130YZZCR", "04130YZZJK");

        String url = properties.getServer() +
                "/rest/V2.0/list/Article/byIdentifiers?identifiers={identifiers}&fields=Article.SupplierAID," +
                "ArticleLang.DescriptionShort(EN),Article.TMEColour,Article.TMEMaterial,ArticleDesign.TMEKnownInNPA" +
                "&includeLabels=true&formatData=true";

        GetResponse.Row row1 = GetResponseBuilder.buildRow("60@1", 1000, Lists.newArrayList("04130YZZCR", "Part Name 1", "", "", ""));
        GetResponse.Row row2 = GetResponseBuilder.buildRow("42@1", 1000, Lists.newArrayList("04130YZZJK", "Part Name 2", "", "", ""));
        GetResponse response1 = GetResponseBuilder.buildResponse(Lists.newArrayList(row1), 1, 1);
        GetResponse response2 = GetResponseBuilder.buildResponse(Lists.newArrayList(row2), 1, 1);

        when(rest.getForObject(url, GetResponse.class, "04130YZZCR")).thenReturn(response1);
        when(rest.getForObject(url, GetResponse.class, "04130YZZJK")).thenReturn(response2);

        Map<String, Part> result = testable.get(identifiers);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("04130YZZCR"));
        assertTrue(result.containsKey("04130YZZJK"));
        assertEquals("04130YZZCR", result.get("04130YZZCR").getPartNumber());
        assertEquals("04130YZZJK", result.get("04130YZZJK").getPartNumber());
        assertEquals("Part Name 1", result.get("04130YZZCR").getPartName());
        assertEquals("Part Name 2", result.get("04130YZZJK").getPartName());
    }

    @Test
    public void testGetBySearch() {
        String queryPartNumber = "04130";
        String queryPartName = "Part";

        PartsFilter filter = PartsFilter.builder()
                .partNumber(queryPartNumber)
                .partName(queryPartName)
                .colourOrMaterial("")
                .projectCode("123")
                .personInCharge("Jan")
                .commodity("123-123-13")
                .build();

        String url = properties.getServer() +
                "/rest/V2.0/list/Article/bySearch?query={query}" +
                "&fields=Article.SupplierAID,ArticleLang.DescriptionShort(EN),Article.TMEColour,Article.TMEMaterial" +
                ",ArticleDesign.TMEKnownInNPA" +
                "&pageSize=100&orderBy=0-ASC" +
                "&includeLabels=true&formatData=true";

        GetResponse.Row row1 = GetResponseBuilder.buildRow("60@1", 1000, Lists.newArrayList("04130YZZCR", "Part Name 1", "", "", true));
        GetResponse.Row row2 = GetResponseBuilder.buildRow("42@1", 1000, Lists.newArrayList("04130YZZJK", "Part Name 2", "", "", false));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 1, 1);

        when(rest.getForObject(eq(url), eq(GetResponse.class), any(String.class))).thenReturn(response);

        List<Part> result = testable.search(filter);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("04130YZZCR", result.get(0).getPartNumber());
        assertEquals("04130YZZJK", result.get(1).getPartNumber());
        assertEquals("Part Name 1", result.get(0).getPartName());
        assertEquals("Part Name 2", result.get(1).getPartName());
        assertEquals(true, result.get(0).getKnownInNPA());
        assertEquals(false, result.get(1).getKnownInNPA());
    }
}
