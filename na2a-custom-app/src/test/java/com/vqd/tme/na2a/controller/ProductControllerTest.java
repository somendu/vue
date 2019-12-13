package com.vqd.tme.na2a.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.model.ResponseModel;
import com.vqd.tme.na2a.model.ResponseVariant;
import com.vqd.tme.na2a.service.ProductService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private ProductController controller;

    @MockBean
    private ProductService productService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getProducts() {
    }

    @Test
    public void save() throws Exception {
        ResponseVariant variant = new ResponseVariant();
        variant.setCancelled(false);
        variant.setEquipment(Lists.newArrayList());
        variant.setExteriorColours(Lists.newArrayList());
        variant.setInteriorColours(Lists.newArrayList());
        variant.setModel(new ResponseModel());
        variant.setSegment("segment");
        variant.setShortDescription("test description");
        variant.setTrimColours(Lists.newArrayList());
        variant.setVariantIds(Maps.newHashMap());
        variant.setVariantNos(Maps.newHashMap());
        variant.setVehicle(Lists.newArrayList());

        when(productService.save(variant)).thenReturn(variant);

        mockMvc.perform(post("/api/products")
                .content(convertToJson(variant))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.segment", is("segment")))
                .andExpect(jsonPath("$.shortDescription", is("test description")))
                .andExpect(jsonPath("$.cancelled", is(false)))
                .andReturn();

        verify(productService).save(variant);
    }

    private String convertToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
