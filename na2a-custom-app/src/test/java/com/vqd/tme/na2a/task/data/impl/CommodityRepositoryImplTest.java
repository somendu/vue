package com.vqd.tme.na2a.task.data.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.data.impl.CommodityRepositoryImpl;
import com.vqd.tme.na2a.partlinking.model.VariantMetaData;
import com.vqd.tme.na2a.partlinking.persistence.impl.P360VariantMetaDataResolver;
import com.vqd.tme.na2a.data.CommodityRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommodityRepositoryImplTest {

    private static final String APPLICATION_ID = "17236";

    private P360VariantMetaDataResolver resolver;
    private CommodityRepository testable;

    @Before
    public void setUp() {
        resolver = mock(P360VariantMetaDataResolver.class);
        testable = new CommodityRepositoryImpl(resolver);
    }

    @Test
    public void findByApplicationId() {
        Map<String, VariantMetaData> variantMetaData = Maps.newHashMap();
        VariantMetaData metaData = new VariantMetaData();
        metaData.setCommodity("Car kit");
        variantMetaData.put(APPLICATION_ID, metaData);

        when(resolver.resolve(Lists.newArrayList(APPLICATION_ID))).thenReturn(variantMetaData);

        String result = testable.findByApplicationId(APPLICATION_ID);

        assertEquals("Car kit", result);
    }

    @Test
    public void findByApplicationIdWithoutMetaData() {
        Map<String, VariantMetaData> variantMetaData = Maps.newHashMap();
        VariantMetaData metaData = new VariantMetaData();
        variantMetaData.put(APPLICATION_ID, null);

        when(resolver.resolve(Lists.newArrayList(APPLICATION_ID))).thenReturn(variantMetaData);

        String result = testable.findByApplicationId(APPLICATION_ID);

        assertNull(result);
    }
}
