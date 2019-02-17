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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 *  The class represent a result of missing device description  
 */
public class LiteMissingDeviceResult extends LiteDependencyResult {
	protected ICpDeviceInfo fDeviceInfo = null; 
	
	public LiteMissingDeviceResult(ILiteComponentItem componentItem, ICpDeviceInfo deviceInfo) {
		super(componentItem);
		fDeviceInfo = deviceInfo;
		setEvaluationResult(deviceInfo.getEvaluationResult());
	}

	@Override
	public String getDescription() {
		EEvaluationResult res = fDeviceInfo.getEvaluationResult();

		String state; 
		switch(res){
		case FAILED:
		case MISSING:
			state = CpStrings.IsMissing;
			break;
		case UNAVAILABLE_PACK:
			state = CpStrings.IsNotAvailableFoCurrentConfiguration;
			break;
		default:
			return super.getDescription();
		}
		String reason = CmsisConstants.EMPTY_STRING;
		reason = CpStrings.Pack + " "; //$NON-NLS-1$
		ICpPackInfo pi = fDeviceInfo.getPackInfo();
		String packId = pi.isVersionFixed() ? pi.getId() : pi.getPackFamilyId();
		if(pi.getPack() == null) {
			reason += CpStrings.IsNotInstalled;  
		} else {
			reason += CpStrings.IsExcluded;
		}
		reason += ": " + packId; //$NON-NLS-1$
		String s = CpStrings.Device + " " + state + ". " +reason;  //$NON-NLS-1$//$NON-NLS-2$
		return s;
	}

	@Override
	public boolean isMaster() {
		return true;
	}
	
	
}
