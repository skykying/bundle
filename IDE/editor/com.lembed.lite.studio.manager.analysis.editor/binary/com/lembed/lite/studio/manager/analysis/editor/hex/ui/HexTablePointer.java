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
package com.lembed.lite.studio.manager.analysis.editor.hex.ui;

import com.lembed.lite.studio.manager.analysis.editor.hex.HexEditorConstants;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;


public class HexTablePointer {

	private int rowIndex;
	private int columnIndex;
	
	public HexTablePointer(int rowIndex, int columnIndex) {
		super();
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public HexTablePointer(int offset) {
		move(offset);
	}

	/**
	 * Zero-based projection from the pointer position (row,column) to index 
	 * @return zero-based position index
	 */
	public int getOffset() {
		return rowIndex * HexEditorConstants.TABLE_NUM_DATA_COLUMNS + columnIndex;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public HexTablePointer move(int offset) {
		int newOffset = getOffset() + offset;
		rowIndex = newOffset / HexEditorConstants.TABLE_NUM_DATA_COLUMNS;
		columnIndex = newOffset % HexEditorConstants.TABLE_NUM_DATA_COLUMNS;
		return this;
	}
	
	public boolean equals(HexTablePointer p) {
		return (rowIndex == p.getRowIndex() && columnIndex == p.getColumnIndex());
	}

	public HexTablePointer adjust() {
		if (rowIndex < 0) rowIndex = 0;
		if (columnIndex < 0) columnIndex = 0;
		return this;
	}
	
	@Override
    public String toString() {
		return Messages.HexTablePointer_0 + rowIndex + Messages.HexTablePointer_1 + columnIndex + ")"; //$NON-NLS-3$
	}
}
