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
package com.lembed.lite.studio.manager.analysis.editor.elf.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import com.lembed.lite.studio.manager.analysis.editor.elf.cdt.elf;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;

/**
 * This class is used to represent the elements in the outline view.
 * https://www.ibm.com/developerworks/cn/opensource/os-cn-eclipse-outline/index.html
 */
public class ElfOutlineElement {

	/** The element is part of a mapping */
	public static final int LINKER_ITEM 	= 1;

	/** The element is part of a sequence */
	public static final int GROUP_ITEM 		= 2;

	/** The element is a document element */
	public static final int SECTION_ITEM 	= 3;
	

	protected elf.Symbol symbol;
	protected String key="key";
	protected String value="value";
	
	protected ElfOutlineElement parent;
	protected List<ElfOutlineElement> children = new ArrayList<ElfOutlineElement>();
	
	protected Position position;
	protected IDocument document;
	protected int type;

	protected List<Integer> sectionPath = new ArrayList<Integer>();


	protected ElfOutlineElement(elf.Symbol section, IDocument document) {
		this(section, null, SECTION_ITEM, document);
	}


	protected ElfOutlineElement(elf.Symbol symbol, ElfOutlineElement parent, int type, IDocument document) {
		this.symbol = symbol;
		this.parent = parent;
		this.type = type;
		this.document = document;

		if (this.parent != null) {
			this.sectionPath = parent.sectionPath;
		}
		this.sectionPath.add(System.identityHashCode(this.symbol));
		
		// child element parser here
//		parseSection(section);
		position = getPosition(symbol);

		try {
			document.addPosition(ElfContentOutlinePage.LINKERFILESEGMENT, position);
		} catch (BadLocationException e) {
			EditorLog.logger.warning(e.toString());
		} catch (BadPositionCategoryException e) {
			EditorLog.logger.warning(e.toString());
		}
	}


	private Position getPosition(elf.Symbol symbol) {
		
		int leng = (int) symbol.st_size;
		int off = symbol.st_shndx;
		Position p = null;
		p = new Position(off, leng);
		
		return p;
	}

	@Override
	public String toString() {

		if (type == ElfOutlineElement.SECTION_ITEM) {
			return "";
		} else if (type == ElfOutlineElement.LINKER_ITEM) {
			if (0 == children.size()) {
				return key + ": " + symbol.toString();
			} else {
				return key;
			}
		} else if (type == ElfOutlineElement.GROUP_ITEM) {
			if (0 == children.size()) {
				return symbol.toString();
			} else {
				return "";
			}
		}

		return super.toString();
	}

}
