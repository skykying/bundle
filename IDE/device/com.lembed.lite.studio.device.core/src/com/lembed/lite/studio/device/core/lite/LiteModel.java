/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* Copyright (c) 2017 LEMBED
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* LEMBED - adapter for LiteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.device.core.lite;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpEnvironmentProvider;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.CpConditionContext;
import com.lembed.lite.studio.device.core.data.CpItem;
import com.lembed.lite.studio.device.core.data.CpPackFilter;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpConditionContext;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.data.ICpGenerator;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.data.ICpTaxonomy;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.info.CpComponentInfo;
import com.lembed.lite.studio.device.core.info.CpFileInfo;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.core.info.ICpPackFilterInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentGroup;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.core.lite.components.LiteComponentRoot;
import com.lembed.lite.studio.device.core.lite.components.LiteMoreClass;
import com.lembed.lite.studio.device.core.lite.components.LiteSelectedDeviceClass;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependencyItem;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependencySolver;
import com.lembed.lite.studio.device.core.lite.dependencies.LiteDependencySolver;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.core.lite.devices.LiteDeviceItem;
import com.lembed.lite.studio.device.utils.Utils;

/**
 * Default implementation of ILiteModel interface
 *
 */
public class LiteModel implements ILiteModel {

	// object to store/load configuration meta data
	protected ICpConfigurationInfo fConfigurationInfo = null;

	// filtered Packs
	protected ICpPackCollection fAllPacks = null;
	protected Collection<ICpPack> fFilteredPacks = null;
	protected ICpPackFilter fPackFilter = null;
	protected Map<String, ICpPackInfo> fUsedPackInfos = null;

	// selected device
	protected ICpDeviceInfo fDeviceInfo = null;
	// selected toolchain
	protected ICpItem fToolchainInfo = null;

	// component filter
	protected ICpConditionContext fComponentFilter = null;

	// filtered components tree
	protected LiteComponentRoot fComponentRoot = null;
	// filtered device tree
	protected ILiteDeviceItem fLiteDevices = null;

	// engine to evaluate/resolve component dependencies
	protected ILiteDependencySolver fDependencySolver = null;

	protected Map<String, ICpPack> fGeneratedPacks = null; 
	// read from configuration

	/**
	 * Default constructor
	 */
	public LiteModel() {
		fDependencySolver = new LiteDependencySolver(this);
	}

	@Override
	public void clear() {
		fAllPacks = null;
		fLiteDevices = null;
		fComponentRoot = null;
		fPackFilter = null;
		fFilteredPacks = null;
		fGeneratedPacks = null;
		fDeviceInfo = null;
		fToolchainInfo = null;
		fConfigurationInfo = null;
	}

	@Override
	public ICpConfigurationInfo getConfigurationInfo() {
		return fConfigurationInfo;
	}

	@Override
	public Map<String, ICpPack> getGeneratedPacks() {
		return fGeneratedPacks;
	}

	@Override
	public ICpPack getGeneratedPack(String gpdsc) {
		if (fGeneratedPacks != null){
			return fGeneratedPacks.get(gpdsc);
		}
		return null;
	}

	@Override
	public boolean isGeneratedPackUsed(String gpdsc) {
		if (fGeneratedPacks != null){
			return fGeneratedPacks.containsKey(gpdsc);
		}
		return false;
	}

	/**
	 * setConfigurationInfo to LiteModel and do necessory processes
	 * @param info ICpConfigurationInfo instance
	 */
	@Override
	public void setConfigurationInfo(ICpConfigurationInfo info) {
		fConfigurationInfo = info;
		if (fConfigurationInfo == null) {
			clear();
			return;
		}
		
		fDeviceInfo = info.getDeviceInfo();
		fToolchainInfo = info.getToolChainInfo();
		fPackFilter = new CpPackFilter(info.createPackFilter());

		// do default initialization
		update();
	}

	@Override
	public void update() {
		update(LiteConstants.NONE);
	}

