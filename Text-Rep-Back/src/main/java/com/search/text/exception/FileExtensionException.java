/**
 * 
 */
package com.search.text.exception;

/**
 * @author somendu
 *
 */
public class FileExtensionException extends Exception {

	public FileExtensionException(String errorMessage) {
		super("Either Source or Target File Extension is Invalid");
	}

}
