/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    keven - initial version
 *******************************************************************************/

package com.lembed.lite.studio.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

@SuppressWarnings("restriction")
public class LiteProjectPacksStorage {

	// ------------------------------------------------------------------------

	//public static final String STORAGE_NAME = "com.lembed.lite.studio.managedbuild.packs";
	
	public static final String LITE_STORAGE = "com.lembed.lite.studio.arm.project"; //$NON-NLS-1$

	public static final String DEVICE_NAME = "Dname"; //$NON-NLS-1$
	public static final String DEVICE_VENDOR_NAME = "Dvendor"; //$NON-NLS-1$
	public static final String DEVICE_VENDOR_ID = "Dvendor"; //$NON-NLS-1$
	
	public static final String OPTION_DEVICE = "device"; //$NON-NLS-1$
	public static final String OPTION_CONFIGURE = "configuration"; //$NON-NLS-1$
	
	public static final String FAMILY_NAME = "Dfamily"; //$NON-NLS-1$
	public static final String SUBFAMILY_NAME = "DsubFamily"; //$NON-NLS-1$
	public static final String CORE_NAME = "Dcore"; //$NON-NLS-1$
	public static final String COMPILER_DEFINE = "Tcompiler"; //$NON-NLS-1$

	public static final String DEVICE_PACK_VENDOR = "cmsis.device.pack.vendor";
	public static final String DEVICE_PACK_NAME = "cmsis.device.pack.name";
	public static final String DEVICE_PACK_VERSION = "cmsis.device.pack.version";

	public static final String BOARD_NAME = "cmsis.board.name";
	public static final String BOARD_REVISION = "cmsis.board.revision";
	public static final String BOARD_VENDOR_NAME = "cmsis.board.vendor.name";
	public static final String BOARD_CLOCK = "cmsis.board.clock";

	public static final String BOARD_PACK_VENDOR = "cmsis.board.pack.vendor";
	public static final String BOARD_PACK_NAME = "cmsis.board.pack.name";
	public static final String BOARD_PACK_VERSION = "cmsis.board.pack.version";

	// ------------------------------------------------------------------------

	private ICStorageElement fStorage;
	private Configuration fConfig;

	// ------------------------------------------------------------------------

