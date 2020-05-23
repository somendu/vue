package com.vqd.tme.na2a.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class LocalProject {
  private KeyValue localProject;
  private KeyValue grade;
  private KeyValue code;
}
