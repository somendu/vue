package com.partspointgroup.priceapproval.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="remote")
public class RemoteConfig {
	private String endpoint;
	private String username;
	private String password;
}
