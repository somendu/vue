package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class P360ToApplicationDesignAdapterTest {

    private P360ToApplicationDesignAdapter testable;

    @Before
    public void setUp() throws Exception {
        testable = new P360ToApplicationDesignAdapter();
    }

    @Test
    public void testInvalidRows() {
        GetResponse.Row row1 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse.Row row2 = GetResponseBuilder.buildRow("2", 2, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 2, 2);

        P360ApplicationDesign result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getLCoOrdinates());
        assertNull(result.getLCoOrdinatesRounded());
        assertNull(result.getReplacementAccessory());
        assertNull(result.getIncalculable());
        assertNull(result.getDeltaMass());
        assertNull(result.getDeltaMassRounded());
    }

    @Test
    public void testWithEmptyValues() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationDesign result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getLCoOrdinates());
        assertNull(result.getLCoOrdinatesRounded());
        assertNull(result.getReplacementAccessory());
        assertNull(result.getIncalculable());
        assertNull(result.getDeltaMass());
        assertNull(result.getDeltaMassRounded());
    }


    @Test
    public void testWithNoValues() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, null);
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationDesign result = testable.convert(response);

        assertNotNull(result);
        assertNull(result.getLCoOrdinates());
        assertNull(result.getLCoOrdinatesRounded());
        assertNull(result.getReplacementAccessory());
        assertNull(result.getIncalculable());
        assertNull(result.getDeltaMass());
        assertNull(result.getDeltaMassRounded());
    }


    @Test
    public void testWithValues() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList("1.12", "1", "false", "false", "2.426", "2"));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationDesign result = testable.convert(response);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1.12), result.getLCoOrdinates());
        assertEquals(new Integer(1), result.getLCoOrdinatesRounded());
        assertFalse(result.getReplacementAccessory());
        assertFalse(result.getIncalculable());
        assertEquals(BigDecimal.valueOf(2.426),result.getDeltaMass());
        assertEquals(new Integer(2), result.getDeltaMassRounded());
    }
}
