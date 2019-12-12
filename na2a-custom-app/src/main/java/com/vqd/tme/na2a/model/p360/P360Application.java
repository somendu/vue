package com.vqd.tme.na2a.model.p360;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
public class P360Application {

    private String id;
    private String vehicleGeneration;
    private String name;
    private String shortDescription;
    private String status;
    @Builder.Default
    private List<String> interiorColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> interiorColours = Lists.newArrayList();
    @Builder.Default
    private List<String> exteriorColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> exteriorColours = Lists.newArrayList();
    @Builder.Default
    private List<String> trimColourIds = Lists.newArrayList();
    @Builder.Default
    private List<String> trimColours = Lists.newArrayList();
    @Builder.Default
    private List<String> equipmentIds = Lists.newArrayList();
    @Builder.Default
    private Boolean isInPPO = false;
}
