package com.vqd.tme.na2a.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class WltpHomologationGenerationCode {

    private final String type;
    private final String from;
    private final String to;
//    @Builder.Default // TODO can be removed
//    private final Boolean infinity = false;
}
