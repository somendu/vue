package com.vqd.tme.na2a.partlinking.controller;

import com.vqd.tme.na2a.model.tme.TmePart;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.PartsFilter;
import com.vqd.tme.na2a.partlinking.persistence.Parts;
import com.vqd.tme.na2a.partlinking.service.PartFilterService;
import com.vqd.tme.na2a.service.TmeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partlinking")
public class PartsController {
  private final Parts parts;
  private final PartFilterService partFilterService;
  private final TmeProductService tmeProductService;

  @GetMapping("/parts")
  public List<Part> search(
          @RequestParam(name = "project", required = false) String project,
          @RequestParam(name = "pic", required = false) String pic,
          @RequestParam(name = "commodity", required = false) String commodity,
          @RequestParam(name = "partNumber", required = false) String partNumber,
          @RequestParam(name = "partName", required = false) String partName,
          @RequestParam(name = "colourMaterial", required = false) String colourMaterial) {

    PartsFilter filter = PartsFilter.builder()
            .projectCode(Optional.ofNullable(project).orElse(""))
            .personInCharge(Optional.ofNullable(pic).orElse(""))
            .commodity(Optional.ofNullable(commodity).orElse(""))
            .partNumber(Optional.ofNullable(partNumber).orElse(""))
            .partName(Optional.ofNullable(partName).orElse(""))
            .colourOrMaterial(Optional.ofNullable(colourMaterial).orElse(""))
            .build();

    return parts.search(filter);
  }

  @GetMapping("/filter-lists")
  public Map<String, Object> getSelectionLists() {
    return partFilterService.getFilterLists();
  }

  @GetMapping("/npa")
  public List<Part> npaSearch(
          @RequestParam(name = "partNumber") String partNumber,
          @RequestParam(name = "partName", required = false) String partName) {

    // clean up search parameters
    String prPartNumber = partNumber.trim();
    String prPartName = partName;

    // only performs search if the parameter has at least 4 characters
    if (prPartNumber.length() < 4) {
      return new ArrayList<>();
    }

    // search from Toyota API
    List<TmePart> tmeParts = tmeProductService.searchParts(prPartNumber, prPartName);

    // convert API result to common UI format
    List<Part> parts = tmeParts.stream()
            .map(tmePart -> {
              Part part = new Part();
              part.setPartNumber(tmePart.getPartNumber());
              part.setPartName(tmePart.getPartName());
              part.setKnownInNPA(true);
              return part;
            }).collect(Collectors.toList());

    return parts;
  }
}
