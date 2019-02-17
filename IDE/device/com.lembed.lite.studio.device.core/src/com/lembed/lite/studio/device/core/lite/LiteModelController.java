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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpEnvironmentProvider;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.CpPackFilter;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpGenerator;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.events.LiteEventProxy;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpPackFilterInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependencyItem;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.core.lite.packs.ILitePack;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackCollection;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackFamily;
import com.lembed.lite.studio.device.core.lite.packs.LitePackCollection;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.generic.IAttributes;

/**
 * Default implementation of ILiteModelController interface
 * 
 */
public abstract class LiteModelController extends LiteEventProxy implements ILiteModelController {

	protected ILiteModel fModel = null;
	// filtered Packs
	protected ICpPackFilter fSavedPackFilter = null;
	protected ICpPackFilter fCurrentPackFilter = null;
	protected ILitePackCollection fLitePackCollection = null;

	protected IAttributes fSavedDeviceAttributes = null;
	protected Set<String> fSavedComponentKeys = null;
	protected Set<String> fSavedGpdscFiles = null;

	protected boolean fbComponentSelectionModified = false;
	protected boolean fbPackFilterModified = false;
	protected boolean fbDeviceModified = false;

	/**
	 * Default constructor
	 */
	public LiteModelController(ILiteModel model) {
		fModel = model;
	}

	@Override
	public ILiteModel getModel() {
		return fModel;
	}

	// @Override
	@Override
	public void clear() {
		if (fModel != null) {
			fModel.clear();
		}
		fModel = null;
		fSavedPackFilter = null;
		fCurrentPackFilter = null;
		fLitePackCollection = null;
		fSavedDeviceAttributes = null;
		fSavedComponentKeys = null;
		fSavedGpdscFiles = null;
	}

	@Override
	public boolean isComponentSelectionModified() {
		return fbComponentSelectionModified;
	}

	@Override
	public boolean isPackFilterModified() {
		return fbPackFilterModified;
	}

	@Override
	public boolean isDeviceModified() {
		return fbDeviceModified;
	}

	protected boolean isGpdscFileListModified() {
		Map<String, ICpPack> genPacks = getGeneratedPacks();

		if (fSavedGpdscFiles == null){
			return genPacks != null && !genPacks.isEmpty();
		}
		if (genPacks == null){
			return fSavedGpdscFiles != null && !fSavedGpdscFiles.isEmpty();
		}

		return !fSavedGpdscFiles.equals(genPacks.keySet());

	}

	protected Set<String> collectGpdscFiles() {
		Map<String, ICpPack> genPacks = getGeneratedPacks();
		if (genPacks == null || genPacks.isEmpty()) {
			return null;
		}
		return new HashSet<String>(genPacks.keySet());
	}

	@Override
	public boolean isModified() {
		return isDeviceModified() || isPackFilterModified() || isComponentSelectionModified();
	}

	protected boolean checkIfComponentsModified() {
		Set<String> keys = collectComponentKeys();
		return !keys.equals(fSavedComponentKeys);
	}

	protected Set<String> collectComponentKeys() {
		Set<String> ids = new HashSet<String>();
		ICpConfigurationInfo info = getConfigurationInfo();
		collectComponentKeys(ids, info.getGrandChildren(CmsisConstants.COMPONENTS_TAG));
		collectComponentKeys(ids, info.getGrandChildren(CmsisConstants.APIS_TAG));
		return ids;
	}

	static protected void collectComponentKeys(Set<String> ids, Collection<? extends ICpItem> children) {
		if (children == null || children.isEmpty()) {
			return;
		}
		for (ICpItem child : children) {
			if (!(child instanceof ICpComponentInfo)) {
				continue;
			}
			ICpComponentInfo ci = (ICpComponentInfo) child;
			String key = ci.getName() + ':' + ci.getAttribute(CmsisConstants.INSTANCES);
			if (ci.isVersionFixed()) {
				key += ':' + ci.getVersion();
			}
			ids.add(key);
		}
	}

	@Override
	public void reloadPacks() {
		collectPacks();
		fLitePackCollection.setPackFilterInfo(fModel.getConfigurationInfo().getPackFilterInfo());
		update();
	}

	protected void collectPacks() {
		ICpPackCollection allPacks = null;
		ICpPackManager pm = CpPlugIn.getPackManager();
		if (pm != null) {
			allPacks = pm.getInstalledPacks();
		}
		fLitePackCollection = new LitePackCollection();
		if (allPacks != null) {
			fLitePackCollection.addCpItem(allPacks);
		}
	}

	@Override
	public void setConfigurationInfo(ICpConfigurationInfo info) {
		if (info == null) {
			clear();
			return;
		}

		fSavedPackFilter = new CpPackFilter(info.createPackFilter());
		fCurrentPackFilter = new CpPackFilter(fSavedPackFilter);
		fSavedDeviceAttributes = new Attributes(info.getDeviceInfo().attributes());
		collectPacks();

		fLitePackCollection.setPackFilterInfo(info.getPackFilterInfo());
		fModel.setConfigurationInfo(info); // will update used packs
		fLitePackCollection.setUsedPacks(getUsedPackInfos());
		fSavedComponentKeys = collectComponentKeys(); // initial update
		fSavedGpdscFiles = collectGpdscFiles();// initial update
	}

