package com.vqd.tme.na2a.model.p360;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
public class P360ApplicationAPM {

    @Builder.Default
    private final List<String> tmeStandard = Lists.newArrayList();
    @Builder.Default
    private final List<String> tmePackOffer = Lists.newArrayList();
    @Builder.Default
    private final List<String> tmeDemo = Lists.newArrayList();
}
