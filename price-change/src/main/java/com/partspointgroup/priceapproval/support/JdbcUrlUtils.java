package com.partspointgroup.priceapproval.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for parsing JDBC URLs
 *
 * @author edgardegraaff
 *
 */
public class JdbcUrlUtils {
	private JdbcUrlUtils() {
	}

	public static Map<String, String> getJdbcUrlProperties(String url) {
		Map<String, String> props = new HashMap<>();

		// properties start after the first semicolon
		int searchPos = url.indexOf(';');

		while (searchPos != -1) {
			// search for next semicolon
			int equalsIndex = url.indexOf('=', searchPos + 1);

			// end of key=value
			int endIndex = url.indexOf(';', searchPos + 1);

			if (endIndex == -1) {
				if (equalsIndex != -1) {
					props.put(
							url.substring(searchPos + 1, equalsIndex),
							url.substring(equalsIndex + 1));
				}
			} else {
				if (equalsIndex != -1 && equalsIndex < endIndex) {
					props.put(
							url.substring(searchPos + 1, equalsIndex),
							url.substring(equalsIndex + 1, endIndex));
				}
			}

			searchPos = endIndex;
		}

		return props;
	}
}
