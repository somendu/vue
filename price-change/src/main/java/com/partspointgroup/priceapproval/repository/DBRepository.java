/**
 *
 */
package com.partspointgroup.priceapproval.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest;
import com.partspointgroup.priceapproval.model.KeyValue;
import com.partspointgroup.priceapproval.model.Page;
import com.partspointgroup.priceapproval.model.SupplierArticleId;

/**
 * Repository for accessing 'our' price change database
 *
 * @author SomenduMaiti
 * @author edgardegraaff
 *
 */
public interface DBRepository {

	/**
	 * Gets a page of current price changes
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listSupplierArticles(GetSupplierArticlesRequest request);

	/**
	 * Gets a page of the price change history
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listSupplierArticleHistory(GetSupplierArticlesRequest request);

	/**
	 * Gets a page of the finance report
	 *
	 * @param request
	 * @return
	 */
	Page<Map<String, Object>> listFinanceReport(GetSupplierArticlesRequest request);

	/**
	 * Returns all identifiers within the given filter
	 *
	 * @param filter
	 * @return
	 */
	List<SupplierArticleId> getIdentifiers(GetSupplierArticlesRequest.Filter filter);

	/**
	 * Sets remarks for a supplier price
	 *
	 * @param articleId - identifier
	 * @param supplierCode - identifier
	 * @param countryCode - identifier
	 * @param remarks - new value
	 */
	void setRemarks(
			String articleId,
			String supplierCode,
			String countryCode,
			String remarks);

	/**
	 * Makes a copy of this row to the history table, extended with action details
	 *
	 * @param articleId - identifier
	 * @param supplierCode - identifier
	 * @param countryCode - identifier
	 * @param now - the action date
	 * @param status - the performed action
	 */
	void moveToHistory(
			String articleId,
			String supplierCode,
			String countryCode,
			Timestamp now,
			ApprovalStatus status);

	/**
	 * Getting product manager values
	 *
	 * @param history
	 * @return
	 */
	List<KeyValue> getProductManagers(boolean history);

	/**
	 * Getting goods group values
	 *
	 * @param history
	 * @return
	 */
	List<KeyValue> getGoodsGroups(boolean history);

	/**
	 * Getting article group values
	 *
	 * @param history
	 * @return
	 */
	List<KeyValue> getArticleGroups(boolean history);

	/**
	 * Getting supplier names
	 *
	 * @param history
	 * @return
	 */
	List<KeyValue> getSuppliers(boolean history);
}
