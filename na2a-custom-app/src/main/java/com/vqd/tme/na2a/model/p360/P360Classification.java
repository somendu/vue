package com.vqd.tme.na2a.model.p360;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class P360Classification {

    private String identifier;
    private String label;
}
