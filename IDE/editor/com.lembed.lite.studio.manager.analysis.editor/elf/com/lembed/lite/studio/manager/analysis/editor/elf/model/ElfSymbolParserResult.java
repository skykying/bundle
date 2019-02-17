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

public class ElfSymbolParserResult extends ParserResult{

	private static final String[] titles = {"Index","Value","Size","Type","Bind","Vis","Ndx","Name"};
	
	String _index;
	String _value;
	String _size;
	String _type;
	String _bind;
	String _vis;
	String _ndx;
	String _name;
	
	public ElfSymbolParserResult(String key, String value) {		
		_index = key;
		_value = value;
	}
	
	

	public ElfSymbolParserResult(String _index, String _value, String _size, String _type, String _bind, String _vis,
			String _ndx, String _name) {
		super();
		this._index = _index;
		this._value = _value;
		this._size = _size;
		this._type = _type;
		this._bind = _bind;
		this._vis = _vis;
		this._ndx = _ndx;
		this._name = _name;
	}



	/**
	 * @return the _key
	 */
	public String get_key() {
		return _index;
	}

	/**
	 * @param _key the _key to set
	 */
	public void set_key(String _key) {
		this._index = _key;
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
		return 8;
	}

	@Override
	public List<Integer> bound() {
		 List<Integer> results = new LinkedList<Integer>();
		 results.add(50);
		 results.add(100);
		 
		 results.add(80);
		 results.add(80);
		 
		 results.add(80);
		 results.add(80);
		 
		 results.add(80);
		 results.add(200);
		return results;
	}

	@Override
	public List<String> get_values() {
		 List<String> results = new LinkedList<String>();
		 results.add(_index);
		 results.add(_value);
		 
		 results.add(_size);
		 results.add(_type);
		 
		 results.add(_bind);
		 results.add(_vis);
		 
		 results.add(_ndx);
		 results.add(_name);
		return results;
	}
}
