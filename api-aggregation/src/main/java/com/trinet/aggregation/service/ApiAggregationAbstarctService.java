package com.trinet.aggregation.service;

import org.springframework.beans.factory.annotation.Value;
/**
 * 
 * @author venkata_badri
 * Api aggregation abstract class is parent class to service classes.
 *
 */
abstract public class ApiAggregationAbstarctService {
    @Value("${api.config.host}")
    protected String API_CONFIG_HOST;

    @Value("${api.config.port}")
    protected String API_CONFIG_PORT;

    @Value("${api.config.context.path}")
    protected String API_CONFIG_CONTEXT_PATH;

    @Value("${api.config.global}")
    protected String API_CONFIG_GLOBAL;

    @Value("${api.config.global.gender}")
    protected String API_CONFIG_GLOBAL_GENDER;
    
    @Value("${api.config.global.maritalStatuses}")
    protected String API_CONFIG_GLOBAL_MARITAL_STATUS;
    
    @Value("${api.config.global.countries}")
    protected String API_CONFIG_GLOBAL_COUNTRIES;
    
    @Value("${api.config.global.countries.states}")
    protected String API_CONFIG_GLOBAL_COUNTRIES_STATES;
    
    @Value("${api.config.company}")
    protected String API_CONFIG_COMPANY;
    
    @Value("${api.config.company.locations}")
    protected String API_CONFIG_COMPANY_LOCATIONS;
    
    @Value("${api.config.company.departments}")
    protected String API_CONFIG_COMPANY_DEPARTMENTS;
    
    @Value("${api.config.company.jobs}")
    protected String API_CONFIG_COMPANY_JOBS;
}
