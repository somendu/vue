package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
public class P360ApplicationDesign {

    private final BigDecimal lCoOrdinates;
    private final Integer lCoOrdinatesRounded;
    private final Boolean replacementAccessory;
    private final Boolean incalculable;
    private final BigDecimal deltaMass;
    private final Integer deltaMassRounded;
//    private final Boolean replacementSpecFlag;


}
