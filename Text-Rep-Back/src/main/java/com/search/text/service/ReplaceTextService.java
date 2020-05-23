/**
 * 
 */
package com.search.text.service;

/**
 * 
 * This is the for the actual replacement of the string
 * 
 * @author somendu
 *
 */
public interface ReplaceTextService {

	/**
	 * The actual replacement method
	 * 
	 * @param actualString
	 * @param searchString
	 * @param replaceString
	 * @return
	 */
	public String replaceText(String actualString, String searchString, String replaceString);

}
