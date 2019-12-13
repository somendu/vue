package com.vqd.tme.na2a.service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LookupServiceTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;

    private LookupService testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9000");

        restTemplate = mock(RestTemplate.class);
        testable = new LookupService(properties, restTemplate);
    }

    @Test
    public void getInteriorColours() {
        String url = properties.getServer() + "/rest/V2.0/list/LookupValue/byLookup?lookup=ColoursInterior&fields=LookupValue.Code%2CLookupValueLang.Name(EN)";

        GetResponse response = getColourInteriorResponse();
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        List<KeyValue> result = testable.getInteriorColours();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("180@15", result.get(0).getKey());
        assertEquals("[00] 00", result.get(0).getValue());
    }

    @Test
    public void getExteriorColours() {
        String url = properties.getServer() + "/rest/V2.0/list/LookupValue/byLookup?lookup=ColoursExterior&fields=LookupValue.Code%2CLookupValueLang.Name(EN)";

        GetResponse response = getColourExteriorResponse();
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        List<KeyValue> result = testable.getExteriorColours();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("952@16", result.get(0).getKey());
        assertEquals("[1D4] Silver Metallic / Silver Ash", result.get(0).getValue());
    }

    @Test
    public void getTrimColours() {
        String url = properties.getServer() + "/rest/V2.0/list/LookupValue/byLookup?lookup=ColoursTrim&fields=LookupValue.Code%2CLookupValueLang.Name(EN)";

        GetResponse response = getTrimColourResponse();
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        List<KeyValue> result = testable.getTrimColours();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("1759@16", result.get(0).getKey());
        assertEquals("[EQ] EQ", result.get(0).getValue());
    }

    @Test
    public void getCancelledReasons() {
        String url = properties.getServer() + "/rest/V2.0/list/LookupValue/byLookup?lookup=ApplicationCancelledReason&fields=LookupValueLang.Name(EN)";

        GetResponse response = getCancelledReasonsResponse();
        when(restTemplate.getForObject(url, GetResponse.class)).thenReturn(response);

        List<KeyValue> result = testable.getCancelledReasons();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("7300_168@11", result.get(0).getKey());
        assertEquals("Deprecated", result.get(0).getValue());
    }

    private GetResponse getCancelledReasonsResponse() {
        List<GetResponse.Row> rows = new ArrayList<>();
        rows.add(GetResponseBuilder.buildRow("167@11", 7300, Lists.newArrayList("Error")));
        rows.add(GetResponseBuilder.buildRow("168@11", 7300, Lists.newArrayList("Deprecated")));
        rows.add(GetResponseBuilder.buildRow("169@11", 7300, Lists.newArrayList("Duplicate")));
        return GetResponseBuilder.buildResponse(rows, 3, 3);
    }

    private GetResponse getColourInteriorResponse() {
        List<GetResponse.Row> rows = new ArrayList<>();
        rows.add(GetResponseBuilder.buildRow("179@15", 7300, Lists.newArrayList("[00] Mellow white Av. with silver for Fabric and smooth leather", "[00] Mellow white Av. with silver for Fabric and smooth leather")));
        rows.add(GetResponseBuilder.buildRow("180@15", 7300, Lists.newArrayList("[00] 00", "[00] 00")));
        rows.add(GetResponseBuilder.buildRow("181@15", 7300, Lists.newArrayList("[10] Moonstone (Piano Black)", "[10] Moonstone (Piano Black)")));
        return GetResponseBuilder.buildResponse(rows, 3, 3);
    }

    private GetResponse getColourExteriorResponse() {
        List<GetResponse.Row> rows = new ArrayList<>();
        rows.add(GetResponseBuilder.buildRow("950@16", 7300, Lists.newArrayList("[2HH] Grayish Green Mica Metallic", "[2HH] Grayish Green Mica Metallic")));
        rows.add(GetResponseBuilder.buildRow("951@16", 7300, Lists.newArrayList("[1G6] New Medium Silver/ Grey", "[1G6] New Medium Silver/ Grey")));
        rows.add(GetResponseBuilder.buildRow("952@16", 7300, Lists.newArrayList("[1D4] Silver Metallic / Silver Ash", "[1D4] Silver Metallic / Silver Ash")));
        return GetResponseBuilder.buildResponse(rows, 3, 3);
    }

    private GetResponse getTrimColourResponse() {
        List<GetResponse.Row> rows = new ArrayList<>();
        rows.add(GetResponseBuilder.buildRow("1757@17", 7300, Lists.newArrayList("[FG] Fabric high + VX", "[FG] Fabric high + VX")));
        rows.add(GetResponseBuilder.buildRow("1758@16", 7300, Lists.newArrayList("[FE] Fabric D", "[FE] Fabric D")));
        rows.add(GetResponseBuilder.buildRow("1759@16", 7300, Lists.newArrayList("[EQ] EQ", "[EQ] EQ")));
        return GetResponseBuilder.buildResponse(rows, 3, 3);
    }
}
