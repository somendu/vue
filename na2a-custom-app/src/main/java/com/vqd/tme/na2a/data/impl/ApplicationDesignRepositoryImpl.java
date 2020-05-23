package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.P360ToApplicationDesignAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationDesignRepository;
import com.vqd.tme.na2a.model.PostApplicationDesigns;
import com.vqd.tme.na2a.model.WltpDesignReplacementOfEquipment;
import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.support.Json;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationDesignRepositoryImpl implements ApplicationDesignRepository {

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    private final P360ToApplicationDesignAdapter adapter;

    private static final String FIELDS = String.join(
            ",",
            "VariantDesign.TMELCoOrdinates",
            "VariantDesign.TMELCoOrdinatesRounded",
            "VariantDesign.TMEReplacementAccessory",
            "VariantDesign.TMEIncalculable",
            "VariantDesign.TMEMassOfRemovedEquipmentOriginal",
            "VariantDesign.TMEMassOfRemovedEquipmentRounded");

    private static final String BASE_PATH = "/rest/V2.0/list/Variant/VariantDesign/";

    @Override
    public P360ApplicationDesign findByApplication(String applicationId) {
        String url = properties.getServer()
                + BASE_PATH
                + "byItems?items={applicationId}"
                + "&fields=" + FIELDS;

        GetResponse response = restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId(applicationId));
        return adapter.convert(response);
    }

    @Override
    public UpdateItemResponse update(PostApplicationDesigns designs) {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setColumns(Lists.newArrayList(
                new UpdateItemRequest.Column("VariantDesign.TMEReplacementAccessory"),
                new UpdateItemRequest.Column("VariantDesign.TMEMassOfRemovedEquipmentOriginal"),
                new UpdateItemRequest.Column("VariantDesign.TMEMassOfRemovedEquipmentRounded"),
                new UpdateItemRequest.Column("VariantDesign.TMEIncalculable"),
                new UpdateItemRequest.Column("VariantDesign.TMELCoOrdinates"),
                new UpdateItemRequest.Column("VariantDesign.TMELCoOrdinatesRounded")
        ));

        List<UpdateItemRequest.Row> rows = Lists.newArrayList();
        designs.forEach(design -> {
            UpdateItemRequest.Row row = new UpdateItemRequest.Row();
            row.setQualification(null);
            row.setObject(new UpdateItemRequest.Row.RowObject(VariantUtils.formatId(design.getApplicationId())));
            row.setValues(Lists.newArrayList(
                    Optional.ofNullable(design.getReplacementAccessory()).map(replacementAccessory -> replacementAccessory.toString()).orElse(null),
                    Optional.ofNullable(design.getDeltaMass()).map(BigDecimal::toString).orElse(null),
                    Optional.ofNullable(design.getDeltaMassRounded()).map(rounded -> rounded.toString()).orElse(null),
                    Optional.ofNullable(design.getIncalculable()).map(incalulable -> incalulable.toString()).orElse(null),
                    Optional.ofNullable(design.getLcoordinates()).map(BigDecimal::toString).orElse(null),
                    Optional.ofNullable(design.getLcoordinatesRounded()).map(rounded -> rounded.toString()).orElse(null)
            ));
            rows.add(row);
        });

        request.setRows(rows);

        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/VariantDesign/";

        log.info("Design update request to URL {} with body {}", url, Json.of(request));

        UpdateItemResponse response = restTemplate.postForObject(url, request, UpdateItemResponse.class);

        log.info("Design update response: {}", Json.of(response));

//        updateReplacementOfEquipment(designs);
        return response;
    }

    private void updateReplacementOfEquipment(PostApplicationDesigns designs) {

        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/";

        designs.forEach(design -> {
            List<WltpDesignReplacementOfEquipment> replacementOfEquipments =
                    resolveEquipmentReplacement(design.getReplacementOfEquipments());

            replacementOfEquipments.forEach(replacementOfEquipment -> {
                List<UpdateItemRequest.Column> columns = Lists.newArrayList();
                List<UpdateItemRequest.Row> rows = Lists.newArrayList();

                // Add columns
                UpdateItemRequest.Column equipmentColumn = new UpdateItemRequest.Column();
                equipmentColumn.setIdentifier("VariantDesignReplacement.TMEEquipment(" +
                        replacementOfEquipment.getEquipment() + ")");

                UpdateItemRequest.Column flagColumn = new UpdateItemRequest.Column();
                flagColumn.setIdentifier("VariantDesignReplacement.TMEReplacementSpecFlag(" +
                        replacementOfEquipment.getEquipment() + ")");

                UpdateItemRequest.Column codeColumn = new UpdateItemRequest.Column();
                codeColumn.setIdentifier("VariantDesignReplacement.TMEReplacementSpecOECode(" +
                        replacementOfEquipment.getEquipment() + ")");

                // TODO whenyy time allows make method with overlapping code
                columns.add(equipmentColumn);
                columns.add(flagColumn);
                columns.add(codeColumn);

                // Add rows
                UpdateItemRequest.Row row = new UpdateItemRequest.Row();
                row.setObject(null);
                row.setObject(new UpdateItemRequest.Row.RowObject(VariantUtils.formatId(design.getApplicationId())));
                row.setValues(Lists.newArrayList(
                       "",
                        "",
                        replacementOfEquipment.getCode()
                ));

                rows.add(row);

                // prepare request
                UpdateItemRequest request = new UpdateItemRequest();
                request.setColumns(columns);
                request.setRows(rows);

                UpdateItemResponse response = restTemplate.postForObject(url, request, UpdateItemResponse.class);

                log.trace("Replacement of Equipment update response: {}", Json.of(response));
            });
        });
    }

    private List<WltpDesignReplacementOfEquipment> resolveEquipmentReplacement(List<Object> replacementOfEquipments) {

        List<WltpDesignReplacementOfEquipment> replacementOfEquipmentsList = new ArrayList<>();

        if (replacementOfEquipments != null) {

            for (Object replacementOfEquipment : replacementOfEquipments) {

                LinkedHashMap<String, Object> aMap = (LinkedHashMap<String, Object>) replacementOfEquipment;

                WltpDesignReplacementOfEquipment roe = new WltpDesignReplacementOfEquipment(
                        (String) aMap.get("code"),
                        (String) aMap.get("equipment"),
                        null
                );

                replacementOfEquipmentsList.add(roe);
            }
        }

        return replacementOfEquipmentsList;
    }
}
