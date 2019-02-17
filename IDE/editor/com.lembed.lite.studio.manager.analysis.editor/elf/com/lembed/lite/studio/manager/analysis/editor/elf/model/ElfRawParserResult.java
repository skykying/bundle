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
package com.lembed.lite.studio.manager.analysis.editor.elf.model;

import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;

public class ElfRawParserResult extends ParserResult{

	private static final String[] titles = {"Index","Value","Size","Type","Bind","Vis","Ndx","Name"};
	String _value;

	public ElfRawParserResult(String line) {	
		_value = line;
	}


	@Override
	public String[] getTitles() {
		return titles;
	}

	@Override
	public int size() {
		return 8;
	}

	@Override
	public List<Integer> bound() {
		 List<Integer> results = new LinkedList<Integer>();
		return results;
	}

	@Override
	public List<String> get_values() {
		 List<String> results = new LinkedList<String>();
		 results.add(_value);
		return results;
	}
}
