package com.vqd.tme.na2a.task.model;

import com.vqd.tme.na2a.model.Variant;
import com.vqd.tme.na2a.partlinking.model.Part;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PostTaskRequest {

    private final Task task = null;
    private final List<Variant> variants = null;
    private final List<Part> parts = null;

}
