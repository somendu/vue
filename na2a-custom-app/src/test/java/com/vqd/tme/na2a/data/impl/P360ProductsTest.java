package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.RowToProductAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class P360ProductsTest {

    private static final String PRODUCT_ID = "111";

    private P360Products testable;
    private RestTemplate restTemplate;
    private RowToProductAdapter productAdapter;

    private InformaticaPimProperties properties;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9000");

        restTemplate = mock(RestTemplate.class);
        productAdapter = mock(RowToProductAdapter.class);

        testable = new P360Products(properties, restTemplate, productAdapter);
    }

    @Test
    public void testGetProductById() {
        GetResponse response = getProductResponse();
        when(restTemplate.getForObject(properties.getServer()
                        + "/rest/V2.0/list/Product2G/byItems"
                        + "?items={productId}"
                        + "&fields=Product2G.Id,Product2G.ProductNo,Product2GLang.DescriptionShort(EN),Product2G.TMEColour,Product2G.TMEMaterial",
                GetResponse.class,
                String.format("%s@1", PRODUCT_ID))).thenReturn(response);

        Product product = Product.builder()
                .id("111")
                .productNumber("58500 009")
                .shortDescription("Textile Floormats 830g m²")
                .build();

        when(productAdapter.convert(any(GetResponse.Row.class))).thenReturn(product);

        Product result = testable.get(PRODUCT_ID);

        assertNotNull(result);
        assertEquals("111", result.getId());
        assertEquals("58500 009", result.getProductNumber());
        assertEquals("Textile Floormats 830g m²", result.getShortDescription());
    }

    @Test
    public void testGetProductByIdNotFound() {
        GetResponse response = getEmptyProductResponse();

        when(restTemplate.getForObject(properties.getServer()
                        + "/rest/V2.0/list/Product2G/byItems"
                        + "?items={productId}"
                        + "&fields=Product2G.Id,Product2G.ProductNo,Product2GLang.DescriptionShort(EN),Product2G.TMEColour,Product2G.TMEMaterial",
                GetResponse.class,
                String.format("%s@1", PRODUCT_ID))).thenReturn(response);

//        when(productAdapter.convert(any(GetResponse.Row.class))).thenReturn(null);

        Product product = testable.get(PRODUCT_ID);

        assertNull(product);
    }

    private GetResponse getProductResponse() {
        List<Object> values = new ArrayList<>();
        values.add("111");
        values.add("58500 009");
        values.add("Textile Floormats 830g m²");

        List<GetResponse.Row> rows = new ArrayList<>();
        rows.add(GetResponseBuilder.buildRow("111@1", 1100, values));

        return GetResponseBuilder.buildResponse(rows, 1 , 1);
    }

    private GetResponse getEmptyProductResponse() {
        return GetResponseBuilder.buildResponse(new ArrayList<>(), 0, 0);
    }

}
