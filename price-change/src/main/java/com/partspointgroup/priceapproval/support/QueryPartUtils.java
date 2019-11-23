package com.partspointgroup.priceapproval.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for {@link QueryPart}
 *
 * @author edgardegraaff
 *
 */
public class QueryPartUtils {
	private QueryPartUtils() {
	}

	/**
	 * Concats multiple parts into one query
	 *
	 * @param parts
	 * @return
	 */
	public static QueryPart concat(QueryPart... parts) {
		// concat sql
		String sql = null;
		for (QueryPart part : parts) {
			if (part != null) {
				if (sql == null) {
					sql = part.getSql();
				} else {
					sql = concatSql(parts);
					break;
				}
			}
		}

		// concat parameters
		List<Object> parameters = null;
		for (QueryPart part : parts) {
			if (part != null && part.getParameters() != null) {
				if (parameters == null) {
					parameters = part.getParameters();
				} else {
					parameters = concatParameters(parts);
					break;
				}
			}
		}

		return new QueryPart(sql, parameters);
	}

	/**
	 * Concats sql statements of parts into one
	 *
	 * @param parts
	 * @return
	 */
	public static String concatSql(QueryPart... parts) {
		StringBuilder sb = new StringBuilder();

		for (QueryPart part : parts) {
			if (part != null) {
				if (sb.length() != 0) {
					sb.append('\n');
				}
				sb.append(part.getSql());
			}
		}

		return sb.toString();
	}

	/**
	 * Concats parameters of parts into one list
	 *
	 * @param parts
	 * @return
	 */
	public static List<Object> concatParameters(QueryPart... parts) {
		List<Object> parameters = new ArrayList<>();

		for (QueryPart part : parts) {
			if (part != null && part.getParameters() != null) {
				parameters.addAll(part.getParameters());
			}
		}

		return parameters;
	}
}
