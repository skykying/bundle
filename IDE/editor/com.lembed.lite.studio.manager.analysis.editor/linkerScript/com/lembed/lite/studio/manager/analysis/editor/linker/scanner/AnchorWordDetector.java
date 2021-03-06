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
package com.lembed.lite.studio.manager.analysis.editor.linker.scanner;

import java.util.regex.*;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Word detector used by the scanner rule for anchors and in the scanner rule
 * for aliases. 
 */
public class AnchorWordDetector implements IWordDetector {

	private Pattern characterPattern = Pattern.compile( "[\\w\\d]" );

	public boolean isWordPart(char c) {
		
		Matcher m = characterPattern.matcher( new Character(c).toString() );
		if( m.matches() ){
			return true;
		}
		
		return false;
	}

	public boolean isWordStart(char c) {

		Matcher m = characterPattern.matcher( new Character(c).toString() );
		if( m.matches() ){
			return true;
		}
		
		return false;

	}

}
