package com.vqd.tme.na2a.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class WltpDesignReplacementOfEquipment {

    private final String code;
    private final String equipment;
    private final Boolean replacementFlagSpec;
}
