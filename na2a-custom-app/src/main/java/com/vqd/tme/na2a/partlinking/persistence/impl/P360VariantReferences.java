package com.vqd.tme.na2a.partlinking.persistence.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.GetResponse.Row;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemRequest.Column;
import com.vqd.tme.na2a.p360.UpdateItemRequest.Row.Qualification;
import com.vqd.tme.na2a.p360.UpdateItemRequest.Row.RowObject;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.partlinking.model.VariantReference;
import com.vqd.tme.na2a.partlinking.persistence.VariantReferences;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class P360VariantReferences implements VariantReferences {
  private static final String READ_FIELDS = String.join(
      ",",
      "Variant2ArticleReference.ReferencedArticleNo",
      "Variant2ArticleReference.Quantity",
      "Variant2ArticleReference.Type");

  private static final List<Column> WRITE_COLUMNS = Collections.singletonList(
      new Column("Variant2ArticleReference.Quantity"));

  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;

  @Override
  public List<VariantReference> list(String variantId) {
    log.debug("get for variant {}", variantId);

    GetResponse res = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/Variant/Variant2ArticleReference/byItems"
        + "?items={productNumber}"
        + "&fields=" + READ_FIELDS,
        GetResponse.class,
        String.format("%s@1", variantId));

    log.trace("raw response: {}", Json.of(res));

    return res
        .getRows()
        .stream()
        .map(this::asVariantReference)
        .collect(Collectors.toList());
  }

  private VariantReference asVariantReference(Row row) {
    return new VariantReference()
        .setArticleCode(CastUtils.asString(row.getValues().get(0)))
        .setQuantity(CastUtils.asBigDecimal(row.getValues().get(1)))
        .setType(CastUtils.asString(row.getValues().get(2)));
  }

  @Override
  public void update(String variantId, List<VariantReference> references) throws CouldNotSaveException {
    List<String> referencedTypes =Lists.newArrayList("1", "12", "13", "14");

    // TODO: Make a rest a call to fetch all the part types
    String deleteUrl = pimProperties.getServer() +
            "/rest/V2.0/list/Variant/Variant2ArticleReference/byItems?" +
            "items={variantId}&qualificationFilter=referenceType({type})";

    referencedTypes.stream().forEach(ref -> {
      rest.delete(deleteUrl, String.format("%s@1", variantId), ref);
    });

    List<UpdateItemRequest.Row> rows = references.stream()
        .map(ref -> new UpdateItemRequest.Row()
            .setObject(new RowObject(String.format("%s@1", variantId)))
            .setQualification(new Qualification(ref.getType(), ref.getArticleCode()))
            .setValues(Collections.singletonList(ref.getQuantity())))
        .collect(Collectors.toList());

    UpdateItemRequest request = new UpdateItemRequest()
        .setColumns(WRITE_COLUMNS)
        .setRows(rows);

    log.debug("save request: {}", Json.of(request));

    UpdateItemResponse response = rest.postForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/Variant/Variant2ArticleReference",
        request,
        UpdateItemResponse.class);

    log.debug("save response: {}", response);

    if (!Objects.equals(response.getCounters().getErrors(), 0)) {
      log.warn("could not save references {} for variant {}: {}", references, variantId, response);
      throw new CouldNotSaveException("Couldn't save variant references");
    }
  }
}
