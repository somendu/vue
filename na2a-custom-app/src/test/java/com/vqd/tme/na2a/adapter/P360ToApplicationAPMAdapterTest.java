package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360ApplicationAPM;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class P360ToApplicationAPMAdapterTest {

    private P360ToApplicationAPMAdapter testable;

    @Before
    public void setUp() {
        testable = new P360ToApplicationAPMAdapter();
    }

    @Test
    public void testConvertMultipleRowsResponse() {
        GetResponse.Row row1 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse.Row row2 = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row1, row2), 2, 2);

        P360ApplicationAPM result = testable.convert(response);

        assertNull(result);
    }

    @Test
    public void testConvertNoRowsResponse() {
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0, 0);
        P360ApplicationAPM result = testable.convert(response);
        assertNull(result);
    }

    @Test
    public void testConvertNoValuesResponse() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList());
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationAPM result = testable.convert(response);

        assertNull(result);
    }

    @Test
    public void testConvertIncorrectValueCountResponse() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList(
                Lists.newArrayList("test1"),
                Lists.newArrayList("test2"),
                Lists.newArrayList("test3"),
                Lists.newArrayList("test4")));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationAPM result = testable.convert(response);

        assertNull(result);
    }

    @Test
    public void testConvertResponse() {
        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList(
                Lists.newArrayList("test1", "test2"),
                Lists.newArrayList("test3"),
                Lists.newArrayList("test4", "test5")));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        P360ApplicationAPM result = testable.convert(response);

        assertNotNull(result);
        assertEquals(Lists.newArrayList("test1", "test2"), result.getTmeStandard());
        assertEquals(Lists.newArrayList("test3"), result.getTmePackOffer());
        assertEquals(Lists.newArrayList("test4", "test5"), result.getTmeDemo());
    }
}
