//package com.vqd.tme.na2a.service;
//
//import com.google.common.collect.Lists;
//import com.vqd.tme.na2a.config.InformaticaPimProperties;
//import com.vqd.tme.na2a.data.*;
//import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
//import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
//import com.vqd.tme.na2a.model.*;
//import com.vqd.tme.na2a.model.p360.*;
//import com.vqd.tme.na2a.p360.UpdateItemResponse;
//import com.vqd.tme.na2a.partlinking.persistence.impl.P360VariantMetaDataResolver;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class WltpServiceTest {
//
//    private ApplicationRepository applicationRepository;
//    private ApplicationDesignRepository applicationDesignRepository;
//    private ApplicationHomologationRepository applicationHomologationRepository;
//    private ApplicationHomologationGenerationCodeRepository applicationHomologationGenerationCodeRepository;
//    private ApplicationAttributesRepository applicationAttributesRepository;
//    private ApplicationDesignReplacementOfEquipmentRepository designReplacementOfEquipmentRepository;
//    private P360VariantMetaDataResolver resolver;
//
//    private InformaticaPimProperties pimProperties;
//    private RestTemplate restTemplate;
//
//    private WltpService testable;
//
//    @Before
//    public void setUp() {
//        applicationRepository = mock(ApplicationRepository.class);
//        applicationDesignRepository = mock(ApplicationDesignRepository.class);
//        applicationHomologationRepository = mock(ApplicationHomologationRepository.class);
//        applicationHomologationGenerationCodeRepository = mock(ApplicationHomologationGenerationCodeRepository.class);
//        applicationAttributesRepository = mock(ApplicationAttributesRepository.class);
//        designReplacementOfEquipmentRepository = mock(ApplicationDesignReplacementOfEquipmentRepository.class);
//        pimProperties = mock(InformaticaPimProperties.class);
//        restTemplate = mock(RestTemplate.class);
//
//        resolver = mock(P360VariantMetaDataResolver.class);
//
//        testable = new WltpService(applicationRepository,
//                applicationDesignRepository,
//                applicationHomologationRepository,
//                applicationHomologationGenerationCodeRepository,
//                designReplacementOfEquipmentRepository,
//                applicationAttributesRepository,
//                restTemplate,
//                pimProperties,
//                resolver);
//    }
//
//    @Test (expected = CouldNotSaveException.class)
//    public void updateHomologationsWithCouldNotSaveExceptions() throws CouldNotSaveException, SavedWithErrorsException {
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(1);
//        counters.setWarnings(0);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(0);
//        counters.setObjectsWithErrors(1);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        when(applicationHomologationRepository.update(any(PostApplicationHomologations.class))).thenReturn(response);
//
//        testable.updateHomologations(new PostApplicationHomologations());
//    }
//
//    @Test (expected = SavedWithErrorsException.class)
//    public void updateHomologationsWithSavedWithErrorsExceptions() throws CouldNotSaveException, SavedWithErrorsException {
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(0);
//        counters.setWarnings(1);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(0);
//        counters.setObjectsWithErrors(0);
//        counters.setObjectsWithWarnings(1);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        when(applicationHomologationRepository.update(any(PostApplicationHomologations.class))).thenReturn(response);
//
//        testable.updateHomologations(new PostApplicationHomologations());
//    }
//
//    @Test
//    public void testUpdateHomologation() throws CouldNotSaveException, SavedWithErrorsException {
//        PostApplicationHomologations homologations = new PostApplicationHomologations();
//        homologations.add(new PostApplicationHomologation()
//                .setApplicationId("1000")
//                .setWltpFlag(true)
//                .setDeltaCDA(BigDecimal.valueOf(56.356)));
//
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(0);
//        counters.setWarnings(0);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(1);
//        counters.setObjectsWithErrors(0);
//        counters.setObjectsWithWarnings(0);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        List<P360Attribute> attributes = Lists.newArrayList(
//                P360Attribute.builder().applicationId("1000").attributeId("componentApproval").name("Component Approval").value("test approval").build(),
//                P360Attribute.builder().applicationId("1000").attributeId("impactedRegulations").name("Impacted Regulations").value("test regulations").build()
//        );
//
//        List<P360HomologationGenerationCode> codes = Lists.newArrayList(
//                P360HomologationGenerationCode.builder()
//                        .code(P360GenerationCode.builder().id("1").label("test").build())
//                        .from("A")
//                        .to("B")
//                        .build()
//        );
//
//        P360ApplicationHomologation updatedHomologation = P360ApplicationHomologation.builder()
//                .wltp(true)
//                .deltaCDA(BigDecimal.valueOf(56.356))
//                .information("")
//                .hubFitmentFlag(true)
//                .homologation(null)
//                .build();
//        when(applicationHomologationRepository.update(homologations)).thenReturn(response);
//        when(applicationHomologationRepository.findByApplication("1000")).thenReturn(updatedHomologation);
//        when(applicationAttributesRepository.findByApplication("1000")).thenReturn(attributes);
//        when(applicationHomologationGenerationCodeRepository.findByApplication("1000")).thenReturn(codes);
//
//        List<ResponseWltpAccessory> accessories = testable.updateHomologations(homologations);
//
//        assertNotNull(accessories);
//        assertEquals(1, accessories.size());
//        assertNotNull(accessories.get(0).getHomologation());
//        assertEquals("1000", accessories.get(0).getId());
//        assertTrue(accessories.get(0).getHomologation().getWltp());
//        assertEquals(BigDecimal.valueOf(56.356), accessories.get(0).getHomologation().getDeltaCDA());
//
//    }
//
//
//    @Test (expected = CouldNotSaveException.class)
//    public void updateDesignsWithCouldNotSaveExceptions() throws CouldNotSaveException, SavedWithErrorsException {
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(1);
//        counters.setWarnings(0);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(0);
//        counters.setObjectsWithErrors(1);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        when(applicationDesignRepository.update(any(PostApplicationDesigns.class))).thenReturn(response);
//
//        testable.updateDesign(new PostApplicationDesigns());
//    }
//
//    @Test (expected = SavedWithErrorsException.class)
//    public void updateDesignsWithSavedWithErrorsExceptions() throws CouldNotSaveException, SavedWithErrorsException {
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(0);
//        counters.setWarnings(1);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(0);
//        counters.setObjectsWithErrors(0);
//        counters.setObjectsWithWarnings(1);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        when(applicationDesignRepository.update(any(PostApplicationDesigns.class))).thenReturn(response);
//
//        testable.updateDesign(new PostApplicationDesigns());
//    }
//
//    @Test
//    public void testUpdateDesign() throws CouldNotSaveException, SavedWithErrorsException {
//        PostApplicationDesigns designs = new PostApplicationDesigns();
//        designs.add(new PostApplicationDesign()
//                .setApplicationId("1000")
//                .setReplacementAccessory(true)
//                .setDeltaMass(BigDecimal.valueOf(1.21))
//                .setDeltaMassRounded(Integer.valueOf(1))
//                .setIncalculable(false)
//                .setLcoordinates(BigDecimal.valueOf(3.96))
//                .setLcoordinatesRounded(Integer.valueOf(4)));
//
//        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
//        counters.setErrors(0);
//        counters.setWarnings(0);
//        counters.setCreatedObjects(0);
//        counters.setUpdatedObjects(1);
//        counters.setObjectsWithErrors(0);
//        counters.setObjectsWithWarnings(0);
//        UpdateItemResponse response = new UpdateItemResponse();
//        response.setCounters(counters);
//
//        P360ApplicationDesign updatedDesign = P360ApplicationDesign.builder()
//                .replacementAccessory(true)
//                .deltaMass(BigDecimal.valueOf(1.21))
//                .deltaMassRounded(Integer.valueOf(1))
//                .incalculable(false)
//                .lCoOrdinates(BigDecimal.valueOf(3.96))
//                .lCoOrdinatesRounded(Integer.valueOf(4))
//                .build();
//        when(applicationDesignRepository.update(designs)).thenReturn(response);
//        when(applicationDesignRepository.findByApplication("1000")).thenReturn(updatedDesign);
//
//        List<ResponseWltpAccessory> accessories = testable.updateDesign(designs);
//
//        assertNotNull(accessories);
//        assertEquals(1, accessories.size());
//        assertNotNull(accessories.get(0).getDesign());
//        assertEquals("1000", accessories.get(0).getId());
//        assertTrue(accessories.get(0).getDesign().getReplacementAccessory());
//        assertEquals(BigDecimal.valueOf(1.21), accessories.get(0).getDesign().getDeltaMass());
//        assertEquals(Integer.valueOf(1), accessories.get(0).getDesign().getDeltaMassRounded());
//        assertFalse(accessories.get(0).getDesign().getIncalculable());
//        assertEquals(BigDecimal.valueOf(3.96), accessories.get(0).getDesign().getLCoOrdinates());
//        assertEquals(Integer.valueOf(4), accessories.get(0).getDesign().getLCoOrdinatesRounded());
//
//        verify(applicationDesignRepository).update(designs);
//        verify(applicationDesignRepository).findByApplication("1000");
//    }
//}
