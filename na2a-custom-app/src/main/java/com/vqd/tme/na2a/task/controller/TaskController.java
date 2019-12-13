package com.vqd.tme.na2a.task.controller;

import com.google.common.base.Strings;
import com.vqd.tme.na2a.task.model.PostTaskRequest;
import com.vqd.tme.na2a.task.model.Task;
import com.vqd.tme.na2a.task.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/task")
@RestController
@RequiredArgsConstructor
public class TaskController {

    final private TaskServiceImpl svc;

    @GetMapping("/{id}")
    public Task getTask(@PathVariable String id) {
        if(Strings.isNullOrEmpty(id) || id.equals("0")){
            return Task.builder().build();
        }

        return svc.getTask(id);
    }

    @PutMapping("/{id}")
    public void putTask(@PathVariable String id,
                        @RequestBody PostTaskRequest body) {
        // TODO process data as of ticket 776
        System.out.println(body);
    }
}
