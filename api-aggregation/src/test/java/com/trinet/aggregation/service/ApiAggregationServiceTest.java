package com.trinet.aggregation.service;

import static org.junit.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.trinet.ApiAggregationApplication;
import com.trinet.api.aggregation.util.APIAggregationConstants;

/**
 * 
 * @author venkata_badri
 * Test class for the ApiAgregation service.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiAggregationApplication.class)
@WebAppConfiguration
public class ApiAggregationServiceTest {
    
    @Autowired
    private ApiAggregationService aggregationService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getGendersTest(){
        JSONObject jsonResponse=aggregationService.getGenders();
        assertEquals(jsonResponse.get("_statusCode"),APIAggregationConstants.SUCCESS_CODE);      
        
    }
    
    
    @Test
    public void getCompanyDepartmentstTest(){
        JSONObject jsonResponse=aggregationService.getCompanyDepartments("31T");
        assertEquals(jsonResponse.get("_statusCode"),APIAggregationConstants.SUCCESS_CODE);      
        
    }
    
    @Test
    public void getCompanyJobCodesTest(){
        JSONObject jsonResponse=aggregationService.getCompanyJobCodes("31T");
        assertEquals(jsonResponse.get("_statusCode"),APIAggregationConstants.SUCCESS_CODE);
    }
        
        
        @Test
        public void getCompanyLocationsTest(){
            JSONObject jsonResponse=aggregationService.getCompanyLocations("31T");
            assertEquals(jsonResponse.get("_statusCode"),APIAggregationConstants.SUCCESS_CODE);      
            
        }
        
        
        @Test
        public void getStatesTest(){
            JSONObject jsonResponse=aggregationService.getStates("US");
            assertEquals(jsonResponse.get("_statusCode"),APIAggregationConstants.SUCCESS_CODE);      
            
        }


    

}
