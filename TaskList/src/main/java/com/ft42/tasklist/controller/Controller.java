package com.ft42.tasklist.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ft42.tasklist.service.TaskDownloadService;
import com.ft42.tasklist.service.TaskListAddService;
import com.ft42.tasklist.support.TaskDownloadRequest;
import com.ft42.tasklist.support.TaskRequest;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller Class for creating end points
 * 
 * @author somendu
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

	private final TaskListAddService taskListAddService;

	private final TaskDownloadService taskDownloadService;

	// Insert Task List
	@PostMapping("/insertTaskList")
	public String insertTaskList(@RequestBody TaskRequest taskRequest) {
		return taskListAddService.insertTaskList(taskRequest);
	}

	// Insert Task List
	@PostMapping("/downloadTaskList")
	public String downloadTaskList(@RequestBody TaskDownloadRequest taskDownloadRequest) {

		return taskDownloadService.downloadExcel(taskDownloadRequest);

	}

}
