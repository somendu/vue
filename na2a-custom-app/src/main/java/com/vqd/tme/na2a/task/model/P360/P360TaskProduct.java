package com.vqd.tme.na2a.task.model.P360;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class P360TaskProduct {

    private String id;
    private String name;
    private String productNumber;
}
