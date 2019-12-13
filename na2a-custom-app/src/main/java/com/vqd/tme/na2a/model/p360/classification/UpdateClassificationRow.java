package com.vqd.tme.na2a.model.p360.classification;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateClassificationRow {

    private UpdateClassificationRowObject object;
    private UpdateClassificationQualification qualification;
    private List<Object> values = Lists.newArrayList();
}
