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
import java.util.LinkedList;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpTaxonomy;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.lite.LiteConstants;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependency;

/**
 *  Class that represents component taxonomy level that can be selected.<br>
 *  Contain collection of component variant items.
 */
public class LiteComponent extends LiteComponentItem implements ILiteComponent {

	protected int fnSelected = 0; // number of selected instances
	protected boolean fbBootStrap = false; // flag indicating that at least one of the component variants is a bootstrap one  
	protected boolean fbGenerated = false; // one of the component variants is generated
	
	public LiteComponent(ILiteComponentItem parent, String name) {
		super(parent, name);
		fComponentAttribute = EComponentAttribute.CVARIANT;
	}

	@Override
	public boolean isSelected() {
		return fnSelected > 0;
	}

	@Override
	public int getSelectedCount() {
		return fnSelected;
	}

	@Override
	public boolean isGenerated() {
		return fbGenerated;
	}

	@Override
	public boolean isBootStrap() {
		return fbBootStrap;
	}

	@Override
	public boolean isUseLatestVersion() {
		if(hasBundle()) {
			return getParentBundle().isUseLatestVersion();
		}
		return super.isUseLatestVersion();
	}
	
	@Override
	public String getEffectiveName() {
		String name = super.getEffectiveName();
		if(name.isEmpty()) {
			ILiteComponentGroup g = getParentGroup();
			if(g != null) {
				name = g.getName();
			} 
		}
		return name;
	}

	
	@Override
	public boolean setSelected(int count) {
		if(fnSelected == count) {
			return false;
		}
		fnSelected = count;
		return true;
	}

	@Override
	public int getMaxInstanceCount() {
		ICpComponent c = getActiveCpComponent();
		if(c != null) {
			return c.getMaxInstances();
		} 
		return 0;
	}
	
	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		if(cpComponent.isApi()) {
			return;
		}

		boolean generated = cpComponent.isGenerated();

		if(cpComponent instanceof ICpComponentInfo) {
			addComponentInfo((ICpComponentInfo)cpComponent, flags);
			return;
		}
		
		if(cpComponent.isBootStrap()) {
			// bootstrap component comes from a regular pack
			fbBootStrap = true; 
		}

		// add variant, vendor and version items
		String variant = cpComponent.getAttribute(CmsisConstants.CVARIANT);
		if(isGenerated()){
			if(variant.equals(getActiveVariant()))
				return; // do not insert a bootstrap component itself
		}
		
		ILiteComponentItem variantItem = getChild(variant);
		if( variantItem == null) {
			variantItem = new LiteComponentVariant(this, variant);
			addChild(variantItem);
		}
		
		// first try to get supplied vendor
		String vendor = cpComponent.getVendor();
		ILiteComponentItem vendorItem = variantItem.getChild(vendor);
		if( vendorItem == null) {
			vendorItem = new LiteComponentVendor(variantItem, cpComponent.getVendor());
			variantItem.addChild(vendorItem);
		}
		
		String version = cpComponent.getVersion();
		ILiteComponentItem versionItem = vendorItem.getChild(version);
		if( versionItem == null) {
			versionItem = new LiteComponentVersion(vendorItem, cpComponent.getVersion());
			vendorItem.addChild(versionItem);
		}
		
		// set the generated flag now 
		if(generated) {
			fbGenerated = generated;
			setSelected(1);
		}
		
