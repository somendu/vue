package com.vqd.tme.na2a.model.p360;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class P360EnumEntry {

    private String label;
    private Object key;
    private String externalCode;
    private List<Object> synonyms = Lists.newArrayList();
}
