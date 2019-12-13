package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.P360ToApplicationAttributesAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationAttributesRepository;
import com.vqd.tme.na2a.model.p360.P360Attribute;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.VariantUtils;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationAttributesRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;

    private P360ToApplicationAttributesAdapter adapter;

    private ApplicationAttributesRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);
        adapter = new P360ToApplicationAttributesAdapter();

        testable = new ApplicationAttributesRepositoryImpl(properties, restTemplate, adapter);
    }

    @Test
    public void testFindByApplicationWithoutAttributes() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantAttribute/byItems?items={applicationId}"
                + "&fields="
                + "VariantAttribute.Identifier,VariantAttributeLang.Name,VariantAttributeValue.Value";

        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0 , 0);
        when(restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId("35070"))).thenReturn(response);

        List<P360Attribute> result = testable.findByApplication("35070");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByApplication() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantAttribute/byItems?items={applicationId}"
                + "&fields="
                + "VariantAttribute.Identifier,VariantAttributeLang.Name,VariantAttributeValue.Value";

        GetResponse.Row row1 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("componentApproval", "Component Approval", "My component approval"));
        GetResponse.Row row2 = GetResponseBuilder.buildRow("2", 2, Lists.newArrayList("impactedRegulations", "Impacted Regulations", "My impacted regulations"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 2, 2);

        when(restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId("35070"))).thenReturn(response);

        List<P360Attribute> result = testable.findByApplication("35070");

        result.sort(Comparator.comparing(P360Attribute::getAttributeId));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("35070", result.get(0).getApplicationId());
        assertEquals("componentApproval", result.get(0).getAttributeId());
        assertEquals("Component Approval", result.get(0).getName());
        assertEquals("My component approval", result.get(0).getValue());
        assertEquals("35070", result.get(1).getApplicationId());
        assertEquals("impactedRegulations", result.get(1).getAttributeId());
        assertEquals("Impacted Regulations", result.get(1).getName());
        assertEquals("My impacted regulations", result.get(1).getValue());
    }
}
