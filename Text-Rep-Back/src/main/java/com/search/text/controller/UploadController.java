package com.search.text.controller;

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

import com.search.text.service.UploadControllerService;

import io.swagger.annotations.ApiOperation;
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
	@ApiOperation(value = "Upload File", notes = "Provide File ", response = ResponseEntity.class)
	public ResponseEntity<?> uploadFile(@RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file,
			@RequestPart("searchText") String searchText, @RequestPart("replaceText") String replaceText) {

		try {
			System.out.println("File Here");
			System.out.println("searchText : " + searchText);
			System.out.println("replaceText : " + replaceText);
			uploadControllerService.saveUploadedFile(file);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);

	}

}
