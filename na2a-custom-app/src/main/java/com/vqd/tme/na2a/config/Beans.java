package com.vqd.tme.na2a.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class Beans {
  @Bean
  public RestTemplate restTemplate() {
    return new PimRestTemplate();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.disable(Feature.AUTO_CLOSE_TARGET);
    om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    om.setSerializationInclusion(Include.NON_NULL);
    return om;
  }
}
