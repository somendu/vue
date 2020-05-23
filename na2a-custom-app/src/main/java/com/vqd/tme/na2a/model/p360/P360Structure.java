package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class P360Structure {

    private final String objectId;
    private final String identifier;
    private final String name;
    private final P360StructureType type;
}
