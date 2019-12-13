package com.vqd.tme.na2a.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.vqd.tme.na2a.auth.AuthorizationHolder;
import com.vqd.tme.na2a.model.TreeNode;
import com.vqd.tme.na2a.support.StructureGroupFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefTableService {
  private final StructureGroupFetcher fetcher;

  public Collection<TreeNode> listBrands() {
    log.debug("listBrands");
    return fetcher.getRoot().values();
  }

  public Collection<TreeNode> listModels(String brand) {
    log.debug("listModels brand={}", brand);
    return byPath(
        brand);
  }

  public Collection<TreeNode> listProjects(String brand, String model) {
    log.debug("listProjects brand={}, brand={}", brand, model);
    return byPath(
        brand,
        model);
  }

  public Collection<TreeNode> getVehicleOptions(String brand, String model, String project) {
    log.debug("getVehicleOptions brand={}, brand={}, model={}", brand, model, project);
    final String equipmentKey = String.format("%s|EquipmentType", project);
    final String colourKey = String.format("%s|Colours", project);

    Collection<TreeNode> types = byPath(
        brand,
        model,
        project);

    List<TreeNode> filtered = types.stream()
        .filter(type -> !equipmentKey.equals(type.getKey()) && !colourKey.equals(type.getKey()))
        .collect(Collectors.toList());

    return getDirectChildren(filtered);
  }

  public Collection<TreeNode> getEquipmentOptions(String brand, String model, String project) {
    log.debug("getEquipmentOptions brand={}, brand={}, model={}", brand, model, project);
    Collection<TreeNode> types = byPath(
        brand,
        model,
        project,
        String.format("%s|EquipmentType", project));

    return getDirectChildren(types);
  }

  private Collection<TreeNode> getDirectChildren(Collection<TreeNode> source) {
    final List<TreeNode> target = new ArrayList<>(source.size());

    // copy entries first (as we don't want to set children for cached nodes)
    for (TreeNode entry : source) {
      target.add(new TreeNode(entry));
    }

    // the worker below is executed in different threads and therefore not able to access the auth token
    final String authToken = AuthorizationHolder.get();
    final Thread parent = Thread.currentThread();

    target.parallelStream().forEach(type -> {
      // don't mess with the auth token if the parent thread is executing this
      boolean workerIsParentThread = Thread.currentThread().equals(parent);

      try {
        if (!workerIsParentThread) {
          AuthorizationHolder.set(authToken);
        }

        // get children for this node
        type.setChildren(fetcher.getChildren(type.getId()).values());
      } finally {
        if (!workerIsParentThread) {
          AuthorizationHolder.reset();
        }
      }
    });

    return target;
  }

  public Collection<TreeNode> listInteriorColours(String brand, String model, String project) {
    log.debug("listInteriorColours brand={}, brand={}, model={}", brand, model, project);
    return byPath(
        brand,
        model,
        project,
        String.format("%s|Colours", project),
        String.format("%s|interior", project));
  }

  public Collection<TreeNode> listExteriorColours(String brand, String model, String project) {
    log.debug("listExteriorColours brand={}, brand={}, model={}", brand, model, project);
    return byPath(
        brand,
        model,
        project,
        String.format("%s|Colours", project),
        String.format("%s|exterior", project));
  }

  public Collection<TreeNode> listTrimColours(String brand, String model, String project) {
    log.debug("listTrimColours brand={}, brand={}, model={}", brand, model, project);
    return byPath(
        brand,
        model,
        project,
        String.format("%s|Colours", project),
        String.format("%s|trim", project));
  }

  public Collection<TreeNode> listSsns(String brand, String model, String project) {
    log.debug("listSsns brand={}, brand={}, model={}", brand, model, project);
    return byPath(
            brand,
            model,
            project,
            String.format("%s|SSNS", project));
  }

  private Collection<TreeNode> byPath(String... path) {
    if (log.isDebugEnabled()) {
      log.debug("get path: {}", Arrays.toString(path));
    }
    Map<String, TreeNode> current = fetcher.getRoot();

    for (String key : path) {
      TreeNode node = current.get(key);
      if (node != null) {
        current = fetcher.getChildren(node.getId());
      } else {
        return Collections.emptyList();
      }
    }

    return current.values();
  }
}
