package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseAccessoryColumn {

    private final String headerName;
    @Builder.Default
    private final List<String> availableItems = Lists.newArrayList();
}
