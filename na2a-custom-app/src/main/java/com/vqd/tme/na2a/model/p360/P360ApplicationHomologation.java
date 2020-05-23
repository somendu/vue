package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
public class P360ApplicationHomologation {

    private final Boolean wltp;
    private final String information;
    private final P360Homologation homologation;
    private final Boolean hubFitmentFlag;
    private final String contentApproval;
    private final BigDecimal deltaCDA;
}
