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

package com.lembed.lite.studio.device.core.lite;

import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.events.ILiteEventProxy;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.core.lite.packs.ILitePack;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackCollection;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackFamily;
import com.lembed.lite.studio.device.generic.ICommitable;

/**
 * Interface represents a controller to edit underlying RTE Configuration.</br>
 * The interface also serves as a proxy to IRteModel to simplify its usage
 */
public interface ILiteModelController extends ICommitable, ILiteEventProxy, ILiteModel {

	/**
	 * Returns controllable RTE model
	 * 
	 * @return underlying IRteModel
	 */
	ILiteModel getModel();

	/**
	 * Returns RTE pack collection
	 * 
	 * @return IRtePackCollection
	 */
	ILitePackCollection getLitePackCollection();

	/**
	 * Check if the component selection in the model has been modified since
	 * last load/apply
	 * 
	 * @return true if modified
	 */
	boolean isComponentSelectionModified();

	/**
	 * Checks if pack filter is modified since lase commit
	 * 
	 * @return true if modified
	 */
	boolean isPackFilterModified();

	/**
	 * Checks if device has been changed or modified
	 * 
	 * @return true if modified
	 */
	boolean isDeviceModified();

	/**
	 * Updates configuration info based on current selection
	 */
	void updateConfigurationInfo();

	/**
	 * Fully reloads packs and updates loaded configuration
	 */
	void reloadPacks();

	/**
	 * Sets active variant or bundle for given parent item. <br>
	 * If selection state has changed re-evaluates dependencies and emits
	 * notification
	 * 
	 * @param item
	 *            for which to set active variant.
	 * @param variant
	 *            new active variant name to set
	 * @see ILiteComponentItem#setActiveVariant(String)
	 */
	void selectActiveVariant(ILiteComponentItem item, final String variant);

	/**
	 * Sets active vendor for given parent item. <br>
	 * If selection state has changed re-evaluates dependencies and emits
	 * notification
	 * 
	 * @param item
	 *            for which to set active variant.
	 * @param variant
	 *            new active vendor name to set
	 * @see ILiteComponentItem#setActiveVendor(String)
	 */
	void selectActiveVendor(ILiteComponentItem item, final String vendor);

	/**
	 * Sets active version for given parent item. <br>
	 * If selection state has changed re-evaluates dependencies and emits
	 * notification
	 * 
	 * @param item
	 *            for which to set active version.
	 * @param variant
	 *            new active version name to set
	 * @see ILiteComponentItem#setActiveVersion(String)
	 */
	void selectActiveVersion(ILiteComponentItem item, final String version);

	/**
	 * Tries to resolve component dependencies
	 * 
	 * @return evaluation result after dependency resolving
	 */
	EEvaluationResult resolveComponentDependencies();

	/**
	 * Explicitly selects specified pack
	 * 
	 * @param pack
	 *            IRtePack to select
	 * @param select
	 *            selection flag
	 */
	void selectPack(ILitePack pack, boolean select);

	/**
	 * Explicitly sets version match mode to specified pack family
	 * 
	 * @param packFamily
	 *            pack family to set mode to
	 * @param mode
	 *            EVersionMatchMode to set
	 */
	void setVesrionMatchMode(ILitePackFamily packFamily, EVersionMatchMode mode);

	/**
	 * Check is to latest versions of all installed packs
	 * 
	 * @return true if the latest versions of packs should be used
	 */
	boolean isUseAllLatestPacks();

	/**
	 * Sets if the model should use the latest versions of all installed packs
	 * 
	 * @param bUseLatest
	 *            flag if to use latest
	 */
	void setUseAllLatestPacks(boolean bUseLatest);

	/**
	 * Opens an URL in a browser or associated system editor
	 * 
	 * @param url
	 *            URL to open
	 * @return null if successfully opened, otherwise reason why operation
	 *         failed
	 */
	String openUrl(String url);

}
