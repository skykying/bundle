/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.render.peripheral;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterFieldVMNode;

/**
 * The Class PeripheralEnumerationCellEditor.
 */
public class PeripheralEnumerationCellEditor extends ComboBoxCellEditor {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral enumeration cell editor.
	 *
	 * @param editorParent the editor parent
	 * @param peripheralRegisterField the peripheral register field
	 */
	public PeripheralEnumerationCellEditor(Composite editorParent,
			PeripheralRegisterFieldVMNode peripheralRegisterField) {

		super(editorParent, peripheralRegisterField.getEnumerationComboItems(), SWT.BORDER);
	}

	@Override
    protected Control createControl(Composite composite) {

		CCombo combo = (CCombo) super.createControl(composite);

		combo.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetDefaultSelected(SelectionEvent event) {
				PeripheralEnumerationCellEditor.this.focusLost();
			}

			@Override
            public void widgetSelected(SelectionEvent event) {
				PeripheralEnumerationCellEditor.this.focusLost();
			}
		});
		return combo;
	}

	// ------------------------------------------------------------------------
}
