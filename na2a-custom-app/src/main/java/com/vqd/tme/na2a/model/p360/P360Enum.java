package com.vqd.tme.na2a.model.p360;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class P360Enum {

    private String identifier;
    private String name;
    private String dataType;
    private List<P360EnumEntry> entries = Lists.newArrayList();
}
