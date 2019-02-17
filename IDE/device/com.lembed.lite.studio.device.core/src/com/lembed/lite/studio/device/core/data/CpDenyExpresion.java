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

package com.lembed.lite.studio.device.core.data;

import com.lembed.lite.studio.device.core.enums.EEvaluationResult;

/**
 *
 */
public class CpDenyExpresion extends CpExpression {

	/**
	 * @param parent
	 * @param tag
	 */
	public CpDenyExpresion(ICpItem parent, String tag) {
		super(parent, tag);
	}

	@Override
	public EEvaluationResult evaluate(ICpConditionContext context) {
		EEvaluationResult result = super.evaluate(context);
		switch(result){
		case FULFILLED:
			if(getExpressionDomain() != ICpExpression.COMPONENT_EXPRESSION)
				return EEvaluationResult.INCOMPATIBLE;
		case UNDEFINED:
		case ERROR:
		case IGNORED:
		case INCOMPATIBLE:
			return result;
		default:
			break;
		}
		return EEvaluationResult.FULFILLED;
	}
}
