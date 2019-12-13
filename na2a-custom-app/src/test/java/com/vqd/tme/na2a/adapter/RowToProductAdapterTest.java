package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.model.p360.P360EntryKey;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.service.EnumLookupService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RowToProductAdapterTest {

    private EnumLookupService enumLookupService;
    private ObjectToP360EntryKeyAdapter entryKeyAdapter;
    private RowToProductAdapter testable;

    @Before
    public void setUp() {
        enumLookupService = mock(EnumLookupService.class);
        entryKeyAdapter = new ObjectToP360EntryKeyAdapter();
        testable = new RowToProductAdapter(enumLookupService, entryKeyAdapter);
    }

    @Test
    public void testConvertInvalidFieldCount() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList());

        Product result = testable.convert(row);

        assertNull(result);
    }

    @Test
    public void testConvertNoColoursAndMaterialFound() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1", "12345", "myProduct", "", ""
        ));

        Product result = testable.convert(row);

        assertNotNull(result);
        assertEquals("1000@1", result.getId());
        assertEquals("12345", result.getProductNumber());
        assertEquals("myProduct", result.getShortDescription());
        assertNull(result.getColour());
        assertNull(result.getMaterial());
    }

    @Test
    public void testConvertNoColourAndMaterialId() {
        Map<String, String> colourMap = Maps.newHashMap();
        Map<String, String> materialMap = Maps.newHashMap();

        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1", "12345", "myProduct", colourMap, materialMap
        ));

        Product result = testable.convert(row);

        assertNotNull(result);
        assertEquals("1000@1", result.getId());
        assertEquals("12345", result.getProductNumber());
        assertEquals("myProduct", result.getShortDescription());
        assertNull(result.getColour());
        assertNull(result.getMaterial());
    }

    @Test
    public void testConvert() {
        Map<String, String> colourMap = Maps.newHashMap();
        colourMap.put("id", "1200@24");
        Map<String, String> materialMap = Maps.newHashMap();
        materialMap.put("id", "1400@20");

        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1", "12345", "myProduct", colourMap, materialMap
        ));

        P360EntryKey colourKey = new P360EntryKey();
        colourKey.setId("1200@24");
        colourKey.setLabel("Exterior-[057] White Pearl");

        P360EnumEntry colourEntry = new P360EnumEntry();
        colourEntry.setKey(colourKey);

        P360EntryKey materialKey = new P360EntryKey();
        materialKey.setId("1400@20");
        materialKey.setLabel("100% polyester");

        P360EnumEntry materialEntry = new P360EnumEntry();
        materialEntry.setKey(materialKey);

        when(enumLookupService.findColourById("1200@24")).thenReturn(colourEntry);
        when(enumLookupService.findMaterialById("1400@20")).thenReturn(materialEntry);

        Product result = testable.convert(row);

        assertNotNull(result);
        assertEquals("1000@1", result.getId());
        assertEquals("12345", result.getProductNumber());
        assertEquals("myProduct", result.getShortDescription());
        assertEquals(colourKey, result.getColour());
        assertEquals(materialKey, result.getMaterial());
    }
}