		versionItem.addComponent(cpComponent, flags);
		if(generated || (!isGenerated() && cpComponent.isDefaultVariant())) {
			setActiveChild(variant);
		}			
	}

	protected void addComponentInfo(ICpComponentInfo ci, int flags) {

		// calculate ignore flags
		boolean versionFixed = (flags & LiteConstants.COMPONENT_IGNORE_VERSION) == 0 && ci.isVersionFixed();
		// version is fixed => variant and vendor implicitly fixed too 
		boolean vendorFixed  = versionFixed || ((flags & LiteConstants.COMPONENT_IGNORE_VENDOR) == 0);  
		String variant = ci.getAttribute(CmsisConstants.CVARIANT);
		boolean variantFixed = versionFixed || ((flags & LiteConstants.COMPONENT_IGNORE_VARIANT) == 0 && !variant.isEmpty());
		
		if(isGenerated()) {
			// in case of a generated component we do not care about bootstrap attributes
			versionFixed = vendorFixed = variantFixed = false;
		}
		
		// add variant, vendor and version items
		// try to get supplied variant
		ILiteComponentItem variantItem = getChild(variant);
		if(isGenerated() || (variantItem == null && !variantFixed)) {
			variantItem = getChild(getActiveVariant()); 
		}
		if( variantItem == null) {
			if(hasChildren()) {
				ci.setEvaluationResult(EEvaluationResult.MISSING_VARIANT);
			} else {
				ci.setEvaluationResult(EEvaluationResult.MISSING);
			}
			variantItem = new LiteComponentVariant(this, variant);
			addChild(variantItem);
		}
		
		// try to get supplied vendor
		String vendor = ci.getVendor();
		ILiteComponentItem vendorItem = variantItem.getChild(vendor);
		if( isGenerated() || (vendorItem == null && !vendorFixed)) {
			vendorItem = variantItem.getActiveChild();
		}
		if( vendorItem == null) {
			if(variantItem.hasChildren()) {
				// there are some vendors in the collection, but not what is needed 
				ci.setEvaluationResult(EEvaluationResult.MISSING_VENDOR);
			} else {
				ci.setEvaluationResult(EEvaluationResult.MISSING);
			}
			vendorItem = new LiteComponentVendor(variantItem, vendor);
			variantItem.addChild(vendorItem);
		}
		
		String version = null;
		if(versionFixed) {
			version = ci.getVersion();
		}
		ILiteComponentItem versionItem = vendorItem.getChild(version);
		if( versionItem == null) {
			if(vendorItem.hasChildren()) {
				// there are some versions in the collection, but not what is needed
				ci.setEvaluationResult(EEvaluationResult.MISSING_VERSION);
			} else {
				ci.setEvaluationResult(EEvaluationResult.MISSING);
			}
			versionItem = new LiteComponentVersion(vendorItem, ci.getVersion());
			vendorItem.addChild(versionItem);
		}
		
		versionItem.addComponent(ci, flags);

		setSelected(ci.getInstanceCount());
		setActiveChild(variant);
		variantItem.setActiveChild(vendor);
		vendorItem.setActiveChild(version);
	}

	
	@Override
	public void addCpItem(ICpItem cpItem) {
		if(cpItem instanceof ICpComponent ) {
			addComponent((ICpComponent)cpItem, LiteConstants.NONE);
		} else if (cpItem instanceof ICpTaxonomy ){
			String csub = cpItem.getAttribute(CmsisConstants.CGROUP);
			if( csub.equals(getName())) {
				if(getTaxonomy() == null) {
					fTaxonomy = cpItem;
				} 
				return;
			}
		}
	}

	
	@Override
	public void setActiveComponentInfo(ICpComponentInfo ci) {
		if(ci == null) {
			return;
		}
		addComponent(ci, LiteConstants.NONE);
	}

	@Override
	public ILiteComponent getParentComponent() {
		return this;
	}
	
	@Override
	public Collection<String> getVariantStrings() {
		return getKeys();
	}
	
	@Override
	public String getActiveVariant() {
		return getActiveChildName();
	}

	
	@Override
	public void setActiveVariant(String variant) {
		setActiveChild(variant);
	}

	@Override
	public String getActiveVendor() {
		if(hasBundle()) {
			return CmsisConstants.EMPTY_STRING;
		}
		return super.getActiveVendor();
	}
	
	@Override
	public void setActiveVendor(String vendor) {
		if(hasBundle()) {
			return;
		}
		super.setActiveVendor(vendor);
	}

	@Override
	public String getActiveVersion() {
		if(hasBundle()) {
			return CmsisConstants.EMPTY_STRING;
		}
		return super.getActiveVersion();
	}

	@Override
	public void setActiveVersion(String version) {
		if(hasBundle()) {
			return;
		}
		super.setActiveVersion(version);
	}

	@Override
	public boolean hasBundle() {
		ILiteComponentBundle bundle = getParentBundle();
		if(bundle != null && !bundle.getName().isEmpty()) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public Collection<ILiteComponent> getSelectedComponents(	Collection<ILiteComponent> components) {
		// is we are here => component is active
		if(isSelected()) {
			if(components == null) {
				components = new LinkedList<ILiteComponent>();
			}
			components.add(this);
		}
		return components;
	}
	

	@Override
	public Collection<ILiteComponent> getUsedComponents(Collection<ILiteComponent> components) {
		// is we are here => component is active
		ICpComponentInfo ci = getActiveCpComponentInfo();
		if(ci != null) {
			if(components == null) {
				components = new LinkedList<ILiteComponent>();
			}
			components.add(this);
		}
		return components;
	}
	
	@Override
	public Collection<ILiteComponent> getGeneratorComponents(String generatorId, Collection<ILiteComponent> components) {
		// is we are here => component is active
		if(components == null) {
			components = new LinkedList<ILiteComponent>();
		}
		if(!isGenerated() && !isBootStrap())
			return components;
		
		ICpComponent c = getActiveCpComponent();
		if(c == null)
			return components;
		String genId = c.getGeneratorId();
		if(genId != null && genId.equals(generatorId)) {
			components.add(this);
		}
		return components;	
	}	
	
	@Override
	public String getGeneratorId() {
		ICpComponent c = getActiveCpComponent();
		if(c != null)
			return c.getGeneratorId();
		return null;	
	}

	@Override
	public EEvaluationResult findComponents(ILiteDependency dependency) {
		EEvaluationResult result = super.findComponents(dependency);
		if(result == EEvaluationResult.SELECTABLE) {
			if(isSelected()) {
				result = EEvaluationResult.FULFILLED;
			}
		} else if (result.ordinal() >= EEvaluationResult.INSTALLED.ordinal()) {
			if(!isActive()) {
				result = EEvaluationResult.INACTIVE;
			}
		}
		dependency.addComponent(this, result);
		return result;
	}

	@Override
	public int getUseCount() {
		if(isGenerated())
			return fnSelected;
		ICpComponentInfo ci = getActiveCpComponentInfo();
		if(ci != null) {
			return ci.getInstanceCount();
		}
		return 0;
	}
}
