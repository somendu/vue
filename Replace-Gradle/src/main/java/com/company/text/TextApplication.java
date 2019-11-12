package com.company.text;

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
public class TextApplication {
	public static void main(String[] args) {

		for (String arg : args) {
			System.out.println(arg);
		}

		SpringApplication.run(TextApplication.class, args);
	}
}
