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

import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.w3c.dom.Document;

/**
 * Interface to CMSIS pack description file (*.pdsc) parser
 */
public interface ICStorageXmlParser extends ICStorageElementFactory {
	/**
	 * Initializes the parser
	 * @return true if successful
	 */
	boolean init();

	/**
	 * Clears internal data, error strings and resets builder
	 */
	void clear();

	/**
	 * Sets schema file to use by the parser
	 * @param xsdFile schema file name to use with absolute path
	 */
	void setXsdFile(String xsdFile);

	/**
	 * Returns schema file used by parser
	 * @return absolute schema file name or null if not set
	 */
	public String getXsdFile();

	/**
	 * @return the errorStrings
	 */
	List<String> getErrorStrings();

	/**
	 * @return number of errors
	 */
	int getErrorCount();

	/**
	 * @return number of warnings
	 */
	int getWarningCount();

	/**
	 * Sets XML tags to ignore during parsing
	 * @param ignoreTags set of tags to ignore
	 */
	void setIgnoreTags(Set<String> ignoreTags);

	/**
	 * Sets XML tags to ignore during writing
	 * @param ignoreTags set of tags to ignore
	 */
	void setWriteIgnoreTags(Set<String> ignoreTags);

	/**
	 * Adjusts deprecated attribute values to modern ones
	 * @param key attribute key
	 * @param value attribute value
	 * @return adjusted attribute value
	 */
	String adjustAttributeValue(String key, String value);

	/**
	 * Factory method to create ICpItem-derived root instance
	 * @param tag XML tag for the item
	 * @return created ICpItem
	 */
	public ICStorageElement createRootElement(String tag);


	/**
	 * Generates XML text out of ICpItem and returns it as a string
	 * @param root ICpItem to write
	 * @return generate XML text if successful, null otherwise
	 */
	String writeToXmlString(ICStorageElement root);


}
