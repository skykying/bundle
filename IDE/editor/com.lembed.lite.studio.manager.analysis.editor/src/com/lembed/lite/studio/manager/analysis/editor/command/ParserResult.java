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
package com.lembed.lite.studio.manager.analysis.editor.command;

import java.util.List;

public abstract class ParserResult {
	
	public void log(String msg){
		System.out.println(msg);
	}

	public abstract String[] getTitles();

	public abstract int size();

	public abstract List<Integer> bound();

	public abstract List<String> get_values();


}
