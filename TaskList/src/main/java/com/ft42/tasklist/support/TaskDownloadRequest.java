/**
 * 
 */
package com.ft42.tasklist.support;

import org.springframework.context.annotation.Scope;

import lombok.Data;

/**
 * <Description>
 * 
 * @author Somendu
 *
 * @Since 30-May-2020
 */
@Data
@Scope("request")
//Added this for ensuring thread-safety as for each request it creates new object.
//scope = prototype can also be used
public class TaskDownloadRequest {

	private String consName;
	private String dateFrom;
	private String dateTo;
}
