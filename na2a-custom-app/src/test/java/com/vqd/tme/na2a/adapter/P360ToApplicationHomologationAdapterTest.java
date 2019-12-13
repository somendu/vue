package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
import com.vqd.tme.na2a.model.p360.P360Homologation;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.*;

public class P360ToApplicationHomologationAdapterTest {

    private P360ToHomologationAdapter adapter;
    private P360ToApplicationHomologationAdapter testable;

    @Before
    public void setUp() throws Exception {
        adapter = new P360ToHomologationAdapter();
        testable = new P360ToApplicationHomologationAdapter(adapter);
    }

    @Test
    public void testInvalidRowCount() {
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0, 0);

        P360ApplicationHomologation result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getInformation());
        assertNull(result.getHomologation());
        assertNull(result.getHubFitmentFlag());
        assertNull(result.getContentApproval());
        assertNull(result.getDeltaCDA());
    }

    @Test
    public void testInvalidValueCount() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("test"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationHomologation result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getInformation());
        assertNull(result.getHomologation());
        assertNull(result.getHubFitmentFlag());
        assertNull(result.getContentApproval());
        assertNull(result.getDeltaCDA());
    }

    @Test
    public void testEmptyValueCount() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationHomologation result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getInformation());
        assertNull(result.getHomologation());
        assertNull(result.getHubFitmentFlag());
        assertNull(result.getContentApproval());
        assertNull(result.getDeltaCDA());
    }

    @Test
    public void testConvert() {
        Map<Object, Object> homologation = Maps.newLinkedHashMap();
        homologation.put("id","1@1");
        homologation.put("entityId", 1);

        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("true", "info", homologation, "true", "contentApproval", "2.12"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationHomologation result = testable.convert(response);

        assertNotNull(result);
        assertEquals("info", result.getInformation());
        assertEquals(P360Homologation.builder().id("1@1").build(), result.getHomologation());
        assertTrue(result.getHubFitmentFlag());
        assertEquals("contentApproval", result.getContentApproval());
        assertEquals(BigDecimal.valueOf(2.12), result.getDeltaCDA());
    }
}
