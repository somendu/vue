package com.vqd.tme.na2a.task.data.impl;

import com.vqd.tme.na2a.data.impl.P360Products;
import com.vqd.tme.na2a.data.impl.ProductRepositoryImpl;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.data.ProductRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductRepositoryImplTest {

    public static final String APPLICATION_ID = "17236";
    private P360Products p360Products;
    private ProductRepository testable;

    @Before
    public void setUp() {
        p360Products = mock(P360Products.class);
        testable = new ProductRepositoryImpl(p360Products);
    }

    @Test
    public void findByApplicationId() {
        Product productWithId = Product.builder().id("17229@1").build();
        Product product = Product.builder()
                .id("17229@1")
                .shortDescription("New car kit")
                .productNumber("Product2G_2109882580556585")
                .build();

        when(p360Products.getForVariant(APPLICATION_ID)).thenReturn(productWithId);
        when(p360Products.get("17229")).thenReturn(product);

        Product result = testable.findByApplicationId(APPLICATION_ID);

        assertNotNull(result);
        assertEquals("17229@1", result.getId());
        assertEquals("New car kit", result.getShortDescription());
        assertEquals("Product2G_2109882580556585", result.getProductNumber());
    }
}
