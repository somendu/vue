package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureGroup;
import com.vqd.tme.na2a.p360.GetResponse;

import java.util.List;

public interface StructureGroupRepository {

    GetResponse findByIdentifier(String structure, String identifier);

    List<P360StructureGroup> findByStructure(P360Structure structure);

}
