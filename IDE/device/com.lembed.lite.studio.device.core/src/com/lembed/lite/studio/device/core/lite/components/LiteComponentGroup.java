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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpTaxonomy;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.lite.LiteConstants;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.utils.AlnumComparator;

/**
 *
 */
public class LiteComponentGroup extends LiteComponentItem implements ILiteComponentGroup{

	protected Map<String, ICpComponent> fApis = null; // api collection sorted by version 
	protected String fActiveApiVersion = null;
	protected boolean fbUseLatesApi = true;
	/**
	 * @param parent
	 */
	public LiteComponentGroup(ILiteComponentItem parent, String name) {
		super(parent, name);
		fComponentAttribute = EComponentAttribute.CSUB;
		fbExclusive = false;
	}

	
	@Override
	public ILiteComponentGroup getParentGroup() {
		return this;
	}
	
	
	@Override
	public ICpItem getCpItem() {
		ICpComponent api = getApi();
		if(api != null) {
			return api;
		}
		return super.getCpItem();
	}


	@Override
	public String getEffectiveName() {
		String name = super.getEffectiveName();
		if(fApis != null && !fApis.isEmpty())
			name += " (API)"; //$NON-NLS-1$
		return name;
	}


	@Override
	public ILiteComponentItem getEffectiveItem() {
		if(fApis == null || fApis.isEmpty()) { 
			if(getChildCount() == 1) { 
				ILiteComponentItem child = fChildMap.entrySet().iterator().next().getValue();
				String childName = child.getName();
				if(childName.isEmpty() || childName.equals(getName()))
					return child; 
			}
		}
		return super.getEffectiveItem();
	}

	@Override
	public void addCpItem(ICpItem cpItem) {
		if(cpItem instanceof ICpComponent) {
			ICpComponent c = (ICpComponent)cpItem;
			if( c.isApi())
				addApi(c);
			else 
				addComponent(c, LiteConstants.NONE);
		} else if (cpItem instanceof ICpTaxonomy ){
			String csub = cpItem.getAttribute(CmsisConstants.CSUB);
			if( csub != null && !csub.isEmpty()) {
				// add component using subName as a key    
				ILiteComponentItem component = getChild(csub);
				if(component != null) {
					component.addCpItem(cpItem);
				}
			} else if(getTaxonomy() == null){
				fTaxonomy = cpItem;
			}
		}
	}

	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		if(cpComponent.isApi()) {
			addApi(cpComponent);
			return;
		}
		String componentName = cpComponent.getAttribute(CmsisConstants.CSUB);
		// add component using subName as a key    
		ILiteComponentItem component = getChild(componentName);
		if(component == null) {
			component = new LiteComponent(this, componentName); 
			addChild(component);
		}
		component.addComponent(cpComponent, flags);
	}
		
	
	/**
	 * Adds an API item to this group
	 * @param cpApi ICpApi item
	 */
	protected void addApi(ICpComponent cpApi) {
		String groupName = cpApi.getAttribute(CmsisConstants.CGROUP); 
		if(!groupName.equals(getName()))
			return;

		ICpComponentInfo apiInfo = null;
		if(cpApi instanceof ICpComponentInfo) {
			apiInfo = (ICpComponentInfo)cpApi;
		}
		
		String version = null;
		if(apiInfo == null || apiInfo.isVersionFixed()) 
			version = cpApi.getVersion();

		ICpComponent existingApi = getApi(version);
		if(existingApi == null) { 
			if(fApis == null)
				fApis = new TreeMap<String, ICpComponent>(new AlnumComparator());
			fApis.put(cpApi.getVersion(), cpApi);
		} 
		if(apiInfo != null) {
			if(existingApi == null || existingApi instanceof ICpComponentInfo) {
				apiInfo.setComponent(null);
				apiInfo.setEvaluationResult(EEvaluationResult.MISSING_API);
			} else {
				apiInfo.setComponent(existingApi);
			}
			setActiveApi(version);
		}
	}


	@Override
	public ICpComponent getApi( final String version){
		if(fApis != null) {
			if(version == null)
				return fApis.entrySet().iterator().next().getValue();
			return fApis.get(version);
		}
		return null;
	}

	@Override
	public ICpComponent getApi() {
		return getApi(getActiveApiVersion());
	}

	
	@Override
	public Map<String, ICpComponent> getApis() {
		return fApis;
	}

	@Override
	public String getActiveApiVersion() {
		if(fApis != null && fActiveApiVersion == null ) {
			fActiveApiVersion = fApis.entrySet().iterator().next().getKey();
		}
		return fActiveApiVersion;
	}

	@Override
	public boolean setActiveApi(final String version) {
		if(fApis == null)
			return false;
		String newVersion = version;
		if(version == null || version.equals(getDefaultVersion())) {
			newVersion = fApis.entrySet().iterator().next().getKey();
			fbUseLatesApi = true;
		} else {
			fbUseLatesApi = false;
		}
		
		String activeApiVersion = getActiveApiVersion();
		if(activeApiVersion.equals(newVersion))
			return false;
		fActiveApiVersion = newVersion;
		return true;
	}
	
	@Override
	public ILiteComponentGroup getGroup(IAttributes attributes) {
		if(attributes.getAttribute(CmsisConstants.CGROUP, CmsisConstants.EMPTY_STRING).equals(getName()))
			return this;
		return null;
	}

	@Override
	public Collection<String> getVersionStrings() {
		if(fApis != null && !fApis.isEmpty())
			return fApis.keySet();
		return super.getVersionStrings();
	}

	@Override
	public String getActiveVersion() {
		if(fApis != null && !fApis.isEmpty())
			return getActiveApiVersion();
		return super.getActiveVersion();
	}

	@Override
	public void setActiveVersion(String version) {
		if(fApis != null && !fApis.isEmpty())
			setActiveApi(version);
		else 
			super.setActiveVersion(version);
	}


	@Override
	public boolean isUseLatestVersion() {
		if(fApis != null && !fApis.isEmpty())
			return fbUseLatesApi;
		return super.isUseLatestVersion();
	}
	
}
