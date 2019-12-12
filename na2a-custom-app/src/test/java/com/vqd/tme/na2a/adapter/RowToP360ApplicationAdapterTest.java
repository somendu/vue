package com.vqd.tme.na2a.adapter;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.p360.GetResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RowToP360ApplicationAdapterTest {

    private RowToP360ApplicationAdapter testable;

    @Before
    public void setUp() {
        testable = new RowToP360ApplicationAdapter();
    }

    @Test
    public void testConvertIncorrectFieldCount() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList());

        P360Application result = testable.convert(row);

        assertNull(result);
    }

    @Test
    public void testConvert() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1",
                "vehicleGeneration",
                "My Application",
                "myApplication",
                "active",
                Lists.newArrayList("interior colour Id"),
                Lists.newArrayList("interior colour"),
                Lists.newArrayList("exterior colour Id"),
                Lists.newArrayList("exterior colour"),
                Lists.newArrayList("trim colour Id"),
                Lists.newArrayList("trim colour"),
                Lists.newArrayList("equipment types"),
                Lists.newArrayList("")
        ));

        P360Application result = testable.convert(row);

        assertNotNull(result);
        assertEquals("1000@1", result.getId());
        assertEquals("vehicleGeneration", result.getVehicleGeneration());
        assertEquals("My Application", result.getName());
        assertEquals("myApplication", result.getShortDescription());
        assertEquals("active", result.getStatus());
        assertEquals(1, result.getInteriorColourIds().size());
        assertEquals("interior colour Id", result.getInteriorColourIds().get(0));
        assertEquals(1, result.getInteriorColours().size());
        assertEquals("interior colour", result.getInteriorColours().get(0));
        assertEquals(1, result.getExteriorColourIds().size());
        assertEquals("exterior colour Id", result.getExteriorColourIds().get(0));
        assertEquals(1, result.getExteriorColours().size());
        assertEquals("exterior colour", result.getExteriorColours().get(0));
        assertEquals(1, result.getTrimColourIds().size());
        assertEquals("trim colour Id", result.getTrimColourIds().get(0));
        assertEquals(1, result.getTrimColours().size());
        assertEquals("trim colour", result.getTrimColours().get(0));
        assertFalse(result.getIsInPPO());
    }

    @Test
    public void testConvertNotInPPOWithEmptyList() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1",
                "vehicleGeneration",
                "My Application",
                "myApplication",
                "active",
                Lists.newArrayList("interior colour Id"),
                Lists.newArrayList("interior colour"),
                Lists.newArrayList("exterior colour Id"),
                Lists.newArrayList("exterior colour"),
                Lists.newArrayList("trim colour Id"),
                Lists.newArrayList("trim colour"),
                Lists.newArrayList("equipment types"),
                Lists.newArrayList()
        ));

        P360Application result = testable.convert(row);

        assertFalse(result.getIsInPPO());
    }

    @Test
    public void testConvertInPPO() {
        GetResponse.Row row = new GetResponse.Row();
        row.setValues(Lists.newArrayList(
                "1000@1",
                "vehicleGeneration",
                "My Application",
                "myApplication",
                "active",
                Lists.newArrayList("interior colour Id"),
                Lists.newArrayList("interior colour"),
                Lists.newArrayList("exterior colour Id"),
                Lists.newArrayList("exterior colour"),
                Lists.newArrayList("trim colour Id"),
                Lists.newArrayList("trim colour"),
                Lists.newArrayList("equipment types"),
                Lists.newArrayList("inPPO")
        ));

        P360Application result = testable.convert(row);

        assertTrue(result.getIsInPPO());
    }
}
