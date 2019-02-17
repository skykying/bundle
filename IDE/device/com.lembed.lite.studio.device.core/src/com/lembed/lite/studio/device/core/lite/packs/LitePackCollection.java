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

package com.lembed.lite.studio.device.core.lite.packs;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpPack;
import com.lembed.lite.studio.device.core.data.CpPackFilter;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.data.ICpPackFamily;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.info.CpPackFilterInfo;
import com.lembed.lite.studio.device.core.info.CpPackInfo;
import com.lembed.lite.studio.device.core.info.ICpPackFilterInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.utils.AlnumComparator;

/**
 * Class that encapsulates filtered and ordered collection of Packs 
 *
 */
public class LitePackCollection extends LitePackItem implements ILitePackCollection {

	protected ICpPackCollection fPackCollection = null;
	protected Map<String, ILitePackFamily> fPackFamilies = new TreeMap<String, ILitePackFamily>(new AlnumComparator(false, false));
	Map<String, ICpPackInfo> fUsedPacks = null; 
	
	
	boolean fbUseAllLatest = true;
	
	public LitePackCollection() {
		super(null);
	}

	@Override
	public void clear() {
		super.clear();
		fPackFamilies = null;
		fUsedPacks = null;
		fPackCollection = null;
	}

	
	@Override
	public boolean purge() {
		if(fPackFamilies != null && ! fPackFamilies.isEmpty()) {
			for (Iterator<ILitePackFamily> iterator = fPackFamilies.values().iterator(); iterator.hasNext();) {
				ILitePackFamily family = iterator.next();
				if(family.purge()) {
					iterator.remove();
				}
			}	
		}
		return false;
	}

	
	
	@Override
	public ILitePackCollection getRoot() {
		return this;
	}


	@Override
	public String getId() {
		if(fPackCollection != null)
			return fPackCollection.getId();
		return null;
	}

	@Override
	public void setPackFilterInfo(ICpPackFilterInfo packFilterInfo) {
		if(packFilterInfo == null)
			return;
		fbUseAllLatest = packFilterInfo.isUseAllLatestPacks();
		// set version match modes and selection
		Collection<? extends ICpItem> children = packFilterInfo.getChildren(); 
		if(children == null)
			return;
		for(ICpItem item : children){
			if(!(item instanceof ICpPackInfo))
				continue;
			addCpItem(item);
			ICpPackInfo packInfo = (ICpPackInfo)item;
			
			String familyId = packInfo.getPackFamilyId();
			ILitePackFamily packFamily = getRtePackFamily(familyId);
			EVersionMatchMode mode = packInfo.getVersionMatchMode();
			// first select packs with fixed version to avoid auto-selection 
			if(mode == EVersionMatchMode.FIXED){
				String version = packInfo.getVersion();
				ILitePack pack = packFamily.getRtePack(version);
				pack.setSelected(true);
			}
			packFamily.setVersionMatchMode(mode);
		}
	}
	
	@Override
	public void addCpItem(ICpItem item) {
		if(item == null)
			return;
		if(item instanceof ICpPackFilterInfo || item instanceof ICpPackCollection) {
			if(item instanceof ICpPackCollection )
				fPackCollection = (ICpPackCollection)item;
			Collection<? extends ICpItem> children = item.getChildren(); 
			if(children == null)
				return;
			for(ICpItem child : children){
				addCpItem(child);
			}
		} else { 
			addRtePackFamily(item);
		} 
	}

	protected void addRtePackFamily(ICpItem item) {
		if(item instanceof ICpPackFamily || item instanceof ICpPack || item instanceof ICpPackInfo) {
			String familyId = CpPack.familyFromId(item.getId());
			ILitePackFamily litePackFamily = ensurePackFamily(familyId);
			litePackFamily.addCpItem(item);
		} 
	}

	protected ILitePackFamily ensurePackFamily(String familyId) {
		ILitePackFamily litePackFamily = getRtePackFamily(familyId);
		if(litePackFamily == null) {
			litePackFamily = new LitePackFamily(this, familyId);
			fPackFamilies.put(familyId, litePackFamily);
		}
		return litePackFamily;
	}
	
	@Override
	public ICpItem getCpItem() {
		return fPackCollection;
	}

	@Override
	public boolean isSelected() {
		for(ILitePackFamily f : fPackFamilies.values()) {
			if(f.isSelected())
				return true;
		}
		return false;
	}

