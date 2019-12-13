package com.vqd.tme.na2a.task.service.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.task.adapter.P360TaskApplicationToTaskApplicationAdapter;
import com.vqd.tme.na2a.task.adapter.P360TaskProductToTaskProductAdapter;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.data.CommodityRepository;
import com.vqd.tme.na2a.task.data.TaskPartRepository;
import com.vqd.tme.na2a.data.ProductRepository;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.task.model.TaskApplicationResponse;
import com.vqd.tme.na2a.task.model.TaskPart;
import com.vqd.tme.na2a.task.service.TaskApplicationService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskApplicationServiceImplTest {

    public static final String APPLICATION_ID = "17236";
    private ApplicationRepository applicationRepository;
    private ProductRepository productRepository;
    private CommodityRepository commodityRepository;
    private TaskPartRepository partRepository;

    private P360TaskApplicationToTaskApplicationAdapter applicationAdapter;
    private P360TaskProductToTaskProductAdapter productAdapter;

    private TaskApplicationService testable;

    @Before
    public void setUp() {
        applicationRepository = mock(ApplicationRepository.class);
        productRepository = mock(ProductRepository.class);
        commodityRepository = mock(CommodityRepository.class);
        partRepository = mock(TaskPartRepository.class);

        applicationAdapter = new P360TaskApplicationToTaskApplicationAdapter();
        productAdapter = new P360TaskProductToTaskProductAdapter();

        testable = new TaskApplicationServiceImpl(
                applicationRepository,
                productRepository,
                commodityRepository,
                partRepository,
                applicationAdapter,
                productAdapter
        );

    }

    @Test
    public void get() {
        List<P360Application> p360Applications = Lists.newArrayList(
                P360Application.builder().id(APPLICATION_ID).shortDescription("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)").build()
        );

        Product p360Product = Product.builder()
                                    .id("17229").shortDescription("New carkit").productNumber("Product2G_2109882580556585").build();
        String p360Commodity = "Car Kit";
        List<TaskPart> p360Parts = Lists.newArrayList(
                TaskPart.builder()
                        .partNumber("0442302010")
                        .partName("Spare fitting kit for TPMS")
                        .type("12")
                        .quantity(new BigDecimal(13))
                        .build()
        );

        when(applicationRepository.findByBrandAndModelAndProject("TOYOTA", "Camry", "Camry 2001\\08 (FMC)")).thenReturn(p360Applications);
        when(productRepository.findByApplicationId(APPLICATION_ID)).thenReturn(p360Product);
        when(commodityRepository.findByApplicationId(APPLICATION_ID)).thenReturn(p360Commodity);
        when(partRepository.findByApplicationId(APPLICATION_ID)).thenReturn(p360Parts);

        List<TaskApplicationResponse> responses = testable.get("TOYOTA", "Camry", "Camry 2001\\08 (FMC)");

        assertEquals(1, responses.size());
        assertTrue(responses.get(0).getPpoCode().isEmpty());
        assertTrue(responses.get(0).getStatus().isEmpty());
        assertEquals(BigDecimal.ZERO, responses.get(0).getPrice());
        assertEquals(BigDecimal.ZERO, responses.get(0).getVolume());
        assertEquals(APPLICATION_ID, responses.get(0).getTaskApplication().getId());
        assertEquals("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)", responses.get(0).getTaskApplication().getName());
        assertEquals("Car Kit", responses.get(0).getTaskApplication().getCommodity().getValue());
        assertEquals("17229", responses.get(0).getTaskApplication().getProduct().getId());
        assertEquals("New carkit", responses.get(0).getTaskApplication().getProduct().getName());
        assertEquals("Product2G_2109882580556585", responses.get(0).getTaskApplication().getProduct().getProductNumber());
        assertEquals("Spare fitting kit for TPMS", responses.get(0).getTaskApplication().getParts().get(0).getPartName());
        assertEquals("0442302010", responses.get(0).getTaskApplication().getParts().get(0).getPartNumber());
        assertEquals("12", responses.get(0).getTaskApplication().getParts().get(0).getType());
        assertEquals(new BigDecimal(13), responses.get(0).getTaskApplication().getParts().get(0).getQuantity());
    }
}
