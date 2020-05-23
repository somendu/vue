package com.vqd.tme.na2a.support;

import org.junit.Test;

import static org.junit.Assert.*;

public class VariantUtilsTest {

    @Test
    public void formatId() {
        assertEquals("1234@1", VariantUtils.formatId("1234"));
    }

    @Test
    public void testFormatNullId() {
        assertTrue(VariantUtils.formatId(null).isEmpty());
    }

    @Test
    public void testFormatEmptyId() {
        assertTrue(VariantUtils.formatId("").isEmpty());
    }

    @Test
    public void testFormatFullId() {
        assertEquals("1234@1", VariantUtils.formatId("1234@1"));
    }
}
