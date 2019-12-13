package com.vqd.tme.na2a.task.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vqd.tme.na2a.task.model.TaskApplicationResponse;
import com.vqd.tme.na2a.task.service.TaskApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskApplicationsController {

    private final TaskApplicationService applicationService;

    @GetMapping("/applications")
    public List<TaskApplicationResponse> getApplications(
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "model") String model,
            @RequestParam(name = "project") String project) {

        return applicationService.get(brand, model, project);
    }
}
