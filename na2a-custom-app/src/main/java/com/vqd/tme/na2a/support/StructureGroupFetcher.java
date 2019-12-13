package com.vqd.tme.na2a.support;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.model.TreeNode;
import com.vqd.tme.na2a.p360.GetResponse;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import static com.vqd.tme.na2a.support.CastUtils.asString;
import static com.vqd.tme.na2a.support.CastUtils.asStrings;

@Slf4j
@Component
@RequiredArgsConstructor
public class StructureGroupFetcher {
  private static final Comparator<TreeNode> COMPARATOR = Comparator.comparing(TreeNode::getValue);

  private static final String FIELDS = String.join(",",
      "StructureGroup.Identifier",
      "StructureGroupLang.Name(EN)",
      "StructureGroupLang.Synonym(EN)");

  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;

  @Cacheable(cacheNames="vehicleStructure", key="'root'", sync=true)
  public Map<String, TreeNode> getRoot() {
    log.debug("get root");

    GetResponse res = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/StructureGroup/byStructure"
        + "?structure=vehicleStructure"
        + "&fields=" + FIELDS
        + "&topLevelOnly=true",
        GetResponse.class);

    log.trace("getRoot response: {}", res);

    return toTreeNodes(res);
  }

  @Cacheable(cacheNames="vehicleStructure", sync=true)
  public Map<String, TreeNode> getChildren(String parentId) {
    log.debug("get children of {}", parentId);

    GetResponse res = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/StructureGroup/byParentGroup"
        + "?structure=vehicleStructure"
        + "&fields=" + FIELDS
        + "&structureGroup={parentId}"
        + "&pageSize=-1",
        GetResponse.class,
        parentId);

    log.trace("getChildren({}) response: {}", parentId, res);

    return toTreeNodes(res);
  }

  private static Map<String, TreeNode> toTreeNodes(GetResponse res) {
    Map<String, TreeNode> map = new LinkedHashMap<>();

    res.getRows()
        .stream()
        .map(row -> {
          return new TreeNode()
              .setId(row.getObject().getId())
              .setKey(asString(row.getValues().get(0)))
              .setValue(asString(row.getValues().get(1)))
              .setKatashiki(asStrings(row.getValues().get(2)));
        })
        .filter(node -> node.getValue() != null)
        .sorted(COMPARATOR)
        .forEach(node -> map.put(node.getKey(), node));

    return map;
  }
}
