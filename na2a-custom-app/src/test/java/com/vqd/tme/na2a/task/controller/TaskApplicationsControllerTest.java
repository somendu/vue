package com.vqd.tme.na2a.task.controller;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.task.model.*;
import com.vqd.tme.na2a.task.service.TaskApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TaskApplicationsController.class)
public class TaskApplicationsControllerTest {

    @Autowired
    private TaskApplicationsController applicationsController;

    @MockBean
    private TaskApplicationService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationsController).build();

    }


    @Test
    public void getApplications() throws Exception {
        List<TaskApplicationResponse> response = Lists.newArrayList(
                TaskApplicationResponse.builder()
                        .taskApplication(TaskApplication.builder()
                                .id("17236")
                                .name("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)")
                                .product(TaskProduct.builder()
                                        .id("17229")
                                        .name("New carkit")
                                        .productNumber("Product2G_2109882580556585")
                                        .build())
                                .commodity(TaskCommodity.builder()
                                        .value("carkit")
                                        .build())
                                .parts(Lists.newArrayList(
                                        TaskPart.builder()
                                                .partNumber("0442302010")
                                                .partName("Spare fitting kit for TPMS")
                                                .type("12")
                                                .quantity(new BigDecimal(13))
                                                .build()
                                ))
                                .build())
                        .build()
        );

        when(service.get("TOYOTA", "Camry", "Camry 2001\\08 (FMC)")).thenReturn(response);

        mockMvc.perform(get("/api/task/applications?brand=TOYOTA&model=Camry&project=Camry 2001\\08 (FMC)"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"))
                .andExpect(jsonPath("$[0].taskApplication.id", is("17236")))
                .andExpect(jsonPath("$[0].taskApplication.name", is("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)")))
                .andExpect(jsonPath("$[0].taskApplication.commodity.value", is("carkit")))
                .andExpect(jsonPath("$[0].taskApplication.product.id", is("17229")))
                .andExpect(jsonPath("$[0].taskApplication.product.name", is("New carkit")))
                .andExpect(jsonPath("$[0].taskApplication.product.productNumber", is("Product2G_2109882580556585")))
                .andExpect(jsonPath("$[0].taskApplication.parts[0].partNumber", is("0442302010")))
                .andExpect(jsonPath("$[0].taskApplication.parts[0].partName", is("Spare fitting kit for TPMS")))
                .andExpect(jsonPath("$[0].taskApplication.parts[0].type", is("12")))
                .andExpect(jsonPath("$[0].taskApplication.parts[0].quantity", is(13)))
                .andExpect(jsonPath("$[0].volume", is(0)))
                .andExpect(jsonPath("$[0].price", is(0)))
                .andExpect(jsonPath("$[0].ppoCode", is("")))
                .andExpect(jsonPath("$[0].status", is("")));

        verify(service).get("TOYOTA", "Camry", "Camry 2001\\08 (FMC)");
    }
}
