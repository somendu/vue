package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.EncodingUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LookupService {
  private static final String CODE = "LookupValue.Code";
  private static final String NAME = "LookupValueLang.Name(EN)";
  private static final String DESCRIPTION = "LookupValueLang.Description(EN)";

  private static final List<String> CODE_AND_NAME = Arrays.asList(CODE, NAME);
  private static final List<String> ONLY_NAME = Collections.singletonList(NAME);
  private static final List<String> CODE_AND_NAME_AND_DESCRIPTION = Arrays.asList(CODE, NAME, DESCRIPTION);

  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;

  @Cacheable(cacheNames="lookups", key="'interiorColours'", sync=true)
  public List<KeyValue> getInteriorColours() {
    return get("ColoursInterior",
            EncodingUtil.encodeURIComponent(String.join(",", CODE_AND_NAME)),
            LookupService::asKeyValue);
  }

  @Cacheable(cacheNames="lookups", key="'exteriorColours'", sync=true)
  public List<KeyValue> getExteriorColours() {
    return get("ColoursExterior",
            EncodingUtil.encodeURIComponent(String.join(",", CODE_AND_NAME)),
            LookupService::asKeyValue);
  }

  @Cacheable(cacheNames="lookups", key="'trimColours'", sync=true)
  public List<KeyValue> getTrimColours() {
    return get("ColoursTrim",
            EncodingUtil.encodeURIComponent(String.join(",", CODE_AND_NAME)),
            LookupService::asKeyValue);
  }

  @Cacheable(cacheNames="lookups", key="'cancelledReasons'", sync=true)
  public List<KeyValue> getCancelledReasons() {
    return get(
        "ApplicationCancelledReason",
            EncodingUtil.encodeURIComponent(String.join(",", ONLY_NAME)),
        row -> new KeyValue(
            String.format("%s_%s", row.getObject().getEntityId(), row.getObject().getId()),
            CastUtils.asString(row.getValues().get(0))));
  }

  @Cacheable(cacheNames = "lookups", key="'homologationtypes'", sync = true)
  public List<KeyValue> getHomologationTypes() {
    return get(
            "Homologation",
            String.join(",", CODE_AND_NAME_AND_DESCRIPTION),
            row -> new KeyValue(
                    String.format("%s_%s", row.getObject().getEntityId(), row.getObject().getId()),
                    CastUtils.asString(row.getValues().get(1)),
                    null,
                    CastUtils.asString(row.getValues().get(0)))
    );
  }

  private List<KeyValue> get(
      String name,
      String fields,
      Function<GetResponse.Row, KeyValue> mapper) {
    GetResponse res = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/LookupValue/byLookup"
        + "?lookup=" + EncodingUtil.encodeURIComponent(name)
        + "&fields=" + fields,
        GetResponse.class);

    return res.getRows()
        .stream()
        .map(mapper)
        .filter(kv -> kv.getValue() != null)
        .sorted(Comparator.comparing(KeyValue::getValue))
        .collect(Collectors.toList());
  }

  private static KeyValue asKeyValue(GetResponse.Row row) {
    return new KeyValue(
        CastUtils.asString(row.getObject().getId()),
        CastUtils.asString(row.getValues().get(0)));
  }
}
