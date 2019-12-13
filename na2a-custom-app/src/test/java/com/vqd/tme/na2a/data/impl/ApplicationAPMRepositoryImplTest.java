package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.P360ToApplicationAPMAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationAPMRepository;
import com.vqd.tme.na2a.model.p360.P360ApplicationAPM;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.VariantUtils;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationAPMRepositoryImplTest {

    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;
    private P360ToApplicationAPMAdapter adapter;
    private ApplicationAPMRepository testable;

    @Before
    public void setUp() throws Exception {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        adapter = new P360ToApplicationAPMAdapter();

        testable = new ApplicationAPMRepositoryImpl(properties, restTemplate, adapter);
    }

    @Test
    public void findById() {
        String applicationId = "8000";

        String url = "http://localhost:9090/rest/V2.0/list/Variant/VariantAPM/byItems?items={applicationId}&fields=VariantAPM.TMEStandardGrade,VariantAPM.TMEPackOffer,VariantAPM.TMEDemoConfiguration";

        GetResponse.Row row = GetResponseBuilder.buildRow("1", 1, Lists.newArrayList(
                Lists.newArrayList("test1"),
                Lists.newArrayList("test2"),
                Lists.newArrayList("test3")));
        GetResponse response = GetResponseBuilder.buildResponse(Lists.newArrayList(row), 1, 1);

        when(restTemplate.getForObject(url, GetResponse.class, "8000@1")).thenReturn(response);

        P360ApplicationAPM result = testable.findById(applicationId);

        assertNotNull(result);
        assertEquals(Lists.newArrayList("test1"), result.getTmeStandard());
        assertEquals(Lists.newArrayList("test2"), result.getTmePackOffer());
        assertEquals(Lists.newArrayList("test3"), result.getTmeDemo());
    }
}
