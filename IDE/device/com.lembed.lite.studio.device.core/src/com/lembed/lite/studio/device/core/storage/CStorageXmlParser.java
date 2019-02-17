/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.core.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpStrings;


/**
 * Base class to parse CMSIS pack-related files
 */
public abstract class CStorageXmlParser implements ICStorageXmlParser {


	protected ICStorageElement rootItem   = null;  // represents top-level item being constructed
	protected String xmlFile = null;  // current XML file
	protected String xmlString = null;  // current XML string

	protected String xsdFile = null;        // schema file with absolute path
	protected Set<String> ignoreTags = null; // tags to ignore (partly parsed file)
	protected Set<String> ignoreWriteTags = null; // tags to ignore when writing to xml file

	// errors for current file
	protected List<String> errorStrings = new LinkedList<String>();
	protected int nErrors = 0;
	protected int nWarnings = 0;

	// DOM
	private DocumentBuilderFactory docBuilderFactory = null;
	protected DocumentBuilder docBuilder = null;
	protected XmlErrorHandler errorHandler = null;

	public CStorageXmlParser() {
	}

	public CStorageXmlParser(String xsdFile) {
		this.xsdFile = xsdFile;
	}

	@Override
	public void clear() {
		xmlFile = null;
		xmlString = null;
		rootItem 	= null;
		errorStrings.clear();
		nErrors   = 0;
		nWarnings = 0;
		if (docBuilder != null) {
			docBuilder.reset();
		}
	}


	@Override
	public String getXsdFile() {
		return xsdFile;
	}

	@Override
	public void setXsdFile(String xsdFile) {
		this.xsdFile = xsdFile;
	}

	/**
	 * @return the xmlFile
	 */
	public String getXmlFile() {
		return xmlFile;
	}

	@Override
	public List<String> getErrorStrings() {
		return errorStrings;
	}

	@Override
	public int getErrorCount() {
		return nErrors;
	}

	@Override
	public int getWarningCount() {
		return nWarnings;
	}


	@Override
	public void setIgnoreTags(Set<String> ignoreTags) {
		this.ignoreTags = ignoreTags;
	}

	@Override
	public void setWriteIgnoreTags(Set<String> ignoreTags) {
		ignoreWriteTags = ignoreTags;
	}

	/**
	 * Check if an item with given tag should be ignored by reading or writing
	 * @param tag tag to check
	 * @return true if tag is ignored
	 */
	protected boolean isTagIgnored(String tag) {
		if(tag == null || tag.isEmpty()) {
			return true;
		}
		if (ignoreTags == null || ignoreTags.isEmpty()) {
			return false;
		}
		return ignoreTags.contains(tag);
	}

	/**
	 * Check if item should be ignored by writing
	 * @param item {@link ICpItem} to check
	 * @return true if ignored
	 */
	protected boolean isItemIgnored(ICStorageElement item) {
		if(item == null) {
			return true;
		}
		return isTagIgnored(item.getName());
	}


	@Override
	public ICStorageElement createElement(ICStorageElement parent, String tag) {
		if(parent != null) {
			return parent.createChild(tag);
		}
		if(rootItem == null) {
			rootItem = createRootElement(tag) ;
		}
		return rootItem;
	}


	protected class XmlErrorHandler implements ErrorHandler {

		public XmlErrorHandler() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void error(SAXParseException arg0) throws SAXException {

			addErrorString(arg0, CpStrings.CpXmlParser_Error);
			nErrors++;
		}

		@Override
		public void fatalError(SAXParseException arg0) throws SAXException {
			addErrorString(arg0, CpStrings.CpXmlParser_FatalError);
			nErrors++;
			throw arg0;
		}

		@Override
		public void warning(SAXParseException arg0) throws SAXException {
			addErrorString(arg0, CpStrings.CpXmlParser_Warning);
			nWarnings++;
		}

		protected void addErrorString(SAXParseException arg0,	final String severity) {
			String err = xmlFile;
			int line = arg0.getLineNumber();
			int col = arg0.getColumnNumber();
			if (line > 0) {
				err += "("; //$NON-NLS-1$
				err += line;
				err += ","; //$NON-NLS-1$
				err += col;
				err += ")"; //$NON-NLS-1$
			}
			err += ": " + severity + ": "; //$NON-NLS-1$ //$NON-NLS-2$
			err += arg0.getLocalizedMessage();
			errorStrings.add(err);
		}
	}

	@Override
	public boolean init() {

		try {
			if(docBuilderFactory == null) {
				docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilderFactory.setValidating(false);
				docBuilderFactory.setNamespaceAware(true);
				if (xsdFile != null && !xsdFile.isEmpty()) {
					SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					Schema schema = schemaFactory.newSchema(new Source[] {new StreamSource(xsdFile)});
					docBuilderFactory.setSchema(schema);
				}
			}
			if(docBuilder == null){
				docBuilder = docBuilderFactory.newDocumentBuilder();
				errorHandler = new XmlErrorHandler();
			}
			docBuilder.setErrorHandler(errorHandler);
		} catch (ParserConfigurationException e) {
			String err = CpStrings.CpXmlParser_ErrorParserInit;
			err += ": "; //$NON-NLS-1$
			err += e.toString();
			errorStrings.add(err);
			nErrors++;
			docBuilder = null;
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			String err = CpStrings.CpXmlParser_ErrorSchemaInit;
			err += " " +  xsdFile; //$NON-NLS-1$
			err += ": "; //$NON-NLS-1$
			err += e.toString();
			errorStrings.add(err);
			nErrors++;
			docBuilder = null;
			e.printStackTrace();
		}

		return true;
	}