	@Override
	public void updateConfigurationInfo() {
		if (getConfigurationInfo() == null) {
			return;
		}
		if (setPackFilter(fCurrentPackFilter)) {
			update();
		} else {
			updateComponentInfos();
		}
	}

	@Override
	public void updateComponentInfos() {
		fModel.updateComponentInfos();
		fLitePackCollection.setUsedPacks(getUsedPackInfos());
	}

	public void update() {
		update(LiteConstants.NONE);
	}

	@Override
	public void update(int flags) {
		updateComponentInfos();
		updatePackFilterInfo();
		fModel.update(flags);
		fLitePackCollection.setUsedPacks(getUsedPackInfos());
		emitLiteEvent(LiteEvent.CONFIGURATION_MODIFIED, this);
	}

	@Override
	public void commit() {

		ICpConfigurationInfo info = fModel.getConfigurationInfo();
		if (info != null) {
			setSavedFlags(info.getGrandChildren(CmsisConstants.COMPONENTS_TAG));
			setSavedFlags(info.getGrandChildren(CmsisConstants.APIS_TAG));
		}
		if (isGpdscFileListModified()) {
			update();
		} else {
			updateConfigurationInfo();
		}

		fModel.getComponents().purge();
		fLitePackCollection.purge();
		fSavedPackFilter = new CpPackFilter(getPackFilter());
		fCurrentPackFilter = new CpPackFilter(fSavedPackFilter);
		fSavedDeviceAttributes = new Attributes(getDeviceInfo().attributes());
		fSavedComponentKeys = collectComponentKeys();
		fSavedGpdscFiles = collectGpdscFiles();
		fbComponentSelectionModified = false;
		fbPackFilterModified = false;
		fbDeviceModified = false;
		emitLiteEvent(LiteEvent.CONFIGURATION_COMMITED, this);
	}

	protected void setSavedFlags(Collection<? extends ICpItem> children) {

		if (children != null) {
			for (ICpItem item : children) {
				if (item instanceof ICpComponentInfo) {
					ICpComponentInfo ci = (ICpComponentInfo) item;
					ci.setSaved(true);
				}
			}
		}
	}

	@Override
	public ILitePackCollection getLitePackCollection() {
		return fLitePackCollection;
	}

	/**
	 * Returns absolute gpdsc filename associated with component
	 * 
	 * @param component
	 *            {@link ILiteComponent}
	 * @return associated gpdsc file or null if none
	 */
	protected String getGpdsc(ILiteComponent component) {
		if (component == null){
			return null;
		}
		ICpComponent c = component.getActiveCpComponent();
		if (c == null){
			return null;
		}

		if (c.isGenerated()) {
			ICpPack pack = c.getPack();
			if (pack == null){
				return null; // should not happen
			}
			return pack.getFileName();
		}
		ICpGenerator gen = c.getGenerator();
		if (gen != null) {
			ICpEnvironmentProvider ep = CpPlugIn.getEnvironmentProvider();
			return ep.expandString(gen.getGpdsc(), getConfigurationInfo(), true);
		}

		return null;
	}

	@Override
	public void selectComponent(ILiteComponent component, int nInstances) {
		if (component == null){
			return;
		}
		ICpComponent old = component.getActiveCpComponent();
		fModel.selectComponent(component, nInstances);
		postChangeSelection(component, old);
	}

	@Override
	public void selectActiveVariant(ILiteComponentItem item, String variant) {
		if (item == null){
			return;
		}
		ICpComponent old = item.getActiveCpComponent();
		item.setActiveVariant(variant);
		postChangeSelection(item, old);
	}

	@Override
	public void selectActiveVendor(ILiteComponentItem item, String vendor) {
		if (item == null){
			return;
		}
		ICpComponent old = item.getActiveCpComponent();
		item.setActiveVendor(vendor);
		postChangeSelection(item, old);
	}

	@Override
	public void selectActiveVersion(ILiteComponentItem item, String version) {
		if (item == null){
			return;
		}
		ICpComponent old = item.getActiveCpComponent();
		item.setActiveVersion(version);
		postChangeSelection(item, old);
	}

	protected void postChangeSelection(ILiteComponentItem item, ICpComponent oldComponent) {
		if (item instanceof ILiteComponent) {
			ILiteComponent component = (ILiteComponent) item;
			String genId = component.getGeneratorId();
			String oldGenId = oldComponent != null ? oldComponent.getGeneratorId() : null;
			if (oldGenId != null && !oldGenId.equals(genId)) {
				adjustGeneratedSelection(component, oldGenId, false);
			}
			if (genId != null) {
				adjustGeneratedSelection(component, genId, component.isSelected());
			}
		}
		updateComponentInfos();
		evaluateComponentDependencies();
	}

