package com.vqd.tme.na2a.task.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class TaskProduct {
	
	private String id;
	private String name;
	private String productNumber;
	
}
