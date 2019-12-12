package com.vqd.tme.na2a.model;

import com.vqd.tme.na2a.model.p360.P360Classification;
import lombok.Data;

import java.util.List;

@Data
public class Accessory {

    private String id;
    private List<P360Classification> classifications;
    private Boolean add;
}
