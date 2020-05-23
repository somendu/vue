package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class P360GenerationCode {

    private final String id;
    private final String label;
}
