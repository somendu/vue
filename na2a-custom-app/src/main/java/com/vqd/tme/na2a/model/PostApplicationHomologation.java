package com.vqd.tme.na2a.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Accessors(chain = true)
public class PostApplicationHomologation {

    private String applicationId;
    private Boolean wltpFlag;
    private BigDecimal deltaCDA;
    private ArrayList<Object> generationCodes;
    private Boolean hubFitmentFlag;

}
