package com.partspointgroup.priceapproval.service;

import java.util.List;
import java.util.Map;

import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.Filter;
import com.partspointgroup.priceapproval.model.Page;
import com.partspointgroup.priceapproval.model.SupplierArticleId;
import com.partspointgroup.priceapproval.support.ItemUpdateException;

public interface SupplierArticleService {

	/**
	 * Method to list Supplier Article
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listSupplierArticles(GetSupplierArticlesRequest request);

	/**
	 * Lists historical price changes
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listSupplierArticleHistory(GetSupplierArticlesRequest request);

	/**
	 * Lists finance report
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listFinanceReport(GetSupplierArticlesRequest request);

	/**
	 * Approve / reject price changes. This method updates items in PIM, removes the row
	 * from the current view and adds a row to the history.
	 *
	 * @param ids
	 * @param action
	 * @throws ItemUpdateException when it couldn't update
	 */
	void digest(List<SupplierArticleId> ids, ApprovalStatus action) throws ItemUpdateException;

	/**
	 * Approve / reject price changes of all articles within the given filter.
	 *
	 * @param filter
	 * @param valueOf
	 * @throws ItemUpdateException when it couldn't update
	 */
	void digest(Filter filter, ApprovalStatus action) throws ItemUpdateException;

	/**
	 * Bulk update remarks
	 *
	 * @param ids
	 * @param remark
	 * @throws ItemUpdateException when it couldn't update
	 */
	void updateRemarks(List<SupplierArticleId> ids, String remarks) throws ItemUpdateException;

	/**
	 * Update remarks of all articles within the given filter.
	 *
	 * @param filter
	 * @param remarks
	 * @throws ItemUpdateException when it couldn't update
	 */
	void updateRemarks(Filter filter, String remarks) throws ItemUpdateException;
}
