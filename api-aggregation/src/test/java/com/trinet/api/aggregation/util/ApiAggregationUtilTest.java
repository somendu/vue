package com.trinet.api.aggregation.util;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.trinet.ApiAggregationApplication;

/**
 * Test class for ApiAggregationUtil
 * 
 * @author venkata_badri
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiAggregationApplication.class)
@WebAppConfiguration
public class ApiAggregationUtilTest {
    private JSONObject data = new JSONObject();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JSONArray jSONArray = new JSONArray();

        JSONObject jSONObject = new JSONObject();

        jSONObject.put("key", "F");
        jSONObject.put("value", "Female");

        jSONArray.add(jSONObject);
        JSONObject jSONObject1 = new JSONObject();

        jSONObject1.put("key", "M");
        jSONObject1.put("value", "Male");

        jSONArray.add(jSONObject1);

        data.put("data", jSONArray);
        data.put("_statusCode", "200");
        data.put("_statusText", "OK");
        data.put("_statusMessage", "Success");
    }

    @Test
    public void removeStatusDataFromResponse() {

        JSONObject resultValue = ApiAggregationUtil.removeStatusDataFromResponse(data);

        assertTrue(resultValue != null);

    }

}