	@Override
	public void update(int flags) {
		fLiteDevices = null;

		// collect packs
		collectPacks();

		// filter active packs
		filterPacks();

		// resolve filted packs
		resolveFilterPacks();

		// creates device tree
		getDevices(); 

		// resolve devices
		resolveDevice();

		// update component filter
		updateComponentFilter();

		// collect components
		collectComponents();

		// resolve components
		resolveComponents(flags);

		// update component informations
		updateComponentInfos();
	}

	protected void collectPacks() {
		fAllPacks = null;
		fGeneratedPacks = null;
		ICpPackManager pm = CpPlugIn.getPackManager();
		if (pm == null){
			return;
		}
		fAllPacks = pm.getInstalledPacks();
		// collect and load generated packs
		collectGeneratedPacks();
	}

	protected void collectGeneratedPacks() {
		fGeneratedPacks = new HashMap<String, ICpPack>();
		Collection<? extends ICpItem> children = fConfigurationInfo.getGrandChildren(CmsisConstants.COMPONENTS_TAG);
		if (children == null){
			return;
		}
		ICpPackManager pm = CpPlugIn.getPackManager();
		for (ICpItem item : children) {
			if (!item.hasAttribute(CmsisConstants.GENERATOR)){
				continue;
			}
			if (!(item instanceof ICpComponentInfo)){
				continue;
			}
			ICpComponentInfo ci = (ICpComponentInfo) item;
			if (ci.isGenerated()){
				// consider only bootstrap
				continue; 
			}
			String gpdsc = ci.getGpdsc(true);
			if (gpdsc == null || gpdsc.isEmpty()){
				continue;
			}
			if (fGeneratedPacks.containsKey(gpdsc)) {
				ICpPack pack = fGeneratedPacks.get(gpdsc);
				if (pack != null || !ci.isSaved()){
					continue;
				}
			}
			ICpPack pack = pm.loadGpdsc(gpdsc);
			fGeneratedPacks.put(gpdsc, pack);
		}
	}

	protected void filterPacks() {
		fFilteredPacks = null;
		if (fAllPacks != null) {
			fPackFilter.setLatestPackIDs(fAllPacks.getLatestPackIDs());
			fFilteredPacks = fAllPacks.getFilteredPacks(fPackFilter);
		}
	}

	protected boolean resolveFilterPacks() {
		if (fConfigurationInfo == null) {
			return false;
		}

		boolean allResolved = true;
		ICpPackFilterInfo packsItem = fConfigurationInfo.getPackFilterInfo();
		if (packsItem == null) {
			return allResolved;
		}

		Collection<? extends ICpItem> packInfos = packsItem.getChildren();
		if (packInfos == null) {
			return allResolved;
		}

		if (fAllPacks == null) {
			return false;
		}

		for (ICpItem item : packInfos) {
			if (!(item instanceof ICpPackInfo)) {
				continue;
			}
			ICpPackInfo packInfo = (ICpPackInfo) item;
			EVersionMatchMode mode = packInfo.getVersionMatchMode();
			ICpPack pack = null;
			switch (mode) {
			case FIXED:
				pack = fAllPacks.getPack(packInfo.getId());
				break;
			case EXCLUDED:
			case LATEST:
				pack = fAllPacks.getPack(packInfo.getPackFamilyId());
				break;
			}
			packInfo.setPack(pack);
			if (pack == null && mode != EVersionMatchMode.EXCLUDED) {
				allResolved = false;
			}
		}
		return allResolved;
	}

	protected ICpPack resolvePack(ICpPackInfo pi) {
		ICpPack pack = pi.getPack();
		if (pack != null) {
			return pack;
		}
		if (fAllPacks == null) {
			return null;
		}
		pack = fAllPacks.getPack(pi.getId());
		if (pack != null) {
			pi.setPack(pack);
		}

		return pack;
	}

