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



import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

import com.lembed.lite.studio.manager.analysis.editor.hex.HexEditorConstants;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexUtils;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;
import com.lembed.lite.studio.manager.analysis.editor.hex.editors.HexEditor;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexEditorControl;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexTable;


/**
 * The listener interface for receiving cellEditorKey events.
 * The class that is interested in processing a cellEditorKey
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCellEditorKeyListener<code> method. When
 * the cellEditorKey event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CellEditorKeyEvent
 */
public class CellEditorKeyListener extends KeyAdapter {
	private final HexEditor hexEditor;
	private final HexEditorControl hexEditorControl;
	private final TableCursor cursor;
	private final Text text;

	/**
	 * Creates a key listener for table cell editor
	 * @param hexEditorControl
	 * @param hexEditor
	 * @param cursor
	 * @param editor
	 * @param text
	 */
	public CellEditorKeyListener(final HexEditorControl hexEditorControl, final HexEditor hexEditor, final TableCursor cursor, final ControlEditor editor, final Text text) {
		super();
		this.hexEditorControl = hexEditorControl;
		this.hexEditor = hexEditor;
		this.cursor = cursor;
		this.text = text;
	}

	/**
	 * Event handler for "key pressed" event
	 * @param e an event containing information about the key press
	 */
	@Override
    public void keyPressed(KeyEvent e) {
		//
		// Close the text editor and copy the data over when the user hits "ENTER"
		//
		if (e.character == SWT.CR) {
			keyPressedEnter();
			return;
		} // if CR

		//
		// Close the text editor when the user hits "ESC"
		//
		if (e.character == SWT.ESC) {
			//
			// Release cell editor, no changes in the table cell
			//
			text.dispose();
			hexEditorControl.updateStatusPanel();
			return;
		} // if ESC

		if (e.character == SWT.DEL || e.character < ' ') {
			//
			// User pressed 'Delete' or other control key
			//
			return;
		}

		boolean isHexaNumeric = (e.character >= '0' && e.character <= '9') || (e.character >= 'A' && e.character <= 'F') || (e.character >= 'a' && e.character <= 'f');
		boolean commaSpecified = text.getText().length() > 0 && text.getText().charAt(0) == ',';
		if (isHexaNumeric || commaSpecified) {
			//
			// User pressed the valid hex digit
			//
			if (text.getText().length() == 1) {
				String result = commaSpecified ? Integer.toHexString(e.character) : text.getText() + e.character;
					
				//
				// The cell contains valid 2-digit hex number - close the cell editor (pressing 'Enter' is not necessary)
				//
				int itemIndex = cursor.getRow().getParent().indexOf(cursor.getRow());
				if (HexUtils.isValidHexNumber(result)) {
					closeCellEditor(itemIndex, cursor.getColumn() - 1, result);
					//
					// Move cursor position to the next data cell
					//
					goToNextCell();
				}
			} // if
			
			hexEditorControl.updateStatusPanel();
		} // if
	} // keyPressed()

	/**
	 * Closes the cell editor and updates the new value in the table
	 * @param row table row where is the cell to close
	 * @param column specify the cell to close
	 * @param str text to be updated in the table cell (taken from cell editor) 
	 */
	public void closeCellEditor(int row, int column, String str) {
		//
		// Store the new value from ControlEditor into the table
		// row.setText(column, str.toUpperCase())
		hexEditorControl.modify(row,column, HexUtils.string2bytes(str.toUpperCase()));
		//
		// Release cell editor
		//
		text.dispose();
	}

	/**
	 * Moves cursor (data cell selector) to the next data cell
	 */
	final void goToNextCell() {
		int column = cursor.getColumn() - 1;
		HexTable table = hexEditorControl.getHexTable();
		int row = table.getSelectionIndex();

		if (row * HexEditorConstants.TABLE_NUM_DATA_COLUMNS + column >= table.getBufferSize())
			return;

		if (++column >= HexEditorConstants.TABLE_NUM_DATA_COLUMNS) {
			//
			// Last table cell at this row - go to next table row if possible
			//
			int numTableRows = table.getItemCount();
			if (row+1 == numTableRows) {
				//
				// Last data cell at the last table row reached - do not move the cursor
				//
				return;
			}
			//
			// Move cursor to the first data cell (column #1)
			//
			column = 0;
			row++;
			//
			// Move table row selector
			//
			table.setSelection(row);
		}
		//
		// Move table cell selector
		//
		cursor.setSelection(row, column + 1);
	}

	public void keyPressedEnter() {
		int itemIndex = cursor.getRow().getParent().indexOf(cursor.getRow());
		int column = cursor.getColumn() - 1;
		String str = text.getText();
			
		if (str.length() == 0) {
			//
			// Replace empty string with 00
			//
			str = "00"; //$NON-NLS-1$
		} else if (str.length() == 1) {
			//
			// Add zero padding
			//
			str = "0" + str; //$NON-NLS-1$
		}
		
		if (str.charAt(0) == ',')
			str = Integer.toHexString(str.charAt(1));

		if (HexUtils.isValidHexNumber(str)) {
			//
			// User entered a valid hex number - close the cell editor
			//
			closeCellEditor(itemIndex, column, str);
			//
			// Move cursor position to the next data cell
			//
			goToNextCell();

			hexEditorControl.updateStatusPanel();
		}
		else {
			//
			// String has incorrect format - do nothing
			//
			MessageDialog.openInformation(hexEditor.getSite().getShell(), HexEditorConstants.DIALOG_TITLE_ERROR, Messages.CellEditorKeyListener_2 + str.toUpperCase() + Messages.CellEditorKeyListener_3);
		} // else
	}
}
