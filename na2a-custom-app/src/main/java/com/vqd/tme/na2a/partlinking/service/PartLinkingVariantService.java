package com.vqd.tme.na2a.partlinking.service;

import com.vqd.tme.na2a.data.impl.P360Variants;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.Variant;
import com.vqd.tme.na2a.partlinking.model.*;
import com.vqd.tme.na2a.partlinking.persistence.Parts;
import com.vqd.tme.na2a.partlinking.persistence.VariantMetaDataResolver;
import com.vqd.tme.na2a.partlinking.persistence.VariantReferences;
import com.vqd.tme.na2a.support.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartLinkingVariantService {
  private final VariantReferences refs;
  private final Parts parts;
  private final VariantMetaDataResolver metaDataResolver;
  private final P360Variants p360variants;

  public List<PartLinkingVariant> get(List<String> variantIds) {
    // collect variant to article references first
    Set<String> occurringPartIds = new HashSet<>();
    Map<String, List<VariantReference>> linksByVariantId = new HashMap<>();

    for (String variantId : variantIds) {

      List<VariantReference> links = refs.list(variantId);
      linksByVariantId.put(variantId, links);

      for (VariantReference link : links) {
        occurringPartIds.add(link.getArticleCode());
      }
    }

    // collect referenced parts
    Map<String, Part> occurringParts = parts.get(occurringPartIds);
    // collect variant info
    Map<String, VariantMetaData> metaDataById = metaDataResolver.resolve(variantIds);

    // construct response
    List<PartLinkingVariant> result = new ArrayList<>(variantIds.size());

    for (String variantId : variantIds) {
      List<VariantReference> links = linksByVariantId.get(variantId);
      // in case a variant doesn't exist ...
      if (links == null) {
        continue;
      }

      PartLinkingVariant entry = new PartLinkingVariant()
          .setId(variantId);

      // Get accessory thing
      VariantMetaData metaData = metaDataById.get(variantId);
      if (metaData != null) {
        entry.setCommodity(metaData.getCommodity());
        entry.setVehicleGeneration(metaData.getVehicleGeneration());
        entry.setName(""); // TODO when available
        entry.setDescription(metaData.getDescription());
        entry.setSummary(metaData.getSummary());
        entry.setAccessoryCategory(metaData.getAccessoryCategory());
        entry.setStatus(metaData.getStatus());
        entry.setInteriorColours(metaData.getInteriorColours());
        entry.setTrimColours(metaData.getExteriorColours());
        entry.setExteriorColours(metaData.getExteriorColours());
        entry.setProductColour(metaData.getAccessoryColour());
        entry.setProductMaterial(metaData.getAccessoryMaterial());
        entry.setVariantNo(metaData.getVariantNo());
      }

      // parts
      List<LinkedPart> linkedParts = new ArrayList<>(links.size());
      for (VariantReference link : links) {
        Part part = occurringParts.get(link.getArticleCode());

        linkedParts.add(new LinkedPart()
            .setPartNumber(link.getArticleCode())
            .setPartName(part != null ? part.getPartName() : null)
            .setQuantity(link.getQuantity())
            .setType(link.getType())
            .setColour(part != null ? part.getColour() : null)
            .setMaterial(part != null ? part.getMaterial() : null)
            .setKnownInNPA(part != null ? part.getKnownInNPA() : null));
      }
      entry.setParts(linkedParts);

      result.add(entry);
    }

    return result;
  }

  public List<PartLinkingVariant>  getByProducts(List<String> products) {
    List<String> variantIds = new ArrayList<>();

    for (String product : products) {
      List<String> ids = p360variants.getForProduct(product).stream()
              .map(Variant::getId)
              .collect(Collectors.toList());
      variantIds.addAll(ids);
    }

    log.debug("Amount of variant ids: {}", variantIds.size());

    return get(variantIds);
  }

  public void update(PartLinkingVariant body) throws CouldNotSaveException {
    log.debug("update {}", Json.of(body));

    List<VariantReference> references = body.getParts()
        .stream()
        .map(p -> new VariantReference()
            .setArticleCode(p.getPartNumber())
            .setType(p.getType())
            .setQuantity(p.getQuantity()))
        .collect(Collectors.toList());

    // create NPA part in P360 if not already exist
    for (LinkedPart part : body.getParts()) {
      if (part.getKnownInNPA()) {
        //search
        PartsFilter searchFilter = PartsFilter.builder()
                .projectCode("")
                .personInCharge("")
                .commodity("")
                .partNumber(part.getPartNumber())
                .partName("")
                .colourOrMaterial("")
                .build();
        List<Part> searchResult = parts.search(searchFilter);

        //create
        if (searchResult.isEmpty()) {
          Part newNpaPart = new Part();
          newNpaPart.setPartNumber(part.getPartNumber());
          newNpaPart.setPartName(part.getPartName());
          newNpaPart.setKnownInNPA(true);
          parts.createNPA(newNpaPart);
        }
      }
    }

    refs.update(body.getId(), references);
  }

  public void delete(String variantId, String partId, String partType) throws CouldNotSaveException {
    log.debug("Delete a part reference on variant {}, with partId {} and partType {}",
            variantId, partId, partType);

    parts.delete(variantId, partId, partType);
  }

}
