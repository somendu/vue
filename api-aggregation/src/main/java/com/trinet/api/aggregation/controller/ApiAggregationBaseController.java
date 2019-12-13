package com.trinet.api.aggregation.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.trinet.api.aggregation.bean.ErrorDetail;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
/**
 * Base controller for the API Aggregation controllers.
 * @author venkata_badri
 *
 */

public class ApiAggregationBaseController {
    /**
     * instance of Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAggregationBaseController.class);

    
    /**
     * Handles the general error 
     * 
     * @param request 
     * @param exception
     * @return The {@link ResponseEntity}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(HttpServletRequest req, Exception exception) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);

        return new ResponseEntity<Object>(
                new ErrorDetail(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),req.getRequestURL().toString() ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
