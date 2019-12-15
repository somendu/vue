package com.trinet.api.aggregation.util;

import org.json.simple.JSONObject;

/**
 * 
 * @author venkata_badri
 *  Util class to hold application common methods.
 */
public class ApiAggregationUtil {

    private ApiAggregationUtil() {
      
    }

    /**
     * while aggregation, we don't need the individual response status data once
     * we receive successful data
     * 
     * @param object
     */
    public static JSONObject removeStatusDataFromResponse(JSONObject object) {
        String statusCode = (String) object.get(APIAggregationConstants.STATUS_CODE_STRING);

        if (statusCode != null && statusCode.equals(APIAggregationConstants.SUCCESS_CODE)) {

            object.remove(APIAggregationConstants.STATUS_CODE_STRING);
            String _statusText = (String) object.get(APIAggregationConstants.STATUS_TEXT_STRING);
            if (_statusText != null) {
                object.remove(APIAggregationConstants.STATUS_TEXT_STRING);
            }
            String _statusMessage = (String) object.get(APIAggregationConstants.STATUS_MESSAGE_STRING);
            if (_statusMessage != null) {
                object.remove(APIAggregationConstants.STATUS_MESSAGE_STRING);
            }

        }

        return object;

    }

}
