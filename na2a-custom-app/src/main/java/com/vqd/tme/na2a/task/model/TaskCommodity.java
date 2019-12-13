package com.vqd.tme.na2a.task.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
public class TaskCommodity {
	
	private String value;
}
