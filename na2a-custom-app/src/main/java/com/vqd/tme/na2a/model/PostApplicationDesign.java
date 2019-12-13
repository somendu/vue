package com.vqd.tme.na2a.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class PostApplicationDesign {

    private String applicationId;
    private Boolean replacementAccessory;
    private BigDecimal deltaMass;
    private Integer deltaMassRounded;
    private Boolean incalculable;
    private BigDecimal lcoordinates;
    private Integer lcoordinatesRounded;
    private List<Object> replacementOfEquipments;
}
