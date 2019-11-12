package com.company.text.support;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReplaceTextInXML {
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException,
			XPathExpressionException, TransformerException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource("sample.xml"));

		XPath xPath = XPathFactory.newInstance().newXPath();
		// replace 'test' value with your replace to value
		// get nodelist having attribute contain test
		NodeList nodes = (NodeList) xPath.evaluate("//*[contains(attribute::*,'test')]", doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i).getAttributes().item(0);
			String val = node.getNodeValue();
			node.setNodeValue(val.replaceAll("test", "the")); // here actual replacement happen
			System.out.println(val);
		}
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(new DOMSource(doc), new StreamResult(new File("./newfile.xml")));
		System.out.println(nodes.getLength());
	}

}
