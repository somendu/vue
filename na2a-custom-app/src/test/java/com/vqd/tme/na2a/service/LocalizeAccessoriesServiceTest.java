package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.adapter.RowToKeyValueAdapter;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.model.p360.P360Classification;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureType;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.util.GetResponseBuilder;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocalizeAccessoriesServiceTest {

    //    private LocalVehicleStructureRepository repository;
    private ApplicationRepository applicationRepository;
    private StructureService structureService;
    private RowToKeyValueAdapter adapter;

    private LocalizeAccessoriesService testable;

    @Before
    public void setUp() {
        applicationRepository = mock(ApplicationRepository.class);
        structureService = mock(StructureService.class);
        adapter = new RowToKeyValueAdapter();

        testable = new LocalizeAccessoriesService(
                applicationRepository,
                null,
                null,
                null,
                null,
                null,
                structureService,
                adapter);
    }

    @Test
    public void testGetKeyValues() {
        GetResponse.Row toyotaBrandRow = GetResponseBuilder.buildRow("r1", 1, Lists.newArrayList("123", "TOYATA", null, ""));
        GetResponse.Row lexusBrandRow = GetResponseBuilder.buildRow("r2", 2, Lists.newArrayList("456", "LEXUS", null, ""));
        GetResponse brandResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(toyotaBrandRow, lexusBrandRow), 2, 2);

        GetResponse.Row projectRow1 = GetResponseBuilder.buildRow("p1", 3, Lists.newArrayList("789", "Auris 1", null, "TOYOTA|AU"));
        GetResponse.Row projectRow2 = GetResponseBuilder.buildRow("p2", 4, Lists.newArrayList("987", "Auris 2", null, "TOYOTA|AU"));
        GetResponse projectResponse1 = GetResponseBuilder.buildResponse(Lists.newArrayList(projectRow1, projectRow2), 2, 2);

        GetResponse.Row projectRow3 = GetResponseBuilder.buildRow("p3", 5, Lists.newArrayList("654", "Camry 1", null, "TOYOTA|CA"));
        GetResponse.Row projectRow4 = GetResponseBuilder.buildRow("p4", 6, Lists.newArrayList("321", "Camry 2", null, "TOYOTA|CA"));
        GetResponse projectResponse2 = GetResponseBuilder.buildResponse(Lists.newArrayList(projectRow3, projectRow4), 2, 2);

//        when(repository.findByParentIdentifier("TGB")).thenReturn(brandResponse);
//        when(repository.findByParentIdentifier("123")).thenReturn(projectResponse1);
//        when(repository.findByParentIdentifier("456")).thenReturn(projectResponse2);
        when(structureService.getByParentIdentifier("TGB", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(brandResponse);
        when(structureService.getByParentIdentifier("123", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(projectResponse1);
        when(structureService.getByParentIdentifier("456", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(projectResponse2);

        List<KeyValue> result = testable.getKeyValues("TGB");

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(1, result.stream().filter(r -> r.getKey().equals("789")).count());
        assertEquals(1, result.stream().filter(r -> r.getKey().equals("987")).count());
        assertEquals(1, result.stream().filter(r -> r.getKey().equals("654")).count());
        assertEquals(1, result.stream().filter(r -> r.getKey().equals("321")).count());
    }

    @Test
    public void testGetLocalModel() {
        String modelId = "3BAD7776-9F87-4AFE-ACFC-112D892BC0E9";

        GetResponse.Row modelRow = GetResponseBuilder.buildRow(modelId, 1, Lists.newArrayList(modelId, "Auris 2006\\M10", null, "TGB|TOYOTA|CONFIG|AU"));
        GetResponse modelResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(modelRow), 1 , 1);

        GetResponse.Row configRow = GetResponseBuilder.buildRow("configId", 1, Lists.newArrayList("configId", "Auris 2006", null, "TGB|TOYOTA|CONFIG"));
        GetResponse configResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(configRow), 1 , 1);

        GetResponse.Row projectRow = GetResponseBuilder.buildRow("projectId", 1, Lists.newArrayList("projectId", "Auris", null, "TGB|TOYOTA"));
        GetResponse projectResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(projectRow), 1 , 1);

//        when(repository.findByIdentifier(modelId)).thenReturn(modelResponse);
//        when(repository.findByIdentifier("TGB|TOYOTA|CONFIG|AU")).thenReturn(configResponse);
//        when(repository.findByIdentifier("TGB|TOYOTA|CONFIG")).thenReturn(projectResponse);
        when(structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(modelResponse);
        when(structureService.getByIdentifier("TGB|TOYOTA|CONFIG|AU", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(configResponse);
        when(structureService.getByIdentifier("TGB|TOYOTA|CONFIG", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(projectResponse);

        LocalModel result = testable.getLocalModel(modelId);

        assertNotNull(result);
        assertNotNull(result.getModel());
        assertEquals(modelId, result.getModel().getKey());
        assertEquals("Auris 2006\\M10", result.getModel().getValue());
        assertEquals("TGB|TOYOTA|CONFIG|AU", result.getModel().getParentKey());
        assertNotNull(result.getProject());
        assertEquals("projectId", result.getProject().getKey());
        assertEquals("Auris", result.getProject().getValue());
        assertEquals("TGB|TOYOTA", result.getProject().getParentKey());
    }

    @Test
    public void testGetLocalModelNoPimModelFound() {
        String modelId = "3BAD7776-9F87-4AFE-ACFC-112D892BC0E9";
        GetResponse modelResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0 , 0);

//        when(repository.findByIdentifier(modelId)).thenReturn(modelResponse);
        when(structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(modelResponse);

        LocalModel result = testable.getLocalModel(modelId);

        assertNotNull(result);
        assertNull(result.getModel());
        assertNull(result.getProject());
    }

    @Test
    public void testGetLocalModelNoPimConfigFound() {
        String modelId = "3BAD7776-9F87-4AFE-ACFC-112D892BC0E9";

        GetResponse.Row modelRow = GetResponseBuilder.buildRow(modelId, 1, Lists.newArrayList(modelId, "Auris 2006\\M10", null, "TGB|TOYOTA|CONFIG|AU"));
        GetResponse modelResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(modelRow), 1 , 1);

        GetResponse configResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0 , 0);

        when(structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(modelResponse);
        when(structureService.getByIdentifier("TGB|TOYOTA|CONFIG|AU", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(configResponse);

        LocalModel result = testable.getLocalModel(modelId);

        assertNotNull(result);
        assertNotNull(result.getModel());
        assertEquals(modelId, result.getModel().getKey());
        assertEquals("Auris 2006\\M10", result.getModel().getValue());
        assertEquals("TGB|TOYOTA|CONFIG|AU", result.getModel().getParentKey());
        assertNull(result.getProject());
    }

    @Test
    public void testGetLocalModelNoPimProjectFound() {
        String modelId = "3BAD7776-9F87-4AFE-ACFC-112D892BC0E9";

        GetResponse.Row modelRow = GetResponseBuilder.buildRow(modelId, 1, Lists.newArrayList(modelId, "Auris 2006\\M10", null, "TGB|TOYOTA|CONFIG|AU"));
        GetResponse modelResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(modelRow), 1 , 1);

        GetResponse.Row configRow = GetResponseBuilder.buildRow("configId", 1, Lists.newArrayList("configId", "Auris 2006", null, "TGB|TOYOTA|CONFIG"));
        GetResponse configResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(configRow), 1 , 1);

        GetResponse projectResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(), 0 , 0);

        when(structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(modelResponse);
        when(structureService.getByIdentifier("TGB|TOYOTA|CONFIG|AU", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(configResponse);
        when(structureService.getByIdentifier("TGB|TOYOTA|CONFIG", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(projectResponse);

        LocalModel result = testable.getLocalModel(modelId);

        assertNotNull(result);
        assertNotNull(result.getModel());
        assertEquals(modelId, result.getModel().getKey());
        assertEquals("Auris 2006\\M10", result.getModel().getValue());
        assertEquals("TGB|TOYOTA|CONFIG|AU", result.getModel().getParentKey());
        assertNull(result.getProject());
    }

    @Test
    public void testGetVehicles() {
        String modelId = "3BAD7776-9F87-4AFE-ACFC-112D892BC0E9";

        GetResponse.Row modelRow = GetResponseBuilder.buildRow(modelId, 1, Lists.newArrayList(modelId, "Auris 2006\\M10", null, "TGB|TOYOTA|CONFIG|AU"));
        GetResponse modelResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(modelRow), 1, 1);

        GetResponse.Row gradeRow1 = GetResponseBuilder.buildRow("grade1", 2, Lists.newArrayList("grade1", "TR", null, modelId));
        GetResponse.Row gradeRow2 = GetResponseBuilder.buildRow("grade2", 3, Lists.newArrayList("grade2", "T3", null, modelId));
        GetResponse gradeResponse = GetResponseBuilder.buildResponse(Lists.newArrayList(gradeRow1, gradeRow2), 2, 2);

        GetResponse.Row codeRow1 = GetResponseBuilder.buildRow("code1", 4, Lists.newArrayList("code1", "Auris 3D [80]", null, "grade1"));
        GetResponse.Row codeRow2 = GetResponseBuilder.buildRow("code2", 5, Lists.newArrayList("code2", "Auris 5D [81]", null, "grade1"));
        GetResponse codeResponse1 = GetResponseBuilder.buildResponse(Lists.newArrayList(codeRow1, codeRow2), 2, 2);
        GetResponse.Row codeRow3 = GetResponseBuilder.buildRow("code3", 6, Lists.newArrayList("code3", "Auris 3D Hatchback [82]", null, "grade2"));
        GetResponse.Row codeRow4 = GetResponseBuilder.buildRow("code4", 7, Lists.newArrayList("code4", "Auris 5D Hatchback [83]", null, "grade2"));
        GetResponse codeResponse2 = GetResponseBuilder.buildResponse(Lists.newArrayList(codeRow3, codeRow4), 2, 2);

//        when(repository.findByIdentifier(modelId)).thenReturn(modelResponse);
//        when(repository.findByParentIdentifier(modelId)).thenReturn(gradeResponse);
//        when(repository.findByParentIdentifier("grade1")).thenReturn(codeResponse1);
//        when(repository.findByParentIdentifier("grade2")).thenReturn(codeResponse2);
        when(structureService.getByIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(modelResponse);
        when(structureService.getByParentIdentifier(modelId, P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(gradeResponse);
        when(structureService.getByParentIdentifier("grade1", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(codeResponse1);
        when(structureService.getByParentIdentifier("grade2", P360StructureType.LOCAL_VEHICLE_STRUCTURE)).thenReturn(codeResponse2);

        ResponseVehicles result = testable.getVehicles(modelId);

        assertNotNull(result);
        assertEquals(4, result.getVehicles().size());
        assertEquals(1, result.getVehicles().stream().filter(v -> v.getLocalCode().equals("Auris 3D [80]")).count());
        assertEquals(1, result.getVehicles().stream().filter(v -> v.getLocalCode().equals("Auris 5D [81]")).count());
        assertEquals(1, result.getVehicles().stream().filter(v -> v.getLocalCode().equals("Auris 3D Hatchback [82]")).count());
        assertEquals(1, result.getVehicles().stream().filter(v -> v.getLocalCode().equals("Auris 5D Hatchback [83]")).count());
    }

    @Test
    public void testAddAccessoryClassification() throws CouldNotSaveException {
        Accessory accessory = new Accessory();
        accessory.setAdd(true);
        accessory.setId("34475");
        P360Classification classification1 = P360Classification.builder().identifier("1").label("classification1").build();
        P360Classification classification2 = P360Classification.builder().identifier("2").label("classification2").build();
        P360Classification classification3 = P360Classification.builder().identifier("3").label("classification3").build();

        accessory.setClassifications(Lists.newArrayList(classification1, classification2));

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setLocalCodeIdentifier("1");
        vehicle1.setLocalCodeLabel("classification1");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setLocalCodeIdentifier("3");
        vehicle2.setLocalCodeLabel("classification3");

        Vehicles vehicles = new Vehicles();
        vehicles.addAll(Lists.newArrayList(vehicle1, vehicle2));

        AccessoryUpdateRequest request = new AccessoryUpdateRequest();
        request.setAccessory(accessory);
        request.setVehicles(vehicles);

        P360Structure structure = P360Structure.builder().build();
        when(structureService.getLocalVehicleStructure()).thenReturn(structure);

        testable.updateAccessoryClassification(request);

        verify(applicationRepository).updateClassifications("34475", structure, Lists.newArrayList(classification1, classification2, classification3));
    }

    @Test
    public void testDeleteAccessoryClassification() throws CouldNotSaveException {
        P360Classification classification1 = P360Classification.builder().identifier("1").label("classification1").build();
        P360Classification classification2 = P360Classification.builder().identifier("2").label("classification2").build();
        P360Classification classification3 = P360Classification.builder().identifier("3").label("classification3").build();

        Accessory accessory = new Accessory();
        accessory.setAdd(false);
        accessory.setId("34475");
        accessory.setClassifications(Lists.newArrayList(classification1, classification2, classification3));

        Vehicle vehicle = new Vehicle();
        vehicle.setLocalCodeIdentifier("2");
        vehicle.setLocalCodeLabel("classification2");

        Vehicles vehicles = new Vehicles();
        vehicles.addAll(Lists.newArrayList(vehicle));

        AccessoryUpdateRequest request = new AccessoryUpdateRequest();
        request.setAccessory(accessory);
        request.setVehicles(vehicles);

        P360Structure structure = P360Structure.builder().build();
        when(structureService.getLocalVehicleStructure()).thenReturn(structure);


        testable.updateAccessoryClassification(request);

        verify(applicationRepository).updateClassifications("34475", structure, Lists.newArrayList(classification1, classification3));
    }
}
