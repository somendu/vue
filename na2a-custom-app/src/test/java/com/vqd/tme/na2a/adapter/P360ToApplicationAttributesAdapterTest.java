package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Attribute;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class P360ToApplicationAttributesAdapterTest {

    private P360ToApplicationAttributesAdapter testable;

    @Before
    public void setUp() throws Exception {
        testable = new P360ToApplicationAttributesAdapter();
    }

    @Test
    public void testConvertWithNoRows() {
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0,0);
        List<P360Attribute> result = testable.convert(response);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConvertWithIncorrectValueCount() {
        GetResponse.Row row1 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("componentApproval", "Component Approval", "My component Approval", "Something else"));
        GetResponse.Row row2 = GetResponseBuilder.buildRow("2", 2, Lists.newArrayList("impactedRegulations", "Impacted Regulations", "My impacted regulations"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 2, 2);
        List<P360Attribute> result = testable.convert(response);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("impactedRegulations", result.get(0).getAttributeId());
        assertEquals("Impacted Regulations", result.get(0).getName());
        assertEquals("My impacted regulations", result.get(0).getValue());
    }

    @Test
    public void testConvert() {
        GetResponse.Row row1 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("componentApproval", "Component Approval", "My component approval"));
        GetResponse.Row row2 = GetResponseBuilder.buildRow("2", 2, Lists.newArrayList("impactedRegulations", "Impacted Regulations", "My impacted regulations"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 2, 2);
        List<P360Attribute> result = testable.convert(response);

        result.sort(Comparator.comparing(P360Attribute::getAttributeId));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("componentApproval", result.get(0).getAttributeId());
        assertEquals("Component Approval", result.get(0).getName());
        assertEquals("My component approval", result.get(0).getValue());
        assertEquals("impactedRegulations", result.get(1).getAttributeId());
        assertEquals("Impacted Regulations", result.get(1).getName());
        assertEquals("My impacted regulations", result.get(1).getValue());
    }
}
