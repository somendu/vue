/**
 * 
 */
package com.charging.station.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;

import com.charging.station.support.ChargeRequest;

/**
 * @author somendu
 *
 */

public class ControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testStartSession() throws URISyntaxException {

		final String baseUrl = "http://localhost:8080/api/charging/chargingSessions";
		URI uri = new URI(baseUrl);
		ChargeRequest chargerequest = new ChargeRequest();
		chargerequest.setStationId("A-19820");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// HttpEntity<ChargeRequest> request = new HttpEntity<>(chargerequest, headers);

		// ResponseEntity<ChargeField> result = this.restTemplate.postForEntity(uri,
		// request, ChargeField.class);

		// Verify request succeed
		// Assert.assertEquals(201, result.getStatusCodeValue());

	}
}
