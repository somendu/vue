package com.partspointgroup.priceapproval.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setDateFormat(new StdDateFormat());
		return om;
	}
}
