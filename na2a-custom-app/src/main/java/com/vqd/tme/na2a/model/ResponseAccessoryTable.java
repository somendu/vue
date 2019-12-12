package com.vqd.tme.na2a.model;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseAccessoryTable {

    @Builder.Default
    private final List<ResponseAccessoryTab> tabs = Lists.newArrayList();
}
