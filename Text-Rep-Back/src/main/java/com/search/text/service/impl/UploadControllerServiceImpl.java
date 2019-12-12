/**
 * 
 */
package com.search.text.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.search.text.service.UploadControllerService;

/**
 * Service layer implementation class for handling all business logic
 * 
 * @author somendu
 *
 */
@Service

public class UploadControllerServiceImpl implements UploadControllerService {

	@Override
	public Path saveUploadedFile(MultipartFile file) throws IOException {

		Path path = null;

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			path = Paths.get("file/" + file.getOriginalFilename());

			System.out.println("Path Parent: " + path.getParent());
			System.out.println("File Name: " + path.getFileName());

			try (OutputStream os = Files.newOutputStream(path)) {
				os.write(bytes);
				os.close();
			}

		}

		return path;

	}

}
