package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.adapter.P360ToApplicationHomologationAdapter;
import com.vqd.tme.na2a.adapter.P360ToHomologationAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationHomologationRepository;
import com.vqd.tme.na2a.model.PostApplicationHomologation;
import com.vqd.tme.na2a.model.PostApplicationHomologations;
import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
import com.vqd.tme.na2a.model.p360.P360Homologation;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationHomologationRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;
    private P360ToApplicationHomologationAdapter applicationHomologationAdapter;
    private P360ToHomologationAdapter homologationAdapter;

    private ApplicationHomologationRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        homologationAdapter = new P360ToHomologationAdapter();
        applicationHomologationAdapter = new P360ToApplicationHomologationAdapter(homologationAdapter);

        testable = new ApplicationHomologationRepositoryImpl(properties, restTemplate, applicationHomologationAdapter);
    }

    @Test
    public void testFindByApplication() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantHomologation/byItems?items={applicationId}"
                + "&fields=VariantHomologation.TMEWLTPFlag,VariantHomologation.TMEHomologationInformation,VariantHomologation.TMEHomologation,VariantHomologation.TMEAvailableForPPORequest,"
                + "VariantHomologation.TMEComponentApprovalDocument,VariantHomologation.TMEDeltaCDA";

        Map<Object, Object> homologation = Maps.newLinkedHashMap();
        homologation.put("id","1@1");
        homologation.put("entityId", 1);

        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("true", "info", homologation, "true", "contentApproval", "2.12"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);
        when(restTemplate.getForObject(url, GetResponse.class, "35172@1")).thenReturn(response);

        P360ApplicationHomologation result = testable.findByApplication("35172@1");

        assertNotNull(result);
        assertEquals(true, result.getWltp());
        assertEquals("info", result.getInformation());
        assertEquals(P360Homologation.builder().id("1@1").build(), result.getHomologation());
        assertTrue(result.getHubFitmentFlag());
        assertEquals("contentApproval", result.getContentApproval());
        assertEquals(BigDecimal.valueOf(2.12), result.getDeltaCDA());
    }

    @Test
    @Ignore
    public void testUpdate() {
        PostApplicationHomologation h1 = new PostApplicationHomologation();
        h1.setApplicationId("1000");
        h1.setWltpFlag(true);
        h1.setDeltaCDA(BigDecimal.valueOf(1.23));

        PostApplicationHomologation h2 = new PostApplicationHomologation();
        h2.setApplicationId("2000");
        h2.setWltpFlag(null);
        h2.setDeltaCDA(null);

        PostApplicationHomologation h3 = new PostApplicationHomologation();
        h3.setApplicationId("3000");
        h3.setWltpFlag(false);
        h3.setDeltaCDA(BigDecimal.TEN);

        PostApplicationHomologations homologations = new PostApplicationHomologations();
        homologations.addAll(Arrays.asList(h1, h2, h3));

        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantHomologation/";

        UpdateItemRequest request = new UpdateItemRequest();
        request.setColumns(Lists.newArrayList(
                new UpdateItemRequest.Column("VariantHomologation.TMEWLTPFlag"),
                new UpdateItemRequest.Column("VariantHomologation.TMEDeltaCDA")
        ));

        request.setRows(Lists.newArrayList(
                new UpdateItemRequest.Row()
                        .setObject(new UpdateItemRequest.Row.RowObject("1000@1"))
                        .setQualification(null)
                        .setValues(Lists.newArrayList("true", "1.23")),
                new UpdateItemRequest.Row()
                        .setObject(new UpdateItemRequest.Row.RowObject("2000@1"))
                        .setQualification(null)
                        .setValues(Lists.newArrayList(null, null)),
                new UpdateItemRequest.Row()
                        .setObject(new UpdateItemRequest.Row.RowObject("3000@1"))
                        .setQualification(null)
                        .setValues(Lists.newArrayList("false", "10"))
        ));

        UpdateItemResponse response = new UpdateItemResponse();
        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
        counters.setUpdatedObjects(1);
        response.setCounters(counters);

        when(restTemplate.postForObject(url, request, UpdateItemResponse.class)).thenReturn(response);

        List<UpdateItemResponse> result = testable.update(homologations);

        assertEquals(response, result.get(0));
        verify(restTemplate).postForObject(url, request, UpdateItemResponse.class);
    }
}
