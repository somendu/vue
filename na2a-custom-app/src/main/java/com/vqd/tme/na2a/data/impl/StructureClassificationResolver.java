package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.ClassificationResolver;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.GetResponse.Row;
import com.vqd.tme.na2a.service.StructureService;
import com.vqd.tme.na2a.service.VariantStructureGroupMapService;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StructureClassificationResolver implements ClassificationResolver {

  private final VariantStructureGroupMapService structureGroupMapService;
  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;

  @Override
  public List<P360Classification> resolve(String variantId, P360Structure structure) {
    List<P360Classification> result = Lists.newArrayList();

    if (StringUtils.isEmpty(variantId)) {
      return result;
    }

    /*
     * get classifications of variants
     */
    GetResponse classificationResp = structureGroupMapService.getStructureGroupMap(String.format("%s@1", variantId));
    Set<String> structureGroupIds = structureGroupMapService.getStructureGroupIds(classificationResp);

    if (!structureGroupIds.isEmpty()) {
      /*
       * Fetch all available structures - we are only interested in the structure for the commodity
       * resolve names of referenced structure groups
       */
      Set<String> filteredStructureGroupIds = structureGroupIds.stream()
              .filter(id -> structure != null && id.endsWith(structure.getObjectId()))
              .collect(Collectors.toSet());

      if(!filteredStructureGroupIds.isEmpty()) {
        GetResponse sgRes = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/StructureGroup/byItems"
                        + "?items={items}"
                        + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN)",
                GetResponse.class,
                String.join(",", filteredStructureGroupIds));

        log.trace("Structure groups raw response: {}", Json.of(sgRes));

        for (Row row : sgRes.getRows()) {
          if(row.getValues() != null && !row.getValues().isEmpty() && row.getValues().size() == 2) {
            result.add(P360Classification.builder()
                            .identifier(CastUtils.asString(row.getValues().get(0)))
                            .label(CastUtils.asString(row.getValues().get(1)))
                            .build());
          }
        }
        log.debug("resolved structure group classification: {}", result);
      }
    }

    return result;
  }
}
