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
package com.lembed.lite.studio.manager.analysis.editor.elf.parser;

import org.eclipse.core.resources.IProject;
import com.lembed.lite.studio.manager.analysis.editor.command.IEditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.elf.model.ElfRawParserResult;

public class ElfRawParser extends ElfParserBase {

	public ElfRawParser(IEditorConsole editorConsole, String binaryPath) {
		super(editorConsole, binaryPath);
		addArguments(" -a ");
	}

	
	@Override
	public ParserResult parseResult(String line, IProject project) {

		return new ElfRawParserResult(line);		
	}

}
