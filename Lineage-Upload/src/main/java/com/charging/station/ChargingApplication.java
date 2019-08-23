package com.charging.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Application starting point
 * 
 * @author somendu
 *
 */
@SpringBootApplication
@EnableCaching
public class ChargingApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChargingApplication.class, args);
	}
}
