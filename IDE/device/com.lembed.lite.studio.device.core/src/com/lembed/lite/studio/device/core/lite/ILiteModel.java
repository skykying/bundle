/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.core.lite;

import java.util.Collection;
import java.util.Map;

import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.enums.IEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependencyItem;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;

/**
 * Interface responsible for manipulating Run-Time-Configuration of a project
 * for selected device and toolchain. It can be considered as a controller that
 * connects CMSIS-Pack items with project model
 * 
 */
public interface ILiteModel extends IEvaluationResult {

	/**
	 * Clears the model
	 */
	void clear();

	/**
	 * Updates the model with using default flags to resolve components by
	 * calling <code>update(RteConstants.NONE)</code>
	 */
	void update();

	/**
	 * Updates the model
	 * 
	 * @param flags
	 *            a combination of COMPONENT__* flags
	 * @see LiteConstants
	 */
	void update(int flags);

	/**
	 * Returns pack filter associated with model
	 * 
	 * @return ICpPackFilter
	 */
	ICpPackFilter getPackFilter();

	/**
	 * Sets pack filter for the model
	 * 
	 * @param filter
	 *            ICpPackFilter to set
	 * @return true if new filter is not equal to previous one
	 */
	boolean setPackFilter(ICpPackFilter filter);

	/**
	 * Returns actual device item used by the model
	 * 
	 * @return ICpDeviceItem
	 */
	ICpDeviceItem getDevice();

	/**
	 * Returns device info used by in this model
	 * 
	 * @return ICpDeviceInfo
	 */
	ICpDeviceInfo getDeviceInfo();

	/**
	 * Sets device info to be used by the model
	 * 
	 * @param deviceInfo
	 *            device info to set
	 */
	void setDeviceInfo(ICpDeviceInfo deviceInfo);

	/**
	 * Returns toolchain information as generic IcpItem with "Tcompiler" and
	 * "Toutput" attributes
	 * 
	 * @return ICpItem describing toolchain info
	 */
	ICpItem getToolchainInfo();

	/**
	 * Returns serializable model object
	 * 
	 * @return ICpConfigurationInfo in its current state
	 */
	ICpConfigurationInfo getConfigurationInfo();

	/**
	 * Sets configuration data to the model
	 * 
	 * @param info
	 *            ICpConfigurationInfo to set
	 */
	void setConfigurationInfo(ICpConfigurationInfo info);

	/**
	 * Returns filtered component tree
	 * 
	 * @return IRteComponentItem representing component tree root
	 */
	ILiteComponentItem getComponents();

	/**
	 * Sets, resets or changes component selection. <br>
	 * If selection state has changed re-evaluates dependencies
	 * 
	 * @param component
	 *            to set, reset or change selection selection
	 * @param nInstances
	 *            number of instances to select, 0 to reset selection
	 */
	void selectComponent(ILiteComponent component, int nInstances);

	/**
	 * Evaluates dependencies of selected components
	 * 
	 * @return dependency evaluation result
	 */
	EEvaluationResult evaluateComponentDependencies();

	/**
	 * Tries to resolve component dependencies
	 * 
	 * @return evaluation result after dependency resolving
	 */
	EEvaluationResult resolveComponentDependencies();

	/**
	 * Returns dependency evaluation result for given item (class, group or
	 * component)
	 * 
	 * @param item
	 *            IRteComponentItem for which to get result
	 * @return condition result or IGNORED if item has no result
	 */
	EEvaluationResult getEvaluationResult(ILiteComponentItem item);

	/**
	 * Returns collection of selected components
	 * 
	 * @return collection of selected components
	 */
	Collection<ILiteComponent> getSelectedComponents();

	/**
	 * Returns collection of used components
	 * 
	 * @return collection of used components
	 */
	Collection<ILiteComponent> getUsedComponents();

	/**
	 * Returns packs currently used by configuration
	 * 
	 * @return map id to ICpPackInfo
	 */
	Map<String, ICpPackInfo> getUsedPackInfos();

	/**
	 * Returns collection of dependency results (items and dependencies)
	 * 
	 * @return collection of dependency results
	 */
	Collection<? extends ILiteDependencyItem> getDependencyItems();

	/**
	 * Updates ICpConfigurationInfo according to selected components
	 */
	void updateComponentInfos();

	/**
	 * Returns hierarchical collection of devices available for this target
	 * 
	 * @return root of device tree as IRteDeviceItem
	 */
	ILiteDeviceItem getDevices();

	/**
	 * Return collection of used generated packs, an entry can be
	 * <code>null</code> if a gpdsc is not loaded
	 * 
	 * @return map absolute filename -> pack, null or empty if no generated
	 *         packs used
	 */
	Map<String, ICpPack> getGeneratedPacks();

	/**
	 * Returns {@link ICpPack} loaded from given gpdsc file
	 * 
	 * @param gpdsc
	 *            absolute gpdsc file name
	 * @return loaded {@link ICpPack} or null if such file does not exists or
	 *         load failed
	 */
	ICpPack getGeneratedPack(String gpdsc);

	/**
	 * Checks if the model requires given gpdsc file
	 * 
	 * @param gpdsc
	 *            absolute gpdsc file name
	 * @return true if given gpdsc file is needed
	 */
	boolean isGeneratedPackUsed(String gpdsc);
}