package com.vqd.tme.na2a.task.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Builder
public class TaskApplicationResponse {

    private TaskApplication taskApplication;

    @Builder.Default
    private BigDecimal volume = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;
    @Builder.Default
    private String ppoCode = "";
    @Builder.Default
    private String status = "";


}
