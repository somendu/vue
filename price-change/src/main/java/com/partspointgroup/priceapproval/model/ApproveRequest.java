package com.partspointgroup.priceapproval.model;

import java.util.List;

import lombok.Data;

@Data
public class ApproveRequest {
	private List<SupplierArticleId> ids;
	private String value;
}
