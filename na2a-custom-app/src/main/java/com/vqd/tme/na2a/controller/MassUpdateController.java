package com.vqd.tme.na2a.controller;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.data.ApplicationDesignRepository;
import com.vqd.tme.na2a.data.ApplicationHomologationRepository;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.model.PostApplicationDesign;
import com.vqd.tme.na2a.model.PostApplicationDesigns;
import com.vqd.tme.na2a.model.PostApplicationHomologations;
import com.vqd.tme.na2a.model.ResponseWltpAccessory;
import com.vqd.tme.na2a.model.p360.P360ApplicationDesign;
import com.vqd.tme.na2a.model.p360.P360ApplicationHomologation;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.service.WltpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MassUpdateController {

    private final WltpService wltpService;
    private final ApplicationDesignRepository applicationDesignRepository;
    private final ApplicationHomologationRepository applicationHomologationRepository;

    @GetMapping("/api/massupdate/products")
    public List<ResponseWltpAccessory> getApplicationsByProduct(@RequestParam(name = "ids") String productIds) {
        List<String> ids = CastUtils.parameterToList(productIds, ",");
        return wltpService.getByProducts(ids);
    }

    @GetMapping("/api/massupdate/variants")
    public List<ResponseWltpAccessory> getApplicationsById(@RequestParam(name = "ids") String applicationIds) {
        List<String> ids = CastUtils.parameterToList(applicationIds, ",");
        return wltpService.getByApplications(ids);
    }

    @GetMapping("/api/massupdate/design")
    public P360ApplicationDesign getApplicationDesign(@RequestParam(name="id") String id) {
        String applicationId = String.format("%s@1", id);
        return applicationDesignRepository.findByApplication(applicationId);
    }

    @GetMapping("/api/massupdate/homologation")
    public P360ApplicationHomologation getApplicationHomologation(@RequestParam(name = "id") String id) {
        String applicationId = String.format("%s@1", id);
        return applicationHomologationRepository.findByApplication(applicationId);
    }

    @PutMapping("/api/massupdate/homologation")
    public List<ResponseWltpAccessory> updateHomologation(@RequestBody() PostApplicationHomologations body) throws CouldNotSaveException, SavedWithErrorsException {
        return wltpService.updateHomologations(body);
    }

    @PutMapping("/api/massupdate/design")
    public List<ResponseWltpAccessory> updateDesign(@RequestBody() PostApplicationDesigns body) throws CouldNotSaveException, SavedWithErrorsException {
        return wltpService.updateDesign(body);
    }

//    @GetMapping("/api/massudate/equipment")
//    public void getEquipment
}
