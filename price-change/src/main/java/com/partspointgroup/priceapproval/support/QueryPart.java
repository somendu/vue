package com.partspointgroup.priceapproval.support;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Part of a SQL
 * @author edgardegraaff
 *
 */
@Data
@RequiredArgsConstructor
public final class QueryPart {
	/**
	 * Statement
	 */
	private final String sql;
	/**
	 * Parameters for placeholders in statement (may be null)
	 */
	private final List<Object> parameters;
}