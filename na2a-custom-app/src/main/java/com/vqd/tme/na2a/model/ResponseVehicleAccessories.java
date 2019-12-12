package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseVehicleAccessories {

    @Builder.Default
    private final List<ResponseVehicleAccessory> accessories = Lists.newArrayList();

}
