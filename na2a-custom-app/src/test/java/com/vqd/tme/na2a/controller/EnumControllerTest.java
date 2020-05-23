package com.vqd.tme.na2a.controller;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.CountryEnumToKeyValueAdapter;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.service.EnumLookupService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EnumController.class)
public class EnumControllerTest {

    @Autowired
    private EnumController controller;

    @MockBean
    private EnumLookupService enumService;

    @MockBean
    private CountryEnumToKeyValueAdapter countryAdapter;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getCountries() throws Exception {

        P360Enum countryEnum = new P360Enum();
        List<KeyValue> countries = Lists.newArrayList(
                new KeyValue("DE", "Germany"),
                new KeyValue("BE", "Belgium")
        );

        when(enumService.getTMECountryEnum()).thenReturn(countryEnum);
        when(countryAdapter.convert(countryEnum)).thenReturn(countries);

        mockMvc.perform(get("/api/all/countries"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"))
                .andExpect(jsonPath("$[0].key", is("DE")))
                .andExpect(jsonPath("$[0].value", is("Germany")))
                .andExpect(jsonPath("$[1].key", is("BE")))
                .andExpect(jsonPath("$[1].value", is("Belgium")));

        verify(enumService).getTMECountryEnum();
        verify(countryAdapter).convert(countryEnum);
    }
}
