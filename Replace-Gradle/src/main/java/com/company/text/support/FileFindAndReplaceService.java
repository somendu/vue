package com.company.text.support;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

class ContentReplaceInFile {

	public static void main(String[] args) {
		ReplaceUtility replaceUtility = new ReplaceUtility();
		long startTime = System.currentTimeMillis();
		String fileName = "SampleText.txt";

		try {
			if (isXmlFile(fileName)) {
				if (!isXmlValid(fileName)) {
					System.out.print("XML is Not Valid");
					return;
				}
			}
			replaceUtility.replaceText(fileName, "client", "customer", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
	}

	public static boolean isXmlValid(String filePath) {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.parse(file);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean isXmlFile(String fileName) {
		String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
		if (ext.equalsIgnoreCase("xml")) {
			return true;
		}
		return false;
	}
}