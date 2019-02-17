/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.render.peripheral;

/**
 * The listener interface for receiving ILinkToolTip events.
 * The class that is interested in processing a ILinkToolTip
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addILinkToolTipListener<code> method. When
 * the ILinkToolTip event occurs, that object's appropriate
 * method is invoked.
 *
 */
public abstract interface ILinkToolTipListener {

	// ------------------------------------------------------------------------

	/**
	 * Link selected.
	 *
	 * @param link the link
	 */
	public abstract void linkSelected(String link);

	// ------------------------------------------------------------------------
}