	@Override
	public boolean isUsed() {
		for(ILitePackFamily f : fPackFamilies.values()) {
			if(f.isUsed())
				return true;
		}
		return false;
	}

	@Override
	public boolean isInstalled() {
		if(fPackFamilies.isEmpty())
			return false;
		for(ILitePackFamily f : fPackFamilies.values()) {
			if(!f.isInstalled())
				return false;
		}
		return true;
	}
	
	@Override
	public boolean isExcluded() {
		return false;
	}

	@Override
	public Object[] getChildArray() {
		return fPackFamilies.values().toArray();
	}
	
	@Override
	public boolean hasChildren() {
		return !fPackFamilies.isEmpty();
	}
	
	@Override
	public int getChildCount() {
		return fPackFamilies.size();
	}


	@Override
	public boolean isUseAllLatestPacks() {
		return fbUseAllLatest;
	}

	@Override
	public void setUseAllLatestPacks(boolean bUseLatest) {
		fbUseAllLatest = bUseLatest;
		if(fbUseAllLatest) 
			return;
		// select all used packs as fixed and all other as excluded
		for(ILitePackFamily f : fPackFamilies.values()) {
			if(f.isUsed())
				f.setVersionMatchMode(EVersionMatchMode.FIXED);
			else
				f.setVersionMatchMode(EVersionMatchMode.EXCLUDED);
		}
	}

	@Override
	public ILitePackFamily getRtePackFamily(String familyId) {
		return fPackFamilies.get(familyId);
	}

	@Override
	public String getVersion() {
		return CmsisConstants.EMPTY_STRING;
	}

	@Override
	public ILitePackItem getFirstChild() {
		if(!fPackFamilies.isEmpty())
			return fPackFamilies.entrySet().iterator().next().getValue();
		return null;
	}
	
	@Override
	public ICpPackFilter createPackFiler() {
		CpPackFilter filter = new CpPackFilter();
		filter.setUseAllLatestPacks(fbUseAllLatest);
		if(fbUseAllLatest)
			return filter;
		
		for(ILitePackFamily f : fPackFamilies.values()) {
			String familyId = f.getId();
			Set<String> versions = null;
			switch(f.getVersionMatchMode()){
			case FIXED:
				versions = f.getSelectedVersions();
			case LATEST:
				filter.setFixed(familyId, versions);
				break;
			case EXCLUDED:
				filter.setExcluded(familyId, true);
				break;
			default:
				break;
			}
		}
		return filter;
	}

	@Override
	public ICpPackFilterInfo createPackFilterInfo() {
		CpPackFilterInfo filterInfo = new CpPackFilterInfo(null);
		filterInfo.setUseAllLatestPacks(fbUseAllLatest);
		if(!fbUseAllLatest) {
			for(ILitePackFamily f : fPackFamilies.values()) {
				EVersionMatchMode mode = f.getVersionMatchMode(); 
				switch(mode){
				case EXCLUDED:
					continue;
				case LATEST:
					ICpPackInfo info = new CpPackInfo(filterInfo, f.getAttributes());
					info.setVersionMatchMode(mode);
					filterInfo.addChild(info);
					continue;
				case FIXED:
					break;
				}
				Collection<ILitePack> packs = f.getSelectedPacks();
				for(ILitePack p : packs){
					ICpPackInfo info = null;
					ICpPack pack = p.getPack();
					if(pack != null)
						info = new CpPackInfo(filterInfo, pack);
					else
						info = new CpPackInfo(filterInfo, p.getAttributes());
					info.setVersionMatchMode(mode);
					filterInfo.addChild(info);
				}
			}
		}
		return filterInfo;
	}

	@Override
	public void setUsedPacks(Map<String, ICpPackInfo> usedPackInfos) {
		fUsedPacks = usedPackInfos;
		// ensure all items exist
		if(fUsedPacks != null && !fUsedPacks.isEmpty()) {
			for(Entry<String, ICpPackInfo> e : usedPackInfos.entrySet()){
				addCpItem(e.getValue());
			}
		}
	}


	@Override
	public boolean isPackUsed(String id) {
		if(fUsedPacks == null || fUsedPacks.isEmpty())
			return false;
		return fUsedPacks.containsKey(id);
	}

	@Override
	public IAttributes getAttributes() {
		return null;
	}

}
