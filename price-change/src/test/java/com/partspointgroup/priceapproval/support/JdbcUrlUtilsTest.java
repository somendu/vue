package com.partspointgroup.priceapproval.support;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JdbcUrlUtilsTest {
	@Test
	public void testGetJdbcUrlProperties() {
		Map<String, String> props = JdbcUrlUtils.getJdbcUrlProperties("jdbc:sqlserver://127.0.0.1:1433;databaseName=DM_PRICES_ACC;readOnly=true");
		Map<String, String> expected = new HashMap<>();
		expected.put("databaseName", "DM_PRICES_ACC");
		expected.put("readOnly", "true");
		assertEquals(expected, props);
	}
}
