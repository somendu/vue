package com.vqd.tme.na2a.model;

import com.vqd.tme.na2a.model.p360.P360EntryKey;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
public class Product {
  private String id;
  private String productNumber;
  private String shortDescription;

  private P360EntryKey colour;
  private P360EntryKey material;
  @Builder.Default
  private Boolean isStdFitPPO =false;
}
