package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RowToKeyValueAdapterTest {

    private RowToKeyValueAdapter testable;

    @Before
    public void setUp() {
        testable = new RowToKeyValueAdapter();
    }

    @Test
    public void convert() {
        KeyValue result = testable.convert(
                GetResponseBuilder
                        .buildRow("1@1", 1, Lists.newArrayList("id", "value", "", "123")));
        assertNotNull(result);
        assertEquals("id", result.getKey());
        assertEquals("value", result.getValue());
        assertTrue(result.getKatashiki().isEmpty());
        assertEquals("123", result.getParentKey());
    }

    @Test
    public void convertNoRowValues() {
        KeyValue result = testable.convert(
                GetResponseBuilder.buildRow("1@1", 1, null));
        assertNotNull(result);
        assertNull(result.getKey());
        assertNull(result.getValue());
        assertTrue(result.getKatashiki().isEmpty());
    }
}
