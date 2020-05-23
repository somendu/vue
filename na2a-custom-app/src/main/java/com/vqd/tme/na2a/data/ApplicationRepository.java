package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.Variant;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;

import java.util.List;

public interface ApplicationRepository {

    List<P360Application> findByBrandAndModelAndProject(String brand, String model, String project);

    void updateClassifications(String applicationId, P360Structure structure, List<P360Classification> classifications) throws CouldNotSaveException;

    P360Application findById(String applicationId);

    List<Variant> findByProductId(String productId);
}
