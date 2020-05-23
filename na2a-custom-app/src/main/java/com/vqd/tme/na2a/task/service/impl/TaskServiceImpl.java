package com.vqd.tme.na2a.task.service.impl;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.task.model.Task;
import com.vqd.tme.na2a.task.service.TaskService;
import com.vqd.tme.na2a.task.support.TaskResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String FIELDS = String.join(",", Arrays.asList(
            "Task.Name",
            "Task.Description",
            "Task.CreationDate",
            "Task.EscalationDate",
            "Task.CreationUser",
            "Task.Deadline",
            "Task.WorkflowStatus"));

    final private InformaticaPimProperties pimProperties;
    final private RestTemplate rest;
    final private TaskResolver resolver;

    @Override
    public Task getTask(String id) {

        GetResponse res = rest.getForObject(pimProperties.getServer() +
                "/rest/V2.0/list/Task/byItems" +
                "?items={id}" +
                "&fields=" + FIELDS,
                GetResponse.class,
                id);

        log.trace("getTask {}", res);

        return resolver.doTask(res);
    }
}
