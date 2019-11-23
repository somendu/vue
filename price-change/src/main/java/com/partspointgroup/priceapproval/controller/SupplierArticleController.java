package com.partspointgroup.priceapproval.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest;
import com.partspointgroup.priceapproval.model.Page;
import com.partspointgroup.priceapproval.model.UpdateAllRemarksRequest;
import com.partspointgroup.priceapproval.model.ApproveAllRequest;
import com.partspointgroup.priceapproval.model.ApproveRequest;
import com.partspointgroup.priceapproval.model.UpdateRemarksRequest;
import com.partspointgroup.priceapproval.service.SupplierArticleService;
import com.partspointgroup.priceapproval.support.ItemUpdateException;

import lombok.RequiredArgsConstructor;
import nl.jibes.mdmce.utils.Maps;

/**
 * REST controller for front-end
 *
 * @author SomenduMaiti
 * @author edgardegraaff
 */
@RestController
@RequiredArgsConstructor
public class SupplierArticleController {
	private final SupplierArticleService svc;

	@PostMapping("/api/supplier-articles")
	public Page<Map<String, Object>> list(@RequestBody GetSupplierArticlesRequest request) {
		return svc.listSupplierArticles(request);
	}

	@PostMapping("/api/supplier-articles-history")
	public Page<Map<String, Object>> listHistory(@RequestBody GetSupplierArticlesRequest request) {
		return svc.listSupplierArticleHistory(request);
	}

	@PostMapping("/api/finance-report")
	public Page<Map<String, Object>> listFinanceReport(@RequestBody GetSupplierArticlesRequest request) {
		return svc.listFinanceReport(request);
	}

	@PostMapping("/api/update-supplier-articles")
	public Map<String, Object> update(
			@RequestBody ApproveRequest req) throws ItemUpdateException {

		svc.digest(req.getIds(), ApprovalStatus.valueOf(req.getValue()));

		return Maps.of("status", 0);
	}

	@PostMapping("/api/update-all-supplier-articles")
	public Map<String, Object> updateAll(
			@RequestBody ApproveAllRequest req) throws ItemUpdateException {

		svc.digest(req.getFilter(), ApprovalStatus.valueOf(req.getValue()));

		return Maps.of("status", 0);
	}

	@PostMapping("/api/update-remarks")
	public Map<String, Object> updateRemarks(
			@RequestBody UpdateRemarksRequest req) throws ItemUpdateException {

		svc.updateRemarks(req.getIds(), req.getRemarks());

		return Maps.of("status", 0);
	}

	@PostMapping("/api/update-all-remarks")
	public Map<String, Object> updateAllRemarks(
			@RequestBody UpdateAllRemarksRequest req) throws ItemUpdateException {

		svc.updateRemarks(req.getFilter(), req.getRemarks());

		return Maps.of("status", 0);
	}
}
