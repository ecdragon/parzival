package com.ecdragon.parzival.features.xml;

import java.lang.invoke.MethodHandles;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
public class XmlService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public Document createNewXmlDocument() {
		String methodLabel = "In createNewXmlDocument(): ";
		DocumentBuilderFactory docFactory = null;
		DocumentBuilder docBuilder = null;
		Document document = null;
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			document = docBuilder.newDocument();
		}
		catch (ParserConfigurationException e) {
			logger.error(methodLabel + "Exception creating an xml Document: " + e.getMessage(), e);
			return null;
		}
		return document;
	}
}
