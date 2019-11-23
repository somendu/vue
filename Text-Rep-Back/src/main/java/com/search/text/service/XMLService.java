/**
 * 
 */
package com.search.text.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * 
 * Interface for XML Processing
 * 
 * @author somendu
 *
 */
public interface XMLService {

	/**
	 * 
	 * Read and Create New XML File
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws TransformerException
	 */
	public void readXMLFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException,
			TransformerException;
}
