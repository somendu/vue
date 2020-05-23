package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RowToP360StructureAdapterTest {

    private RowToP360StructureAdapter testable;

    @Before
    public void setUp() {
        testable = new RowToP360StructureAdapter();
    }

    @Test
    public void testConvert() {
        GetResponse.Row row = GetResponseBuilder.buildRow("9000", 9000, Lists.newArrayList("productStructure","Product Structure"));

        P360Structure result = testable.convert(row);

        assertNotNull(result);
        assertEquals("9000", result.getObjectId());
        assertEquals("productStructure", result.getIdentifier());
        assertEquals("Product Structure", result.getName());
        assertEquals(P360StructureType.PRODUCT_STRUCTURE, result.getType());
    }

    @Test
    public void testConvertNoObjectInRow() {
        P360Structure result = testable.convert(new GetResponse.Row());
        assertNull(result);
    }

    @Test
    public void testConvertNoValuesInRow() {
        GetResponse.Row row = new GetResponse.Row();
        row.setObject(new GetResponse.Row.RowObject());
        row.setValues(Lists.newArrayList());

        P360Structure result= testable.convert(row);
        assertNull(result);
    }

    @Test
    public void testConvertToManyValuesInRow() {
        GetResponse.Row row = new GetResponse.Row();
        row.setObject(new GetResponse.Row.RowObject());
        row.setValues(Lists.newArrayList("productStructure","Product Structure", "Vehicle Structure"));

        P360Structure result= testable.convert(row);
        assertNull(result);
    }
}
