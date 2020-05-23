package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;

import java.util.List;

public interface ClassificationResolver {

    List<P360Classification> resolve(String variantId, P360Structure structure);

}
