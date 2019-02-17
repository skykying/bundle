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

package com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral;

/**
 * The Class PeripheralPath.
 */
public class PeripheralPath {

	// ------------------------------------------------------------------------

	private String[] fSegments = null;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral path.
	 */
	public PeripheralPath() {
		fSegments = new String[0];
	}

	/**
	 * Instantiates a new peripheral path.
	 *
	 * @param parentPath the parent path
	 * @param namePath the name path
	 */
	public PeripheralPath(PeripheralPath parentPath, PeripheralPath namePath) {
		String[] parentSegments = parentPath.getSegments();
		String[] nameSegments = namePath.getSegments();
		fSegments = new String[parentSegments.length + nameSegments.length];
		System.arraycopy(parentSegments, 0, fSegments, 0, parentSegments.length);
		System.arraycopy(nameSegments, 0, fSegments, parentSegments.length, nameSegments.length);
	}

	/**
	 * Instantiates a new peripheral path.
	 *
	 * @param name the name
	 */
	public PeripheralPath(String name) {
		fSegments = new String[] { name };
	}

	/**
	 * Instantiates a new peripheral path.
	 *
	 * @param parent the parent
	 * @param name the name
	 */
	public PeripheralPath(String parent, String name) {
		fSegments = new String[] { parent, name };
	}

	// ------------------------------------------------------------------------

	private String[] getSegments() {
		return fSegments;
	}

	/**
	 * To path.
	 *
	 * @return the string
	 */
	public String toPath() {
		return toStringBuilder().toString();
	}

	/**
	 * To path.
	 *
	 * @param addSlash the add slash
	 * @return the string
	 */
	public String toPath(boolean addSlash) {

		StringBuilder sb = toStringBuilder();
		if (addSlash) {
			sb.append('/');
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return toPath();
	}

	private StringBuilder toStringBuilder() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fSegments.length; i++)
			if (fSegments[i] != null) {
				if (sb.length() != 0) {
					sb.append('.');
				}
				sb.append(fSegments[i]);
			}
		return sb;
	}

	// ------------------------------------------------------------------------
}
