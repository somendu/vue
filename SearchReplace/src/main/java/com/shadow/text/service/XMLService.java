/**
 * 
 */
package com.shadow.text.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
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

/**
 * @author somendu
 *
 */
public class XMLService {

	private ReplaceTextService replaceTextService = new ReplaceTextService();

	// java -jar build/libs/SearchReplace.jar --fileType xml --searchString test
	// --replaceString the --sourceFile sample.xml --targetFile Result.xml

	public void readXMLFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException,
			TransformerException {

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(sourceFile));

		XPath xPath = XPathFactory.newInstance().newXPath();
		// replace 'test' value with your replace to value
		// get nodelist having attribute contain test
		NodeList nodes = (NodeList) xPath.evaluate("//*[contains(attribute::*,'" + searchString + "')]", doc,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i).getAttributes().item(0);
			String val = node.getNodeValue();

			node.setNodeValue(replaceTextService.replaceText(val, searchString, replaceString));

		}

		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(new DOMSource(doc), new StreamResult(new File(targetFile)));

	}

	public void readHugeXMLFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException,
			TransformerException, XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newInstance();

		try (final InputStream stream = this.getClass().getResourceAsStream(sourceFile)) {

			final XMLEventReader reader = factory.createXMLEventReader(stream);

			while (reader.hasNext()) {
				final XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					parsePage(reader);
				}
			}
		}

	}

	private void parsePage(final XMLEventReader reader) throws XMLStreamException {

		while (reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			if (event.isEndElement()) {
				return;
			}
			if (event.isStartElement()) {
				final StartElement element = event.asStartElement();
				final String elementName = element.getName().getLocalPart();

			}
		}

	}
}
