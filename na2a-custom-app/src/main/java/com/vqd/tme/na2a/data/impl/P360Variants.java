package com.vqd.tme.na2a.data.impl;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.Variants;
import com.vqd.tme.na2a.data.support.P360Field;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.model.Variant;
import com.vqd.tme.na2a.model.Variant.Options;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.support.CollectionUtils;
import com.vqd.tme.na2a.support.Json;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class P360Variants implements Variants {
	private static final List<P360Field<Variant, ?>> VARIANT_FIELDS = Arrays.asList(
			new P360Field<>("Variant.Id", P360Field.Type.STRING, Variant::setId),
			new P360Field<>("Variant.VariantNo", P360Field.Type.STRING, Variant::setVariantNo),
			new P360Field<>("VariantLang.TMEApplication(EN)", P360Field.Type.STRING, Variant::getShortDescription,
					Variant::setShortDescription),
			new P360Field<>("VariantLang.Keyword(EN)", P360Field.Type.MULTI_STRING, Variant::getKatashiki,
					Variant::setKatashiki));

	private static final List<P360Field<Options, ?>> OPTIONS_FIELDS = Arrays.asList(
			new P360Field<>("Variant.ManufacturerName", P360Field.Type.STRING, Options::getBrand, Options::setBrand),
			new P360Field<>("Variant.ManufacturerAID", P360Field.Type.STRING, Options::getModel, Options::setModel),
			new P360Field<>("Variant.CurrentStatus", P360Field.Type.STRING, Options::getStatusCode,
					Options::setStatusCode),
			new P360Field<>("Variant.TMECancelationReason", P360Field.Type.LOOKUP, Options::getCancelledReason,
					Options::setCancelledReason),
			new P360Field<>("Variant.TMEGeneration", P360Field.Type.STRING, Options::getGeneration,
					Options::setGeneration),
			new P360Field<>("Variant.TMESubprojects", P360Field.Type.MULTI_STRING, Options::getSubProjectIds,
					Options::setSubProjectIds),
			new P360Field<>("VariantLang.TMESubprojects(EN)", P360Field.Type.MULTI_STRING, Options::getSubProjects,
					Options::setSubProjects),
			new P360Field<>("Variant.TMEBodyTypes", P360Field.Type.MULTI_STRING, Options::getBodyTypeIds,
					Options::setBodyTypeIds),
			new P360Field<>("VariantLang.TMEBodyTypes(EN)", P360Field.Type.MULTI_STRING, Options::getBodyTypes,
					Options::setBodyTypes),
			new P360Field<>("Variant.TMEEngines", P360Field.Type.MULTI_STRING, Options::getEngineIds,
					Options::setEngineIds),
			new P360Field<>("VariantLang.TMEEngines(EN)", P360Field.Type.MULTI_STRING, Options::getEngines,
					Options::setEngines),
			new P360Field<>("Variant.TMETransmissions", P360Field.Type.MULTI_STRING, Options::getTransmissionIds,
					Options::setTransmissionIds),
			new P360Field<>("VariantLang.TMETransmissions(EN)", P360Field.Type.MULTI_STRING, Options::getTransmissions,
					Options::setTransmissions),
			new P360Field<>("Variant.TMEFactoryGrades", P360Field.Type.MULTI_STRING, Options::getProductionGradeIds,
					Options::setProductionGradeIds),
			new P360Field<>("VariantLang.TMEFactoryGrades(EN)", P360Field.Type.MULTI_STRING,
					Options::getProductionGrades, Options::setProductionGrades),
			new P360Field<>("Variant.TMEProductionLocations", P360Field.Type.MULTI_STRING,
					Options::getProductionLocationIds, Options::setProductionLocationIds),
			new P360Field<>("VariantLang.TMEProductionLocations(EN)", P360Field.Type.MULTI_STRING,
					Options::getProductionLocations, Options::setProductionLocations),
			new P360Field<>("Variant.TMESteering", P360Field.Type.STRING, Options::getSteeringId,
					Options::setSteeringId),
			new P360Field<>("VariantLang.TMESteering(EN)", P360Field.Type.STRING, Options::getSteering,
					Options::setSteering),
			new P360Field<>("Variant.TMEEquipmentSpecs", P360Field.Type.MULTI_STRING, Options::getFactoryEquipmentIds,
					Options::setFactoryEquipmentIds),
			new P360Field<>("VariantLang.TMEEquipmentSpecs(EN)", P360Field.Type.MULTI_STRING,
					Options::getFactoryEquipments, Options::setFactoryEquipments),
			new P360Field<>("Variant.TMEInteriorColours", P360Field.Type.MULTI_STRING, Options::getInteriorColourIds,
					Options::setInteriorColourIds),
			new P360Field<>("VariantLang.TMEInteriorColours(EN)", P360Field.Type.MULTI_STRING,
					Options::getInteriorColours, Options::setInteriorColours),
			new P360Field<>("Variant.TMEExteriorColours", P360Field.Type.MULTI_STRING, Options::getExteriorColourIds,
					Options::setExteriorColourIds),
			new P360Field<>("VariantLang.TMEExteriorColours(EN)", P360Field.Type.MULTI_STRING,
					Options::getExteriorColours, Options::setExteriorColours),
			new P360Field<>("Variant.TMETrimColours", P360Field.Type.MULTI_STRING, Options::getTrimColourIds,
					Options::setTrimColourIds),
			new P360Field<>("VariantLang.TMETrimColours(EN)", P360Field.Type.MULTI_STRING, Options::getTrimColours,
					Options::setTrimColours),
			new P360Field<>("Variant.TMECountries", P360Field.Type.MULTI_ENUM, Options::getCountries,
					Options::setCountries));
//TODO: fields below are not available in PIM yet
//      new P360Field<>("Variant.TMELocalNMSC", P360Field.Type.STRING, Options::getOrganisation, Options::setOrganisation),
//      new P360Field<>("VariantLang.TMELocalProject(EN)", P360Field.Type.MULTI_STRING, Options::getLocalProject, Options::setLocalProject),
//      new P360Field<>("VariantLang.TMELocalModel(EN)", P360Field.Type.MULTI_STRING, Options::getLocalModel, Options::setLocalModel),
//      new P360Field<>("VariantLang.TMELocalGrade(EN)", P360Field.Type.MULTI_STRING, Options::getLocalGrade, Options::setLocalGrade),
//      new P360Field<>("VariantLang.TMELocalModelCode(EN)", P360Field.Type.MULTI_STRING, Options::getLocalModelCode, Options::setLocalModelCode));
	// field.Variant.TMEInteriorColours.description = Interior Colours IDs

	private static final String ID_GENERATION_FIELD = "latestApplicationSequence";

	private static final String READ_FIELDS = Stream.concat(VARIANT_FIELDS.stream(), OPTIONS_FIELDS.stream())
			.map(P360Field::name).collect(Collectors.joining(","));

	private static final List<UpdateItemRequest.Column> WRITE_COLUMNS = Stream
			.concat(VARIANT_FIELDS.stream(), OPTIONS_FIELDS.stream()).filter(f -> !f.readOnly()).map(P360Field::name)
			.map(UpdateItemRequest.Column::new).collect(Collectors.toList());

	private final InformaticaPimProperties pimProperties;

	// Using Rest Template Here
	private final RestTemplate rest;

	@Override
	public List<Variant> getForProduct(String productId) {
		log.debug("getForProduct productId: {}", productId);

		GetResponse res = rest.getForObject(pimProperties.getServer() + "/rest/V2.0/list/Variant/byProducts"
				+ "?products={productNumber}" + "&fields=" + READ_FIELDS, GetResponse.class,
				String.format("%s@1", productId));

		log.trace("raw response: {}", Json.of(res));

		List<Variant> result = res.getRows().stream().map(this::resolve).collect(Collectors.toList());

		log.debug("getForProduct result: {}", Json.of(result));
		return result;
	}

	private Variant resolve(GetResponse.Row row) {
		Iterator<Object> values = row.getValues().iterator();

		Variant variant = new Variant();

		Iterator<P360Field<Variant, ?>> variantIter = VARIANT_FIELDS.iterator();
		while (variantIter.hasNext() && values.hasNext()) {
			variantIter.next().set(variant, values.next());
		}

		Options options = new Options();

		variant.setOptions(options);
		Iterator<P360Field<Options, ?>> optionsIter = OPTIONS_FIELDS.iterator();
		while (optionsIter.hasNext() && values.hasNext()) {
			optionsIter.next().set(options, values.next());
		}

		return variant;
	}

	@Override
	public void create(String productNumber, Variant variant) throws CouldNotSaveException, SavedWithErrorsException {
		String label = getLabel();

		// create in P360
		UpdateItemResponse res = save(String.format("'APPL%s'@1", label), variant);

		validateSaveResponse(variant, res);

		String id = res.getObjects().get(0).getObject().getId();
		String variantId = StringUtils.substringBefore(id, "@");

		log.debug("created variant {}", variantId);

		// bind variant to product
		bind(id, productNumber);

		// update id
		variant.setId(variantId);
	}

	private void validateSaveResponse(Variant variant, UpdateItemResponse res)
			throws CouldNotSaveException, SavedWithErrorsException {
		if (hasNotCreatedOrUpdatedObject(res)) {
			if (!Objects.equals(res.getCounters().getWarnings(), 0)) {
				log.warn("variant {} saved with errors: {}", variant, res);

				throw new SavedWithErrorsException("Variant saved with errors!");
			}

			if (!Objects.equals(res.getCounters().getErrors(), 0)) {
				log.warn("could not save variant {}: {}", variant, res);

				throw new CouldNotSaveException("Couldn't save variant");
			}
		}
	}

	private boolean hasNotCreatedOrUpdatedObject(UpdateItemResponse res) {
		return !((Objects.equals(res.getCounters().getCreatedObjects(), 1)
				|| Objects.equals(res.getCounters().getUpdatedObjects(), 1))
				&& Objects.equals(res.getCounters().getErrors(), 0)
				&& Objects.equals(res.getCounters().getWarnings(), 0));
	}

	@Override
	public void update(Variant variant) throws CouldNotSaveException, SavedWithErrorsException {
		// save in P360
		UpdateItemResponse res = save(String.format("%s@1", variant.getId()), variant);

		validateSaveResponse(variant, res);
	}

	private UpdateItemResponse save(String id, Variant variant) {
		List<Object> values = new ArrayList<>();

		for (P360Field<Variant, ?> field : VARIANT_FIELDS) {
			if (!field.readOnly()) {
				// TODO: temporary hack to truncate application name - DO REMOVE !!!!!!
				if (field.name().equals("VariantLang.TMEApplication(EN)")) {
					String applicationName = (String) field.get(variant);
					values.add(StringUtils.truncate(applicationName, 250));
				} else {
					values.add(field.get(variant));
				}
			}
		}

		Options options = variant.getOptions();
		for (P360Field<Options, ?> field : OPTIONS_FIELDS) {
			if (!field.readOnly()) {
				values.add(field.get(options));
			}
		}

		UpdateItemRequest request = new UpdateItemRequest().setColumns(WRITE_COLUMNS).setRows(Collections.singletonList(
				new UpdateItemRequest.Row().setObject(new UpdateItemRequest.Row.RowObject(id)).setValues(values)));

		log.trace("save request: {}", Json.of(request));

		UpdateItemResponse response = rest.postForObject(pimProperties.getServer() + "/rest/V2.0/list/Variant/",
				request, UpdateItemResponse.class);

		log.trace("save response: {}", response);

		return response;
	}

	private void bind(String variantId, String productNumber) throws CouldNotSaveException {
		UpdateItemRequest request = new UpdateItemRequest()
				.setColumns(Collections.singletonList(new UpdateItemRequest.Column(
						String.format("SuperordinateProductReference.ReferencedSupplierAid(\"%s\")", productNumber))))
				.setRows(Collections.singletonList(
						new UpdateItemRequest.Row().setObject(new UpdateItemRequest.Row.RowObject(variantId))
								.setValues(Collections.singletonList(""))));

		log.trace("bind request: {}", Json.of(request));

		UpdateItemResponse response = rest.postForObject(pimProperties.getServer() + "/rest/V2.0/list/Variant/",
				request, UpdateItemResponse.class);

		log.trace("bind response: {}", response);

		if (response.getCounters().getErrors() != 0) {
			UpdateItemResponse.Entry entry = CollectionUtils.firstOf(response.getEntries());
			if (entry != null) {
				throw new CouldNotSaveException("Couldn't bind variant to product: %s", entry.getMessage());
			}
			throw new CouldNotSaveException("Couldn't bind variant to product");
		}
	}

	private String getLabel() {
		GetResponse response = rest.getForObject(pimProperties.getServer() + "/rest/V2.0/list/LookupValue/"
				+ "byIdentifiers/?lookup=identifierGenerator&identifiers=" + ID_GENERATION_FIELD
				+ "&fields=LookupValue.Order", GetResponse.class);

		log.trace("getLabel response: {}", response);

		int sequenceNo = parseInt(response.getRows().get(0).getValues().get(0).toString());

		updateLabel(++sequenceNo);

		return String.valueOf(sequenceNo);
	}

	private void updateLabel(int newNo) {
		UpdateItemRequest request = new UpdateItemRequest()
				.setColumns(Collections.singletonList(new UpdateItemRequest.Column("LookupValue.Order")))
				.setRows(Collections.singletonList(new UpdateItemRequest.Row()
						.setObject(new UpdateItemRequest.Row.RowObject(
								"'" + ID_GENERATION_FIELD + "'@'identifierGenerator'"))
						.setValues(Collections.singletonList(newNo))));

		UpdateItemResponse response = rest.postForObject(pimProperties.getServer() + "/rest/V2.0/list/LookupValue",
				request, UpdateItemResponse.class);

		log.trace("updateLabel response: {}", response);
	}
}
