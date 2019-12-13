package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360EntryKey;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CountryEntryToKeyValueAdapterTest {

    private CountryEntryToKeyValueAdapter testable;

    @Before
    public void setUp() throws Exception {
        testable = new CountryEntryToKeyValueAdapter();
    }

    @Test
    public void testConvert() {
        P360EnumEntry entry = new P360EnumEntry();
        entry.setKey("BE");
        entry.setLabel("Belgium");
        entry.setExternalCode("BE");
        entry.setSynonyms(Lists.newArrayList("belgium", "056", "be", "BE"));

        KeyValue result = testable.convert(entry);

        assertNotNull(result);
        assertEquals("BE", result.getKey());
        assertEquals("Belgium", result.getValue());
    }

    @Test
    public void testConvertWithIncorrectKeyType() {
        P360EnumEntry entry = new P360EnumEntry();
        entry.setKey(new P360EntryKey());
        entry.setLabel("Belgium");
        entry.setExternalCode("BE");
        entry.setSynonyms(Lists.newArrayList("belgium", "056", "be", "BE"));

        KeyValue result = testable.convert(entry);

        assertNotNull(result);
        assertNull(result.getKey());
        assertEquals("Belgium", result.getValue());
    }

    @Test
    public void testConvertWithoutEntry() {
        KeyValue result = testable.convert(null);
        assertNull(result);
    }
}
