package com.vqd.tme.na2a.partlinking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartsFilter {

    private String projectCode;
    private String personInCharge;
    private String commodity;
    private String partNumber;
    private String partName;
    private String colourOrMaterial;
}
