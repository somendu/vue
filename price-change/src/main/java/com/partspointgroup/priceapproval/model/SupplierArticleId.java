package com.partspointgroup.priceapproval.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplier Article identifier
 *
 * @author SomenduMaiti
 * @author edgardegraaff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierArticleId {
	private String articleId;
	private String supplierCode;
	private String countryCode;
}
