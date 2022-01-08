package sampleXmlReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class XmlParser {

	String inputFilePathName;
	String outputFilePathName;

	Document subjectDocument;

	private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

	private DocumentBuilder createDocumentBuidler() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder;

	}

	public void createDocument() {
		String sourceFile = getInputFilePathName();
		logger.info("SourceFile:" + sourceFile);
		Document document = null;
		try {

			InputStream inputStream = new FileInputStream(sourceFile);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			DocumentBuilder buidler = createDocumentBuidler();
			document = buidler.parse(is);
			logger.info("document");
			setSubjectDocument(document);

		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error during loading document", e);
		}
	}
	/*
	 * public void writeToXml() { Document documentToWrite = getSubjectDocument();
	 * if(null !=documentToWrite) { TransformerFactory transformerFactory =
	 * TransformerFactory.newInstance(); documentToWrite.setXmlStandalone(true);
	 * Source xslDoc = new StreamSource("./resources/stylesheet.xsl"); Transformer
	 * transformer; try { transformer = transformerFactory.newTransformer(xslDoc);
	 * DOMSource source = new DOMSource(documentToWrite); String outputName =
	 * getOutputFilePathName(); logger.info("outputName:"+outputName); File
	 * directory = new File(outputName); if (!directory.exists()) {
	 * directory.mkdirs(); } StreamResult result = new StreamResult(new
	 * File("./resources/"+outputName)); transformer.transform(source, result); }
	 * catch (TransformerException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }else { logger.info("subject document is null"); }
	 * 
	 * }
	 */

	public String getQuery(String queryString) {
		String resultQuery = null;
		Document document = getSubjectDocument();

		XPath xPath = XPathFactory.newInstance().newXPath();

		try {

			NodeList nodes = (NodeList) xPath.evaluate(queryString, document, XPathConstants.NODESET);
			if (null != nodes && nodes.getLength() > 0) {
				logger.info("START:" + nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					Element e = (Element) nodes.item(i);
					// String textContent = e.getTextContent();
					resultQuery = e.getAttribute("query");
					logger.info("ResultQuery:"+resultQuery);
				}
				logger.info("got something");
			} else {
				logger.info("NothingFound");
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// setSubjectDocument(document);
		return resultQuery;
	}

	public void writeToXml() {
		Document documentToWrite = getSubjectDocument();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			DOMImplementation domImpl = documentToWrite.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype", "databaseQueryTemplate.dtd",
					"databaseQueryTemplate.dtd");

			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getSystemId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getPublicId());

			DOMSource source = new DOMSource(documentToWrite);
			String outputName = getOutputFilePathName();
			StreamResult result = new StreamResult(new File("./resources/" + outputName));
			transformer.transform(source, result);

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			logger.error("writeToXml", e);
			e.printStackTrace();
		}

	}

	public String getOutputFilePathName() {
		return outputFilePathName;
	}

	public void setOutputFilePathName(String outputFilePathName) {
		this.outputFilePathName = outputFilePathName;
	}

	public Document getSubjectDocument() {
		return subjectDocument;
	}

	public void setSubjectDocument(Document subjectDocument) {
		this.subjectDocument = subjectDocument;
	}

	public String getInputFilePathName() {
		return inputFilePathName;
	}

	public void setInputFilePathName(String inputFilePathName) {
		this.inputFilePathName = inputFilePathName;
	}
}
