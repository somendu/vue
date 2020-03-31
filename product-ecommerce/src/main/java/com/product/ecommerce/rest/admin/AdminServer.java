package com.product.ecommerce.rest.admin;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@EnableDiscoveryClient
@ComponentScan
public class AdminServer {
	protected Logger logger = Logger.getLogger(AdminServer.class.getName());

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "admin-server");

		SpringApplication.run(AdminServer.class, args);
	}
}
