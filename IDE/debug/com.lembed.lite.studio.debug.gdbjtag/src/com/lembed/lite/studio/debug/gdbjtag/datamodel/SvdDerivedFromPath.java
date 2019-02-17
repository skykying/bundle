/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.datamodel;

/**
 * The Class SvdDerivedFromPath.
 */
public class SvdDerivedFromPath {

	// ------------------------------------------------------------------------

	/** The peripheral name. */
	public String peripheralName;
	
	/** The register name. */
	public String registerName;
	
	/** The field name. */
	public String fieldName;
	
	/** The enumeration name. */
	public String enumerationName;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new svd derived from path.
	 */
	public SvdDerivedFromPath() {
		peripheralName = null;
		registerName = null;
		fieldName = null;
		enumerationName = null;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return String.format("[P=%s,R=%s,F=%s,E=%s]", peripheralName, registerName, fieldName, enumerationName); //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	/**
	 * Creates the peripheral path.
	 *
	 * @param str the str
	 * @return the svd derived from path
	 */
	public static SvdDerivedFromPath createPeripheralPath(String str) {

		if (str == null) {
			return null;
		}

		String as[] = str.split("."); //$NON-NLS-1$

		SvdDerivedFromPath path = new SvdDerivedFromPath();
		if (as.length == 0) {
			path.peripheralName = str;
		} else {
			path.peripheralName = as[0];
		}

		return path;
	}

	/**
	 * Creates the register path.
	 *
	 * @param str the str
	 * @return the svd derived from path
	 */
	public static SvdDerivedFromPath createRegisterPath(String str) {

		if (str == null) {
			return null;
		}

		String as[] = str.split("."); //$NON-NLS-1$

		SvdDerivedFromPath path = new SvdDerivedFromPath();
		if (as.length == 0) {
			path.registerName = str;
		} else if (as.length == 1) {
			path.registerName = as[0];
		} else {
			path.peripheralName = as[0];
			path.registerName = as[1];
		}

		return path;
	}

	/**
	 * Creates the field path.
	 *
	 * @param str the str
	 * @return the svd derived from path
	 */
	public static SvdDerivedFromPath createFieldPath(String str) {

		if (str == null) {
			return null;
		}

		String as[] = str.split("."); //$NON-NLS-1$

		SvdDerivedFromPath path = new SvdDerivedFromPath();
		if (as.length == 0) {
			path.fieldName = str;
		} else if (as.length == 1) {
			path.fieldName = as[0];
		} else if (as.length == 1) {
			path.registerName = as[0];
			path.fieldName = as[1];
		} else {
			path.peripheralName = as[0];
			path.registerName = as[1];
			path.fieldName = as[2];
		}

		return path;
	}

	/**
	 * Creates the enumeration path.
	 *
	 * @param str the str
	 * @return the svd derived from path
	 */
	public static SvdDerivedFromPath createEnumerationPath(String str) {

		if (str == null) {
			return null;
		}

		String as[] = str.split("."); //$NON-NLS-1$

		SvdDerivedFromPath path = new SvdDerivedFromPath();
		if (as.length == 0) {
			path.enumerationName = str;
		} else if (as.length == 1) {
			path.enumerationName = as[0];
		} else if (as.length == 2) {
			path.fieldName = as[0];
			path.enumerationName = as[1];
		} else if (as.length == 3) {
			path.registerName = as[0];
			path.fieldName = as[1];
			path.enumerationName = as[2];
		} else {
			path.peripheralName = as[0];
			path.registerName = as[1];
			path.fieldName = as[2];
			path.enumerationName = as[3];
		}

		return path;
	}

	// ------------------------------------------------------------------------
}
