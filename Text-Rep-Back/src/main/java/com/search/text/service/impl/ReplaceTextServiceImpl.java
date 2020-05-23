/**
 * 
 */
package com.search.text.service.impl;

import org.springframework.stereotype.Service;

import com.search.text.service.ReplaceTextService;

/**
 * @author somendu
 *
 */
@Service
public class ReplaceTextServiceImpl implements ReplaceTextService {

	@Override
	public String replaceText(String actualString, String searchString, String replaceString) {

		actualString = actualString.replaceAll(searchString, replaceString);

		return actualString;
	}

}
