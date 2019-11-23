/**
 *
 */
package com.partspointgroup.priceapproval.repository.impl;

import com.heiler.pim.webservice.client.EntityItemReference;
import com.heiler.pim.webservice.client.EntityItemReferenceFactory;
import com.heiler.pim.webservice.client.RestClient;
import com.heiler.pim.webservice.client.list.EntityItemTable;
import com.heiler.pim.webservice.client.list.EntityItemTableColumn;
import com.heiler.pim.webservice.client.list.EntityItemTableRow;
import com.heiler.pim.webservice.client.list.ListWriteRequest;
import com.partspointgroup.priceapproval.config.RemoteConfig;
import com.partspointgroup.priceapproval.model.ApprovalStatus;
import com.partspointgroup.priceapproval.repository.PIMRepository;
import java.util.Collections;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * PIM repository Implementation class
 *
 * @author SomenduMaiti
 *
 */
@Slf4j
@Component
public class PIMRepositoryImpl implements PIMRepository {
	private final RestClient restClient;

	public PIMRepositoryImpl(RemoteConfig config) {
		restClient = new RestClient();
		restClient.loginWithBasicAuth(
				config.getEndpoint(), config.getUsername(), config.getPassword(), Locale.ENGLISH);
	}

	@Override
	public void setSupplierArticleApprovalStatus(String supplierArticleNumber, String supplier, ApprovalStatus value) {
		log.debug("set approval status (supplierArticleNumber: {}, supplier: {}) to: {}", supplierArticleNumber, supplier, value);

		EntityItemTable entityItemTable = new EntityItemTable("Article");

		// Price Status column added
		EntityItemTableColumn priceStatus = new EntityItemTableColumn("Article.PriceStatus");

		entityItemTable.addColumn(priceStatus);

		// Article number, Supplier got from request
		EntityItemReference valuesReference = EntityItemReferenceFactory
				.createByIdentifier(supplierArticleNumber, supplier);

		// 3 and 4 are respective for price disapproved/approved

		EntityItemTableRow valuesRowItem = new EntityItemTableRow(valuesReference, Collections.singletonList(value.getPimValue()));
		entityItemTable.addRow(valuesRowItem);

		ListWriteRequest listWriteRequest = restClient.createListWriteRequest();

		listWriteRequest.writeEntityItemTable(entityItemTable);
	}

	@Override
	public void setSupplierArticleRemark(String supplierArticleNumber, String supplier, String remark) {
		log.debug("set remark (supplierArticleNumber: {}, supplier: {}) to: {}", supplierArticleNumber, supplier, remark);

		EntityItemTable entityItemTable = new EntityItemTable("Article");

		EntityItemTableColumn remarksStatus = new EntityItemTableColumn("ArticleLang.Remarks(Dutch)");

		entityItemTable.addColumn(remarksStatus);

		// Article number, Supplier got from request
		EntityItemReference valuesReference = EntityItemReferenceFactory.createByIdentifier(supplierArticleNumber,
				supplier);

		EntityItemTableRow valuesRowItem = new EntityItemTableRow(valuesReference, Collections.singletonList(remark));
		entityItemTable.addRow(valuesRowItem);

		ListWriteRequest listWriteRequest = restClient.createListWriteRequest();

		listWriteRequest.writeEntityItemTable(entityItemTable);
	}
}
