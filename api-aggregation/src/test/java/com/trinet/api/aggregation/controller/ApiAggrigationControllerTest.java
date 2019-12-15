package com.trinet.api.aggregation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.trinet.ApiAggregationApplication;
import com.trinet.api.aggregation.controller.ApiAggregationController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiAggregationApplication.class)
@WebAppConfiguration
public class ApiAggrigationControllerTest {
   
    /* Instance of Logger Factory */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAggregationController.class);

    

    @Mock
    private ApiAggregationController apiAggregationController;

    private MockMvc mvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(apiAggregationController).build();
    }

    @Test
    public void configServiceTest() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/config").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
           LOGGER.error("error in configServiceGenderTest" + e);
           assertFalse(true);
        }

    }

    @Test
    public void configServiceGenderTest()  {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/genders").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in configServiceGenderTest" + e);
            assertFalse(true);
        }

    }

    @Test
    public void getCountryStatesTest(){

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/countries/US/states").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in getCountryStatesTest" + e);
            assertFalse(true);
        }

    }

    @Test
    public void getCompanyDetailsByIDTest() {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/31T").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in getCompanyDetailsByIDTest" + e);
            assertFalse(true);
        }

    }

    @Test
    public void getCompanyLocationsTest() {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/31T/locations").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in getCompanyLocationsTest" + e);
            assertFalse(true);
        }

    }

    @Test
    public void getCompanyDepartmentsTest() {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/31T/departments").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in getCompanyDepartmentsTest" + e);
            assertFalse(true);
        }

    }

    @Test
    public void getCompanyJobCodesTest() {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/config/31T/job-codes").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            LOGGER.error("error in getCompanyJobCodesTest" + e);
            assertFalse(true);
        }

    }

}
