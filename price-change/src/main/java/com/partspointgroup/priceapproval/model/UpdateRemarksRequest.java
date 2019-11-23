package com.partspointgroup.priceapproval.model;

import java.util.List;
import lombok.Data;

@Data
public class UpdateRemarksRequest {
	private List<SupplierArticleId> ids;
	private String remarks;
}
