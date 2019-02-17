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

package com.lembed.lite.studio.device.core.lite.dependencies;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpConditionContext;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpExpression;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.ILiteModel;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentGroup;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.utils.AlnumComparator;

/**
 * Class responsible for evaluating component dependencies and resolving them 
 */
public class LiteDependencySolver extends CpConditionContext implements ILiteDependencySolver {

	protected ILiteModel liteModel = null;

	//results of evaluating expressions
	protected Map<ICpExpression, ILiteDependency> fDependencies = null;
	protected Map<ICpExpression, ILiteDependency> fDenyDependencies = null;
	
	// collected results for selected components
	protected Map<ILiteComponentItem, ILiteDependencyItem> fDependencyItems = null;
	
	// temporary collection of selected components
	protected Collection<ILiteComponent> tSelectedComponents = null; 
	
	protected Map<ILiteComponentItem, EEvaluationResult> fEvaluationResults = null;

	 /**
	 *  Helper class to compare component by evaluation result (descending) and component name (acceding) 
	 */
	class ComponentResultComparator implements Comparator<ILiteComponent> {
		@Override
		public int compare(ILiteComponent c0, ILiteComponent c1) {
			int res0 = getEvaluationResult(c0).ordinal();
			int res1 = getEvaluationResult(c1).ordinal();
			int res = res0 - res1;
			if(res != 0)  
				return res;
			String name0 = c0.getActiveCpItem().getName();
			String name1 = c1.getActiveCpItem().getName();
			return AlnumComparator.alnumCompare(name0, name1);
		}
	 };
	
	/**
	 * Default constructor 
	 */
	public LiteDependencySolver(ILiteModel model) {
		liteModel = model;
	}

	
	@Override
	public void resetResult() {
		super.resetResult();
		fDependencies = null;
		fDenyDependencies = null;
		fEvaluationResults = null;
		fDependencyItems = null;
		tSelectedComponents = null;
	}
	
	protected Collection<ILiteComponent> getSelectedComponents(){
		if(tSelectedComponents == null) {
			if(liteModel != null){
				tSelectedComponents = liteModel.getSelectedComponents();
			}
		}
		return tSelectedComponents;
	}

	protected Collection<ILiteComponent> getUsedComponents(){
		if(liteModel != null){
			return liteModel.getUsedComponents();
		}
		return null;
	}
	
	protected Map<String, ICpPack> getGeneratedPacks(){
		if(liteModel != null){
			return liteModel.getGeneratedPacks();
		}
		return null;
	}
	
	
	
	@Override
	protected boolean isEvaluate(EEvaluationResult res) {
		if(super.isEvaluate(res)){
			return true;
		}
		
		if(res == EEvaluationResult.ERROR){
			return false;
		}

		// do not re-evaluate fulfilled and ignored conditions,
		// for other results do trigger calls to evaluateDependency(), it has its own cache
		return !res.isFulfilled();
	}
	
	protected void collectDependencies(ILiteDependencyResult depRes, ICpItem condition, EEvaluationResult overallResult) {
		// first check require and deny expressions
		Collection<? extends ICpItem> children = condition.getChildren();
		for(ICpItem child :  children) {
			if(!(child instanceof ICpExpression)){
				continue;
			}
			ICpExpression expr = (ICpExpression)child;
			EEvaluationResult res =  getCachedResult(expr);
			if(res == EEvaluationResult.IGNORED || res == EEvaluationResult.UNDEFINED || res == EEvaluationResult.FULFILLED){
				continue;
			}
			
			if(expr.getExpressionType() == ICpExpression.ACCEPT_EXPRESSION) {
				if(res.ordinal() < overallResult.ordinal()){
					continue; // ignored 
				}
			} else {
				if(res.ordinal() > overallResult.ordinal()){
					continue;
				}	
			}
			boolean bDeny = tbDeny; // save deny context  
			if(expr.getExpressionType() == ICpExpression.DENY_EXPRESSION){
				tbDeny = !tbDeny; // invert the deny context 
			}

			if(expr.getExpressionDomain() == ICpExpression.REFERENCE_EXPRESSION) {
				collectDependencies(depRes, expr.getCondition(), overallResult);
			} else if(expr.getExpressionDomain() == ICpExpression.COMPONENT_EXPRESSION) {
				ILiteDependency dep = getDependency(expr);
				if(dep != null) {
					depRes.addDependency(dep);
				}
			}
			tbDeny = bDeny; // restore deny context
		}
	}


