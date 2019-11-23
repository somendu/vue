/**
 * 
 */
package com.search.text.service;

import java.io.IOException;

/**
 * The service class for Text File
 * 
 * @author somendu
 *
 */
public interface TextService {

	/**
	 * 
	 * Read the text file using File.lines creating a string stream
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void readTextFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException;

	/**
	 * 
	 * Read the text file using File.lines creating a string stream
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void testRead(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException;

	/**
	 * Read file using File Utils from Apache
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void readUsingFileUtils(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException, InterruptedException;

	/**
	 * Read using File.readAllLines
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void readUsingReadAllLines(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException;
}
