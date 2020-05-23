package com.vqd.tme.na2a.task.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
public class TaskApplication {

    private String id;
    private String name;

    private TaskCommodity commodity;
    private TaskProduct product;
    private List<TaskPart> parts;
}
