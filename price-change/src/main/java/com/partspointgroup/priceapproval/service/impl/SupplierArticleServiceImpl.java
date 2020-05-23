package com.partspointgroup.priceapproval.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest;
import com.partspointgroup.priceapproval.model.GetSupplierArticlesRequest.Filter;
import com.partspointgroup.priceapproval.model.Page;
import com.partspointgroup.priceapproval.model.SupplierArticleId;
import com.partspointgroup.priceapproval.repository.DBRepository;
import com.partspointgroup.priceapproval.repository.PIMRepository;
import com.partspointgroup.priceapproval.service.SupplierArticleService;
import com.partspointgroup.priceapproval.support.ItemUpdateException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Supplier Article service class for business logics. Basically dispatches
 * actions to {@link PIMRepository} and {@link DBRepository}
 *
 * @author SomenduMaiti
 * @author edgardegraaff
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierArticleServiceImpl implements SupplierArticleService {
	private final DBRepository dbRepository;
	private final PIMRepository pimRepository;

	@Override
	public Page<Map<String, Object>> listSupplierArticles(GetSupplierArticlesRequest request) {
		return dbRepository.listSupplierArticles(request);
	}

	@Override
	public Page<Map<String, Object>> listSupplierArticleHistory(GetSupplierArticlesRequest request) {
		return dbRepository.listSupplierArticleHistory(request);
	}

	@Override
	public Page<Map<String, Object>> listFinanceReport(GetSupplierArticlesRequest request) {
		return dbRepository.listFinanceReport(request);
	}

	@Override
	public void digest(List<SupplierArticleId> ids, ApprovalStatus action) throws ItemUpdateException {
		List<String> caughtErrors = new ArrayList<>();

		Timestamp now = new Timestamp(System.currentTimeMillis());

		for (SupplierArticleId id : ids) {
			try {
				pimRepository.setSupplierArticleApprovalStatus(id.getArticleId(), id.getSupplierCode(), action);
				dbRepository.moveToHistory(id.getArticleId(), id.getSupplierCode(), id.getCountryCode(), now, action);
			} catch (Exception e) {
				log.warn("could not update article {}: {}", id, e);
				caughtErrors.add(String.format("couldn't set status for %s/%s: %s", id.getSupplierCode(),
						id.getArticleId(), e.getMessage()));
			}

		}

		if (!caughtErrors.isEmpty()) {
			throw new ItemUpdateException(caughtErrors);
		}
	}

	@Override
	public void digest(Filter filter, ApprovalStatus action) throws ItemUpdateException {
		digest(dbRepository.getIdentifiers(filter), action);
	}

	@Override
	public void updateRemarks(List<SupplierArticleId> ids, String remarks) throws ItemUpdateException {
		List<String> caughtErrors = new ArrayList<>();

		for (SupplierArticleId id : ids) {
			try {
				pimRepository.setSupplierArticleRemark(id.getArticleId(), id.getSupplierCode(), remarks);
				dbRepository.setRemarks(id.getArticleId(), id.getSupplierCode(), id.getCountryCode(), remarks);
			} catch (Exception e) {
				log.warn("could not update remarks for {}: {}", id, e);
				caughtErrors.add(String.format("couldn't update remarks for %s/%s: %s", id.getSupplierCode(),
						id.getArticleId(), e.getMessage()));
			}
		}

		if (!caughtErrors.isEmpty()) {
			throw new ItemUpdateException(caughtErrors);
		}
	}

	@Override
	public void updateRemarks(Filter filter, String remarks) throws ItemUpdateException {
		updateRemarks(dbRepository.getIdentifiers(filter), remarks);
	}
}
