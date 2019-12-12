package com.vqd.tme.na2a.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="informatica.pim")
public class InformaticaPimProperties {
  private String server;
  private String externalUrl;
}
