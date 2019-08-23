/**
 * 
 */
package com.charging.station.service;

import java.util.List;

import com.charging.station.support.ChargeField;
import com.charging.station.support.ChargeFieldCount;
import com.charging.station.support.ChargeRequest;

/**
 * 
 * Service layer interface for the controller
 * 
 * @author somendu
 *
 */
public interface ControllerService {

	/**
	 * Start Charging Session
	 * 
	 * @param request
	 * @return
	 */
	public ChargeField startChargingSession(ChargeRequest request);

	/**
	 * Stop Charging Session
	 * 
	 * @param stationID
	 * @return
	 */
	public ChargeField stopChargingSession(String stationID);

	/**
	 * Get All charging Sessions
	 * 
	 * @return
	 */
	public List<ChargeField> getChargingSessions();

	/**
	 * Get All charging Summary
	 * 
	 * @return
	 */
	public ChargeFieldCount getChargingSummary();

}
