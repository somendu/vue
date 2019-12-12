package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.Projects;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.p360.GetResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class P360Projects implements Projects {
  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;

  @Override
  public List<KeyValue> get(Collection<String> projectNames, String organisation) {
    List<KeyValue> tmeProjects = new ArrayList<>(projectNames.size());

    for (String projectName : projectNames) {
      KeyValue project = new KeyValue();
      project.setValue(projectName);
      project.setKey(getProjectId(projectName));
      tmeProjects.add(project);
    }

    log.debug("resolved local projects: {}", tmeProjects);

    for (KeyValue project : tmeProjects) {
      String productId = project.getKey();

      log.debug("Getting tme model with productID {}", productId);

      GetResponse res =
          rest.getForObject(
              pimProperties.getServer()
                  + "/rest/V2.0/list/StructureGroup/bySearch"
                  + "?structure=localProjectStructure"
                  + "&query=StructureGroup.Identifier = \"{organisation}|{productId}\""
                  + "&fields=StructureGroup.Identifier,StructureGroupLang.Name(EN)",
              GetResponse.class,
              organisation,
              productId);

      log.debug("Result for object: {}", res);

      for (GetResponse.Row row : res.getRows()) {
        project.setValue((String) row.getValues().get(1));
      }
    }

    return tmeProjects;
  }

  private String getProjectId(String name) {
    GetResponse res =
        rest.getForObject(
            pimProperties.getServer()
                + "/rest/V2.0/list/StructureGroup/bySearch"
                + "?structure=vehicleStructure"
                + "&query=StructureGroupLang.Name(EN) = \"{name}\""
                + "&fields=StructureGroup.Identifier",
            GetResponse.class,
            name);

    return res.getRows().get(0).getValues().get(0).toString();
  }
}
