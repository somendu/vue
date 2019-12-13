package com.vqd.tme.na2a.task.controller;

import com.vqd.tme.na2a.partlinking.controller.PartsController;
import com.vqd.tme.na2a.task.model.Task;
import com.vqd.tme.na2a.task.service.impl.TaskServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskServiceImpl service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

    }

    @Test
    public void getTask() throws Exception {

        Task task = new Task("Name", "Description", "CreationDate",
                "EscalationDate", "CreationUser","Deadline",
                "Status", false);

        when(service.getTask(anyString())).thenReturn(task);

        mockMvc.perform(get("/api/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.creationDate", is("CreationDate")))
                .andExpect(jsonPath("$.escalationDate", is("EscalationDate")))
                .andExpect(jsonPath("$.deadline", is("Deadline")))
                .andExpect(jsonPath("$.status", is("Status")))
                .andExpect(jsonPath("$.isPack", is(false)));
    }

}