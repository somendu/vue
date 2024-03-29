/**
 * 
 */
package com.charging.station.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.charging.station.service.UploadControllerService;
import com.charging.station.support.ChargeRequest;

/**
 * Service layer implementation class for handling all business logic
 * 
 * @author somendu
 *
 */
@Service

public class UploadControllerServiceImpl implements UploadControllerService {

	@Override
	public String uploadFile(ChargeRequest request) {

		// TODO Read the file

		// Return File Uploaded

		return "Uploaded";
	}

	@Override
	public void saveUploadedFile(MultipartFile file) throws IOException {

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			Path path = Paths
					.get("/Users/somendu/Work-Area/Workspace/Vue/Lineage-Upload/file/" + file.getOriginalFilename());

			System.out.println("Path Parent: " + path.getParent());
			System.out.println("File Name: " + path.getFileName());

			try (OutputStream os = Files.newOutputStream(path)) {
				os.write(bytes);
				os.close();
			}

		}

	}

}
