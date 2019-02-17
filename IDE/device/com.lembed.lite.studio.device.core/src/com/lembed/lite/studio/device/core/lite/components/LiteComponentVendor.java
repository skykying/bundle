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

package com.lembed.lite.studio.device.core.lite.components;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;
import com.lembed.lite.studio.device.utils.VersionComparator;

/**
 * Class represent Cvendor hierarchy level, contains collection of variants.
 * Direct child for bundles and components   
 */
public class LiteComponentVendor extends LiteComponentItem {

	/**
	 * @param parent
	 */
	public LiteComponentVendor(ILiteComponentItem parent, String name) {
		super(parent, name);
		fComponentAttribute = EComponentAttribute.CVERSION;
	}

	@Override
	public Map<String, ILiteComponentItem> createMap() {
		// versions are sorted in descending order  
		return new TreeMap<String, ILiteComponentItem>(new VersionComparator());
	}

	@Override
	public Collection<String> getVersionStrings() {
		ILiteComponent component = getParentComponent();
		if(component != null && component.hasBundle() ) {
			return null;
		}
		return getKeys();
	}

	@Override
	public String getDefaultChildName() {
		return CpStrings.LiteComponentVersionLatest;
	}

	@Override
	public String getActiveVersion() {
		return getActiveChildName();
	}

	
	@Override
	public void setActiveVersion(String version) {
		setActiveChild(version);
	}

	@Override
	public boolean isUseLatestVersion() {
		return isActiveChildDefault();
	}
	
}
