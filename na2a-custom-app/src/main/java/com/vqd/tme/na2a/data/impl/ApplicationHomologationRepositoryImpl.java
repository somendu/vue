package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.P360ToApplicationHomologationAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ApplicationHomologationRepository;
import com.vqd.tme.na2a.model.PostApplicationHomologation;
import com.vqd.tme.na2a.model.PostApplicationHomologations;
import com.vqd.tme.na2a.model.WltpHomologationGenerationCode;
import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
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
public class ApplicationHomologationRepositoryImpl implements ApplicationHomologationRepository {

    private static final String FIELDS = String.join(
            ",",
            "VariantHomologation.TMEWLTPFlag",
            "VariantHomologation.TMEHomologationInformation",
            "VariantHomologation.TMEHomologation",
            "VariantHomologation.TMEAvailableForPPORequest",
            "VariantHomologation.TMEComponentApprovalDocument",
            "VariantHomologation.TMEDeltaCDA");

    private static final String BASE_PATH = "/rest/V2.0/list/Variant/VariantHomologation/";

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;

    private final P360ToApplicationHomologationAdapter adapter;


    @Override
    public P360ApplicationHomologation findByApplication(String applicationId) {

        String url = properties.getServer()
                + BASE_PATH
                + "byItems?items={applicationId}"
                + "&fields=" + FIELDS;

        log.trace("Request {}", Json.of(url));

        GetResponse response = restTemplate.getForObject(url, GetResponse.class, VariantUtils.formatId(applicationId));
        log.trace("Response {}", Json.of(response));

        return adapter.convert(response);
    }

    @Override
    public List<UpdateItemResponse> update(PostApplicationHomologations homologations) {
        List<UpdateItemResponse> responses = new ArrayList<>();

        updateGenerationCode(homologations);

        homologations.forEach(homologation -> {
            UpdateItemRequest request = new UpdateItemRequest();
            List<UpdateItemRequest.Column> columns = Lists.newArrayList();
            List<UpdateItemRequest.Row> rows = Lists.newArrayList();

            UpdateItemRequest.Column columnFlag = new UpdateItemRequest.Column();
            columnFlag.setIdentifier("VariantHomologation.TMEWLTPFlag");

            UpdateItemRequest.Column columnCDA = new UpdateItemRequest.Column();
            columnCDA.setIdentifier("VariantHomologation.TMEDeltaCDA");

            UpdateItemRequest.Column columnFitment = new UpdateItemRequest.Column();
            columnFitment.setIdentifier("VariantHomologation.TMEAvailableForPPORequest");

            columns.add(columnFlag);
            columns.add(columnCDA);
            columns.add(columnFitment);
            request.setColumns(columns);

            UpdateItemRequest.Row row = new UpdateItemRequest.Row();
            row.setObject(new UpdateItemRequest.Row.RowObject(VariantUtils.formatId(homologation.getApplicationId())));
            row.setValues(Lists.newArrayList(
                    Optional.ofNullable(homologation.getWltpFlag()).map(Object::toString).orElse(null),
                    Optional.ofNullable(homologation.getDeltaCDA()).map(BigDecimal::toString).orElse(null),
                    Optional.ofNullable(homologation.getHubFitmentFlag()).map(Object::toString).orElse(null)
            ));

            rows.add(row);

            request.setRows(rows);
            request.setColumns(columns);

            String url = properties.getServer()
                    + "/rest/V2.0/list/Variant/VariantHomologation/";

            log.trace("Homologation update request to URL {} with body {}", url, Json.of(request));

            UpdateItemResponse response = restTemplate.postForObject(url, request, UpdateItemResponse.class);

            log.trace("Homologation update response: {}", Json.of(response));

            responses.add(response);
        });

        return responses;
    }

