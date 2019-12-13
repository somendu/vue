package com.trinet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan("com.trinet")
@SpringBootApplication
@EnableSwagger2
public class ApiAggregationApplication {
  
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAggregationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiAggregationApplication.class, args);
        LOGGER.info("Api Aggrigation  ...");

    }
}
