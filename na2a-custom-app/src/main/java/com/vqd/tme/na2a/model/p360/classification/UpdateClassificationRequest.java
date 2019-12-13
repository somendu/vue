package com.vqd.tme.na2a.model.p360.classification;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateClassificationRequest {

    private List<UpdateClassificationColumn> columns;
    private List<UpdateClassificationRow> rows;
}
