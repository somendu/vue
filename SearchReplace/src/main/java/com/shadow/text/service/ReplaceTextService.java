/**
 * 
 */
package com.shadow.text.service;

/**
 * @author somendu
 *
 */
public class ReplaceTextService {

	public String replaceText(String actualString, String searchString, String replaceString) {

		actualString = actualString.replaceAll(searchString, replaceString);

		return actualString;
	}

}
