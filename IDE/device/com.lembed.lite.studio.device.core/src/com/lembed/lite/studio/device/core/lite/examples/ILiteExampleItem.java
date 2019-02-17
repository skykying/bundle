/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.core.lite.examples;

import java.util.Collection;

import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.item.ICmsisMapItem;

/**
 * Interface that represents an RTE view on an example
 */
public interface ILiteExampleItem extends ICmsisMapItem<ILiteExampleItem> {

	/**
	 * Add the cmsis example
	 * @param item the example item
	 */
	void addExample(ICpExample item);

	/**
	 * Add examples from supplied pack
	 * @param pack IcpPack to add examples from
	 */
	void addExamples(ICpPack pack);

	/**
	 * Remove the cmsis example
	 * @param item the example item
	 */
	void removeExample(ICpExample item);

	/**
	 * Remove examples from supplied pack
	 * @param pack IcpPack to remove examples from
	 */
	void removeExamples(ICpPack pack);

	/**
	 * Get the cmsis example in this RTE example item
	 * @return the cmsis example contained in this RTE example
	 */
	ICpExample getExample();

	/**
	 * Get a collection of cmsis examples in this RTE example
	 * @return a collection of cmsis examples
	 */
	Collection<ICpExample> getExamples();


}
