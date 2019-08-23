/**
 * 
 */
package com.charging.station.support;

import org.springframework.context.annotation.Scope;

import lombok.Data;

/**
 * 
 * request body class
 * 
 * @author somendu
 *
 */
@Data
@Scope("request")
//Added this for ensuring thread-safety as for each request it creates new object.
//scope = prototype can also be used
public class ChargeRequest {

	private String stationId;

}