	/**
	 * Create a new custom storage for packs related config options. The new
	 * storage is specific for each build configuration, so it is created inside
	 * the "cconfiguration" element.
	 * 
	 * @param config
	 *            a Configuration object (like Debug/Release).
	 * @throws CoreException
	 */
	public LiteProjectPacksStorage(IConfiguration config) throws CoreException {

		if (config instanceof Configuration) {
			fConfig = (Configuration) config;
			ICConfigurationDescription configDesc = fConfig.getConfigurationDescription();
			if(configDesc != null) {
				fStorage = configDesc.getStorage(LITE_STORAGE, false);
			}
			
		} else {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID,
					"ConfigStorage() requires Configuration"));
		}
	}

	public LiteProjectPacksStorage(ICProjectDescription projDesc) {
		
			try {
				if(projDesc != null) {
					fStorage = projDesc.getStorage(LITE_STORAGE, false);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Retrieve a map with all properties.
	 * 
	 * @return a map of strings.
	 */
	public Map<String, String> getOptions() {

		assert (fStorage != null);

		Map<String, String> map = new HashMap<String, String>();
		for (ICStorageElement child : fStorage.getChildrenByName("option")) {
			if (child.hasAttribute("id")) {
				map.put(child.getAttribute("id"), child.getAttribute("value"));
			}
		}

		return map;
	}

	public String getAttribute(String name, String attribute) {
		String att = null;
		
		if(fStorage!=null) {
			List<ICStorageElement> all = getAllChildren(fStorage);
			if(all != null) {
				for (ICStorageElement child : all) {
					
					if(child.hasAttribute(attribute)) {
						att = child.getAttribute(attribute);
					}
				}
			}			
		}
		
		return att;
	}
	
	public List<ICStorageElement> getAllChildren(ICStorageElement storage) {
		Objects.requireNonNull(storage, "Storage must not be null");
		
		List<ICStorageElement> map = new LinkedList<ICStorageElement>();			
		ICStorageElement[] children = storage.getChildren();
		for(ICStorageElement child : children) {
			map.add(child);
			if(child.hasChildren()) {
				map.addAll(getAllChildren(child));
			}
		}
		
		return map;
	}
	 
	/**
	 * Retrieve the value of an option.
	 * 
	 * @param id
	 *            a string uniquely identifying the option
	 * @return its value or null, if not found
	 */
	public String getOption(String id) {
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(fStorage, "Storage must not be null");
	
		for (ICStorageElement child : fStorage.getChildrenByName("option")) {

			if (child.hasAttribute("id") && id.equals(child.getAttribute("id"))) {
				return child.getAttribute("value");
			}
		}

		return null;
	}

	public String getOption(String id, String defaultValue) {

		String result = getOption(id);
		if (result != null && result.length() > 0) {
			return result;
		}

		return defaultValue;
	}

	/**
	 * Store the value of an option.
	 * 
	 * @param id
	 *            a string uniquely identifying the option
	 * @param value
	 *            a string to be assigned as option value; if null, the empty
	 *            string is assigned
	 */
	public void setOption(String id, String value) {

		assert (id != null);
		assert (fStorage != null);

		ICStorageElement option = null;
		for (ICStorageElement child : fStorage.getChildrenByName("option")) {

			if (id.equals(child.getAttribute("id"))) {
				if (option == null) {
					// Remember first occurrence
					option = child;
				} else {
					// remove possible duplicates
					fStorage.removeChild(child);
				}
			}
		}

		if (option == null) {
			option = fStorage.createChild("option");
			option.setAttribute("id", id);
		}
		if (value == null) {
			value = "";
		} else {
			value = value.trim();
		}
		option.setAttribute("value", value);
	}

	/**
	 * Store the value of an option. If the value is null or empty, the
	 * operation is not performed..
	 * 
	 * @param id
	 *            a string uniquely identifying the option
	 * @param value
	 *            a string to be assigned as option value
	 */
	public void setNonEmptyOption(String id, String value) {

		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(value, "value must not be null");
		
		if (value != null) {
			value = value.trim();
			if (value.length() > 0) {
				setOption(id, value);
			}
		}
	}

	/**
	 * Store the description of a memory section.
	 * 
	 * @param section
	 *            a string with the section name, using the CMSIS convention
	 *            (like IRAM1, IROM1)
	 * @param start
	 *            a string with the hex value of the start address
	 * @param size
	 *            a string with the hex value of the size, in bytes
	 * @param startup
	 *            a string with 1 if the section will be used for startup (to
	 *            host the vectors table)
	 */
	public void setMemory(String section, String start, String size,
			String startup) {

		ICStorageElement memory = null;
		for (ICStorageElement child : fStorage.getChildrenByName("memory")) {

			if (section.equals(child.getAttribute("section"))) {
				if (memory == null) {
					// Remember first occurrence
					memory = child;
				} else {
					// remove possible duplicates
					fStorage.removeChild(child);
				}
			}
		}

		if (memory == null) {
			memory = fStorage.createChild("memory");
			memory.setAttribute("section", section);
		}
		memory.setAttribute("start", start);
		memory.setAttribute("size", size);
		memory.setAttribute("startup", startup);
	}

	public Map<String, String[]> getMemoryMap() {

		Map<String, String[]> map = new TreeMap<String, String[]>();

		for (ICStorageElement child : fStorage.getChildrenByName("memory")) {

			String section = child.getAttribute("section");
			String arr[] = new String[] { section, child.getAttribute("start"),
					child.getAttribute("size"), child.getAttribute("startup") };
			map.put(section, arr);
		}

		return map;
	}

	// ------------------------------------------------------------------------
}
