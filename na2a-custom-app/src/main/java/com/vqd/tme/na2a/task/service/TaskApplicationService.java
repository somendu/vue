package com.vqd.tme.na2a.task.service;

import com.vqd.tme.na2a.task.model.TaskApplicationResponse;

import java.util.List;

public interface TaskApplicationService {

    List<TaskApplicationResponse> get(String brand, String model, String project);
}
