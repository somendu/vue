/**
 * 
 */
package com.search.text.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.search.text.exception.FileExtensionException;
import com.search.text.exception.FileTypeException;

/**
 * @author somendu
 *
 */
public interface ReadFileService {

	public void readFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile)
			throws FileTypeException, FileExtensionException, IOException, ParserConfigurationException, IOException,
			SAXException, XPathExpressionException, TransformerException, InterruptedException;
}
