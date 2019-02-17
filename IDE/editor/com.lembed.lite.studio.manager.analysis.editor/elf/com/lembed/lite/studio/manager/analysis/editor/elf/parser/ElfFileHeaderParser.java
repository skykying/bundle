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
import com.google.common.base.Strings;
import com.lembed.lite.studio.manager.analysis.editor.command.IEditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.elf.model.ElfFileHeaderParserResult;

public class ElfFileHeaderParser extends ElfParserBase {

	public ElfFileHeaderParser(IEditorConsole editorConsole, String binaryPath) {
		super(editorConsole, binaryPath);
		addArguments(" -h ");
	}

	@Override
	public ParserResult parseResult(String line, IProject project) {
		line = line.replaceAll(" +", " ").trim()+" ";
		
		String[] lineParts = line.split(DELIMITER, 2);
		if (lineParts.length < 2) {
			throw new IllegalArgumentException(
					"Not enough tokens in line '" + line + "'. Expected 2 tokens but got " + lineParts.length);
		}

		/**
		 * line should have the following format
		 * <file>;<line>;<severity>;<id>;<message>
		 * 
		 * TODO: <file> and <line> might be empty!
		 */
		try {

			String key;
			if (Strings.isNullOrEmpty(lineParts[0])) {
				key = null;
			} else {
				key = lineParts[0];
			}
			// if line is empty set it to -1
			String  value;
			if (Strings.isNullOrEmpty(lineParts[1])) {
				value = "";
			} else {
				value = lineParts[1];
			}
			
			return new ElfFileHeaderParserResult(key, value);

		} catch (NumberFormatException e2) {
			throw new IllegalArgumentException(
					"Could not parse the second token in line: '" + line + "' into an Integer", e2);
		}
	}
}
