package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.p360.GetResponse;

public interface VehicleStructureRepository {

    GetResponse findByIdentifier(String identifier);

}
