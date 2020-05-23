/**
 * 
 */
package com.shadow.text.exception;

/**
 * @author somendu
 *
 */
public class FileTypeException extends Exception {

	public FileTypeException(String errorMessage) {
		super("File Type is not Text or XML");
	}

}
