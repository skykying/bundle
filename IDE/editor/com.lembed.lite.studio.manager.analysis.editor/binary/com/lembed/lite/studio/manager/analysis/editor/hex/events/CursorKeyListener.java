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
package com.lembed.lite.studio.manager.analysis.editor.hex.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

import com.lembed.lite.studio.manager.analysis.editor.binary.Activator;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexEditorConstants;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexManager;
import com.lembed.lite.studio.manager.analysis.editor.hex.editors.HexEditor;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexEditorControl;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexTable;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexTablePointer;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.jface.preference.PreferenceConverter;



public class CursorKeyListener extends KeyAdapter {

	private final HexEditor hexEditor;
	private final HexEditorControl hexEditorControl;
	private final TableCursor cursor;
	private final ControlEditor editor;
	private Text text = null;
	private CellEditorKeyListener cellEditorKeyListener = null;
	private boolean shiftHold;

	private int eventColumnIndex;
	private int eventRowIndex;
	
	/**
	 * Creates a key listener for table cursor.
	 *
	 * @param hexEditorControl the hex editor control
	 * @param hexEditor the hex editor
	 * @param cursor the cursor
	 * @param editor the editor
	 */
	public CursorKeyListener(final HexEditorControl hexEditorControl, final HexEditor hexEditor, final TableCursor cursor, final ControlEditor editor) {
		super();
		this.hexEditorControl = hexEditorControl;
		this.hexEditor = hexEditor;
		this.cursor = cursor;
		this.editor = editor;
	}

	@Override
    public void keyReleased(KeyEvent e) {
		if (e.keyCode == SWT.SHIFT)
			shiftHold = false;
	}

	private byte[] cmpBuffer;
	/**
	 * Event handler for "key pressed" event
	 * @param e an event containing information about the key press
	 */
	@Override
    public void keyPressed(KeyEvent e) {
		shiftHold = (e.stateMask & SWT.SHIFT) > 0 || e.keyCode == SWT.SHIFT;
		if (hexEditor.isReadOnly()) {
			//
			// File is read-only - do nothing
			//
			return;
		} // if
		
		if (Activator.getDefault().getPreferenceStore().getBoolean(HexEditorConstants.PROPERTY_DEBUG_MODE)) {
			if (e.character == 'c') {
				if (cmpBuffer != null) {
					System.out.println("comparing..."); //$NON-NLS-1$
					if (hexEditorControl.getHexTable().getBufferSize() != cmpBuffer.length) {
						System.out.println("they differ in length"); //$NON-NLS-1$
					} else {
						byte[] bs = new byte[cmpBuffer.length];
						hexEditorControl.getHexTable().getData(bs, 0, bs.length);
						boolean equal = true;
						int positions = 0;
						for (int i = 0; i < bs.length; i++) {
							if (bs[i] == cmpBuffer[i])
								continue;
							positions++;
							equal = false;
						}
						if (equal) {
							System.out.println("they are equal!"); //$NON-NLS-1$
						} else {
							System.out.println("they differ at " + positions + " positions");				 //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				} else {
					System.out.println("no cmp buffer!"); //$NON-NLS-1$
				}
				return;
			}
			if (e.character == 's'){
				System.out.println("saving..."); //$NON-NLS-1$
				cmpBuffer = new byte[hexEditorControl.getHexTable().getBufferSize()];
				hexEditorControl.getHexTable().getData(cmpBuffer, 0, cmpBuffer.length);
				return;
			}
		}
		
		if (e.keyCode == SWT.INSERT) {
			keyInsert();
			hexEditorControl.updateStatusPanel();
			return;
		} // if
		
		if (e.character != ',' && Character.digit(e.character, 16) == -1) {
			//
			// Invalid hex digit - ignore
			//
			return;
		} // if
		
		//
		// Valid hex digit
		//
		TableItem row = cursor.getRow();
		int column = cursor.getColumn();
		
		if (column == 0 || column > HexEditorConstants.TABLE_NUM_DATA_COLUMNS) {
			//
			// Wrong column - ignore
			//
			return;
		} // if
		
		if (row.getText(column).compareTo(HexEditorConstants.TABLE_EMPTY_CELL) == 0) {
			//
			// Cursor is out of file (over the cell without a content)
			//
			return;
		} // if
				
		//
		// Following operations are allowed only for columns 1...16 (actual hex view)
		//
		text = new Text(cursor, SWT.NONE);
		text.insert("" + e.character); //$NON-NLS-1$
		text.setFont(HexManager.getFont(PreferenceConverter.getFontData(Activator.getDefault().getPreferenceStore(), HexEditorConstants.PROPERTY_FONT)));
		text.setBackground(HexManager.getColor(PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), HexEditorConstants.PROPERTY_COLOR_BACKGROUND_EDITOR)));
		text.setForeground(HexManager.getColor(PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), HexEditorConstants.PROPERTY_COLOR_FOREGROUND_EDITOR)));
		text.setTextLimit(2);
		editor.setEditor(text);
		text.setFocus();

		//
		// Remember where the text editor was opened
		//
		HexTable table = hexEditorControl.getHexTable();
		int rowIndex = table.getSelectionIndex();
		eventColumnIndex = cursor.getColumn();
		eventRowIndex = rowIndex;

		//
		// Add key listener to cell editor (Text component)
		//
		cellEditorKeyListener = new CellEditorKeyListener(hexEditorControl, hexEditor, cursor, editor, text);
		text.addKeyListener(cellEditorKeyListener);
	}

	private void keyInsert() {
		//
		// Insert new byte into the table
		//
		HexTablePointer position = hexEditorControl.getCursorPosition();
			
		if (position.getColumnIndex() < 0 || position.getColumnIndex() >= HexEditorConstants.TABLE_NUM_DATA_COLUMNS) {
			//
			// Ignore - data insertion is not allowed
			//
			return;
		} // if
			
		//
		// Insert one table cell with default value 00h
		//
		hexEditorControl.insertData(1, 0, position.getRowIndex(), position.getColumnIndex());
	}

	/**
	 * Closed the cell editor if one is active without modifying the data
	 */
	public void closeCellEditor() {
		if (text != null && !text.isDisposed())
			text.dispose();
	}

	/**
	 * Gets the cell editor key listener.
	 *
	 * @return the cell editor key listener
	 */
	public CellEditorKeyListener getCellEditorKeyListener() {
		return cellEditorKeyListener;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public Text getText() {
		return (text == null || text.isDisposed()) ? null : text;
	}
	
	/**
	 * Checks if is shift hold.
	 *
	 * @return true, if is shift hold
	 */
	public boolean isShiftHold() {
		return shiftHold;
	}
	
	/**
	 * Sets the selection.
	 */
	public void setSelection() {
		cursor.setSelection(eventRowIndex, eventColumnIndex);
	}

	/**
	 * Gets the event row.
	 *
	 * @return the event row
	 */
	public int getEventRow() {
		return eventRowIndex;
	}
	
	/**
	 * Gets the event column.
	 *
	 * @return the event column
	 */
	public int getEventColumn() {
		return eventColumnIndex;
	}
}
