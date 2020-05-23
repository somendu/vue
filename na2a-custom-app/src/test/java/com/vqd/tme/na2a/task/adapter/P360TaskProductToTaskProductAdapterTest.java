package com.vqd.tme.na2a.task.adapter;

import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.task.model.TaskProduct;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class P360TaskProductToTaskProductAdapterTest {

    private P360TaskProductToTaskProductAdapter testable;

    @Before
    public void setUp() {
        testable = new P360TaskProductToTaskProductAdapter();
    }

    @Test
    public void convert() {
        Product p360Product = Product.builder()
                                    .id("17229")
                                    .shortDescription("New Car kit")
                                    .productNumber("12345")
                                    .build();

        TaskProduct result = testable.convert(p360Product);

        assertNotNull(result);
        assertEquals("17229", result.getId());
        assertEquals("New Car kit", result.getName());
        assertEquals("12345", result.getProductNumber());
    }

    @Test
    public void convertProductIsNull() {
        TaskProduct result = testable.convert(null);
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getProductNumber());
    }
}
