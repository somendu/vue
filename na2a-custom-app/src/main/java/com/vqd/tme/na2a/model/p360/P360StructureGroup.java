package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class P360StructureGroup {

    private String parentIdentifier;
    private String identifier;
    private String name;
}
