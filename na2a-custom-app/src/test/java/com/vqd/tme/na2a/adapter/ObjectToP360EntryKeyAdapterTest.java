package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360EntryKey;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectToP360EntryKeyAdapterTest {

    private ObjectToP360EntryKeyAdapter testable;

    @Before
    public void setUp() throws Exception {
        testable = new ObjectToP360EntryKeyAdapter();
    }

    @Test
    public void testConvertNullObject() {
        P360EntryKey result = testable.convert(null);
        assertNull(result);
    }

    @Test
    public void testConvertString() {
        P360EntryKey result = testable.convert("test");
        assertNull(result);
    }

    @Test
    public void testConvert() {
        P360EntryKey object = new P360EntryKey();
        object.setId("1");
        object.setLabel("red");
        P360EntryKey result = testable.convert((Object)object);
        assertNotNull(result);
        assertEquals(object, result);
    }
}
