package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.p360.UpdateItemRequest;
import com.vqd.tme.na2a.p360.UpdateItemResponse;

public interface VariantStructureGroupMapRepository {

    GetResponse findStructureGroupIds(String items);

    GetResponse findByApplicationId(String applicationId);

}
