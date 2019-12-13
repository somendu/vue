package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.PostApplicationDesigns;
import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.p360.UpdateItemResponse;

public interface ApplicationDesignRepository {

    P360ApplicationDesign findByApplication(String applicationId);

    UpdateItemResponse update(PostApplicationDesigns designs);
}
