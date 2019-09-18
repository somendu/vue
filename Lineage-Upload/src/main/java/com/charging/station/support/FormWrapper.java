/**
 * 
 */
package com.charging.station.support;

import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author somendu
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Scope("request")
public class FormWrapper {

	private MultipartFile image;
	private String title;
	private String description;
}
