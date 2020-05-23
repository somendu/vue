package com.partspointgroup.priceapproval.support;

import com.partspointgroup.priceapproval.model.Page;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * Reads rows from the resultSet as a Page, skipping {offset} number of rows
 * and not reading more than {limit}. It does not count the total number of
 * rows.
 *
 * @author edgardegraaff
 *
 * @param <T>
 */
@RequiredArgsConstructor
public class PageResultSetExtractor<T> implements ResultSetExtractor<Page<T>> {
	private final RowMapper<T> rowMapper;
	private final int offset;
	private final int limit;

	@Override
	public Page<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<T> results = new ArrayList<>(limit);

		// skip offset number of rows
		int pos = 0;
		while (pos++ < offset && rs.next()) {
		}

		int rowNum = 0;
		while (rowNum < limit && rs.next()) {
			results.add(this.rowMapper.mapRow(rs, rowNum++));
		}

		return new Page<T>()
				.setRows(results)
				.setStart(offset)
				.setEnd(offset + rowNum);
	}
}
