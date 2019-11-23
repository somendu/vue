package com.partspointgroup.priceapproval.model;

import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.Filter;

import lombok.Data;

@Data
public class ApproveAllRequest {
	private Filter filter;
	private String value;
}
