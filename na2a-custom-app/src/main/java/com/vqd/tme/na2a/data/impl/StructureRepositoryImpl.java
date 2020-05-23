package com.vqd.tme.na2a.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToP360StructureAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.StructureRepository;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class StructureRepositoryImpl implements StructureRepository {

    private static final String FIELDS = String.join(",", Arrays.asList(
            "StructureGroup.Identifier",
            "StructureGroupLang.Name(EN)",
            "StructureGroupLang.Synonym(EN)",
            "StructureGroup.ParentIdentifier"));

    private final InformaticaPimProperties properties;
    private final RestTemplate restTemplate;
    private final RowToP360StructureAdapter structureAdapter;

    @Override
    public Map<P360StructureType, P360Structure> findAll() {
        String url = properties.getServer() + "/rest/V2.0/list/Structure/bySearch?fields=Structure.Identifier,StructureLang.Name(EN)";

        log.debug("get url: {}", url);

        GetResponse structureResponse = restTemplate.getForObject(url, GetResponse.class);

        List<P360Structure> structures = Lists.newArrayList();
        structureResponse.getRows().forEach(row -> structures.add(structureAdapter.convert(row)));

        // As new structures can be added to PIM, we'll end up with multiple "UNKNOWN" structure types. To avoid duplicate keys in the map
        // we perform a distinct on the type, meaning that we'll only keep the first "UNKNOWN" structure found.
        return structures.stream()
                .filter(distinctByKey(s -> s.getType()))
                .collect(Collectors.toMap(P360Structure::getType, Function.identity()));
    }

    @Override
    public GetResponse findByParentIdentifier(String identifier, P360Structure structure) {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure={structure}"
                + "&query=" + String.format("StructureGroup.ParentIdentifier = \"%s\"", identifier)
                + "&fields=" + FIELDS;

        log.debug("get url: {}", url);

        return restTemplate.getForObject(url, GetResponse.class, structure.getObjectId());
    }

    @Override
    public GetResponse findByIdentifier(String identifier, P360Structure structure) {
        String url = properties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure={structure}"
                + "&query=" + String.format("StructureGroup.Identifier = \"%s\"", identifier)
                + "&fields=" + FIELDS;

        log.debug("get url: {}", url);

        return restTemplate.getForObject(url, GetResponse.class, structure.getObjectId());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
