package com.vqd.tme.na2a.task.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.VariantReference;
import com.vqd.tme.na2a.partlinking.persistence.Parts;
import com.vqd.tme.na2a.partlinking.persistence.VariantReferences;
import com.vqd.tme.na2a.task.data.TaskPartRepository;
import com.vqd.tme.na2a.task.model.TaskPart;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskPartRepositoryImpl implements TaskPartRepository {

	private final Parts p360PartsRepo;
	private final VariantReferences variantRefs;

	@Override
	public List<TaskPart> findByApplicationId(String applicationId) {

		Set<String> occurringPartIds = new HashSet<>();
//	    Map<String, List<VariantReference>> linksByVariantId = new HashMap<>();

	    List<VariantReference> links = variantRefs.list(applicationId);
//	    linksByVariantId.put(applicationId, links);

	    for (VariantReference link : links) {
	    	occurringPartIds.add(link.getArticleCode());
	    }

		Map<String, Part> p360Parts = p360PartsRepo.get(occurringPartIds);

		// parts
		return getParts(links, p360Parts);
	}

	private List<TaskPart> getParts(List<VariantReference> links, Map<String, Part> p360Parts) {
		List<TaskPart> taskParts = new ArrayList<>();
		if(links != null) {
			for (VariantReference link : links) {
				Part part = p360Parts.get(link.getArticleCode());

				taskParts.add(TaskPart.builder()
						.partNumber(link.getArticleCode())
						.partName(part != null ? part.getPartName() : null)
						.quantity(link.getQuantity())
						.type(link.getType()).build());
			}
		}
		return taskParts;
	}
}