	@Override
	public EEvaluationResult evaluateExpression(ICpExpression expression) {
		if(expression == null){
			return EEvaluationResult.IGNORED;
		}
		
		switch(expression.getExpressionDomain()) {
		case ICpExpression.COMPONENT_EXPRESSION:
			return evaluateDependency(expression);
		case ICpExpression.REFERENCE_EXPRESSION:
			return evaluate(expression.getCondition()); 
		
		case ICpExpression.DEVICE_EXPRESSION:
		case ICpExpression.TOOLCHAIN_EXPRESSION:
			return EEvaluationResult.IGNORED;
		default: 
			break;
		}
		return EEvaluationResult.ERROR;
	}


	protected EEvaluationResult evaluateDependency( ICpExpression expression) {
		if(liteModel == null){
			return EEvaluationResult.IGNORED; // nothing to do
		}
		
		ILiteDependency dep = getDependency(expression); 
		if(dep == null){
			dep = new LiteDependency(expression, tbDeny);
			if(tbDeny) {
				EEvaluationResult res = evaluateDenyDependency(dep);
				dep.setEvaluationResult(res);
			} else {
				ILiteComponentItem components = liteModel.getComponents();
				if(components != null){
					components.findComponents(dep);
				}
			} 
			putDependency(expression, dep);
		}
		
		EEvaluationResult result = dep.getEvaluationResult();
		return result;
	}

	protected EEvaluationResult evaluateDenyDependency(ILiteDependency dep) {
		EEvaluationResult res = EEvaluationResult.FULFILLED;
		Collection<ILiteComponent> selectedComponents = getSelectedComponents(); 
		if(selectedComponents == null || selectedComponents.isEmpty()){
			return res;
		}

		IAttributes attr = dep.getCpItem().attributes();
		for(ILiteComponent liteComponent : selectedComponents) {
			ICpComponent c = liteComponent.getActiveCpComponent();
			if(c == null){
				continue; // should not happen
			}
					
			if(attr.matchAttributes(c.attributes())) {
				res = EEvaluationResult.INCOMPATIBLE;
				dep.addComponent(liteComponent, res);
			}
		}
		return res;
	}


	protected ILiteDependency getDependency(ICpExpression expression) {
		if(tbDeny) { // cache deny results separately
			if(fDenyDependencies != null){
				return fDenyDependencies.get(expression);
			}
		} else if(fDependencies != null) {
			return fDependencies.get(expression);
		}
		return null;
	}

	protected void putDependency(ICpExpression expression,  ILiteDependency dep ) {
		if(tbDeny) { // cache deny results separately
			if(fDenyDependencies == null){
				fDenyDependencies = new HashMap<ICpExpression, ILiteDependency>();
			}
			fDenyDependencies.put(expression, dep);
			
		} else {
			if(fDependencies == null){
				fDependencies = new HashMap<ICpExpression, ILiteDependency>();
			}
			fDependencies.put(expression, dep);
		}
	}


	@Override
	public ILiteDependencyItem getDependencyItem(ILiteComponentItem componentItem) {
		if(fDependencyItems != null){
			return fDependencyItems.get(componentItem);
		}
		return null;
	}


