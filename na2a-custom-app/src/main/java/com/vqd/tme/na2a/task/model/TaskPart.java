package com.vqd.tme.na2a.task.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain=true)
@Builder
public class TaskPart {
	
	  private String partNumber;
	  private String partName;
	  private String type;
	  private BigDecimal quantity;

}
