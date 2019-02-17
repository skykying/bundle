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


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.lembed.lite.studio.manager.analysis.editor.hex.HexEditorConstants;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;


public class DeleteDataDialog extends Dialog {
	private int result = 0;
	private Text dataSizeText;
	private Combo sizeCombo;
	private Button chbToTheEnd;
	public DeleteDataDialog(Shell parent) {
		super(parent);
	}

	@Override
    protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DeleteDataDialog_0);
	}
	
	public int getResult() {
		return result;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = (Composite) super.createDialogArea(parent);
		//
		// Panel with data size entry
		//
		Composite sizePanel = new Composite(control, SWT.NONE);
		GridLayout sizeGrid = new GridLayout();
		sizeGrid.numColumns = 3;
		sizePanel.setLayout(sizeGrid);
		sizePanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		Label label = new Label(sizePanel, SWT.NULL);
		label.setText(Messages.DeleteDataDialog_1);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataSizeText = new Text(sizePanel, SWT.SINGLE | SWT.BORDER);
		dataSizeText.setTextLimit(8);
		dataSizeText.setText("1"); //$NON-NLS-1$
		dataSizeText.selectAll();
		dataSizeText.setFocus();
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 50;
		dataSizeText.setLayoutData(data);
		sizeCombo = new Combo(sizePanel, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		sizeCombo.add("B"); //$NON-NLS-1$
		sizeCombo.add("kB"); //$NON-NLS-1$
		sizeCombo.add("MB"); //$NON-NLS-1$
		sizeCombo.select(0);

		//
		// Options
		//
		Composite optionPanel = new Composite(control, SWT.NONE);
		GridLayout optionGrid = new GridLayout();
		optionGrid.numColumns = 3;
		optionPanel.setLayout(optionGrid);
		optionPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		chbToTheEnd = new Button(optionPanel, SWT.CHECK);
		chbToTheEnd.setText(Messages.DeleteDataDialog_6);
		chbToTheEnd.setSelection(false);
		chbToTheEnd.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				//
				// Enable/disable other GUI controls according to the checkbox
				//
				dataSizeText.setEnabled(!chbToTheEnd.getSelection());
				sizeCombo.setEnabled(!chbToTheEnd.getSelection());
			} // widgetSelected()
		} // SelectionAdapter()
		);

		//
		// Panel with dialog buttons
		//
		Composite buttonPanel = new Composite(control, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		buttonPanel.setLayout(grid);
		buttonPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		return control;
	}
	
	@Override
	protected void okPressed() {
		String sDataSize = dataSizeText.getText().trim();
		int iDataSize = 0;
		
		if (chbToTheEnd.getSelection()) {
			//
			// User wants to delete the rest of the file
			//
			result = -1;
		} else  {
		
			try {
				iDataSize = Integer.parseInt(sDataSize,10);
			}
			catch (NumberFormatException nfeSize) {
				MessageDialog.openError(getShell(), HexEditorConstants.DIALOG_TITLE_ERROR, Messages.DeleteDataDialog_7);
				return;
			}
			
			if (iDataSize < 0) {
				MessageDialog.openError(getShell(), HexEditorConstants.DIALOG_TITLE_ERROR, Messages.DeleteDataDialog_8);
				return;
			} // if
	
			result = (int) (iDataSize * Math.pow(1024,sizeCombo.getSelectionIndex()));
		}

		super.okPressed();
	}
}
