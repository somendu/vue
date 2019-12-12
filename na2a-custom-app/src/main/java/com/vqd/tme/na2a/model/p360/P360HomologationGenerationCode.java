package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class P360HomologationGenerationCode {

    private final P360GenerationCode code;
    private final String from;
    private final String to;
}
