package com.vqd.tme.na2a.partlinking.controller;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.PartsFilter;
import com.vqd.tme.na2a.partlinking.persistence.impl.P360Parts;
import com.vqd.tme.na2a.partlinking.service.PartFilterService;
import com.vqd.tme.na2a.service.TmeProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PartsController.class)
public class PartsControllerTest {

    @Autowired
    private PartsController controller;

    @MockBean
    private P360Parts partsService;

    @MockBean
    private PartFilterService partFilterService;

    @MockBean
    private TmeProductService tmeProductService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void search() throws Exception {

        List<Part> parts = Lists.newArrayList(
                new Part("04130YZZJK", "Part 1", true),
                new Part("04130YZZFA", "Part 2", false)
        );

        when(partsService.search(any(PartsFilter.class))).thenReturn(parts);

        mockMvc.perform(get("/api/partlinking/parts?partNumber=04130&partName=Part&characteristic=Char"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"))
                .andExpect(jsonPath("$[0].partNumber", is("04130YZZJK")))
                .andExpect(jsonPath("$[0].partName", is("Part 1")))
                .andExpect(jsonPath("$[0].knownInNPA", is(true)))
                .andExpect(jsonPath("$[1].partNumber", is("04130YZZFA")))
                .andExpect(jsonPath("$[1].partName", is("Part 2")))
                .andExpect(jsonPath("$[1].knownInNPA", is(false)));

        verify(partsService).search(any(PartsFilter.class));
    }
}
