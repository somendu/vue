package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360Attribute;

import java.util.List;

public interface ApplicationAttributesRepository {

    List<P360Attribute> findByApplication(String applicationId);
}
