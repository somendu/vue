package com.vqd.tme.na2a.partlinking.model;

import lombok.Data;

@Data
public class Commodity {

    private final String id;
    private final String value;

    public Commodity(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
