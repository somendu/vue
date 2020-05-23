package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToP360ApplicationAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.Variant;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.classification.*;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {
    private static final String READ_FIELDS = String.join(
            ",",
            "Variant.Id",
            "Variant.TMEGeneration",
            "VariantLang.TMEApplication(EN)",
            "VariantLang.TMEDescriptionShort(EN)",
            "Variant.CurrentStatus",
            "Variant.TMEInteriorColours",
            "VariantLang.TMEInteriorColours(EN)",
            "Variant.TMEExteriorColours",
            "VariantLang.TMEExteriorColours(EN)",
            "Variant.TMETrimColours",
            "VariantLang.TMETrimColours(EN)",
            "Variant.TMEEquipmentSpecs",
            "VariantStructureMap.StructureGroup(\"LocalVehicleStructure\")");

    private final InformaticaPimProperties pimProperties;
    private final RestTemplate rest;
    private final P360Variants variantsRepo;

    private final RowToP360ApplicationAdapter applicationAdapter;

    @Override
    public List<P360Application> findByBrandAndModelAndProject(String brand, String model, String project) {

        log.debug("get application for brand {}, brand {} and model {}", brand, model, project);

        String query = new StringBuilder()
                .append("Variant.ManufacturerName equalsic \"")
                .append(brand)
                .append("\" AND ")
                .append("Variant.ManufacturerAID equalsic \"")
                .append(model)
                .append("\" AND ")
                .append("Variant.TMEGeneration equalsic \"")
                .append(project)
                .append("\"").toString();

        GetResponse res = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/Variant/bySearch"
                        + "?query={productNumber}"
                        + "&fields=" + READ_FIELDS,
                GetResponse.class,
                query);

        log.trace("raw response: {}", Json.of(res));

        return res
                .getRows()
                .stream()
                .map(applicationAdapter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void updateClassifications(String applicationId, P360Structure structure, List<P360Classification> classifications) throws CouldNotSaveException {
        List<String> classificationIdentifiers = classifications.stream().map(P360Classification::getIdentifier).collect(Collectors.toList());
        UpdateClassificationRow row = new UpdateClassificationRow()
                .setObject(new UpdateClassificationRowObject().setId(String.format("%s@1", applicationId)))
                .setQualification(new UpdateClassificationQualification().setStructureId(structure.getObjectId()))
                .setValues(Collections.singletonList(CastUtils.asStrings(classificationIdentifiers)));

        UpdateClassificationRequest request = new UpdateClassificationRequest();
        request.setColumns(Collections.singletonList(new UpdateClassificationColumn().setIdentifier("VariantStructureMap.ManualMap")));
        request.setRows(Lists.newArrayList(row));

        String url = pimProperties.getServer() + "/rest/V2.0/list/Variant/VariantStructureMap";

        log.info("Update classification request: {}", Json.of(request));

        UpdateItemResponse response = rest.postForObject(url,
                request,
                UpdateItemResponse.class);

        log.info("Update classification response: {}", Json.of(response));

        if (!Objects.equals(response.getCounters().getErrors(), 0)) {
            log.warn("could not save classifications {} for variant {}: {}", classifications, applicationId, response);
            throw new CouldNotSaveException("Couldn't save application classifications");
        }
    }

    @Override
    public P360Application findById(String applicationId) {
        log.debug("get application for id {}", VariantUtils.formatId(applicationId));

        GetResponse res = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/Variant/byItems?items={applicationId}"
                        + "&fields=" + READ_FIELDS,
                GetResponse.class,
                VariantUtils.formatId(applicationId));

        log.trace("raw response: {}", Json.of(res));

        List<P360Application> applications = res.getRows()
                .stream()
                .map(applicationAdapter::convert)
                .collect(Collectors.toList());
        if (applications.size() == 1) {
            return applications.get(0);
        }
        // TODO: check if no exception needs to be thrown
        return null;
    }

    @Override
    public List<Variant> findByProductId(String productId) {
        if(StringUtils.endsWith(productId, "@1")){
            return variantsRepo.getForProduct(StringUtils.removeEnd(productId, "@1"));
        }

        return variantsRepo.getForProduct(productId);
    }
}
