package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360ApplicationAPM;

public interface ApplicationAPMRepository {

    P360ApplicationAPM findById(String applicationId);
}
