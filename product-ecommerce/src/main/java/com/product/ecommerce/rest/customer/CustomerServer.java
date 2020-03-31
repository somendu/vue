package com.product.ecommerce.rest.customer;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@EnableDiscoveryClient
@ComponentScan
public class CustomerServer {

	protected Logger logger = Logger.getLogger(CustomerServer.class.getName());

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "customer-server");

		SpringApplication.run(CustomerServer.class, args);
	}
}
