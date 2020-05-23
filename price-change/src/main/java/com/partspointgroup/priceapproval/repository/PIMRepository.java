/**
 *
 */
package com.partspointgroup.priceapproval.repository;

import com.partspointgroup.priceapproval.model.ApprovalStatus;

/**
 * PIM repository Interface
 *
 * @author SomenduMaiti
 *
 */
public interface PIMRepository {
	/**
	 * Method for price approval status check
	 *
	 * @param supplierArticleNumber
	 * @param supplier
	 * @param statusString
	 */
	void setSupplierArticleApprovalStatus(String supplierArticleNumber, String supplier, ApprovalStatus value);

	/**
	 * Method for setting supplier article remarks
	 *
	 * @param supplierArticleNumber
	 * @param supplier
	 * @param remark
	 */
	void setSupplierArticleRemark(String supplierArticleNumber, String supplier, String remark);
}
