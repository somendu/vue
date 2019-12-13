package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.adapter.RowToKeyValueAdapter;
import com.vqd.tme.na2a.data.VehicleStructureRepository;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.p360.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleStructureService {

    private final VehicleStructureRepository vehicleStructureRepository;
    private final RowToKeyValueAdapter keyValueAdapter;

    public KeyValue getByLocalProject(String localProjectIdentifier) {
        // First find the TME model in the vehicle structure based on the tmeProjectIdentifier
        String[] split = localProjectIdentifier.split("\\|");
        if(split.length != 2) {
            throw new IllegalArgumentException(String.format("Invalid format of model identifier - %s", localProjectIdentifier));
        }

        GetResponse projectResponse = vehicleStructureRepository.findByIdentifier(split[1]);
        if(projectResponse.getRowCount() != 1) {
            throw new RuntimeException(String.format("There should only be one model for identifier %s", split[0]));
        }

        return keyValueAdapter.convert(projectResponse.getRows().get(0));
    }
}