	protected void adjustGeneratedSelection(ILiteComponent component, String genId, boolean bSelect) {
		ILiteComponentItem componentRoot = getComponents();
		Collection<ILiteComponent> componentsToSelect = componentRoot.getGeneratorComponents(genId, null);
		if (componentsToSelect == null || componentsToSelect.isEmpty()){
			return;
		}
		int count = bSelect ? 1 : 0;
		for (ILiteComponent c : componentsToSelect) {
			if (c == component){
				continue;
			}
			c.setSelected(count);
		}
	}

	protected void emitComponentSelectionModified() {
		fbComponentSelectionModified = checkIfComponentsModified();
		emitLiteEvent(LiteEvent.COMPONENT_SELECTION_MODIFIED, this);
	}

	protected void emitPackFilterModified() {
		fCurrentPackFilter = fLitePackCollection.createPackFiler();
		fbPackFilterModified = !fSavedPackFilter.equals(fCurrentPackFilter);
		emitLiteEvent(LiteEvent.FILTER_MODIFIED, this);
	}

	@Override
	public EEvaluationResult resolveComponentDependencies() {
		EEvaluationResult res = fModel.resolveComponentDependencies();
		updateComponentInfos();
		emitComponentSelectionModified();
		return res;
	}

	@Override
	public ICpPackFilter getPackFilter() {
		return fModel.getPackFilter();
	}

	@Override
	public boolean setPackFilter(ICpPackFilter filter) {
		return fModel.setPackFilter(filter);
	}

	@Override
	public ICpDeviceItem getDevice() {
		return fModel.getDevice();
	}

	@Override
	public ICpDeviceInfo getDeviceInfo() {
		return fModel.getDeviceInfo();
	}

	@Override
	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		boolean changed = false;
		int updateFlags = LiteConstants.NONE;
		if (getDeviceInfo() == null) {
			changed = true;
		} else {
			changed = !getDeviceInfo().attributes().equals(deviceInfo.attributes());
			if (changed){
				updateFlags = LiteConstants.COMPONENT_IGNORE_ALL;
			}
		}

		if (changed) {
			fbDeviceModified = !fSavedDeviceAttributes.equals(deviceInfo.attributes());
			fModel.setDeviceInfo(deviceInfo);
			update(updateFlags);
		}
	}

	@Override
	public ICpItem getToolchainInfo() {
		return fModel.getToolchainInfo();
	}

	@Override
	public ICpConfigurationInfo getConfigurationInfo() {
		return fModel.getConfigurationInfo();
	}

	@Override
	public ILiteComponentItem getComponents() {
		return fModel.getComponents();
	}

	@Override
	public EEvaluationResult evaluateComponentDependencies() {
		EEvaluationResult res = fModel.evaluateComponentDependencies();
		emitComponentSelectionModified();
		return res;
	}

	@Override
	public EEvaluationResult getEvaluationResult() {
		return fModel.getEvaluationResult();
	}

	@Override
	public EEvaluationResult getEvaluationResult(ILiteComponentItem item) {
		return fModel.getEvaluationResult(item);
	}

	@Override
	public void setEvaluationResult(EEvaluationResult result) {
		fModel.setEvaluationResult(result);
	}

	@Override
	public Collection<ILiteComponent> getSelectedComponents() {
		return fModel.getSelectedComponents();
	}

	@Override
	public Collection<ILiteComponent> getUsedComponents() {
		return fModel.getUsedComponents();
	}

	@Override
	public Map<String, ICpPackInfo> getUsedPackInfos() {
		return fModel.getUsedPackInfos();
	}

	@Override
	public Collection<? extends ILiteDependencyItem> getDependencyItems() {
		return fModel.getDependencyItems();
	}

	// @Override
	public void updatePackFilterInfo() {
		ICpPackFilterInfo packFilterInfo = fLitePackCollection.createPackFilterInfo();
		ICpConfigurationInfo confInfo = getConfigurationInfo();
		packFilterInfo.setParent(confInfo);
		confInfo.replaceChild(packFilterInfo);
	}

	@Override
	public void selectPack(ILitePack pack, boolean select) {
		if (pack != null) {
			pack.setSelected(select);
			ILitePackFamily family = pack.getFamily();
			if (family != null) {
				family.updateVersionMatchMode();
			}
			emitPackFilterModified();
		}
	}

	@Override
	public void setVesrionMatchMode(ILitePackFamily packFamily, EVersionMatchMode mode) {
		if (packFamily != null) {
			packFamily.setVersionMatchMode(mode);
			emitPackFilterModified();
		}
	}

	@Override
	public boolean isUseAllLatestPacks() {
		return fLitePackCollection.isUseAllLatestPacks();
	}

	@Override
	public void setUseAllLatestPacks(boolean bUseLatest) {
		fLitePackCollection.setUseAllLatestPacks(bUseLatest);
		emitPackFilterModified();
	}

	@Override
	public ILiteDeviceItem getDevices() {
		return fModel.getDevices();
	}

	@Override
	public Map<String, ICpPack> getGeneratedPacks() {
		return fModel.getGeneratedPacks();
	}

	@Override
	public ICpPack getGeneratedPack(String gpdsc) {
		return fModel.getGeneratedPack(gpdsc);
	}

	@Override
	public boolean isGeneratedPackUsed(String gpdsc) {
		return fModel.isGeneratedPackUsed(gpdsc);
	}
}
