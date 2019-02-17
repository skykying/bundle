/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.hex;

import org.eclipse.swt.widgets.Composite;

import java.util.*;
import java.io.UnsupportedEncodingException;

/**
 * The Class HexEncoder.
 */
public class HexEncoder {
	private Vector<String> encodingTypes = new Vector<>();
	
	/**
	 * Loads the encoding types from plugin property file
	 * @param parent Composite
	 */
	public HexEncoder(Composite parent) {
		int encodingDefault = 0;
		
//		String encodingList = PropertyChangeListener.getResourceString("encoding.list"); //$NON-NLS-1$
//		
//		if (encodingList == null) {
			//
			// Missing 'encoding.list' parameter - set default
			//
		String encodingList = "ASCII"; //$NON-NLS-1$
//		}
		
//		try {
//			//
//			// Get index of the default encoding from EHEP property file
//			//
//			encodingDefault = Integer.parseInt(PropertyChangeListener.getResourceString("encoding.default")); //$NON-NLS-1$
//		}
//		catch (NumberFormatException e) {
			//
			// Missing or incorrect 'encoding.default' parameter - set default
			//
			encodingDefault = 0;
//		}
		
		String encodingName;
		StringTokenizer st = new StringTokenizer(encodingList, ","); //$NON-NLS-1$
		
		while (st.hasMoreTokens()) {
			encodingName = st.nextToken();
			if (isEncodingSupported(encodingName)) {
				//
				// Encoding is supported, add it to the vector
				//
				encodingTypes.add(encodingName);
			} // if
		} // while
		
		//
		// Move default encoding at the beginning of the vector
		//
		if (encodingDefault > 0) {
			String s = encodingTypes.get(encodingDefault);
			encodingTypes.removeElementAt(encodingDefault);
			encodingTypes.insertElementAt(s, 0);
		} // if
	}

	/**
	 * Returns vector with encoding types (canonical names)
	 * @return vector with encoding types (canonical names)
	 */
	public Vector<String> getEncodingTypes() {
		return encodingTypes;
	}

	/**
	 * Checks whether the particular encoding is really supported by installed JRE or not<br/>
	 * Bugfix #847459 (see http://sourceforge.net/projects/ehep)
	 * @param encodingName canonical encoding name
	 * @return true if encoding is supported otherwise false
	 */
	private static boolean isEncodingSupported(String encodingName) {
		//
		// Testing data sample
		//
		byte[] b = {-128, -127, -126, -125, 0, 1, 2, 3, 124, 125, 126, 127};
		String s;
		
		try {
			//
			// Try to create a string from byte array (data sample) with required encoding
			//
			s = new String(b, encodingName);
			s.getClass();
		}
		catch (UnsupportedEncodingException e) {
			//
			// Oops, JRE doesn't support this encoding :o(
			//
			return false;
		}
		//
		// Sure, this encoding is supported :o)
		//
		return true;
	}
}
