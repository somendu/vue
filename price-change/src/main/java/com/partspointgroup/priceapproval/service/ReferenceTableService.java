/**
 *
 */
package com.partspointgroup.priceapproval.service;

import java.util.List;

import com.partspointgroup.priceapproval.model.KeyValue;

/**
 * Reference for Master records
 * 
 * @author SomenduMaiti
 *
 */
public interface ReferenceTableService {

	/**
	 * Values for product managers
	 * 
	 * @param history
	 * @return
	 */
	public List<KeyValue> getProductManagers(boolean history);

	/**
	 * Values for goods group
	 * 
	 * @param history
	 * @return
	 */
	public List<KeyValue> getGoodsGroups(boolean history);

	/**
	 * Getting article groups
	 * 
	 * @param history
	 * @return
	 */
	public List<KeyValue> getArticleGroups(boolean history);

	/**
	 * Getting supplier values
	 * 
	 * @param history
	 * @return
	 */
	public List<KeyValue> getSuppliers(boolean history);

}