	protected boolean resolveDevice() {
		if (fDeviceInfo == null) {
			return false;
		}
		fDeviceInfo.setLiteDevice(null);
		ILiteDeviceItem rteDevice = getDevices().findItem(fDeviceInfo.attributes());
		fDeviceInfo.setLiteDevice(rteDevice);
		ICpPackInfo packInfo = fDeviceInfo.getPackInfo();
		if (rteDevice == null) {
			resolvePack(packInfo);
		}
		ICpDeviceItem device = fDeviceInfo.getDevice();
		EEvaluationResult res = EEvaluationResult.FULFILLED;
		if (device == null) {
			if (packInfo.getPack() == null) {
				res = EEvaluationResult.FAILED;
			} else {
				res = EEvaluationResult.UNAVAILABLE_PACK;
			}
		}
		fDeviceInfo.setEvaluationResult(res);
		return device != null;
	}

	@Override
	public void updateComponentInfos() {

		if (fConfigurationInfo == null) {
			return;
		}

		ICpItem apiInfos = fConfigurationInfo.getApisItem();
		apiInfos.clear();

		ICpItem componentInfos = fConfigurationInfo.getComponentsItem();
		componentInfos.clear();

		fUsedPackInfos = new HashMap<String, ICpPackInfo>();
		ICpPackInfo devicePackInfo = fDeviceInfo.getPackInfo();
		addUsedPackInfo(devicePackInfo.getPackInfo());

		Map<ICpComponent, EVersionMatchMode> selectedApis = new HashMap<ICpComponent, EVersionMatchMode>();
		Collection<ILiteComponent> selectedComponents = getSelectedComponents();
		for (ILiteComponent component : selectedComponents) {
			ICpComponent c = component.getActiveCpComponent();
			if (c == null) {
				continue;
			}
			ICpComponentInfo ci = null;
			if (c instanceof ICpComponentInfo) {
				// unresolved component, leave as is
				ci = (ICpComponentInfo) c;
				ci.setParent(componentInfos);
			} else {
				ICpGenerator gen = c.getGenerator();
				if (gen != null) {
					// keep generator component info as is
					ci = component.getActiveCpComponentInfo();
				}
				if (ci == null) {
					ci = new CpComponentInfo(componentInfos, c, component.getSelectedCount());
					if (gen != null) {
						ICpItem gpdscItem = new CpItem(ci, CmsisConstants.GPDSC_TAG);
						ci.addChild(gpdscItem);
						gpdscItem.attributes().setAttribute(CmsisConstants.NAME, gen.getGpdsc());
					}
				} else {
					ci.setComponent(c);
					ci.setParent(componentInfos);
				}
				collectFilteredFiles(ci, c);
			}
			EVersionMatchMode versionMode = component.isUseLatestVersion() ? EVersionMatchMode.LATEST : EVersionMatchMode.FIXED;
			ci.setVersionMatchMode(versionMode);

			componentInfos.addChild(ci);
			component.setActiveComponentInfo(ci);
			addUsedPackInfo(ci.getPackInfo());

			ILiteComponentGroup g = component.getParentGroup();
			// collect used APIs
			ICpComponent api = g.getApi();
			if (api != null) {
				EVersionMatchMode vmm = EVersionMatchMode.LATEST;
				if (!g.isUseLatestVersion()) {
					vmm = EVersionMatchMode.FIXED;
				}
				selectedApis.put(api, vmm);
			}
		}

		for (Entry<ICpComponent, EVersionMatchMode> e : selectedApis.entrySet()) {
			ICpComponent api = e.getKey();
			EVersionMatchMode versionMode = e.getValue();
			ICpComponentInfo ai = null;
			if (api instanceof ICpComponentInfo) {
				ai = (ICpComponentInfo) api;
				ai.setParent(apiInfos);
			} else {
				ai = new CpComponentInfo(apiInfos, api, 1);
				collectFilteredFiles(ai, api);
				ICpPackInfo pi = ai.getPackInfo();
				if (!fUsedPackInfos.containsKey(pi.getId())) {
					fUsedPackInfos.put(pi.getId(), pi);
				}
			}
			ai.setVersionMatchMode(versionMode);
			apiInfos.addChild(ai);
			addUsedPackInfo(ai.getPackInfo());
		}

		collectGeneratedPacks();
	}

