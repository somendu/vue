/**
 * 
 */
package com.charging.station.support;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Checking count
 * 
 * @author somendu
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Scope("request")
//Added this for ensuring thread-safety as for each request it creates new object.
//scope = prototype can also be used
public class ChargeFieldCount {

	private int totalCount;
	private int startedCount;
	private int stoppedCount;

}
