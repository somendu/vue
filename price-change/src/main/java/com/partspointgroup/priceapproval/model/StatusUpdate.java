/**
 * 
 */
package com.partspointgroup.priceapproval.model;

import lombok.Data;

/**
 * Model for status update in Batch
 * 
 * @author SomenduMaiti
 *
 */
@Data
public class StatusUpdate {

	// This is from the new PIM repository - PPG
	private boolean priceStatus;
	private String supplierArticleNumber;
	private String supplier;

}
