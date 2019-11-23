package com.partspointgroup.priceapproval.model;

import java.sql.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Request parameters for getting supplier articles
 *
 * @author edgardegraaff
 *
 */
@Data
@Accessors(chain=true)
public class GetSupplierArticlesRequest {
	/**
	 * Articles filter
	 * @author edgardegraaff
	 *
	 */
	@Data
	@Accessors(chain=true)
	public static class Filter {
		private ApprovalStatus status;
		private String productManager;
		private String goodsGroup;
		private String articleGroup;
		private String supplierCode;
		private String sapArticleNumber;
		private Date startDateFrom;
		private Date startDateTo;
	}

	@Data
	@Accessors(chain=true)
	public static class SortSpecifier {
		private String column;
		private boolean descending;
	}

	@Data
	@Accessors(chain=true)
	public static class Paging {
		private Integer offset;
		private Integer limit;
		private Boolean dontReturnCount;
	}

	private Filter filter;
	private SortSpecifier sort;
	private Paging paging;
}
