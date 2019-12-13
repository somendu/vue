package com.vqd.tme.na2a.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LocalModel {
    private KeyValue project;
    private KeyValue model;
}
