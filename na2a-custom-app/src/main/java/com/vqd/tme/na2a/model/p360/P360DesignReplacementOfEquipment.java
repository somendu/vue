package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class P360DesignReplacementOfEquipment {

    private final String code;
    private final String equipment;
    private final Boolean replacementFlagSpec;


}
