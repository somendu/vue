/**
 * 
 */
package com.partspointgroup.priceapproval.config;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author SomenduMaiti
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
public class PriceApprovalConfig {

	private String appName;
	private String environment;
	private boolean outputAsXml;
	private String keyIndexLkp;
	private Set<String> supportedLocales;
}
