package com.vqd.tme.na2a.partlinking.service;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.partlinking.model.Commodity;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for listing for front-end filter purposes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartFilterService {

    private final InformaticaPimProperties pimProperties;
    private final RestTemplate rest;

    public List<String> listProjectCodes() {

        GetResponse res = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/Article/PartProject/bySearch/"
                        + "?query=PartProject.TMEProjectCode+<>+\"\""
                        + "&fields=PartProject.TMEProjectCode"
                        + "&pageSize=100000", GetResponse.class);


        log.trace("get model codes response: {}", res);

        return res.getRows().stream()
                .map(this::resolveToList)
                .distinct()
                .collect(Collectors.toList());

    }

    public List<String> listPersonInCharge() {

        GetResponse res = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/Article/PartProject/bySearch/"
                        + "?query=PartProject.TMEPersonInCharge+<>+\"\""
                        + "&fields=PartProject.TMEPersonInCharge", GetResponse.class);

        log.trace("get person in charge response: {}", res);

        List<String> personsInCharge = res.getRows().stream()
                .map(this::resolveToList)
                .distinct()
                .collect(Collectors.toList());

        personsInCharge.sort(String::compareTo);

        return personsInCharge;
    }

    public List<Commodity> listCommodities() {

        GetResponse res = rest.getForObject(
                pimProperties.getServer()
                        + "/rest/V2.0/list/StructureGroup/byStructure/"
                        + "?structure='productStructure'"
                        + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN),StructureGroup.Level"
                        + "&pageSize=1000"
                        + "&orderBy=0-ASC"
                        , GetResponse.class);

        log.trace("get commodities response: {}", res);

        List<Commodity> commodities = res.getRows().stream()
                .filter(row -> row.getValues().get(2).equals("4"))
                .map(this::resolveCommodity).sorted(Comparator.comparing(Commodity::getValue)).collect(Collectors.toList());

        return commodities;

    }

    private String resolveToList(GetResponse.Row row) {
        Iterator<Object> iter = row.getValues().iterator();

        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof String) {
                return CastUtils.asString(o);
            }
            ArrayList<?> list = (ArrayList<?>) o;
            return CastUtils.asString(list.get(0));
        }

        return null;
    }

    private Commodity resolveCommodity(GetResponse.Row row) {
        return new Commodity(CastUtils.asString(row.getValues().get(0)),
                CastUtils.asString(row.getValues().get(1)));
    }

    public Map<String, Object> getFilterLists() {
        Map<String, Object> map = new HashMap<>();

        map.put("projects", listProjectCodes());
        map.put("pics", listPersonInCharge());
        map.put("commodities", listCommodities());

        return map;
    }
}
