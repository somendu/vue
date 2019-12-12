/**
 * 
 */
package com.search.text.service.impl;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.search.text.exception.FileExtensionException;
import com.search.text.exception.FileTypeException;
import com.search.text.service.ReadFileService;
import com.search.text.service.TextService;
import com.search.text.service.XMLService;

/**
 * @author somendu
 *
 */

@Service
public class ReadFileServiceImpl implements ReadFileService {

	@Autowired
	private XMLService xmlService;

	@Autowired
	private TextService textService;

	@Override
	public void readFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile)
			throws FileTypeException, FileExtensionException, IOException, ParserConfigurationException, IOException,
			SAXException, XPathExpressionException, TransformerException, InterruptedException {

		if (checkFileExtension(sourceFile) && checkFileExtension(targetFile)) {
			if (fileType.equalsIgnoreCase("txt")) {

				textService.readTextFile(sourceFile, fileType, searchString, replaceString, targetFile);

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

	private boolean checkFileExtension(String fileName) {

		String extension = FilenameUtils.getExtension(fileName);

		if ("txt".equalsIgnoreCase(extension) || "xml".equalsIgnoreCase(extension)) {

			return true;
		}

		return false;
	}
}