	@Override
	public EEvaluationResult evaluateDependencies() {
		resetResult();
		if(liteModel == null){
			return EEvaluationResult.IGNORED; // nothing to do
		}
		
		fDependencyItems = new LinkedHashMap<ILiteComponentItem, ILiteDependencyItem>();
		ILiteComponentItem devClass = getSelectedDeviceClass();
		// first check if the selected device is available
		ICpDeviceInfo di = liteModel.getDeviceInfo();
		if(devClass != null && di != null) {
			if(di.getDevice() == null){
				fResult = EEvaluationResult.FAILED;
				ILiteDependencyResult depRes = new LiteMissingDeviceResult(devClass, di);
				fDependencyItems.put(devClass, depRes);
				cacheConditionResult(devClass, fResult);
				return fResult; // missing device => no use to evaluate something else 
			}
			cacheConditionResult(devClass, EEvaluationResult.FULFILLED);
		}
		
		// report missing components and gpdsc files
		Collection<ILiteComponent> usedComponents = getUsedComponents();
		if(usedComponents != null && !usedComponents.isEmpty()) {
			for(ILiteComponent component : usedComponents){
				if(!component.isSelected()) {
					continue;
				}
				ICpComponentInfo ci = component.getActiveCpComponentInfo();
				if(ci == null){
					continue;
				}
				EEvaluationResult r = EEvaluationResult.IGNORED; 
				
				ILiteDependencyResult depRes = null;
				if(ci.getComponent() != null) {
					if(ci.isGenerated() || !ci.isSaved()){
						continue;
					}
					String gpdsc = ci.getGpdsc(true);
					if(gpdsc == null){
						continue;
					}
					ICpPack pack = liteModel.getGeneratedPack(gpdsc);
					if(pack != null){
						continue;
					}
					r = EEvaluationResult.MISSING_GPDSC;
					depRes = new LiteMissingGpdscResult(component, gpdsc);
				} else {
					r = ci.getEvaluationResult();
					depRes = new LiteMissingComponentResult(component);
					updateEvaluationResult(EEvaluationResult.FAILED);
				}

				depRes.setEvaluationResult(r);
				fDependencyItems.put(component, depRes);
				
				cacheConditionResult(component, r);
				cacheConditionResult(component.getParentClass(), r);
				cacheConditionResult(component.getParentGroup(), r);
				
				updateEvaluationResult(r);
			}
		}
		
		Collection<ILiteComponent> selectedComponents = getSelectedComponents(); 
		if(selectedComponents == null || selectedComponents.isEmpty()){
			return getEvaluationResult();
		}
		
		// sorted map : MISSING comes earlier than SELECTABLE
		Map<ILiteComponent, ILiteDependencyResult> componentResults = new TreeMap<ILiteComponent, ILiteDependencyResult>(new ComponentResultComparator());
		Map<ILiteComponentGroup, ILiteDependency> apiConflicts = new HashMap<ILiteComponentGroup, ILiteDependency>();
		for(ILiteComponent component : selectedComponents){
			ICpComponent c = component.getActiveCpComponent();
			if(c == null || c instanceof ICpComponentInfo){
				continue;
			}
			EEvaluationResult r = evaluate(c);

			updateEvaluationResult(r);

			cacheConditionResult(component, r);
			cacheConditionResult(component.getParentClass(), r);

			if(r.ordinal() < EEvaluationResult.FULFILLED.ordinal()) {
				ILiteDependencyResult  depRes = new LiteDependencyResult(component);
				ICpItem condition = c.getCondition();
				if(r != EEvaluationResult.ERROR){
					collectDependencies(depRes, condition, r);
				}
				depRes.setEvaluationResult(r);
				componentResults.put(component, depRes);
			}
			
			ILiteComponentGroup g = component.getParentGroup();
			cacheConditionResult(g, r);
			//	 check for missing APIs and  API conflicts  
			ICpComponent api = g.getApi();
			if(api != null) {
				if(api instanceof ICpComponentInfo ) {
					ICpComponentInfo apiInfo = (ICpComponentInfo)api;
					if(apiInfo.getComponent() == null) {
						r = EEvaluationResult.MISSING_API;
						ILiteDependencyResult depRes = new LiteMissingComponentResult(g);
						depRes.setEvaluationResult(r);
						fDependencyItems.put(g, depRes);
						cacheConditionResult(g, r);
						cacheConditionResult(g.getParentClass(), r);
						fResult = EEvaluationResult.FAILED;
					}
				} else if(api.isExclusive()) {
					ILiteDependency d = apiConflicts.get(g);
					if(d == null) {
						d = new LiteDependency(api, true);
						apiConflicts.put(g, d);
					}
					d.addComponent(component, r);
				}
			}
		}
		if(!fDependencyItems.isEmpty())
			return getEvaluationResult(); // no need to evaluate further if components or APIs are missing 
		
		// add API evaluation results
		for(Entry<ILiteComponentGroup, ILiteDependency> e : apiConflicts.entrySet()) {
			ILiteDependency d = e.getValue();
			if(d.getChildCount() > 1) {
				d.setEvaluationResult(EEvaluationResult.CONFLICT);
				ILiteComponentGroup g = e.getKey(); 
				fDependencyItems.put(g, d);
				cacheConditionResult(g, EEvaluationResult.CONFLICT);
				cacheConditionResult(g.getParentClass(), EEvaluationResult.CONFLICT);
				if(fResult.ordinal() > EEvaluationResult.CONFLICT.ordinal() ){
					fResult = EEvaluationResult.CONFLICT;
				}
			}
		}
		// finally add sorted dependency results
		for(Entry<ILiteComponent, ILiteDependencyResult> e : componentResults.entrySet()) {
			ILiteComponent c = e.getKey();
			ILiteDependencyResult r = e.getValue();
			fDependencyItems.put(c, r);
		}
		purgeResults();
		return getEvaluationResult();
	}
	
