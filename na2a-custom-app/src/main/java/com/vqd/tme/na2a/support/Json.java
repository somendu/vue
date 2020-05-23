package com.vqd.tme.na2a.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for writing values as formatted JSON (for logging purpose)
 *
 * @author edgar
 *
 */
public class Json {
	private static final ObjectMapper om;

	static {
		om = new ObjectMapper();
		om.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static Json of(Object obj) {
		return new Json(obj);
	}

	private final Object obj;
	private Json(Object obj) { this.obj = obj; }

	@Override
	public String toString() {
		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return String.valueOf(obj);
		}
	}
}
