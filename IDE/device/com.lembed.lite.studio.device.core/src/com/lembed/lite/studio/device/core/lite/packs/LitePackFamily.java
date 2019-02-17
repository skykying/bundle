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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackFamily;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.utils.VersionComparator;

/**
 * 
 */
public class LitePackFamily extends LitePackItem implements ILitePackFamily {

	protected String fId = null;
	protected Map<String, ILitePack> fPacks = new TreeMap<String, ILitePack>(new VersionComparator());
	protected EVersionMatchMode fVersionMatchMode = EVersionMatchMode.EXCLUDED; // until pack filter says other way
	protected ICpPackFamily fPackFamily = null; 
	
	public LitePackFamily(ILitePackItem parent, ICpPackFamily packFamily) {
		this(parent, packFamily.getId());
		addCpItem(packFamily);
	}
	
	public LitePackFamily(ILitePackItem parent, String id) {
		super(parent);
		fId = id;
	}

	@Override
	public void clear() {
		super.clear();
		fPacks = null;
		fPackFamily = null;
		fId = null;
	}

	
	@Override
	public boolean purge() {
		if(fPacks != null && ! fPacks.isEmpty()) {
			for (Iterator<ILitePack> iterator = fPacks.values().iterator(); iterator.hasNext();) {
				ILitePack pack = iterator.next();
				if(pack.purge()) {
					iterator.remove();
				}
			}	
		}
		
		if(fPacks == null || fPacks.isEmpty()) {
			clear();
			return true;
		}
		return false;
	}

	@Override
	public String getId() {
		return fId;
	}

	@Override
	public boolean isSelected() {
		for(ILitePack pack : fPacks.values()){
			if(pack.isSelected())
				return true;
		}
		return false;
	}

	protected boolean isExplicitlySelected() {
		for(ILitePack pack : fPacks.values()){
			if(pack.isExplicitlySelected())
				return true;
		}
		return false;
	}

	
	
	@Override
	public ILitePackFamily getFamily() {
		return this;
	}


	@Override
	public boolean isUsed() {
		for(ILitePack pack : fPacks.values()){
			if(pack.isUsed())
				return true;
		}
		return false;
	}

	@Override
	public ICpPack getPack() {
		ILitePack litePack = getLatestRtePack();
		if(litePack != null )
			return litePack.getPack();
		return null;
	}

	@Override
	public ICpPackInfo getPackInfo() {
		ILitePack litePack = getLatestRtePack();
		if(litePack != null )
			return litePack.getPackInfo();
		return null;
	}

	@Override
	public boolean isInstalled() {
		if(fPacks.isEmpty())
			return false;
		for(ILitePack pack : fPacks.values()){
			if(!pack.isInstalled())
				return false;
		}
		return true;
	}

	@Override
	public boolean isExcluded() {
		return getVersionMatchMode() == EVersionMatchMode.EXCLUDED;
	}
	
	@Override
	public EVersionMatchMode getVersionMatchMode() {
		if(isUseAllLatestPacks())
			return EVersionMatchMode.LATEST;
		return fVersionMatchMode;
	}

	
	@Override
	public void setVersionMatchMode(EVersionMatchMode mode) {
		fVersionMatchMode = mode;
		if(mode == EVersionMatchMode.FIXED && !isExplicitlySelected()){
			ILitePack pack= getLatestRtePack();
			if(pack != null)
				pack.setSelected(true); 
		}
	}
	
	@Override
	public void updateVersionMatchMode() {
		if(fVersionMatchMode != EVersionMatchMode.LATEST) {
			if(isExplicitlySelected())
				fVersionMatchMode = EVersionMatchMode.FIXED;
			else 
				fVersionMatchMode = EVersionMatchMode.EXCLUDED;
		}
	}

	
	@Override
	public ILitePack getLatestRtePack() {
		if(fPacks.isEmpty())
			return null;
		return fPacks.entrySet().iterator().next().getValue();
	}

	@Override
	public ILitePack getRtePack(String version) {
		if(version == null || version.isEmpty())
			return getLatestRtePack();
		return fPacks.get(version);
	}
	
	@Override
	public ILitePackItem getFirstChild() {
		return getLatestRtePack();
	}

	@Override
	public void addCpItem(ICpItem item) {
		if(item instanceof ICpPackFamily) {
			fPackFamily = (ICpPackFamily)item;
			Collection<? extends ICpItem> children = fPackFamily.getChildren(); 
			if(children == null)
				return;
			for(ICpItem child : children) {
				addCpItem(child);
			}
		} else if(item instanceof ICpPack || item instanceof ICpPackInfo) {
			addRtePackItem(item);
		}
	}

	protected void addRtePackItem(ICpItem item) {
		String version = item.getVersion();
		ILitePack litePack = getRtePack(version);
		if(litePack == null) { 
			litePack = new LitePack(this, item);
			fPacks.put(version,  litePack);
		} else {
			litePack.addCpItem(item);
		}
	}

	@Override
	public ICpItem getCpItem() {
		return fPackFamily;
	}

	@Override
	public Object[] getChildArray() {
		return fPacks.values().toArray();
	}
	
	@Override
	public boolean hasChildren() {
		return !fPacks.isEmpty();
	}
	
	@Override
	public int getChildCount() {
		return fPacks.size();
	}

	@Override
	public String getVersion() {
		switch(getVersionMatchMode()){
		case FIXED:
			break;
		case LATEST:
			ILitePackItem rtePack = getLatestRtePack();
			if(rtePack != null)
				return rtePack.getVersion();
		case EXCLUDED:
		default:
			return CmsisConstants.EMPTY_STRING;
		}

		String version = CmsisConstants.EMPTY_STRING;  
		for(ILitePack pack : fPacks.values()){
			if(pack.isSelected()) {
				if(!version.isEmpty())
					version += " ,"; //$NON-NLS-1$
				version += pack.getVersion();
			}
		}
		return version;
	}

	@Override
	public Set<String> getSelectedVersions() {
		Set<String> versions = new HashSet<String>();  
		for(ILitePack pack : fPacks.values()){
			if(pack.isSelected())
				versions.add(pack.getVersion());
		}	
		return versions;
	}

	@Override
	public Collection<ILitePack> getSelectedPacks() {
		Collection<ILitePack> packs = new LinkedList<ILitePack>();  
		for(ILitePack pack : fPacks.values()){
			if(pack.isSelected())
				packs.add(pack);
		}	
		return packs;
	}

	@Override
	public IAttributes getAttributes() {
		ILitePack p = getLatestRtePack();
		if(p != null)
			return p.getAttributes();
		return null;
	}
	
	
}
