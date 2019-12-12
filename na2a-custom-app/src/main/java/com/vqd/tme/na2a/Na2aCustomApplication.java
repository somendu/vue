package com.vqd.tme.na2a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Na2aCustomApplication {
  public static void main(String[] args) {
    SpringApplication.run(Na2aCustomApplication.class, args);
  }
}
