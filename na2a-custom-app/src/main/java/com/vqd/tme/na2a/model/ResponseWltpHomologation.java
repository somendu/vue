package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.p360.P360Homologation;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ResponseWltpHomologation {

    private final Boolean wltp;
    private final String information;
    private final P360Homologation homologation;
    private final Boolean hubFitmentFlag;
    private final String componentApproval;
    private final String impactedRegulations;
    private final BigDecimal deltaCDA;
    @Builder.Default
    private final List<WltpHomologationGenerationCode> generationCodes = Lists.newArrayList();
}
