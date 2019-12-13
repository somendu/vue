package com.vqd.tme.na2a.model.p360;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class P360Attribute {

    private String applicationId;
    private String attributeId;
    private String name;
    private String value;

}
