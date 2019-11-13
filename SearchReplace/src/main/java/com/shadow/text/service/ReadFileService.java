/**
 * 
 */
package com.shadow.text.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import com.shadow.text.exception.FileExtensionException;
import com.shadow.text.exception.FileTypeException;

/**
 * @author somendu
 *
 */
public class ReadFileService {

	private XMLService xmlService = new XMLService();

	private TextService textService = new TextService();

	public void readFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile)
			throws FileTypeException, FileExtensionException, IOException, ParserConfigurationException, IOException,
			SAXException, XPathExpressionException, TransformerException, InterruptedException {

		if (checkFileExtension(sourceFile) && checkFileExtension(targetFile)) {
			if (fileType.equalsIgnoreCase("txt")) {

				textService.testRead(sourceFile, fileType, searchString, replaceString, targetFile);
				// textService.readTextFile(sourceFile, fileType, searchString, replaceString,
				// targetFile);

				// textService.readUsingFileUtils(sourceFile, fileType, searchString,
				// replaceString, targetFile);
			}

			else if (fileType.equalsIgnoreCase("xml")) {
				xmlService.readXMLFile(sourceFile, fileType, searchString, replaceString, targetFile);

			} else {
				throw new FileTypeException("File Type is incorrect");
			}
		} else {
			throw new FileExtensionException("Either Source or Target File Extension is Invalid");
		}
	}

	/**
	 * Check File Extension
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean checkFileExtension(String fileName) {

		String extension = FilenameUtils.getExtension(fileName);

		if ("txt".equalsIgnoreCase(extension) || "xml".equalsIgnoreCase(extension)) {

			return true;
		}

		return false;
	}
}
