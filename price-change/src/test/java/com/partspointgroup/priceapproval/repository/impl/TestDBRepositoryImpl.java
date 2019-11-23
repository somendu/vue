package com.partspointgroup.priceapproval.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.Arrays;

import org.junit.Test;

import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.Filter;
import com.partspointgroup.priceapproval.support.QueryPart;

public class TestDBRepositoryImpl {
	@Test
	public void testOneFilter() {
		QueryPart result = DBRepositoryImpl.getWhereClause(new Filter().setProductManager("234"), true, false);

		assertEquals("where productManager = ?", result.getSql());

		assertEquals(Arrays.asList("234"), result.getParameters());
	}

	@Test
	public void testFilterWhere() {
		QueryPart result = DBRepositoryImpl.getWhereClause(new Filter().setArticleGroup("123").setProductManager("234"),
				true, false);

		assertEquals("where articleGroup = ?" + "\nand productManager = ?", result.getSql());

		assertEquals(Arrays.asList("123", "234"), result.getParameters());

		result = DBRepositoryImpl.getWhereClause(new Filter().setArticleGroup("123").setProductManager("234"), false, false);

		assertEquals("and articleGroup = ?" + "\nand productManager = ?", result.getSql());

		assertEquals(Arrays.asList("123", "234"), result.getParameters());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testDateRange() {
		Date start = new Date(118, 6, 1);
		Date end = new Date(118, 7, 1);
		QueryPart result;

		result = DBRepositoryImpl.getWhereClause(new Filter().setStartDateFrom(start), true, true);
		assertEquals("where grossPriceSupplierStartDateNew >= ?", result.getSql());
		assertEquals(Arrays.asList(start), result.getParameters());

		result = DBRepositoryImpl.getWhereClause(new Filter().setStartDateTo(end), true, true);
		assertEquals("where grossPriceSupplierStartDateNew <= ?", result.getSql());
		assertEquals(Arrays.asList(end), result.getParameters());

		result = DBRepositoryImpl.getWhereClause(new Filter().setStartDateFrom(start).setStartDateTo(end), true, true);
		assertEquals("where grossPriceSupplierStartDateNew between ? and ?", result.getSql());
		assertEquals(Arrays.asList(start, end), result.getParameters());
	}

	@Test
	public void testEmptyFilter() {
		// test null filter value
		QueryPart result = DBRepositoryImpl.getWhereClause(null, false, false);

		assertNull(result);

		// test filter without anything set (all nulls)
		result = DBRepositoryImpl.getWhereClause(new Filter(), false, false);

		assertNull(result);

		// test filter with empty strings / spaces only
		result = DBRepositoryImpl
				.getWhereClause(new Filter().setSapArticleNumber(null).setProductManager(" ").setGoodsGroup(""), false, false);

		assertNull(result);

		result = DBRepositoryImpl.getWhereClause(new Filter().setProductManager("123").setGoodsGroup(""), false, false);

		assertEquals("and productManager = ?", result.getSql());
		assertEquals(Arrays.asList("123"), result.getParameters());
	}
}
