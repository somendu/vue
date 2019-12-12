package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseAccessoryTab {

    private final String name;
    @Builder.Default
    private final List<ResponseAccessoryColumn> columns = Lists.newArrayList();
}