	protected void addUsedPackInfo(ICpPackInfo packInfo) {
		if (packInfo.isGenerated()){
			return; 
		}
		// TODO: maybe in future we need to display generated packs as well as used
		String packId = packInfo.getId();
		if (fPackFilter.isFixed(packId)) {
			packInfo.setVersionMatchMode(EVersionMatchMode.FIXED);
		} else {
			packInfo.setVersionMatchMode(EVersionMatchMode.LATEST);
		}

		if (!fUsedPackInfos.containsKey(packId)) {
			fUsedPackInfos.put(packId, packInfo);
		}
	}

	protected void collectFilteredFiles(ICpComponentInfo ci, ICpComponent c) {
		if (c == null) {
			return;
		}

		Collection<? extends ICpItem> allFiles = c.getGrandChildren(CmsisConstants.FILES_TAG);
		Collection<ICpItem> filtered = fComponentFilter.filterItems(allFiles); 
		// filter by device & toolchain
		filtered = fDependencySolver.filterItems(filtered); 
		// filter by selection
		ci.removeAllChildren(CmsisConstants.FILE_TAG);

		createFileInfos(ci, filtered, false);
		// collect generator project file to the bootstrap component
		if (ci.isGenerated() || !ci.isSaved()) {
			return;
		}

		ICpGenerator gen = c.getGenerator();
		if (gen == null){
			return;
		}
		createFileInfos(ci, gen.getGrandChildren(CmsisConstants.PROJECT_FILES_TAG), true);
	}

	protected void createFileInfos(ICpComponentInfo ci, Collection<? extends ICpItem> files, boolean generated) {
		if (files == null || files.isEmpty()){
			return;
		}
		for (ICpItem item : files) {
			if (item instanceof ICpFile) {
				ICpFile f = (ICpFile) item;
				ICpFileInfo fi = new CpFileInfo(ci, f);
				ci.addChild(fi);
				if (generated){
					fi.attributes().setAttribute(CmsisConstants.GENERATED, true);
				}
			}
		}
	}

	protected void resolveComponents(int flags) {
		if (fConfigurationInfo == null) {
			return;
		}
		// resolve components and select them
		EEvaluationResult result = EEvaluationResult.FULFILLED;
		EEvaluationResult res = resolveComponents(fConfigurationInfo.getGrandChildren(CmsisConstants.COMPONENTS_TAG), flags);
		if (res.ordinal() < result.ordinal()) {
			result = res;
		}
		res = resolveComponents(fConfigurationInfo.getGrandChildren(CmsisConstants.APIS_TAG), flags);
		if (res.ordinal() < result.ordinal()) {
			result = res;
		}
		evaluateComponentDependencies();
	}

