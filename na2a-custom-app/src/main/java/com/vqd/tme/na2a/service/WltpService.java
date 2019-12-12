package com.vqd.tme.na2a.service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.*;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.model.p360.*;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.partlinking.model.VariantMetaData;
import com.vqd.tme.na2a.partlinking.persistence.impl.P360VariantMetaDataResolver;
import com.vqd.tme.na2a.support.CastUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WltpService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationDesignRepository designRepository;
    private final ApplicationHomologationRepository homologationRepository;
    private final ApplicationHomologationGenerationCodeRepository generationCodeRepository;
    private final ApplicationDesignReplacementOfEquipmentRepository replacementOfEquipmentRepository;
    private final ApplicationAttributesRepository applicationAttributesRepository;

    private final RestTemplate restTemplate;
    private final InformaticaPimProperties pimProperties;

    private final P360VariantMetaDataResolver metaDataResolver;


    public List<ResponseWltpAccessory> getByProducts(Collection<String> productIds) {
        List<ResponseWltpAccessory> accessories = Lists.newArrayList();
        productIds.forEach(id -> {

            List<Variant> variants = applicationRepository.findByProductId(id);

            List<String> applicationIds = variants.stream().map(Variant::getId).collect(Collectors.toList());

            Map<String, VariantMetaData> metaData = metaDataResolver.resolve(applicationIds);

            accessories.addAll(variants.stream()
                    .map(variant -> getWltpAccessory(variant.getId(), metaData.get(variant.getId())))
                    .collect(Collectors.toList()));
        });

        return accessories;
    }

    public List<ResponseWltpAccessory> getByApplications(Collection<String> applicationIds) {
        Map<String, VariantMetaData> metaData = metaDataResolver.resolve(applicationIds);
        return applicationIds.stream()
                .map(applicationId -> getWltpAccessory(applicationId, metaData.get(applicationId)))
                .collect(Collectors.toList());
    }

    private ResponseWltpAccessory getWltpAccessory(String id, VariantMetaData metaData) {
        P360ApplicationDesign design = designRepository.findByApplication(id);
        P360ApplicationHomologation homologation = homologationRepository.findByApplication(id);
        List<P360Attribute> attributes = applicationAttributesRepository.findByApplication(id);

        // todo resolve equipment
        List<KeyValue> equipment = resolveEquipment(metaData);


        return ResponseWltpAccessory.builder()
                .id(id)
                .commodity(metaData.getCommodity())
                .variantNo(metaData.getVariantNo())
                .vehicleGeneration(metaData.getVehicleGeneration())
                .name(metaData.getName())
                .summary(metaData.getSummary())
                .description(metaData.getDescription())
                .status(metaData.getStatus())
                .equipmentOptions(equipment)
                .exteriorColours(metaData.getExteriorColours())
                .interiorColours(metaData.getInteriorColours())
                .trimColours(metaData.getTrimColours())
                .productColour(metaData.getAccessoryColour())
                .productMaterial(metaData.getAccessoryMaterial())
                .accessoryCategory(metaData.getAccessoryCategory())
                .accessoryMass(metaData.getAccessoryMass())
                .design(ResponseWltpDesign.builder()
                        .lCoOrdinates(design.getLCoOrdinates())
                        .lCoOrdinatesRounded(design.getLCoOrdinatesRounded())
                        .incalculable(design.getIncalculable())
                        .replacementAccessory(design.getReplacementAccessory())
                        .deltaMass(design.getDeltaMass())
                        .deltaMassRounded(design.getDeltaMassRounded())
                        .replacementOfEquipments(getWltpReplacementOfEquipment(id))
                        .build())
                .homologation(ResponseWltpHomologation.builder()
                        .wltp(homologation.getWltp())
                        .information(homologation.getInformation())
                        .hubFitmentFlag(homologation.getHubFitmentFlag())
                        .deltaCDA(homologation.getDeltaCDA())
                        .homologation(homologation.getHomologation())
                        .componentApproval(findAttributeValue(attributes, "componentApproval"))
                        .impactedRegulations(findAttributeValue(attributes, "impactedRegulations"))
                        .generationCodes(getWltpGenerationCodes(id))
                        .build())
                .build();
    }

    private List<WltpHomologationGenerationCode> getWltpGenerationCodes(String applicationId) {
        List<P360HomologationGenerationCode> generationCodes = generationCodeRepository.findByApplication(applicationId);

        List<WltpHomologationGenerationCode> responseCodes = Lists.newArrayList();
        generationCodes.forEach(p360Code -> {
            responseCodes.add(
                    WltpHomologationGenerationCode.builder()
                            .type(Optional.ofNullable(p360Code.getCode()).map(P360GenerationCode::getLabel).orElse("Unknown"))
                            .from(p360Code.getFrom())
                            .to(p360Code.getTo())
                            .build()
            );
        });
        return responseCodes;
    }

    private List<WltpDesignReplacementOfEquipment> getWltpReplacementOfEquipment(String applicationId) {
        List<P360DesignReplacementOfEquipment> roes = replacementOfEquipmentRepository.findByApplication(applicationId);

        List<WltpDesignReplacementOfEquipment> responseRoes = Lists.newArrayList();
        roes.forEach(roe -> {
            responseRoes.add(
                    WltpDesignReplacementOfEquipment.builder()
                            .code(roe.getCode())
                            .equipment(roe.getEquipment())
                            .build()
            );
        });
        return responseRoes;
    }


    private String findAttributeValue(List<P360Attribute> attributes, String id) {
        return attributes.stream().filter(attribute -> attribute.getAttributeId().equalsIgnoreCase(id))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .map(P360Attribute::getValue)
                .orElse(null);
    }

    public List<ResponseWltpAccessory> updateHomologations(PostApplicationHomologations homologations) throws CouldNotSaveException, SavedWithErrorsException {
        List<UpdateItemResponse> responses = homologationRepository.update(homologations);
        for (UpdateItemResponse response : responses) {
            validateResponse(response);
        }

        List<ResponseWltpAccessory> accessories = Lists.newArrayList();
        homologations.forEach(homologation -> {
            P360ApplicationHomologation updatedHomologation = homologationRepository.findByApplication(homologation.getApplicationId());
            List<P360Attribute> attributes = applicationAttributesRepository.findByApplication(homologation.getApplicationId());
            accessories.add(
                    ResponseWltpAccessory.builder()
                            .id(homologation.getApplicationId())
                            .homologation(ResponseWltpHomologation.builder()
                                    .wltp(updatedHomologation.getWltp())
                                    .information(updatedHomologation.getInformation())
                                    .hubFitmentFlag(updatedHomologation.getHubFitmentFlag())
                                    .deltaCDA(updatedHomologation.getDeltaCDA())
                                    .homologation(updatedHomologation.getHomologation())
                                    .componentApproval(findAttributeValue(attributes, "componentApproval"))
                                    .impactedRegulations(findAttributeValue(attributes, "impactedRegulations"))
                                    .generationCodes(getWltpGenerationCodes(homologation.getApplicationId()))
                                    .build())
                            .build()
            );
        });
        return accessories;
    }

    public List<ResponseWltpAccessory> updateDesign(PostApplicationDesigns designs) throws CouldNotSaveException, SavedWithErrorsException {
        UpdateItemResponse response = designRepository.update(designs);
        validateResponse(response);
        List<ResponseWltpAccessory> accessories = Lists.newArrayList();
        designs.forEach(design -> {
            P360ApplicationDesign updatedDesign = designRepository.findByApplication(design.getApplicationId());
            accessories.add(ResponseWltpAccessory.builder()
                    .id(design.getApplicationId())
                    .design(ResponseWltpDesign.builder()
                            .lCoOrdinates(updatedDesign.getLCoOrdinates())
                            .lCoOrdinatesRounded(updatedDesign.getLCoOrdinatesRounded())
                            .incalculable(updatedDesign.getIncalculable())
                            .replacementAccessory(updatedDesign.getReplacementAccessory())
                            .deltaMass(updatedDesign.getDeltaMass())
                            .deltaMassRounded(updatedDesign.getDeltaMassRounded())
                            .replacementOfEquipments(getWltpReplacementOfEquipment(design.getApplicationId()))
                            .build())
                    .build());
        });
        return accessories;
    }

    private void validateResponse(UpdateItemResponse res) throws CouldNotSaveException, SavedWithErrorsException {
        if (hasNotCreatedOrUpdatedObject(res)) {
            if (!Objects.equals(res.getCounters().getWarnings(), 0)) {
                log.warn("Homologations updated with errors: {}", res);

                throw new SavedWithErrorsException("Homologations updated with errors!");
            }

            if (!Objects.equals(res.getCounters().getErrors(), 0)) {
                log.warn("could not update homologations: {}", res);

                throw new CouldNotSaveException("Couldn't update homologations");
            }
        }
    }

    private boolean hasNotCreatedOrUpdatedObject(UpdateItemResponse res) {
        return !((Objects.equals(res.getCounters().getCreatedObjects(), 1) || Objects.equals(res.getCounters().getUpdatedObjects(), 1)) &&
                Objects.equals(res.getCounters().getErrors(), 0) &&
                Objects.equals(res.getCounters().getWarnings(), 0));
    }

    private List<KeyValue> resolveEquipment(VariantMetaData metaData) {
        List<KeyValue> specs = new ArrayList<>();

        Arrays.asList(metaData.getEquipmentSpecs().split(",")).forEach(spec -> {

            if (spec == null || spec.isEmpty()) {
                return;
            }

            GetResponse res = restTemplate.getForObject(pimProperties.getServer() +
                            "/rest/V2.0/list/StructureGroup/bySearch" +
                            "?structure=vehicleStructure" +
                            "&query=StructureGroup.Identifier = \"{identifier}\"" +
                            "&fields=StructureGroupLang.Name(EN),StructureGroup.ParentIdentifier",
                    GetResponse.class,
                    spec
            );

            log.trace("spec response: {}", res);

            String parentIdentifier = CastUtils.asString(res.getRows().get(0).getValues().get(1));
            String[] parent = parentIdentifier.split("\\|");

            KeyValue kv = new KeyValue();
            kv.setKey(spec);
            kv.setValue(CastUtils.asString(res.getRows().get(0).getValues().get(0)));
            kv.setParentKey(parent[1]);

            specs.add(kv);
        });

        return specs;
    }
}

