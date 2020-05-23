/**
 *
 */
package com.partspointgroup.priceapproval.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.partspointgroup.priceapproval.support.ItemUpdateException;

 

/**
 * @author SomenduMaiti
 *
 */
@ControllerAdvice
public class SupplierArticleControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, Object> handleRemarksUpdateException(ItemUpdateException ex) {
		  return new HashMap<String, Object>();
	}
}
