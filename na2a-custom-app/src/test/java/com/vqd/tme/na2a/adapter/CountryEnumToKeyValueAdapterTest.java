package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CountryEnumToKeyValueAdapterTest {

    private CountryEnumToKeyValueAdapter testable;

    @Before
    public void setUp() {
        testable = new CountryEnumToKeyValueAdapter(new CountryEntryToKeyValueAdapter());
    }

    @Test
    public void convert() {
        P360Enum countries = new P360Enum();

        P360EnumEntry beEntry = new P360EnumEntry();
        beEntry.setKey("BE");
        beEntry.setLabel("Belgium");

        P360EnumEntry deEntry = new P360EnumEntry();
        deEntry.setKey("DE");
        deEntry.setLabel("Germany");

        countries.setEntries(Lists.newArrayList(beEntry, deEntry));

        List<KeyValue> result = testable.convert(countries);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.stream().filter(country -> country.getKey().equalsIgnoreCase("BE")).count());
        assertEquals(1, result.stream().filter(country -> country.getValue().equalsIgnoreCase("Belgium")).count());
    }
}
