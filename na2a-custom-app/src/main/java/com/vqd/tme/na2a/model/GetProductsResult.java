package com.vqd.tme.na2a.model;

import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class GetProductsResult {
  private Set<Product> details;
  private List<ResponseVariant> entries;
  private List<ResponseLocalProject> localProjects;
}