    private void updateGenerationCode(PostApplicationHomologations homologations) {

        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/";

        homologations.forEach(homologation -> {
            List<WltpHomologationGenerationCode> generationCodes =
                    resolveGenerationCodes(homologation.getGenerationCodes());

            if (generationCodes.size() == 0) {
                return;
            }

            deleteCurrentGenerationCodes(VariantUtils.formatId(homologation.getApplicationId()));

            generationCodes.forEach(generationCode -> {

                List<UpdateItemRequest.Column> columns = Lists.newArrayList();
                List<UpdateItemRequest.Row> rows = Lists.newArrayList();

                // Add columns
                UpdateItemRequest.Column codeColumn = new UpdateItemRequest.Column();
                codeColumn.setIdentifier("VariantHomologationGenerationCode.TMECode(" + generationCode.getFrom() + "," + generationCode.getTo() + ")");

                UpdateItemRequest.Column fromColumn = new UpdateItemRequest.Column();
                fromColumn.setIdentifier("VariantHomologationGenerationCode.TMEFrom(" + generationCode.getFrom() + "," + generationCode.getTo() + ")");

                UpdateItemRequest.Column toColumn = new UpdateItemRequest.Column();
                toColumn.setIdentifier("VariantHomologationGenerationCode.TMETo(" + generationCode.getFrom() + "," + generationCode.getTo() + ")");

                // TODO whenyy time allows make method with overlapping code
                columns.add(codeColumn);
                columns.add(fromColumn);
                columns.add(toColumn);

                // Add rows
                UpdateItemRequest.Row row = new UpdateItemRequest.Row();
                row.setObject(null);
                row.setObject(new UpdateItemRequest.Row.RowObject(VariantUtils.formatId(homologation.getApplicationId())));
                row.setValues(Lists.newArrayList(
                        generationCode.getType(),
                        "",
                        ""
                ));

                rows.add(row);

                // prepare request
                UpdateItemRequest request = new UpdateItemRequest();
                request.setColumns(columns);
                request.setRows(rows);

                UpdateItemResponse response = restTemplate.postForObject(url, request, UpdateItemResponse.class);

                log.trace("GenerationCodes update response: {}", Json.of(response));
            });
        });
    }

    private List<WltpHomologationGenerationCode> resolveGenerationCodes(List<Object> generationCodes) {

        List<WltpHomologationGenerationCode> generationCodeList = new ArrayList<>();

        if (generationCodes != null) {

            for (Object code : generationCodes) {
                LinkedHashMap<String, Object> aCode = (LinkedHashMap<String, Object>) code;

                String type = (String) aCode.get("type");
                String typeCode;

                switch (type) {
                    case "MSTA": {
                        typeCode = "200";
                        break;
                    }

                    case "WVTA": {
                        typeCode = "100";
                        break;
                    }

                    default: {
                        typeCode = "";
                    }
                }


                WltpHomologationGenerationCode gc = new WltpHomologationGenerationCode(
                        typeCode,
                        (String) aCode.get("from"),
                        (String) aCode.get("to"));

                generationCodeList.add(gc);
            }
        }

        return generationCodeList;
    }

    private void deleteCurrentGenerationCodes (String id) {

        String urlDelete = properties.getServer() + "/rest/V2.0/list/Variant/VariantHomologation/byItems?items=" + id +
        "&fields=VariantHomologationGenerationCode.TMECode,VariantHomologationGenerationCode.TMEFrom,VariantHomologationGenerationCode.TMETo";

        restTemplate.delete(urlDelete);

        // TODO replace when bug is solved
        UpdateItemRequest request = new UpdateItemRequest();
        List<UpdateItemRequest.Column> columns = Lists.newArrayList();
        List<UpdateItemRequest.Row> rows = Lists.newArrayList();

        UpdateItemRequest.Column column = new UpdateItemRequest.Column();
        column.setIdentifier("VariantHomologation.TMEWLTPFlag");

        UpdateItemRequest.Row row = new UpdateItemRequest.Row();
        row.setObject(new UpdateItemRequest.Row.RowObject(id));
        row.setValues(Lists.newArrayList("True"));

        columns.add(column);
        rows.add(row);

        request.setRows(rows);
        request.setColumns(columns);

        String urlPost = properties.getServer() + "/rest/V2.0/list/Variant/VariantHomologation/";
        UpdateItemResponse response = restTemplate.postForObject(urlPost, request, UpdateItemResponse.class);
        log.trace("FLAG update response: {}", Json.of(response));
    }
}
