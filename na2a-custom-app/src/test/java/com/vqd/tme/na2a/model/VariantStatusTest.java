package com.vqd.tme.na2a.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class VariantStatusTest {

    @Test
    public void testFindByCode() {
        assertEquals(VariantStatus.findByCode("100").get(), VariantStatus.CREATED);
        assertEquals(VariantStatus.findByCode("300").get(), VariantStatus.WITHDRAWN);
    }

    @Test
    public void testFindByInvalidCode() {
        assertFalse(VariantStatus.findByCode("1").isPresent());
    }

    @Test
    public void testFindByValueCaseInsensitive() {
        assertEquals(VariantStatus.findByValue("CREATED").get(), VariantStatus.CREATED);
        assertEquals(VariantStatus.findByValue("Created").get(), VariantStatus.CREATED);
        assertEquals(VariantStatus.findByValue("created").get(), VariantStatus.CREATED);
        assertEquals(VariantStatus.findByValue("WITHDRAWN").get(), VariantStatus.WITHDRAWN);
        assertEquals(VariantStatus.findByValue("Withdrawn").get(), VariantStatus.WITHDRAWN);
        assertEquals(VariantStatus.findByValue("withdrawn").get(), VariantStatus.WITHDRAWN);
    }

    @Test
    public void testFindByInvalidValue() {
        assertFalse(VariantStatus.findByValue("test").isPresent());
    }

    @Test
    public void testIsCancelled() {
        assertTrue(VariantStatus.WITHDRAWN.isCancelled());
        assertFalse(VariantStatus.CREATED.isCancelled());
    }
}
