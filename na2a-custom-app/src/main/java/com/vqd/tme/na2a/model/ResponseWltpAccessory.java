package com.vqd.tme.na2a.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ResponseWltpAccessory {

    private final String id;
    private final String variantNo;
    private final String commodity;
    private final String summary;
    private final String vehicleGeneration;
    private final String name;
    private final String description;
    private final String status;
    private final String interiorColours;
    private final String trimColours;
    private final String exteriorColours;
    private final String productColour;
    private final String productMaterial;
    private final String accessoryCategory;
    private final BigDecimal accessoryMass;
    private final List<KeyValue> equipmentOptions;

    private final ResponseWltpDesign design;
    private final ResponseWltpHomologation homologation;
}
