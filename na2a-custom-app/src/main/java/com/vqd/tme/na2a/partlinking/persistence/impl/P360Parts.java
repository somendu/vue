package com.vqd.tme.na2a.partlinking.persistence.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.support.P360Field;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.GetResponse.Row;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.PartsFilter;
import com.vqd.tme.na2a.partlinking.persistence.Parts;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Json;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class P360Parts implements Parts {

	private static final List<P360Field<Part, ?>> NPA_ARTICLE_FIELDS = Arrays.asList(
			new P360Field<>("Article.SupplierAID", P360Field.Type.STRING, Part::getPartNumber, Part::setPartNumber),
			new P360Field<>("ArticleLang.DescriptionShort(EN)", P360Field.Type.STRING, Part::getPartName,
					Part::setPartName),
			new P360Field<>("ArticleDesign.TMEKnownInNPA", P360Field.Type.BIT, Part::getKnownInNPA,
					Part::setKnownInNPA));

	private static final String READ_FIELDS = String.join(",", "Article.SupplierAID",
			"ArticleLang.DescriptionShort(EN)", "Article.TMEColour", "Article.TMEMaterial",
			"ArticleDesign.TMEKnownInNPA");

	private static final List<UpdateItemRequest.Column> WRITE_COLUMNS = NPA_ARTICLE_FIELDS.stream()
			.filter(f -> !f.readOnly()).map(P360Field::name).map(UpdateItemRequest.Column::new)
			.collect(Collectors.toList());

	private static final String ID_GENERATION_FIELD = "latestArticleSequence";

	private final InformaticaPimProperties pimProperties;
	private final RestTemplate rest;

	@Override
	public Map<String, Part> get(Collection<String> identifiers) {
		if (identifiers == null || identifiers.isEmpty()) {
			return Collections.emptyMap();
		}

		// The Rest Api call byIdentifier doesn't take multiple identifiers in one call,
		// so we need to iterate through the list
		Map<String, Part> result = Maps.newHashMap();
		identifiers.forEach(identifier -> result.putAll(this.get(identifier)));
		return result;
	}

	@Override
	public Map<String, Part> get(String identifier) {
		if (StringUtils.isEmpty(identifier)) {
			return Collections.emptyMap();
		}

		GetResponse res = rest.getForObject(
				pimProperties.getServer() + "/rest/V2.0/list/Article/byIdentifiers" + "?identifiers={identifiers}"
						+ "&fields=" + READ_FIELDS + "&includeLabels=true&formatData=true",
				GetResponse.class, identifier);

		Map<String, Part> result = new HashMap<>();

		for (Row row : res.getRows()) {
			String id = CastUtils.asString(row.getValues().get(0));
			result.put(id, new Part(id, CastUtils.asString(row.getValues().get(1)), getLabel(row.getValues().get(2)),
					getLabel(row.getValues().get(3)), CastUtils.asBoolean(row.getValues().get(4))));
		}

		return result;
	}

	@Override
	@Ignore
	public List<Part> search(PartsFilter filter) {
		String query = generateQuery(filter);

		GetResponse res = rest.getForObject(
				pimProperties.getServer() + "/rest/V2.0/list/Article/bySearch" + "?query={query}" + "&fields="
						+ READ_FIELDS + "&pageSize=100" + "&orderBy=0-ASC" + "&includeLabels=true&formatData=true",
				GetResponse.class, query);

		// to do filter colours:
		if (!filter.getColourOrMaterial().isEmpty()) {
			return filterColourOrMaterial(res, filter.getColourOrMaterial());
		}

		return res.getRows().stream().map(row -> createPart(row)).collect(Collectors.toList());
	}

	private String getEscapedString(String text) {
		return text.replaceAll("\"", "\\\"");
	}

	// TODO take characteristics in to account when available in PIM.
	private String generateQuery(PartsFilter filter) {
		List<String> andList = new ArrayList<>();

		if (!filter.getPartNumber().isEmpty()) {
			andList.add(
					String.format("Article.SupplierAID containsIC \"%s\"", getEscapedString(filter.getPartNumber())));
		}

		if (!filter.getPartName().isEmpty()) {
			andList.add(String.format("ArticleLang.DescriptionShort(EN) containsIC \"%s\"",
					getEscapedString(filter.getPartName())));
		}

		if (!filter.getProjectCode().isEmpty()) {
			andList.add(String.format("PartProject.TMEProjectCode containsIC \"%s\"",
					getEscapedString(filter.getProjectCode())));
		}

		if (!filter.getPersonInCharge().isEmpty()) {
			andList.add(String.format("PartProject.TMEPersonInCharge containsIC \"%s\"",
					getEscapedString(filter.getPersonInCharge())));
		}

		if (!filter.getCommodity().isEmpty()) {
			andList.add(String.format("PartProject.TMECommodity containsIC \"%s\"",
					getEscapedString(filter.getCommodity())));
		}

		return String.join(" AND ", andList);
	}

	@Override
	/**
	 * NOTE: A delete does not trigger an HTTP error status, even though the user is
	 * not allowed to do so. Therefore a workaround is to compare the amount of
	 * results before and after the results. When better solution arises, please
	 * implement or share :)
	 */
	public void delete(String variantId, String partId, String partType) throws CouldNotSaveException {
		String query = String.format(
				"Variant.Id+=+%s&qualificationFilter=referenceType(\"%s\"),referencedSupplierAid(\"%s\")", variantId,
				partType, partId);

		GetResponse initialPartReferences = rest.getForObject(pimProperties.getServer()
				+ "/rest/V1.0/list/Variant/Variant2ArticleReference/bySearch/" + "?query=" + query, GetResponse.class);

		int initialRows = initialPartReferences.getRowCount();

		rest.delete(pimProperties.getServer() + "/rest/V1.0/list/Variant/Variant2ArticleReference/bySearch/" + "?query="
				+ query);

		GetResponse afterDeleteCall = rest.getForObject(pimProperties.getServer()
				+ "/rest/V1.0/list/Variant/Variant2ArticleReference/bySearch/" + "?query=" + query, GetResponse.class);

		int responseRows = afterDeleteCall.getRowCount();

		if (responseRows == initialRows) {
			log.warn("Did not delete parts reference... {}");

			throw new CouldNotSaveException("Couldn't save changes...");
		}
	}

	private String getLabel(Object row) {
		if (row instanceof LinkedHashMap) {
			LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) row;
			String label = map.get("label");

			if (label == null) {
				return "";
			}

			return label;
		}
		return "";
	}

	/**
	 * Colour and Material are ENTITY_ITEMS, therefore not as straightforward to
	 * filter on these bySearch as strings. As these fields are filtered after
	 * partnumber/commodity/PIC is set when can easily filter the way below.
	 * 
	 * @param res
	 * @param searchable
	 * @return
	 */
	private List<Part> filterColourOrMaterial(GetResponse res, String searchable) {
		List<Part> relevantList = new ArrayList<>();

		List<Part> relevantColours = getRelevantItems(res, searchable, 2);
		List<Part> relevantMaterial = getRelevantItems(res, searchable, 3);

		Stream.of(relevantColours, relevantMaterial).forEach(relevantList::addAll);

		return relevantList;
	}

	private List<Part> getRelevantItems(GetResponse res, String searchable, int idx) {
		return res.getRows().stream().filter(row -> {
			Object o = row.getValues().get(idx);
			String label = getLabel(o);
			return label.toLowerCase().contains(searchable.toLowerCase());
		}).map(row -> createPart(row)).collect(Collectors.toList());
	}

	private Part createPart(GetResponse.Row row) {
		return new Part(CastUtils.asString(row.getValues().get(0)), CastUtils.asString(row.getValues().get(1)),
				getLabel(row.getValues().get(2)), getLabel(row.getValues().get(3)),
				CastUtils.asBoolean(row.getValues().get(4)));
	}

	@Override
	public void createNPA(Part part) throws CouldNotSaveException {
		log.debug("createNPA {}", part);

		String id = String.format("'%s'@1", part.getPartNumber());

		List<Object> values = new ArrayList<>();
		for (P360Field<Part, ?> field : NPA_ARTICLE_FIELDS) {
			values.add(field.get(part));
		}

		UpdateItemRequest request = new UpdateItemRequest().setColumns(WRITE_COLUMNS).setRows(Collections.singletonList(
				new UpdateItemRequest.Row().setObject(new UpdateItemRequest.Row.RowObject(id)).setValues(values)));

		log.trace("createNPA POST request: {}", Json.of(request));

		UpdateItemResponse response = rest.postForObject(pimProperties.getServer() + "/rest/V2.0/list/Article/",
				request, UpdateItemResponse.class);

		log.trace("createNPA POST response: {}", response);
		validateSaveResponse(part, response);
	}

	private void validateSaveResponse(Part part, UpdateItemResponse response) throws CouldNotSaveException {
		if (!Objects.equals(response.getCounters().getWarnings(), 0)) {
			log.warn("Part saved with errors: {}", part, response);
			throw new CouldNotSaveException("Part saved with errors!");
		}
		if (!Objects.equals(response.getCounters().getErrors(), 0)) {
			log.warn("Could not save part {}: {}", part, response);
			throw new CouldNotSaveException("Could not save part!");
		}
	}

}
