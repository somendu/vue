package com.trinet.aggregation.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author laxmi_pabbaraju This is the main service used to get the global data
 *         by consuming the micro services
 */
@Service
public class ApiAggregationService extends ApiAggregationAbstarctService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiAggregationService.class);

    /**
     * This method will give global genders
     * 
     * @return the response of the api config global genders URI
     */
    public JSONObject getGenders() {
        JSONObject genders = null;
        LOGGER.info("Genders : " + API_CONFIG_HOST + ":" + API_CONFIG_PORT + API_CONFIG_CONTEXT_PATH + API_CONFIG_GLOBAL
                + API_CONFIG_GLOBAL_GENDER);
        RestTemplate restTemplate = new RestTemplate();
        genders = restTemplate.getForObject(API_CONFIG_HOST + ":" + API_CONFIG_PORT + API_CONFIG_CONTEXT_PATH
                + API_CONFIG_GLOBAL + API_CONFIG_GLOBAL_GENDER, JSONObject.class);
        LOGGER.info("Genders : " + genders);
        return genders;

    }

    /**
     * This method will give global Marital statuses
     * 
     * @return the response of the api config martal statueses URI
     */
    public JSONObject getMaritalStatuses() {
        JSONObject genders = null;
        LOGGER.info("Genders : " + API_CONFIG_HOST + ":" + API_CONFIG_PORT + API_CONFIG_CONTEXT_PATH + API_CONFIG_GLOBAL
                + API_CONFIG_GLOBAL_MARITAL_STATUS);
        RestTemplate restTemplate = new RestTemplate();
        genders = restTemplate.getForObject(API_CONFIG_HOST + ":" + API_CONFIG_PORT + API_CONFIG_CONTEXT_PATH
                + API_CONFIG_GLOBAL + API_CONFIG_GLOBAL_MARITAL_STATUS, JSONObject.class);
        LOGGER.info("Genders : " + genders);
        return genders;

    }

    /**
     * This method will give states of the given country
     * 
     * @param country
     *            is name of the country to get it's list of states
     * @return the response of the api config states URI
     */

    public JSONObject getStates(String country) {
        String uri = API_CONFIG_HOST + ":" + API_CONFIG_PORT + API_CONFIG_CONTEXT_PATH + API_CONFIG_GLOBAL
                + API_CONFIG_GLOBAL_COUNTRIES + "/" + country + API_CONFIG_GLOBAL_COUNTRIES_STATES;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject states = restTemplate.getForObject(uri, JSONObject.class);
        LOGGER.info("States : " + states);
        return states;

    }

    /**
     * This method will give list of departments for the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of departments
     * @return the response of the api config company departments URI
     */
    public JSONObject getCompanyDepartments(String compnayId) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject departments = restTemplate.getForObject(API_CONFIG_HOST + ":" + API_CONFIG_PORT
                + API_CONFIG_CONTEXT_PATH + API_CONFIG_COMPANY + "/" + compnayId + "/" + API_CONFIG_COMPANY_DEPARTMENTS,
                JSONObject.class);
        LOGGER.info("departments : " + departments);
        return departments;

    }

    /**
     * This method will give the list of locations for the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of locations
     * @return the response of the api config company locations URI
     */
    public JSONObject getCompanyLocations(String compnayId) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject locations = restTemplate.getForObject(API_CONFIG_HOST + ":" + API_CONFIG_PORT
                + API_CONFIG_CONTEXT_PATH + API_CONFIG_COMPANY + "/" + compnayId + "/" + API_CONFIG_COMPANY_LOCATIONS,
                JSONObject.class);
        LOGGER.info("locations : " + locations);
        return locations;

    }

    /**
     * This method will give job code of the given company
     * 
     * @param compnayId
     *            is id of the company to get it's list of job codes
     * @return the response of the api config company jobs URI
     */
    public JSONObject getCompanyJobCodes(String compnayId) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject departments = restTemplate.getForObject(API_CONFIG_HOST + ":" + API_CONFIG_PORT
                + API_CONFIG_CONTEXT_PATH + API_CONFIG_COMPANY + "/" + compnayId + "/" + API_CONFIG_COMPANY_JOBS,
                JSONObject.class);
        LOGGER.info("job codes : " + departments);
        return departments;

    }

}
