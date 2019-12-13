package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.PostApplicationHomologations;
import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
import com.vqd.tme.na2a.p360.UpdateItemResponse;

import java.util.List;

public interface ApplicationHomologationRepository {

    P360ApplicationHomologation findByApplication(String applicationId);

    List<UpdateItemResponse> update(PostApplicationHomologations homologations);
}
