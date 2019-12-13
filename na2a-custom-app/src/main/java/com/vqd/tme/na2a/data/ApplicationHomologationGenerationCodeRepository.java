package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360HomologationGenerationCode;

import java.util.List;

public interface ApplicationHomologationGenerationCodeRepository {

    List<P360HomologationGenerationCode> findByApplication(String applicationId);

}
