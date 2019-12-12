package com.vqd.tme.na2a.controller;

import com.vqd.tme.na2a.adapter.CountryEnumToKeyValueAdapter;
import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.p360.P360Enum;
import com.vqd.tme.na2a.service.EnumLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnumController {

    private final EnumLookupService enumService;
    private final CountryEnumToKeyValueAdapter enumAdapter;

    @GetMapping("/api/all/countries")
    public List<KeyValue> getCountries() {
        P360Enum countryEnum = enumService.getTMECountryEnum();
        return enumAdapter.convert(countryEnum);
    }
}
