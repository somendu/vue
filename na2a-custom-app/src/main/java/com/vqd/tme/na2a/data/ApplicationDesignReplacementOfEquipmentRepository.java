package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.p360.P360DesignReplacementOfEquipment;

import java.util.List;

public interface ApplicationDesignReplacementOfEquipmentRepository {

    List<P360DesignReplacementOfEquipment> findByApplication(String applicationId);
}
