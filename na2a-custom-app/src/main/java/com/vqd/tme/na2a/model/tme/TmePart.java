package com.vqd.tme.na2a.model.tme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmePart {
    private String _id;
    private String partNumber;
    private String partName;
}
