/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Lembed Electronic - for LiteSTUDIO
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.data;

import com.lembed.lite.studio.core.LiteProjectPacksStorage;
import com.lembed.lite.studio.core.EclipseUtils;

import java.util.Map;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;


/**
 * Lite Project Attributes utils
 * @author Lembed
 *
 */
public class LiteProjectAttributes {

	/**
	 * Get the value of a given attribute.
	 *
	 * @param configuration ILaunchConfiguration
	 * @param attributeName String
	 * @return attribute value or null.
	 */
	public static String getCmsisAttribute(ILaunchConfiguration configuration, String attributeName) {

		// Get the build configuration description from the launch configuration
		ICConfigurationDescription cConfigDescription = EclipseUtils.getBuildConfigDescription(configuration);

		String atttributeValue = null;
		if (cConfigDescription != null) {
			// GdbActivator.log(cConfigDescription);

			// The next step is to get the CDT configuration.
			IConfiguration config = EclipseUtils.getConfigurationFromDescription(cConfigDescription);
			// GdbActivator.log(config);

			// The custom storage is specific to the CDT configuration.
			LiteProjectExtraDataManagerProxy dataManager = LiteProjectExtraDataManagerProxy.getInstance();
			Map<String, String> propertiesMap = dataManager.getExtraProperties(config);
			if (propertiesMap != null) {
				atttributeValue = propertiesMap.get(attributeName);
			}

			// GdbActivator.log("CMSIS device name: " + cmsisDeviceName
			// + ", config: " + config + "/"
			// + config.getArtifactName() + ", launch: "
			// + configuration);
		}
		return atttributeValue;
	}

	/**
	 * @param configuration ILaunchConfiguration
	 * @param attributeName String
	 * @return String
	 */
	public static String storageAttribute(ILaunchConfiguration configuration, String attributeName) {

		// Get the build configuration description from the launch configuration
		ICConfigurationDescription cConfigDescription = EclipseUtils.getBuildConfigDescription(configuration);

		String atttributeValue = null;
		if (cConfigDescription != null) {
			ICProjectDescription projDesc  = cConfigDescription.getProjectDescription();

			atttributeValue = storageAttribute(projDesc, LiteProjectPacksStorage.OPTION_DEVICE, attributeName);
		}
		return atttributeValue;
	}

	/**
	 * @param config IConfiguration
	 * @param option String
	 * @param attributeName String
	 * @return String
	 */
	public static String storageAttribute(IConfiguration config, String option, String attributeName) {

		String atttributeValue = null;

		try {
			LiteProjectPacksStorage storage = new LiteProjectPacksStorage(config);
			atttributeValue = storage.getAttribute(option, attributeName);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return atttributeValue;
	}

	/**
	 * @param projDesc     ICProjectDescription
	 * @param option       String
	 * @param attributeName String
	 * @return String
	 */
	public static String storageAttribute(ICProjectDescription projDesc, String option, String attributeName) {

		String atttributeValue = null;
		
		LiteProjectPacksStorage storage = new LiteProjectPacksStorage(projDesc);
		atttributeValue = storage.getAttribute(option, attributeName);
		
		return atttributeValue;
	}

	/**
	 * @param configuration ILaunchConfiguration
	 * @return String
	 */
	public static String getCmsisDeviceName(ILaunchConfiguration configuration) {

		return storageAttribute(configuration, LiteProjectPacksStorage.DEVICE_NAME);
	}

	/**
	 * @param configuration ILaunchConfiguration
	 * @return String
	 */
	public static String getCmsisBoardName(ILaunchConfiguration configuration) {

		return storageAttribute(configuration, LiteProjectPacksStorage.BOARD_NAME);
	}




	/**
	 * @param configuration IConfiguration
	 * @return String
	 */
	public static String getCmsisDeviceName(IConfiguration configuration) {

		return storageAttribute(configuration, LiteProjectPacksStorage.OPTION_DEVICE, LiteProjectPacksStorage.DEVICE_NAME);
	}

	/**
	 * @param config IConfiguration
	 * @return String
	 */
	public static String getCmsisVenderId(IConfiguration config) {

		String Nameid = storageAttribute(config, LiteProjectPacksStorage.OPTION_DEVICE, LiteProjectPacksStorage.DEVICE_VENDOR_ID);
		String id = null;

		if (Nameid.contains(":")) { //$NON-NLS-1$
			id = Nameid.split(":")[1]; //$NON-NLS-1$
		}

		if (id != null) {
			return id;
		}
        return Nameid;
	}



	/**
	 * @param projDesc ICProjectDescription
	 * @return String
	 */
	public static String getCmsisVenderId(ICProjectDescription projDesc) {
		String Nameid = storageAttribute(projDesc, LiteProjectPacksStorage.OPTION_DEVICE, LiteProjectPacksStorage.DEVICE_VENDOR_ID);
		String id = null;

		if (Nameid.contains(":")) { //$NON-NLS-1$
			id = Nameid.split(":")[1]; //$NON-NLS-1$
		}

		if (id != null) {
			return id;
		}
        return Nameid;
	}

	/**
	 * @param projDesc ICProjectDescription
	 * @return String
	 */
	public static String getCmsisDeviceName(ICProjectDescription projDesc) {
		return storageAttribute(projDesc, LiteProjectPacksStorage.OPTION_DEVICE, LiteProjectPacksStorage.DEVICE_NAME);
	}

}
