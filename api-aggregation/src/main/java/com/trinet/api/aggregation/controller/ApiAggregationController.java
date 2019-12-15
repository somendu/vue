package com.trinet.api.aggregation.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinet.aggregation.service.ApiAggregationService;

import com.trinet.api.aggregation.bean.Response;
import com.trinet.api.aggregation.util.ApiAggregationUtil;
import com.trinet.api.aggregation.util.APIAggregationConstants;

/**
 * @author laxmi_pabbaraju Aggregation controller will in turn invokes micro
 *         services for global data It returns response data
 */

@RestController
@RequestMapping("/config")
public class ApiAggregationController extends ApiAggregationBaseController {

    /* Instance of Logger Factory */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAggregationController.class);
    /**
     * Instance of the ApiAggregationService
     */
    private ApiAggregationService aggregationService;

    /**
     * 
     * @param aggregationService
     */
    @Autowired
    public void setAggregationService(ApiAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    /**
     * Here we are aggregating genders and marital-statuses
     * 
     * @return
     */

    @RequestMapping
    public Response apiAggregation() {
        LOGGER.info("apiAggregation");
        /* preparing consolidated response */
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();

        map.put("genders", ApiAggregationUtil.removeStatusDataFromResponse(aggregationService.getGenders()));
        map.put("marital-statuses",
                ApiAggregationUtil.removeStatusDataFromResponse(aggregationService.getMaritalStatuses()));

        Response resp = new Response();

        /* adding genders and states data to response */
        resp.setData(map);
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;

    }

    /**
     * this method will give genders data.
     * 
     * @return the response of the api config genders URI
     */
    @RequestMapping(value = "/genders")
    public Response getGenders() {
        LOGGER.info("getGenders");
        Response resp = new Response();
        resp.setData(aggregationService.getGenders().get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;

    }

    /**
     * this method will give marital statuses.
     * 
     * @return the response of the api config martal statueses URI
     */
    @RequestMapping(value = "/marital-statuses")
    public Response getMaritalStatuses() {
        LOGGER.info("getMaritalStatuses");
        Response resp = new Response();
        resp.setData(aggregationService.getMaritalStatuses().get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;

    }

    /**
     * this method will give all departments of the given company
     * 
     * @param country
     *            is name of the country to get it's list of states
     * @return the response of the api config states URI
     */
    @RequestMapping(value = "/{countryId}/states")
    public Response getCountryStates(@PathVariable("countryId") String countryId) {
        LOGGER.info("getCountryStates");
        Response resp = new Response();
        resp.setData(aggregationService.getStates(countryId).get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;
    }

    /**
     * Here we are aggregating jobTitles, departments and locations
     * 
     * @param compnayId
     *            is id of the company to get it's aggregated data
     * @return aggregated company data
     */
    @RequestMapping(value = "/{companyId}")
    public Response getCompanyDetailsByID(@PathVariable("companyId") String companyId) {
        LOGGER.info("getCompanyDetailsByID");
        Response resp = new Response();

        JSONObject jobTitles = ApiAggregationUtil
                .removeStatusDataFromResponse(aggregationService.getCompanyJobCodes(companyId));
        JSONObject depts = ApiAggregationUtil
                .removeStatusDataFromResponse(aggregationService.getCompanyDepartments(companyId));
        JSONObject locations = ApiAggregationUtil
                .removeStatusDataFromResponse(aggregationService.getCompanyLocations(companyId));
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        map.put("job-titles", jobTitles);
        map.put("departments", depts);
        map.put("locations", locations);
        resp.setData(map);
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;
    }

    /**
     * this method will give all departments of the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of departments
     * @return the response of the api config company departments URI
     */
    @RequestMapping(value = "/{companyId}/departments")
    public Response getCompanyDepartments(@PathVariable("companyId") String companyId) {
        LOGGER.info("getCompanyDepartments");
        Response resp = new Response();
        resp.setData(aggregationService.getCompanyDepartments(companyId).get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;
    }

    /**
     * this method will give all locations of the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of locations
     * @return the response of the api config company locations URI
     */
    @RequestMapping(value = "/{companyId}/locations")
    public Response getCompanyLocations(@PathVariable("companyId") String companyId) {
        LOGGER.info("getCompanyLocations");
        Response resp = new Response();
        resp.setData(aggregationService.getCompanyLocations(companyId).get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;
    }

    /**
     * this method will give all job titles of the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of job codes
     * @return the response of the api config company jobs URI
     */
    @RequestMapping(value = "/{companyId}/job-codes")
    public Response getCompanyJobCodes(@PathVariable("companyId") String companyId) {
        LOGGER.info("getCompanyJobCodes");
        Response resp = new Response();
        resp.setData(aggregationService.getCompanyJobCodes(companyId).get("data"));
        resp.setStatusCode(APIAggregationConstants.SUCCESS_CODE);
        resp.setStatusText(APIAggregationConstants.MESSAGE_RESPONSE_TEXT_OK);
        resp.setStatusMessage(APIAggregationConstants.MESSAGE_RESPONSE_SUCCESS);

        return resp;
    }

}
