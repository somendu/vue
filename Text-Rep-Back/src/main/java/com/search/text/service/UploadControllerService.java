/**
 * 
 */
package com.search.text.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * Service layer interface for the uploading of file
 * 
 * @author somendu
 *
 */
public interface UploadControllerService {

	/**
	 * 
	 * Save the file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public Path saveUploadedFile(MultipartFile file) throws IOException;

}