	/**
	 * Process the item just created
	 * @param item item just created
	 */
	protected void processItem(ICStorageElement item) {
	}


	@Override
	public String adjustAttributeValue(String key, String value) {
		if(key.equals(CmsisConstants.DFPU)) {
			switch(value){
			case "1":				 //$NON-NLS-1$
			case "FPU": //$NON-NLS-1$
				return CmsisConstants.SP_FPU;
			case "0": //$NON-NLS-1$
				return CmsisConstants.NO_FPU;
			default:
				return value;
			}
		} else if(key.equals(CmsisConstants.DMPU)){
			switch(value){
			case "1":	//$NON-NLS-1$
				return CmsisConstants.MPU;
			case "0":	//$NON-NLS-1$
				return CmsisConstants.NO_MPU;
			default:
				return value;
			}
		} else if(key.equals(CmsisConstants.DDSP)){
			switch(value){
			case "1":	//$NON-NLS-1$
				return CmsisConstants.DSP;
			case "0":	//$NON-NLS-1$
				return CmsisConstants.NO_DSP;
			default:
				return value;
			}
		} else if(key.equals(CmsisConstants.DTZ)){
			switch(value){
			case "1":	//$NON-NLS-1$
				return CmsisConstants.TZ;
			case "0":	//$NON-NLS-1$
				return CmsisConstants.NO_TZ;
			default:
				return value;
			}
		} else if(key.equals(CmsisConstants.DSECURE)){
			switch(value){
			case "1":	//$NON-NLS-1$
				return CmsisConstants.SECURE;
			case "0":	//$NON-NLS-1$
				return CmsisConstants.NON_SECURE;
			default:
				return value;
			}
		}


		// convert boolean values to 1 for consistency
		if(value.equals("true")) { //$NON-NLS-1$
			return "1"; //$NON-NLS-1$
		} else if(value.equals("false")) //$NON-NLS-1$
		 {
			return "0"; //$NON-NLS-1$
		}
		if(value.startsWith("\\\\")) { //$NON-NLS-1$
			return value;
		}
		if(value.indexOf(':') == 1) {
			return value;
		}
		return value.replace('\\', '/'); // convert all backslashes to slashes for consistency
	}


	@Override
	public String writeToXmlString(ICStorageElement root) {
		String xml = null;
		clear();
		this.xmlFile = null;
		if(!init()) {
			return xml;
		}

		Document domDoc = createDomDocument(root);

		if(domDoc == null) {
			return xml;
		}

		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
	        DOMSource source = new DOMSource(domDoc);
	        StringWriter buffer = new StringWriter();
	        StreamResult dest = new StreamResult(buffer);
	        transformer.transform(source, dest);
	        buffer.close();
	        xml = buffer.toString();

		} catch ( TransformerFactoryConfigurationError | TransformerException | IOException e) {
			String error = CpStrings.CpXmlParser_ErrorCreatingXML;
			error += ": "; //$NON-NLS-1$
			error += e.toString();
			errorStrings.add(error);
			e.printStackTrace();
			return null;
		}
		return xml;
	}

	/**
	 * Creates DOM document
	 * @param root root IcpItem to create document for
	 * @return creates DOM
	 */
	protected Document createDomDocument(ICStorageElement root){
		Document domDoc = docBuilder.newDocument();
        createElement(domDoc, null, root);
		return domDoc;
	}


	/**
	 * Recursively creates DOM element node for supplied ICpItem
	 * @param doc DOM document owner of the element
	 * @param parentNode parent DOM node for the element
	 * @param item ICpItem to process
	 * @return created element node
	 */
	protected Node createElement(Document doc, Node parentNode, ICStorageElement element){

		if(isItemIgnored(element)) {
			return null;
	    }		

		Element node = null;
		node = doc.createElement(element.getName());
		String[] attributeNames = element.getAttributeNames();
		for(String aname : attributeNames){
			String value = element.getAttribute(aname);
			node.setAttribute(aname, value);
		}

		if(parentNode != null) {
			parentNode.appendChild(node);
		} else {
			doc.appendChild(node);
			node.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema-instance"); //$NON-NLS-1$ //$NON-NLS-2$
			if (xsdFile != null && !xsdFile.isEmpty()) {
				File f = new File(xsdFile);
				String xsdName = f.getName();
				node.setAttribute("xs:noNamespaceSchemaLocation", xsdName); //$NON-NLS-1$
			}
		}

		if(element.hasChildren()){
			ICStorageElement[] children = element.getChildren();
			for(ICStorageElement child : children) {
				createElement(doc, node, child);
			}
		}
		return node;
	}
	
	private static void log(String msg){
		System.out.println(msg);
	}
}
