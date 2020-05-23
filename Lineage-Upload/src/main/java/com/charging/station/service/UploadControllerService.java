/**
 * 
 */
package com.charging.station.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.charging.station.support.ChargeRequest;

/**
 * 
 * Service layer interface for the controller
 * 
 * @author somendu
 *
 */
public interface UploadControllerService {

	/**
	 * Start Upload
	 * 
	 * @param request
	 * @return
	 */
	public String uploadFile(ChargeRequest request);

	public void saveUploadedFile(MultipartFile file) throws IOException;

}
