package com.vqd.tme.na2a.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ResponseModel {
  private boolean all;
  private String brand;
  private KeyValue model;
  private KeyValue project;
}
