package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;

import java.util.Map;

public interface StructureRepository {

    Map<P360StructureType, P360Structure> findAll();

    GetResponse findByParentIdentifier(String identifier, P360Structure structure);

    GetResponse findByIdentifier(String identifier, P360Structure structure);
}
