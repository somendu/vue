package com.charging.station.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charging.station.service.ControllerService;
import com.charging.station.support.ChargeField;
import com.charging.station.support.ChargeFieldCount;
import com.charging.station.support.ChargeRequest;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller Class for creating end points
 * 
 * @author somendu
 *
 */
@RestController
@RequestMapping("/api/charging")
@RequiredArgsConstructor
public class Controller {

	private final ControllerService controllerService;

	// Charging Sessions Method for Setting Session to Start
	@PostMapping("/chargingSessions")

	public ChargeField setStartSession(@RequestBody ChargeRequest request) {

		return controllerService.startChargingSession(request);

	}

	// Stop the Session based on passed station ID
	@PutMapping("/chargingSessions/{stationID}")
	public ChargeField setStartSession(@PathVariable String stationID) {

		return controllerService.stopChargingSession(stationID);

	}

	// Get the Mapping of all the sessions
	@GetMapping("/sessions")
	public List<ChargeField> getSessions() {

		return controllerService.getChargingSessions();

	}

	// To get count of all the Charging Sessions
	@GetMapping("/summary")
	public ChargeFieldCount getSummary() {
		return controllerService.getChargingSummary();
	}

}
