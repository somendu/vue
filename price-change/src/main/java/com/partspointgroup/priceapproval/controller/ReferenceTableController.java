/**
 *
 */
package com.partspointgroup.priceapproval.controller;

import com.partspointgroup.priceapproval.model.KeyValue;
import com.partspointgroup.priceapproval.service.ReferenceTableService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reference table rest controller
 *
 * @author SomenduMaiti
 *
 */
@RestController
@RequestMapping("/api/reference-tables")
@RequiredArgsConstructor
public class ReferenceTableController {
	private final ReferenceTableService referenceTableService;

	@GetMapping("buyers")
	public List<KeyValue> getProductManagers(@RequestParam(defaultValue="false") boolean history) {
		return referenceTableService.getProductManagers(history);
	}

	@GetMapping("goodsGroups")
	public List<KeyValue> getGoodsGroups(@RequestParam(defaultValue="false") boolean history) {
		return referenceTableService.getGoodsGroups(history);
	}

	@GetMapping("articleGroups")
	public List<KeyValue> getArticleGroups(@RequestParam(defaultValue="false") boolean history) {
		return referenceTableService.getArticleGroups(history);
	}

	@GetMapping("supplierCodes")
	public List<KeyValue> getSuppliers(@RequestParam(defaultValue="false") boolean history) {
		return referenceTableService.getSuppliers(history);
	}
}
