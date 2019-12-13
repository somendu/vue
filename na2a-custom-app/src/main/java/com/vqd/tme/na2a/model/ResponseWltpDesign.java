package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class ResponseWltpDesign {

    private final BigDecimal lCoOrdinates;
    private final Integer lCoOrdinatesRounded;
    private final Boolean replacementAccessory;
    private final Boolean incalculable;
    private final BigDecimal deltaMass;
    private final Integer deltaMassRounded;

    @Builder.Default
    private final List<WltpDesignReplacementOfEquipment> replacementOfEquipments = Lists.newArrayList();
}
