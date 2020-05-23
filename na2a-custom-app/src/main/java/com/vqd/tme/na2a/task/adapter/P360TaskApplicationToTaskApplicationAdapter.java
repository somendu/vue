package com.vqd.tme.na2a.task.adapter;

import com.vqd.tme.na2a.adapter.Adapter;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.task.model.TaskApplication;
import org.springframework.stereotype.Component;

@Component
public class P360TaskApplicationToTaskApplicationAdapter implements Adapter<P360Application, TaskApplication> {

    @Override
    public TaskApplication convert(P360Application p360Application) {
        return TaskApplication.builder()
                .id(p360Application.getId())
                .name(p360Application.getShortDescription())
                .build();
    }
}
