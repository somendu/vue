package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.p360.GetResponse;

public interface EnumRepository {

    P360Enum findByType(String enumType);
}
