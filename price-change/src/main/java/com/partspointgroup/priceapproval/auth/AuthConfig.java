package com.partspointgroup.priceapproval.auth;

import javax.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
	@Bean
	public Filter authFilter() {
		return new AuthFilter();
	}
}