	protected EEvaluationResult resolveComponents(Collection<? extends ICpItem> children, int flags) {
		EEvaluationResult result = EEvaluationResult.FULFILLED;
		if (children == null || children.isEmpty()) {
			return result;
		}
		for (ICpItem item : children) {
			if (item instanceof ICpComponentInfo) { 
			// skip doc and description	items
				ICpComponentInfo ci = (ICpComponentInfo) item;
				if (ci.isGenerated()){
					continue; 
					// Component info will be re-created
				}
				ci.setComponent(null);
				ci.setEvaluationResult(EEvaluationResult.UNDEFINED);
				if (ci.isApi()) {
					fComponentRoot.addCpItem(ci);
				} else {
					fComponentRoot.addComponent(ci, flags);
				}

				EEvaluationResult res = ci.getEvaluationResult();
				if (ci.getComponent() == null) {
					ICpPackInfo pi = ci.getPackInfo();
					if (resolvePack(pi) != null) {
						if (fPackFilter.isExcluded(pi.getId())) {
							ci.setEvaluationResult(EEvaluationResult.UNAVAILABLE_PACK);
						} else {
							ci.setEvaluationResult(EEvaluationResult.UNAVAILABLE);
						}
					}
				}
				if (res.ordinal() < result.ordinal()) {
					result = res;
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, ICpPackInfo> getUsedPackInfos() {
		return fUsedPackInfos;
	}

	@Override
	public ICpPackFilter getPackFilter() {
		return fPackFilter;
	}

	@Override
	public boolean setPackFilter(ICpPackFilter filter) {
		if (filter.equals(fPackFilter)) {
			return false;
		}
		fPackFilter = new CpPackFilter(filter);
		return true;
	}

	@Override
	public ICpDeviceInfo getDeviceInfo() {
		return fDeviceInfo;
	}

	@Override
	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		fDeviceInfo = deviceInfo;
		fConfigurationInfo.replaceChild(deviceInfo);
	}

	@Override
	public ICpDeviceItem getDevice() {
		if (fDeviceInfo != null) {
			return fDeviceInfo.getDevice();
		}
		return null;
	}

	@Override
	public ICpItem getToolchainInfo() {
		return fToolchainInfo;
	}

	@Override
	public ILiteDeviceItem getDevices() {
		if (fLiteDevices == null) {
			fLiteDevices = LiteDeviceItem.createTree(fFilteredPacks);
		}
		return fLiteDevices;
	}

	@Override
	public ILiteComponentItem getComponents() {
		return fComponentRoot;
	}

	/**
	 * Updates component filter by setting new device information
	 */
	protected void updateComponentFilter() {
		fComponentFilter = new CpConditionContext();
		if (fDeviceInfo != null) {
			fComponentFilter.setAttributes(fDeviceInfo.attributes().getAttributesAsMap());
			fComponentFilter.removeAttribute(CmsisConstants.URL); 
			// this	 attribute is not needed for filtering
		}
		if (fToolchainInfo != null) {
			fComponentFilter.mergeAttributes(fToolchainInfo.attributes());
		}
		fComponentFilter.setAttribute(CmsisConstants.THOST, Utils.getHostType());
		ICpEnvironmentProvider ep = CpPlugIn.getEnvironmentProvider();
		if (ep != null){
			fComponentFilter.setAttribute(CmsisConstants.TENVIRONMENT, ep.getName());
		}

		fComponentFilter.resetResult();
	}

	/**
	 * Builds filtered components tree
	 */
	protected void collectComponents() {
		fComponentRoot = new LiteComponentRoot(fConfigurationInfo.getName());

		// add artificial class items:
		// selected device
		LiteSelectedDeviceClass devClass = new LiteSelectedDeviceClass(fComponentRoot, fDeviceInfo);
		fComponentRoot.addChild(devClass);

		Collection<? extends ICpItem> children;
		// process components from generated packs
		if (fGeneratedPacks != null && !fGeneratedPacks.isEmpty()) {
			for (ICpPack pack : fGeneratedPacks.values()) {
				if (pack == null){
					continue;
				}
				children = pack.getGrandChildren(CmsisConstants.COMPONENTS_TAG);
				collectComponents(children);
			}
		}
		// process regular packs
		if (fFilteredPacks == null || fFilteredPacks.isEmpty()) {
			return;
		}
		// device pack has precedence, always collect its components, APIs and
		// taxonomy first
		ICpPack devicePack = null;
		ICpDeviceItem device = fDeviceInfo.getDevice();
		if (device != null) {
			devicePack = device.getPack();
		}

		// first add components
		if (devicePack != null) {
			children = devicePack.getGrandChildren(CmsisConstants.COMPONENTS_TAG);
			collectComponents(children);
		}
		for (ICpPack pack : fFilteredPacks) {
			if (pack == devicePack) {
				continue;
			}
			children = pack.getGrandChildren(CmsisConstants.COMPONENTS_TAG);
			collectComponents(children);
		}
		// then add APIs and taxonomy items
		if (fGeneratedPacks != null && !fGeneratedPacks.isEmpty()) {
			for (ICpPack pack : fGeneratedPacks.values()) {
				if (pack == null){
					continue;
				}
				children = pack.getGrandChildren(CmsisConstants.APIS_TAG);
				collectCpItems(children);
				children = pack.getGrandChildren(CmsisConstants.TAXONOMY_TAG);
				collectCpItems(children);
			}
		}

		if (devicePack != null) {
			children = devicePack.getGrandChildren(CmsisConstants.APIS_TAG);
			collectCpItems(children);
			children = devicePack.getGrandChildren(CmsisConstants.TAXONOMY_TAG);
			collectCpItems(children);
		}
		for (ICpPack pack : fFilteredPacks) {
			if (pack == devicePack) {
				continue;
			}
			children = pack.getGrandChildren(CmsisConstants.APIS_TAG);
			collectCpItems(children);

			children = pack.getGrandChildren(CmsisConstants.TAXONOMY_TAG);
			collectCpItems(children);
		}

		// "more.." when filter is effect
		if (!fPackFilter.isUseAllLatestPacks()) {
			LiteMoreClass more = new LiteMoreClass(fComponentRoot);
			fComponentRoot.addChild(more);
		}
	}

	/**
	 * Adds collection members to the hierarchy
	 * 
	 * @param children
	 */
	protected void collectCpItems(Collection<? extends ICpItem> children) {
		if (children == null || children.isEmpty()) {
			return;
		}
		for (ICpItem item : children) {
			if (item instanceof ICpTaxonomy || item instanceof ICpComponent) { 
			// skip	doc and description items
				fComponentRoot.addCpItem(item);
			}
		}
	}

	/**
	 * Collect components from given pack
	 * 
	 * @param pack
	 */
	protected void collectComponents(Collection<? extends ICpItem> children) {
		if (children == null || children.isEmpty()) {
			return;
		}
		for (ICpItem item : children) {
			if (item.getTag().equals(CmsisConstants.BUNDLE_TAG)) {
			// insert bundle implicitly since its components can be filtered out
				collectComponents(item.getChildren());
			} else if (item instanceof ICpComponent) { 
			// skip doc and description items
				ICpComponent c = (ICpComponent) item;
				EEvaluationResult res = c.evaluate(fComponentFilter);
				if (res.ordinal() < EEvaluationResult.FULFILLED.ordinal()) {
					continue; // filtered out
				}
				fComponentRoot.addComponent(c);
			}
		}
	}

	@Override
	public Collection<ILiteComponent> getSelectedComponents() {
		if (fComponentRoot != null) {
			return fComponentRoot.getSelectedComponents(new LinkedHashSet<ILiteComponent>());
		}
		return null;
	}

	@Override
	public Collection<ILiteComponent> getUsedComponents() {
		if (fComponentRoot != null) {
			return fComponentRoot.getUsedComponents(new LinkedHashSet<ILiteComponent>());
		}
		return null;
	}

	@Override
	public EEvaluationResult evaluateComponentDependencies() {
		return fDependencySolver.evaluateDependencies();

	}

	@Override
	public EEvaluationResult resolveComponentDependencies() {
		return fDependencySolver.resolveDependencies();
	}

	@Override
	public EEvaluationResult getEvaluationResult(ILiteComponentItem item) {
		return fDependencySolver.getEvaluationResult(item);
	}

	@Override
	public Collection<? extends ILiteDependencyItem> getDependencyItems() {
		return fDependencySolver.getDependencyItems();
	}

	@Override
	public EEvaluationResult getEvaluationResult() {
		return fDependencySolver.getEvaluationResult();
	}

	@Override
	public void setEvaluationResult(EEvaluationResult result) {
		fDependencySolver.setEvaluationResult(result);
	}

	@Override
	public void selectComponent(ILiteComponent component, int nInstances) {
		if (component == null){
			return;
		}
		component.setSelected(nInstances);
	}
}
