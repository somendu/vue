package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.P360ToApplicationDesignAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationDesignRepository;
import com.vqd.tme.na2a.model.PostApplicationDesign;
import com.vqd.tme.na2a.model.PostApplicationDesigns;
import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationDesignRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;

    private P360ToApplicationDesignAdapter adapter;

    private ApplicationDesignRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        adapter = new P360ToApplicationDesignAdapter();

        testable = new ApplicationDesignRepositoryImpl(properties, restTemplate, adapter);
    }

    @Test
    public void testFindByApplication() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantDesign/"
                + "byItems?items={applicationId}"
                + "&fields=VariantDesign.TMELCoOrdinates,VariantDesign.TMELCoOrdinatesRounded,VariantDesign.TMEReplacementAccessory,"
                + "VariantDesign.TMEIncalculable,VariantDesign.TMEMassOfRemovedEquipmentOriginal,VariantDesign.TMEMassOfRemovedEquipmentRounded";

        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList( "1.12", "1", "false", "false", "2.426", "2"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1 , 1);
        when(restTemplate.getForObject(url, GetResponse.class, "35172@1")).thenReturn(response);

        P360ApplicationDesign result = testable.findByApplication("35172@1");

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1.12), result.getLCoOrdinates());
        assertEquals(new Integer(1), result.getLCoOrdinatesRounded());
        assertFalse(result.getReplacementAccessory());
        assertFalse(result.getIncalculable());
        assertEquals(BigDecimal.valueOf(2.426),result.getDeltaMass());
        assertEquals(new Integer(2), result.getDeltaMassRounded());
    }

    @Test
    public void testEmptyDesignByFindByApplication() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantDesign/"
                + "byItems?items={applicationId}"
                + "&fields=VariantDesign.TMELCoOrdinates,VariantDesign.TMELCoOrdinatesRounded,VariantDesign.TMEReplacementAccessory,"
                + "VariantDesign.TMEIncalculable,VariantDesign.TMEMassOfRemovedEquipmentOriginal,VariantDesign.TMEMassOfRemovedEquipmentRounded";

        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1 , 1);
        when(restTemplate.getForObject(url, GetResponse.class, "35172@1")).thenReturn(response);

        P360ApplicationDesign result = testable.findByApplication("35172@1");

        assertNotNull(result);
        assertNull(result.getLCoOrdinates());
        assertNull(result.getLCoOrdinatesRounded());
        assertNull(result.getReplacementAccessory());
        assertNull(result.getIncalculable());
        assertNull(result.getDeltaMass());
        assertNull(result.getDeltaMassRounded());
    }

    @Test
    public void testUpdate() {
        PostApplicationDesign d1 = new PostApplicationDesign();
        d1.setApplicationId("1000");
        d1.setReplacementAccessory(true);
        d1.setDeltaMass(BigDecimal.valueOf(1.21));
        d1.setDeltaMassRounded(Integer.valueOf(1));
        d1.setIncalculable(false);
        d1.setLcoordinates(BigDecimal.valueOf(3.96));
        d1.setLcoordinatesRounded(Integer.valueOf(4));

        PostApplicationDesign d2 = new PostApplicationDesign();
        d2.setApplicationId("2000");
        d2.setReplacementAccessory(null);
        d2.setDeltaMass(null);
        d2.setDeltaMassRounded(null);
        d2.setIncalculable(null);
        d2.setLcoordinates(null);
        d2.setLcoordinatesRounded(null);

        PostApplicationDesigns designs = new PostApplicationDesigns();
        designs.addAll(Arrays.asList(d1, d2));

        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantDesign/";

        UpdateItemRequest request = new UpdateItemRequest();
        request.setColumns(Lists.newArrayList(
                new UpdateItemRequest.Column("VariantDesign.TMEReplacementAccessory"),
                new UpdateItemRequest.Column("VariantDesign.TMEMassOfRemovedEquipmentOriginal"),
                new UpdateItemRequest.Column("VariantDesign.TMEMassOfRemovedEquipmentRounded"),
                new UpdateItemRequest.Column("VariantDesign.TMEIncalculable"),
                new UpdateItemRequest.Column("VariantDesign.TMELCoOrdinates"),
                new UpdateItemRequest.Column("VariantDesign.TMELCoOrdinatesRounded")
        ));

        request.setRows(Lists.newArrayList(
                new UpdateItemRequest.Row()
                        .setObject(new UpdateItemRequest.Row.RowObject("1000@1"))
                        .setQualification(null)
                        .setValues(Lists.newArrayList("true", "1.21", "1", "false", "3.96", "4")),
                new UpdateItemRequest.Row()
                        .setObject(new UpdateItemRequest.Row.RowObject("2000@1"))
                        .setQualification(null)
                        .setValues(Lists.newArrayList(null, null, null, null, null, null))
        ));

        UpdateItemResponse response = new UpdateItemResponse();
        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
        counters.setUpdatedObjects(1);
        response.setCounters(counters);

        when(restTemplate.postForObject(url, request, UpdateItemResponse.class)).thenReturn(response);

        UpdateItemResponse result = testable.update(designs);

        assertEquals(response, result);
        verify(restTemplate).postForObject(url, request, UpdateItemResponse.class);
    }
}
