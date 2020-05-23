package com.vqd.tme.na2a.task.controller;

import com.vqd.tme.na2a.model.TreeNode;
import com.vqd.tme.na2a.task.service.impl.TaskRefTableServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/task")
public class TaskRefTableController {

    private TaskRefTableServiceImpl service;

    public TaskRefTableController(TaskRefTableServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/brands/{brand}/models/{model}")
    public Collection<TreeNode> getSsns(@PathVariable String brand,
                                        @PathVariable String model) {

       return service.getSsns(brand, model);
    }
}
