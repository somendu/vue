package com.vqd.tme.na2a.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseVehicle {

    private String localGrade;
    @Builder.Default
    private String suffix = "Not available";
    @Builder.Default
    private String interiorColour = "Not available";
    @Builder.Default
    private String exteriorColour = "Not available";
    @Builder.Default
    private String trimColour = "Not available";
    private String localCode;
    private String localCodeIdentifier;
}
