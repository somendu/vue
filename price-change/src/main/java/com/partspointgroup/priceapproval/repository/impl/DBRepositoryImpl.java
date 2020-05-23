/**
 *
 */
package com.partspointgroup.priceapproval.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.Filter;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.SortSpecifier;
import com.partspointgroup.priceapproval.model.KeyValue;
import com.partspointgroup.priceapproval.model.Page;
import com.partspointgroup.priceapproval.model.SupplierArticleId;
import com.partspointgroup.priceapproval.repository.DBRepository;
import com.partspointgroup.priceapproval.support.JdbcUrlUtils;
import com.partspointgroup.priceapproval.support.PageResultSetExtractor;
import com.partspointgroup.priceapproval.support.QueryPart;
import com.partspointgroup.priceapproval.support.QueryPartUtils;
import com.zaxxer.hikari.HikariDataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Price Change DB repository implementation
 *
 * @author SomenduMaiti
 * @author edgardegraaff
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DBRepositoryImpl implements DBRepository {
	private static final int DEFAULT_PAGE_SIZE = 100;
	private static final int MAX_PAGE_SIZE = 2500;

	private static final String CURRENT_TABLE_NAME = "vw_SupplierArticles_v2";
	private static final String CURRENT_ORIGIN_TABLE_NAME = "SupplierPrice";
	private static final String HISTORY_TABLE_NAME = "SupArtHistory_v2";
	private static final String FINANCE_REPORT_TABLE_NAME = "vw_FinanceReport";

	private static final String LIST_QUERY = "select * from " + CURRENT_TABLE_NAME + "\n";
	private static final String LIST_COUNT_QUERY = "select count(*) as total from " + CURRENT_TABLE_NAME;

	private static final String LIST_HISTORY_QUERY = "select * from " + HISTORY_TABLE_NAME + "\n";
	private static final String LIST_HISTORY_COUNT_QUERY = "select count(*) as total from " + HISTORY_TABLE_NAME;

	private static final String LIST_FINANCE_REPORT_QUERY = "select * from " + FINANCE_REPORT_TABLE_NAME + "\n";
	private static final String LIST_FINANCE_REPORT_COUNT_QUERY = "select count(*) as total from " + FINANCE_REPORT_TABLE_NAME;

	private static final String LIST_IDENTIFIERS_QUERY = "select articleId,supplierCode,countryCode from " + CURRENT_TABLE_NAME;

	private static final String UPDATE_REMARKS_QUERY =
			"update " + CURRENT_TABLE_NAME + " "
			+ "set remarks = ? "
			+ "where articleId = ? and supplierCode = ? and countryCode = ?";

	private static final String DELETE_CURRENT_QUERY =
			"delete from " + CURRENT_ORIGIN_TABLE_NAME + " "
			+ "where articleId = ? and supplierCode = ? and countryCode = ?";

	@RequiredArgsConstructor
	@AllArgsConstructor
	@Getter
	private static final class FilterHandler {
		private final Function<Filter, Object> valueGetter;
		private final String columnName;
		private Function<Object, Object> transformer;
	}

	// keep these filters in alphabetic order for unit tests
	private static final List<FilterHandler> FILTER_HANDLERS = Arrays.asList(
			new FilterHandler(Filter::getArticleGroup, "articleGroup"),
			new FilterHandler(Filter::getProductManager, "productManager"),
			new FilterHandler(Filter::getGoodsGroup, "goodsGroup"),
			new FilterHandler(Filter::getSapArticleNumber, "sapArticleNumber"),
			new FilterHandler(Filter::getStatus, "status", value -> {
				return ((ApprovalStatus) value).getPimValue();
			}), new FilterHandler(Filter::getSupplierCode, "supplierCode"));

	private final JdbcTemplate jdbcTemplate;
	private final DataSource dataSource;
	private Set<String> currentTableColumns;
	private Set<String> historyTableColumns;
	private String insertInHistoryQuery;

	@PostConstruct
	public void init() throws SQLException {
		// extract databaseName from jdbcUrl
		String jdbcUrl = ((HikariDataSource) dataSource).getJdbcUrl();
		String databaseName = JdbcUrlUtils.getJdbcUrlProperties(jdbcUrl).get("databaseName");

		// collect column names of views
		try (Connection conn = dataSource.getConnection()) {
			currentTableColumns = getColumns(conn, databaseName, "dbo", CURRENT_TABLE_NAME);
			historyTableColumns = getColumns(conn, databaseName, "dbo", HISTORY_TABLE_NAME);
		}

		Set<String> mutualColumns = new HashSet<>();
		for (String columnName : currentTableColumns) {
			if (historyTableColumns.contains(columnName)) {
				mutualColumns.add(columnName);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("insert into ");
		sb.append(HISTORY_TABLE_NAME);
		sb.append(" (");
		for (String columnName : mutualColumns) {
			sb.append(columnName).append(',');
		}
		sb.append("status,performed)\n");

		sb.append("select ");
		for (String columnName : mutualColumns) {
			sb.append(columnName).append(',');
		}
		sb.append("?,? from ");
		sb.append(CURRENT_TABLE_NAME);
		sb.append(" where articleId = ? and supplierCode = ? and countryCode = ?");

		insertInHistoryQuery = sb.toString();

		log.debug("insertInHistoryQuery: {}", insertInHistoryQuery);
	}

	private Set<String> getColumns(Connection conn, String databaseName, String schema, String tableName) throws SQLException {
		Set<String> columnNames = new HashSet<>();

		try (ResultSet resultSet = conn.getMetaData().getColumns(databaseName, schema, tableName, null)) {
			while (resultSet.next()) {
				columnNames.add(resultSet.getString("COLUMN_NAME"));
			}
		}

		return columnNames;
	}

	@Override
	public Page<Map<String, Object>> listSupplierArticles(GetSupplierArticlesRequest request) {
		QueryPart whereClause = getWhereClause(request.getFilter(), true, false);
		return list(request, LIST_QUERY, LIST_COUNT_QUERY, whereClause);
	}

	@Override
	public Page<Map<String, Object>> listSupplierArticleHistory(GetSupplierArticlesRequest request) {
		QueryPart whereClause = getWhereClause(request.getFilter(), true, true);
		return list(request, LIST_HISTORY_QUERY, LIST_HISTORY_COUNT_QUERY, whereClause);
	}

	@Override
	public Page<Map<String, Object>> listFinanceReport(GetSupplierArticlesRequest request) {
		QueryPart whereClause = getWhereClause(request.getFilter(), true, true);
		return list(request, LIST_FINANCE_REPORT_QUERY, LIST_FINANCE_REPORT_COUNT_QUERY, whereClause);
	}

	@Override
	public List<SupplierArticleId> getIdentifiers(Filter filter) {
		QueryPart whereClause = getWhereClause(filter, true, false);
		QueryPart query = QueryPartUtils.concat(new QueryPart(LIST_IDENTIFIERS_QUERY, null), whereClause);

		log.debug("get identifiers query: {}", query);

		ResultSetExtractor<List<SupplierArticleId>> extractor = resultSet -> {
			List<SupplierArticleId> result = new ArrayList<>(1024);

			while (resultSet.next()) {
				result.add(new SupplierArticleId(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
			}

			return result;
		};

		if (query.getParameters() == null) {
			return jdbcTemplate.query(query.getSql(), extractor);
		} else {
			return jdbcTemplate.query(query.getSql(), query.getParameters().toArray(), extractor);
		}
	}

	private Page<Map<String, Object>> list(GetSupplierArticlesRequest request, String rowsQuery, String countQuery,
			QueryPart whereClause) {
		QueryPart sortClause = getSortClause(request.getSort());

		log.debug("where clause: {}", whereClause);
		log.debug("sort clause: {}", sortClause);

		// paging
		Integer offset = null;
		Integer limit = null;

		if (request.getPaging() != null) {
			offset = request.getPaging().getOffset();
			limit = request.getPaging().getLimit();
		}

		if (offset == null || offset < 0) {
			offset = 0;
		}

		if (limit == null || limit > MAX_PAGE_SIZE) {
			limit = DEFAULT_PAGE_SIZE;
		}

		ResultSetExtractor<Page<Map<String, Object>>> extractor = new PageResultSetExtractor<>(new ColumnMapRowMapper(),
				offset, limit);

		Page<Map<String, Object>> page;
		Integer count;

		QueryPart query = QueryPartUtils.concat(new QueryPart(rowsQuery, null), whereClause, sortClause);
		log.debug("query: {}", query);

		if (query.getParameters() == null) {
			page = jdbcTemplate.query(query.getSql(), extractor);
		} else {
			page = jdbcTemplate.query(query.getSql(), query.getParameters().toArray(), extractor);
		}

		for (Map<String, Object> row : page.getRows()) {
			ApprovalStatus status = ApprovalStatus.byValue((Integer) row.get("status"));
			row.put("statusDisplay", status != null ? status.getDisplayValue() : null);
		}

		if (request.getPaging() == null || !Boolean.TRUE.equals(request.getPaging().getDontReturnCount())) {
			QueryPart totals = QueryPartUtils.concat(new QueryPart(countQuery, null), whereClause);
			log.debug("totals: {}", totals);

			if (totals.getParameters() == null) {
				count = jdbcTemplate.queryForObject(totals.getSql(), Integer.class);
			} else {
				count = jdbcTemplate.queryForObject(totals.getSql(), totals.getParameters().toArray(), Integer.class);
			}

			if (count != null) {
				page.setCount(count);
			} else {
				page.setCount(0);
			}
		}

		return page;
	}

	private QueryPart getSortClause(SortSpecifier sort) {
		if (sort != null) {
			if (currentTableColumns.contains(sort.getColumn())) {
				return new QueryPart(
						String.format("order by %s %s", sort.getColumn(), sort.isDescending() ? "desc" : "asc"), null);
			}
		}

		return null;
	}

	static QueryPart getWhereClause(Filter filter, boolean startWithWhere, boolean supportDateRange) {
		if (filter == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		List<Object> parameters = new ArrayList<>();

		boolean nextIsWhere = startWithWhere;

		for (FilterHandler handler : FILTER_HANDLERS) {
			// is a filter set?
			Object value = handler.valueGetter.apply(filter);

			// pre-filter empty values
			if (value == null) {
				continue;
			}
			if (value instanceof String && StringUtils.isBlank((String) value)) {
				continue;
			}

			// transform if necessary
			if (handler.transformer != null) {
				value = handler.transformer.apply(value);
			}

			if (sb.length() != 0) {
				sb.append('\n');
			}

			sb.append(nextIsWhere ? "where " : "and ").append(handler.columnName).append(" = ?");

			parameters.add(value);
			nextIsWhere = false;
		}

		if (supportDateRange && (filter.getStartDateFrom() != null || filter.getStartDateTo() != null)) {
			if (sb.length() != 0) {
				sb.append('\n');
			}
			sb.append(nextIsWhere ? "where " : "and ").append("grossPriceSupplierStartDateNew");
			nextIsWhere = false;

			if (filter.getStartDateFrom() != null) {
				if (filter.getStartDateTo() != null) {
					sb.append(" between ? and ?");
					parameters.add(filter.getStartDateFrom());
					parameters.add(filter.getStartDateTo());
				} else {
					sb.append(" >= ?");
					parameters.add(filter.getStartDateFrom());
				}
			} else {
				sb.append(" <= ?");
				parameters.add(filter.getStartDateTo());
			}
		}

		// no filter = nothing to do
		if (sb.length() == 0) {
			return null;
		}

		return new QueryPart(sb.toString(), parameters);
	}

	@Override
	public List<KeyValue> getProductManagers(boolean history) {
		return getDistinctValues(history ? HISTORY_TABLE_NAME : CURRENT_TABLE_NAME, "productManager");
	}

	@Override
	public List<KeyValue> getGoodsGroups(boolean history) {
		return getDistinctValues(history ? HISTORY_TABLE_NAME : CURRENT_TABLE_NAME, "goodsGroup", "goodsDescription");
	}

	@Override
	public List<KeyValue> getArticleGroups(boolean history) {
		return getDistinctValues(history ? HISTORY_TABLE_NAME : CURRENT_TABLE_NAME, "articleGroup", "articleGroupDescription");
	}

	@Override
	public List<KeyValue> getSuppliers(boolean history) {
		return getDistinctValues(history ? HISTORY_TABLE_NAME : CURRENT_TABLE_NAME, "supplierCode");
	}

	private List<KeyValue> getDistinctValues(String tableName, String columnName) {
		return jdbcTemplate.query(
				String.format(
						"select distinct %s from %s where %s is not null and %s != ''",
						columnName, tableName, columnName, columnName),
				new SingleKeyValueRowMapper());
	}

	private List<KeyValue> getDistinctValues(String tableName, String keyColumnName, String valueColumnName) {
		return jdbcTemplate.query(
				String.format(
						"select distinct %s, %s from %s where %s is not null and %s != ''",
						keyColumnName, valueColumnName, tableName, keyColumnName, keyColumnName),
				new KeyValueRowMapper());
	}

	class SingleKeyValueRowMapper implements RowMapper<KeyValue> {
		@Override
		public KeyValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			String key = rs.getString(1);
			return new KeyValue(key, key);
		}
	}

	class KeyValueRowMapper implements RowMapper<KeyValue> {
		@Override
		public KeyValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			String key = rs.getString(1);
			String value = rs.getString(2);
			return new KeyValue(key, String.format("%s - %s", key, value));
		}
	}

	@Override
	public void setRemarks(String articleId, String supplierCode, String countryCode, String remarks) {
		jdbcTemplate.update(UPDATE_REMARKS_QUERY, remarks, articleId, supplierCode, countryCode);
	}

	@Override
	@Transactional
	public void moveToHistory(
			String articleId, String supplierCode, String countryCode, Timestamp now, ApprovalStatus status) {
		jdbcTemplate.update(insertInHistoryQuery, status.getPimValue(), now, articleId, supplierCode, countryCode);
		jdbcTemplate.update(DELETE_CURRENT_QUERY, articleId, supplierCode, countryCode);
	}
}
