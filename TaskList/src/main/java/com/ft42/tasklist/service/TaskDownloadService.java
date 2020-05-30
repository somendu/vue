/**
 * 
 */
package com.ft42.tasklist.service;

import com.ft42.tasklist.support.TaskDownloadRequest;

/**
 * Tasklist Download Request
 * 
 * @author Somendu
 *
 */
public interface TaskDownloadService {

	/**
	 * Download Task List
	 * 
	 * @param taskDownloadRequest
	 * @return
	 */
	public String downloadExcel(TaskDownloadRequest taskDownloadRequest);

}
