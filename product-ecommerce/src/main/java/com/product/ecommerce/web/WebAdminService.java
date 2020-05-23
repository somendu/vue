package com.product.ecommerce.web;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebAdminService {
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(WebAdminService.class
			.getName());

	public WebAdminService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://" + serviceUrl;
	}

	public String subtract(String minuend, String subtrahend) {
		return restTemplate.getForObject(serviceUrl + "/subtract?minuend={minuend}&subtrahend={subtrahend}", String.class, minuend, subtrahend);
	}
}
