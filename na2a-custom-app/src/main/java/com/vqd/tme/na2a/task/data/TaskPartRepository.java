package com.vqd.tme.na2a.task.data;

import java.util.List;

import com.vqd.tme.na2a.task.model.TaskPart;

public interface TaskPartRepository {
	
	List<TaskPart> findByApplicationId(String applicationId);

}
