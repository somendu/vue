package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Maps;
import com.vqd.tme.na2a.model.p360.P360Homologation;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class P360ToHomologationAdapterTest {

    private P360ToHomologationAdapter testable;

    @Before
    public void setUp() {
        testable = new P360ToHomologationAdapter();
    }

    @Test
    public void testConvertWithNullSource() {
        P360Homologation result = testable.convert(null);
        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void testConvertWithEmptySource() {
        P360Homologation result = testable.convert(Maps.newHashMap());
        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void testConvert() {
        Map<String, String> source = Maps.newHashMap();
        source.put("id", "1@1");

        P360Homologation result = testable.convert(source);
        assertNotNull(result);
        assertEquals("1@1", result.getId());
    }
}
