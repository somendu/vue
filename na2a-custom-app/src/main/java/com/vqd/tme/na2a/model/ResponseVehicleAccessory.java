package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Classification;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseVehicleAccessory {

    private String applicationId;
    private String applicationName;

    @Builder.Default
    private List<String> interiorColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> exteriorColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> trimColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> equipmentIds = Lists.newArrayList();
    @Builder.Default
    private List<P360Classification> classifications = Lists.newArrayList();

    private String productId;
    private String productName;

    private String commodity;

    private String productColour;
    private String productMaterial;

    private String isStdFitPPO;
    private String isInPPO;

    private List<String> tmeStandard;
    private List<String> tmePackOffer;
    private List<String> tmeDemo;
}
