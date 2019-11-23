/**
 *
 */
package com.partspointgroup.priceapproval.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.partspointgroup.priceapproval.model.KeyValue;
import com.partspointgroup.priceapproval.repository.DBRepository;
import com.partspointgroup.priceapproval.service.ReferenceTableService;

/**
 * Service implementation class for reference tables
 * 
 * @author SomenduMaiti
 *
 */
@Service
public class ReferenceTableServiceimpl implements ReferenceTableService {
	private final DBRepository dbRepository;

	public ReferenceTableServiceimpl(DBRepository dbRepository) {
		this.dbRepository = dbRepository;
	}

	@Override
	public List<KeyValue> getProductManagers(boolean history) {
		return dbRepository.getProductManagers(history);
	}

	@Override
	public List<KeyValue> getGoodsGroups(boolean history) {
		return dbRepository.getGoodsGroups(history);
	}

	@Override
	public List<KeyValue> getArticleGroups(boolean history) {
		return dbRepository.getArticleGroups(history);
	}

	@Override
	public List<KeyValue> getSuppliers(boolean history) {
		return dbRepository.getSuppliers(history);
	}
}
