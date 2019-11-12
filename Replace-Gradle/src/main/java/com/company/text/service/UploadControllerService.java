/**
 * 
 */
package com.company.text.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * Service layer interface for the controller
 * 
 * @author somendu
 *
 */
public interface UploadControllerService {

	public void saveUploadedFile(MultipartFile image) throws IOException;

}
