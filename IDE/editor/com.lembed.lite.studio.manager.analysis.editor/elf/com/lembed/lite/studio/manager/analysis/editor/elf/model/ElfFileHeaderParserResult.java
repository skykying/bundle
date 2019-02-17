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

public class ElfFileHeaderParserResult extends ParserResult{

	String _key;
	String _value;
	
	private static final String[] titles = {"Header","Value"};
	
	public ElfFileHeaderParserResult(String key, String value) {
		_key = key;
		_value = value;
	}
	
	/**
	 * @return the _key
	 */
	public String get_key() {
		return _key;
	}
	/**
	 * @param _key the _key to set
	 */
	public void set_key(String _key) {
		this._key = _key;
	}
	/**
	 * @return the _value
	 */
	public String get_value() {
		return _value;
	}
	/**
	 * @param _value the _value to set
	 */
	public void set_value(String _value) {
		this._value = _value;
	}

	@Override
	public String[] getTitles() {
		
		return titles;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public List<Integer> bound() {
		 List<Integer> results = new LinkedList<Integer>();
		 results.add(100);
		 results.add(100);
		
		return results;
	}

	@Override
	public List<String> get_values() {
		 List<String> results = new LinkedList<String>();
		 results.add(_key);
		 results.add(_value);
		return results;
	}
}
