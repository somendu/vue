package com.vqd.tme.na2a.model;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ResponseLocalProject {
  private String project;
  private String projectName;
  private String variantId;

  private List<String> localProject;
  private List<String> localModel;
  private List<String> localGrade;
  private List<String> localModelCode;
}
