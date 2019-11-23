package com.charging.station.controller;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.charging.station.service.UploadControllerService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller Class for creating end points
 * 
 * @author somendu
 *
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UploadController {

	private final UploadControllerService uploadControllerService;

	// Sessions Method for Setting Session to Start
	@PostMapping("/uploadFile")

	public ResponseEntity<?> uploadFile(@RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file) {

		try {
			System.out.println("File Here");
			uploadControllerService.saveUploadedFile(file);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);

	}

}
