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

import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.eclipse.cdt.core.settings.model.XmlStorageUtil;
//import org.eclipse.cdt.core.settings.model.util.XmlStorageElement;
import org.w3c.dom.Document;

//import com.lembed.lite.studio.device.core.data.ICpItem;
//import com.lembed.lite.studio.device.core.info.CpConfigurationInfo;

/**
 *  Simple parser to read ICpConfigurationInfo from a file 
 */
public class CStorageConfigParser extends CStorageXmlParser {

	/**
	 *  Default constructor
	 */
	public CStorageConfigParser() {
	}

	/**
	 * @param xsdFile schema file
	 */
	public CStorageConfigParser(String xsdFile) {
		super(xsdFile);
	}

	@Override
	public ICStorageElement createRootElement(String tag) {
		Document domDoc = docBuilder.newDocument();
		return XmlStorageUtil.createCStorageTree(domDoc);
	}
	
}
