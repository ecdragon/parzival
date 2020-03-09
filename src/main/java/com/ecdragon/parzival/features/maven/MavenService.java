package com.ecdragon.parzival.features.maven;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ecdragon.parzival.features.xml.XmlService;

@Component
public class MavenService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private XmlService xmlService;
	public XmlService getXmlService() {
		return xmlService;
	}
	@Autowired
	public void setXmlService(XmlService xmlService) {
		this.xmlService = xmlService;
	}
	
	public String generateInitialParentPom(
			String groupId,
			String artifact
			) {
		return null;
	}
	
	public static void main(String[] args) {
		
		try {
			String pomXmlString = "<bar><foo><dependencies><dependency>foo1</dependency></dependencies></foo><yet><foo>foo2</foo></yet></bar>";
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pomXmlString.getBytes("UTF-8"));
			Document document = documentBuilder.parse(byteArrayInputStream);
			Element rootElement = document.getDocumentElement();
			rootElement.normalize();
			System.out.println("Root element :" + rootElement.getNodeName());
			
			Node firstDependenciesNode = findFirstChildNode(rootElement, "dependencies", 1, 100);
			System.out.println("First dependencies node name: " + firstDependenciesNode.getNodeName());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Node findFirstChildNode(
			Node nodeToFindDependenciesWithin, 
			String nodeNameToFind, 
			Integer currentDepth, 
			Integer maxDepth
			) {
		
		if (nodeToFindDependenciesWithin == null || currentDepth == null || maxDepth == null) {
			return null;
		}
		
		if (nodeToFindDependenciesWithin.getNodeName().equalsIgnoreCase(nodeNameToFind)) {
			return nodeToFindDependenciesWithin;
		}
		
		NodeList projectChildren = nodeToFindDependenciesWithin.getChildNodes();
		if (projectChildren != null && currentDepth <= maxDepth) {
		    for (int i = 0; i < projectChildren.getLength(); i++) {
		    	Node projectChild = projectChildren.item(i);
		    	Node firstChildNodeFound = findFirstChildNode(projectChild, nodeNameToFind, currentDepth + 1, maxDepth);
		    	if (firstChildNodeFound != null) {
		    		return firstChildNodeFound;
		    	}
		    }
		}
		
		return null;
	}
	
	public List<MavenDependency> findDependenciesInPomXmlString(String pomXmlString) {
		
		if (pomXmlString == null) {
			return null;
		}
		try {
			// Trim it!!
			pomXmlString = pomXmlString.trim();
			
			List<MavenDependency> results = null;
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pomXmlString.getBytes("UTF-8"));
			Document document = documentBuilder.parse(byteArrayInputStream);
			Element rootElement = document.getDocumentElement();
			rootElement.normalize();
			System.out.println("Root element :" + rootElement.getNodeName());

			Node dependenciesNode = findFirstChildNode(rootElement, "dependencies", 1, 1);
			
			if (dependenciesNode == null) {
				return results;
			}
			
			NodeList dependenciesChildren = dependenciesNode.getChildNodes();
			for (int j = 0; j < dependenciesChildren.getLength(); j++) {
				Node dependenciesChild = dependenciesChildren.item(j);
				if (dependenciesChild.getNodeName().equalsIgnoreCase("dependency")) {
					MavenCoordinates mavenCoordinates = new MavenCoordinates();
					MavenDependency mavenDependency = new MavenDependency();
					NodeList dependencyChildren = dependenciesChild.getChildNodes();
					for (int k = 0; k < dependencyChildren.getLength(); k++) {
						Node dependencyChild = dependencyChildren.item(k);
						if (dependencyChild.getNodeName().equalsIgnoreCase("groupId")) {
							Node groupId = dependencyChild;
							NodeList groupIdChildren = groupId.getChildNodes();
							for (int l = 0; l < groupIdChildren.getLength(); l++) {
								Node groupIdChild = groupIdChildren.item(l);
								if (groupIdChild.getNodeName().equalsIgnoreCase("#text")) {
									mavenCoordinates.setGroupId(groupIdChild.getTextContent());
								}
							}
						}
						else if (dependencyChild.getNodeName().equalsIgnoreCase("artifactId")) {
							Node artifactId = dependencyChild;
							NodeList artifactIdChildren = artifactId.getChildNodes();
							for (int l = 0; l < artifactIdChildren.getLength(); l++) {
								Node artifactIdChild = artifactIdChildren.item(l);
								if (artifactIdChild.getNodeName().equalsIgnoreCase("#text")) {
									mavenCoordinates.setArtifactId(artifactIdChild.getTextContent());
								}
							}
						}
						else if (dependencyChild.getNodeName().equalsIgnoreCase("version")) {
							Node version = dependencyChild;
							NodeList versionChildren = version.getChildNodes();
							for (int l = 0; l < versionChildren.getLength(); l++) {
								Node versionChild = versionChildren.item(l);
								if (versionChild.getNodeName().equalsIgnoreCase("#text")) {
									mavenCoordinates.setVersion(versionChild.getTextContent());
								}
							}
						}
						else if (dependencyChild.getNodeName().equalsIgnoreCase("scope")) {
							Node scope = dependencyChild;
							NodeList scopeChildren = scope.getChildNodes();
							for (int l = 0; l < scopeChildren.getLength(); l++) {
								Node scopeChild = scopeChildren.item(l);
								if (scopeChild.getNodeName().equalsIgnoreCase("#text")) {
									MavenDependencyScopeEnum mavenDependencyScopeEnum =
											MavenDependencyScopeEnum.findByXmlTextValue(scopeChild.getTextContent());
									mavenDependency.setMavenScope(mavenDependencyScopeEnum);
								}
							}
						}
					}
					if (results == null) {
						results = new ArrayList<>();
					}
					mavenDependency.setMavenCoordinates(mavenCoordinates);
					results.add(mavenDependency);
				}
			}
	        
		    if (results != null) {
		    	Collections.sort(results);
		    }
			return results;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * From MKYong example: https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	 * result:
<?xml version="1.0" encoding="UTF-8" standalone="no" ?> 
<company>
	<staff id="1">
		<firstname>yong</firstname>
		<lastname>mook kim</lastname>
		<nickname>mkyong</nickname>
		<salary>100000</salary>
	</staff>
</company>
	 * 
	 * ...and from:
	 *   https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java?page=1&tab=votes#tab-top
	 *   https://stackoverflow.com/questions/19939830/how-do-you-create-xml-nodes-using-java-dom
	 */
	public String createPomXmlDependenciesStringFromMavenDependencies(List<MavenDependency> mavenDependencies) {
		
		String methodLabel = "In createPomXmlDependenciesStringFromMavenDependencies(): ";
		
		if (mavenDependencies == null) {
			logger.error(methodLabel + "null input.");
			return null;
		}
		
		try {
			Document document = xmlService.createNewXmlDocument();
			if (document == null) {
				logger.error(methodLabel + "Unable to create a document");
				return null;
			}
			
			// Root element (project)
			Element elementRootProject = document.createElement("project");
			document.appendChild(elementRootProject);
			
			// Create dependencies tag and put under root
			Element elementDependencies = document.createElement("dependencies");
			elementRootProject.appendChild(elementDependencies);
			
			for (MavenDependency mavenDependency : mavenDependencies) {
				mavenDependency.appendAsChildElement(document, elementDependencies);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new StringWriter());
			transformer.transform(source, streamResult);
			String xmlString = streamResult.getWriter().toString();
			xmlString = xmlString.replaceAll("    ", "\t");

			return xmlString;
		} 
		catch (Exception e) {
			logger.error(methodLabel + "Exception creating deps: " + e.getMessage(), e);
			return null;
		}

	}
}
