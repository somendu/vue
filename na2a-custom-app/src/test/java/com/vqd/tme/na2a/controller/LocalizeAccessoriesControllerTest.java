package com.vqd.tme.na2a.controller;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.model.*;
import com.vqd.tme.na2a.service.AccessoryTableService;
import com.vqd.tme.na2a.service.LocalizeAccessoriesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LocalizeAccessoriesController.class)
public class LocalizeAccessoriesControllerTest {

    @Autowired
    private LocalizeAccessoriesController controller;

    @MockBean
    private LocalizeAccessoriesService service;

    @MockBean
    private AccessoryTableService accessoryTableService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetLocalProject() throws Exception {

        List<KeyValue> projects = Lists.newArrayList(
                new KeyValue("id", "value", null, "parentKey")
        );

        when(service.getKeyValues("TGB")).thenReturn(projects);

        mockMvc.perform(get("/api/localisation/TGB/projects/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"))
                .andExpect(jsonPath("$[0].key", is("id")))
                .andExpect(jsonPath("$[0].value", is("value")))
                .andExpect(jsonPath("$[0].parentKey", is("parentKey")));

        verify(service).getKeyValues("TGB");
    }

    @Test
    public void testGetLocalModels() throws Exception {

        List<KeyValue> projects = Lists.newArrayList(
                new KeyValue("id", "value", null, "parentKey")
        );

        when(service.getKeyValues("TGB|TOYOTA")).thenReturn(projects);

        mockMvc.perform(get("/api/localisation/models?projectId=TGB|TOYOTA"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"))
                .andExpect(jsonPath("$[0].key", is("id")))
                .andExpect(jsonPath("$[0].value", is("value")))
                .andExpect(jsonPath("$[0].parentKey", is("parentKey")));

        verify(service).getKeyValues("TGB|TOYOTA");
    }

    @Test
    public void testGetLocalModel() throws Exception {
        KeyValue modelData = new KeyValue("modelId", "modelValue", null, "modelParentKey");
        KeyValue projectData = new KeyValue("projectId", "projectValue", null, "projectParentKey");

        LocalModel model = new LocalModel();
        model.setModel(modelData);
        model.setProject(projectData);

        when(service.getLocalModel("TGB|TOYOTA")).thenReturn(model);

        mockMvc.perform(get("/api/localisation/model?modelId=TGB|TOYOTA"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.model.key", is("modelId")));

        verify(service).getLocalModel("TGB|TOYOTA");
    }

    @Test
    public void testGetVehicles() throws Exception {
        ResponseVehicles vehicles = ResponseVehicles.builder().build();
        vehicles.getVehicles().add(
                ResponseVehicle.builder()
                        .localGrade("localGrade")
                        .localCode("localCode")
                        .build()
        );

        when(service.getVehicles("modelId")).thenReturn(vehicles);

        mockMvc.perform(get("/api/localisation/vehicles?modelId=modelId"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.vehicles[0].localGrade", is("localGrade")))
                .andExpect(jsonPath("$.vehicles[0].localCode", is("localCode")));

        verify(service).getVehicles("modelId");
    }

    @Test
    public void testGetVehicleAccessories() throws Exception {
        ResponseVehicleAccessories accessories = ResponseVehicleAccessories.builder().build();
        accessories.getAccessories().add(
                ResponseVehicleAccessory.builder()
                        .applicationId("1000@1")
                        .productId("2000@1")
                        .commodity("Airco")
                        .build()
        );

        when(service.getVehicleAccessories("TOYOTA", "Auris", "TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42")).thenReturn(accessories);

        mockMvc.perform(get("/api/localisation/accessories?brand=TOYOTA&model=Auris&tmeProject=TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.accessories[0].applicationId", is("1000@1")))
                .andExpect(jsonPath("$.accessories[0].productId", is("2000@1")))
                .andExpect(jsonPath("$.accessories[0].commodity", is("Airco")));

    }

    @Test
    public void testGetBodyVehicleOptions() throws Exception {
        List<KeyValue> options = Lists.newArrayList(
                new KeyValue("1", "5D Hatchback"),
                new KeyValue("2", "3D Hatchback")
        );
        when(service.getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "bodies")).thenReturn(options);

        mockMvc.perform(get("/api/localisation/vehicleOptions?tmeProject=TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42&optionType=bodies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("1")))
                .andExpect(jsonPath("$[0].value", is("5D Hatchback")))
                .andExpect(jsonPath("$[1].key", is("2")))
                .andExpect(jsonPath("$[1].value", is("3D Hatchback")));

        verify(service).getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "bodies");
    }

    @Test
    public void testGetEngineVehicleOptions() throws Exception {
        List<KeyValue> options = Lists.newArrayList(
                new KeyValue("1", "1.4D-4D (Diesel)"),
                new KeyValue("2", "2.2D-4D DCAT (Diesel)")
        );
        when(service.getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "engines")).thenReturn(options);

        mockMvc.perform(get("/api/localisation/vehicleOptions?tmeProject=TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42&optionType=engines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("1")))
                .andExpect(jsonPath("$[0].value", is("1.4D-4D (Diesel)")))
                .andExpect(jsonPath("$[1].key", is("2")))
                .andExpect(jsonPath("$[1].value", is("2.2D-4D DCAT (Diesel)")));

        verify(service).getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "engines");
    }

    @Test
    public void testGetTransmissionVehicleOptions() throws Exception {
        List<KeyValue> options = Lists.newArrayList(
                new KeyValue("1", "5 MMT 2WD"),
                new KeyValue("2", "5 MT 2WD")
        );
        when(service.getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "transmissions")).thenReturn(options);

        mockMvc.perform(get("/api/localisation/vehicleOptions?tmeProject=TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42&optionType=transmissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("1")))
                .andExpect(jsonPath("$[0].value", is("5 MMT 2WD")))
                .andExpect(jsonPath("$[1].key", is("2")))
                .andExpect(jsonPath("$[1].value", is("5 MT 2WD")));

        verify(service).getVehicleOptionsForProject("TGB|CF4C66D02-E7CB-4705-92F4-388502A97E42", "transmissions");
    }
}
