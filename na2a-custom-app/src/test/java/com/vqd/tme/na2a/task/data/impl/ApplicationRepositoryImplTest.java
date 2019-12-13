package com.vqd.tme.na2a.task.data.impl;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToP360ApplicationAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.impl.ApplicationRepositoryImpl;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.classification.UpdateClassificationRequest;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.p360.UpdateItemResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationRepositoryImplTest {


    private InformaticaPimProperties properties;
    private RestTemplate restTemplate;
    private RowToP360ApplicationAdapter applicationAdapter;


    private ApplicationRepository testable;

    @Before
    public void setUp() {
        properties = new InformaticaPimProperties();
        properties.setServer("http://localhost:9090");

        restTemplate = mock(RestTemplate.class);

        applicationAdapter = new RowToP360ApplicationAdapter();

        testable = new ApplicationRepositoryImpl(properties, restTemplate, null, applicationAdapter);
    }

    @Test
    public void findByBrandAndModelAndProject() {
        String url = properties.getServer()
                + "/rest/V2.0/list/Variant/bySearch?query={productNumber}" +
                "&fields=Variant.Id," +
                "Variant.TMEGeneration," +
                "VariantLang.TMEApplication(EN)," +
                "VariantLang.TMEDescriptionShort(EN)," +
                "Variant.CurrentStatus," +
                "Variant.TMEInteriorColours," +
                "VariantLang.TMEInteriorColours(EN)," +
                "Variant.TMEExteriorColours," +
                "VariantLang.TMEExteriorColours(EN)," +
                "Variant.TMETrimColours," +
                "VariantLang.TMETrimColours(EN)," +
                "Variant.TMEEquipmentSpecs," +
                "VariantStructureMap.StructureGroup(\"LocalVehicleStructure\")";

        String query = "Variant.ManufacturerName equalsic \"TOYOTA\" AND Variant.ManufacturerAID equalsic \"Camry\" AND Variant.TMEGeneration equalsic \"Camry 2001\\08 (FMC)\"";

        GetResponse response = new GetResponse();
        List<Object> rowValues = Lists.newArrayList(
                "17236@1",
                "vehicleGeneration",
                "Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)",
                "Y",
                "active",
                Lists.newArrayList("interior colour Id"),
                Lists.newArrayList("interior colour"),
                Lists.newArrayList("exterior colour Id"),
                Lists.newArrayList("exterior colour"),
                Lists.newArrayList("trim colour Id"),
                Lists.newArrayList("trim colour"),
                Lists.newArrayList("equipment types"),
                Lists.newArrayList("")
        );
        List<GetResponse.Row> rows =Lists.newArrayList(
                GetResponseBuilder.buildRow("17236",1200, rowValues)
        );
        response.setRows(rows);

        when(restTemplate.getForObject(url, GetResponse.class, query)).thenReturn(response);

        List<P360Application> applications = testable.findByBrandAndModelAndProject("TOYOTA", "Camry", "Camry 2001\\08 (FMC)");

        assertEquals(1, applications.size());
        assertEquals("17236@1", applications.get(0).getId());
        assertEquals("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)", applications.get(0).getName());
        assertEquals("Y", applications.get(0).getShortDescription());
        assertEquals(1, applications.get(0).getInteriorColourIds().size());
        assertEquals(1, applications.get(0).getExteriorColourIds().size());
        assertEquals(1, applications.get(0).getTrimColourIds().size());
        assertEquals(1, applications.get(0).getEquipmentIds().size());
        assertFalse(applications.get(0).getIsInPPO());
    }

    @Test
    public void testUpdateClassification() throws CouldNotSaveException {
        String applicationId = "34475";
        P360Structure structure = P360Structure.builder().objectId("10000").identifier("localVehicleStructure").build();
        List<P360Classification> classifications = Lists.newArrayList(
                P360Classification.builder().identifier("A0E68CF3-1D09-4595-923A-B3A60B56CEC3").build(),
                P360Classification.builder().identifier("175FC741-9E77-4181-8A43-D25F544AE3CE").build()
        );

        UpdateItemResponse response = new UpdateItemResponse();
        UpdateItemResponse.Counters counters = new UpdateItemResponse.Counters();
        counters.setErrors(0);
        response.setCounters(counters);
        when(restTemplate.postForObject(anyString(), any(UpdateClassificationRequest.class), eq(UpdateItemResponse.class))).thenReturn(response);

        testable.updateClassifications(applicationId, structure, classifications);

        verify(restTemplate).postForObject(anyString(), any(UpdateClassificationRequest.class), eq(UpdateItemResponse.class));
    }
}