	// remove all items that are higher than overall result  
	protected void purgeResults() {
	 Iterator<ILiteDependencyItem> iterator = fDependencyItems.values().iterator();
		while(iterator.hasNext()) {
			ILiteDependencyItem d = iterator.next();
			EEvaluationResult res = d.getEvaluationResult();
			if(res.ordinal() > fResult.ordinal()) {
				iterator.remove();
			}
		}
	}

	
	
	protected ILiteComponentItem getSelectedDeviceClass(){
		return liteModel.getComponents().getFirstChild(CmsisConstants.EMPTY_STRING); // always first
	}
	

	protected void cacheConditionResult(ILiteComponentItem item, EEvaluationResult res) {
		if(item == null){
			return;
		}
		if(getEvaluationResult(item).ordinal() <= res.ordinal()){
			return;
		}

		if(fEvaluationResults == null){
			fEvaluationResults = new HashMap<ILiteComponentItem, EEvaluationResult>(); 
		}
		fEvaluationResults.put(item, res);
	}
	
	
	@Override
	public EEvaluationResult getEvaluationResult(ILiteComponentItem item) {
		if(fEvaluationResults != null) {
			EEvaluationResult res = fEvaluationResults.get(item);
			if(res != null){
				return res;
			}
		}
		return EEvaluationResult.IGNORED;
	}


	@Override
	public EEvaluationResult resolveDependencies() {
		// try to run resolve iteration until all dependencies are resolved or no resolution is available
		while(fDependencyItems != null && getEvaluationResult().ordinal() < EEvaluationResult.FULFILLED.ordinal())
		{	
			if(resolveIteration() == false){
				break;
			}
		}
		return getEvaluationResult();
	}
	
	/**
	 * Tries to resolve SELECTABLE dependencies 
	 * @return true if one of dependencies gets resolved => the state changes 
	 */
	protected boolean resolveIteration(){
		for(ILiteDependencyItem depItem : fDependencyItems.values()) {
			if(resolveDependency(depItem)){
				return true;
			}
		}
		return false;
	}
	
	
	protected boolean resolveDependency(ILiteDependencyItem depItem){
		if(depItem.getEvaluationResult() != EEvaluationResult.SELECTABLE){
			return false;
		}

		if(depItem instanceof ILiteDependencyResult) { 
			ILiteDependencyResult depRes = (ILiteDependencyResult)depItem; 
			Collection<ILiteDependency> deps = depRes.getDependencies();
			if(deps == null){
				return false;
			}
			
			for(ILiteDependency d : deps) {
				if(resolveDependency(d)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	protected boolean resolveDependency(ILiteDependency dependency){
		if(dependency.getEvaluationResult() != EEvaluationResult.SELECTABLE){
			return false;
		}
		
		ILiteComponent c = dependency.getBestMatch();
		if(c != null) {
			liteModel.selectComponent(c, 1);
			liteModel.evaluateComponentDependencies(); // re-evaluate dependencies to remove resolved ones
			return true;
		}
		return false;
	}


	@Override
	public Collection<? extends ILiteDependencyItem> getDependencyItems() {
		if(fDependencyItems != null) {
			return fDependencyItems.values();
		}
		return null;
	}
}
