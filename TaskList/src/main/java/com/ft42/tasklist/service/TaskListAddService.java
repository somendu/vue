/**
 * 
 */
package com.ft42.tasklist.service;

import com.ft42.tasklist.support.TaskRequest;

/**
 * 
 * Service layer interface for the controller
 * 
 * @author somendu
 *
 */
public interface TaskListAddService {

	public String insertTaskList(TaskRequest taskRequest);
}
