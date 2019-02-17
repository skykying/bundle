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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

import com.lembed.lite.studio.manager.analysis.editor.hex.HexUtils;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class AboutDialog extends Dialog {
	private Shell dialog;
	private Image image = null;

	public AboutDialog(Shell parent) {
		super(parent, SWT.APPLICATION_MODAL);
	}

	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Opens a dialog
	 */
	public void open() {
		dialog = new Shell(getParent(), getStyle());
		dialog.setText(Messages.AboutDialog_0);
		dialog.setLayout(new GridLayout());

		//
		// Panel with plugin info
		//
		Composite infoPanel = new Composite(dialog, SWT.NONE);
		GridLayout infoGrid = new GridLayout();
		infoGrid.numColumns = 2;
		infoPanel.setLayout(infoGrid);
		infoPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		//
		// Sub-panel with plugin icon
		//
		Composite iconPanel = new Composite(infoPanel, SWT.NONE);
		GridLayout iconGrid = new GridLayout();
		iconGrid.numColumns = 1;
		iconPanel.setLayout(iconGrid);
		iconPanel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		Label icon = new Label(iconPanel, SWT.NONE);

		//
		// Try to open a plugin icon
		//
//		URL url = null;
//		String fileName = PropertyChangeListener.getResourceString("about.icon");
//		Bundle bundle = Platform.getBundle(EHEP.FULL_PLUGIN_NAME);
//		url = FileLocator.find(bundle, new Path(fileName), null);
//		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		
//		if (imageDescriptor != null) {
//			//
//			// Plugin icon was found and opened
//			//
//			image = imageDescriptor.createImage();
//			icon.setImage(image);
//		}	else {
			//
			// Plugin icon is missing
			//
			icon.setText(""); //$NON-NLS-1$
//		}

		//
		// Sub-panel with plugin info
		//
		Composite textPanel = new Composite(infoPanel, SWT.NONE);
		GridLayout textGrid = new GridLayout();
		textGrid.numColumns = 1;
		textPanel.setLayout(textGrid);
		textPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		StyledText text = new StyledText(textPanel, SWT.MULTI | SWT.READ_ONLY);
		String pluginVersion = HexUtils.getBinaryEditorVersion();
		if (pluginVersion == null) {
			pluginVersion = Messages.AboutDialog_3;
		}
//		String aboutInfo = PropertyChangeListener.getResourceString("about.info");
//		aboutInfo = aboutInfo.replaceAll("(\\{pluginVersion\\})", pluginVersion); //$NON-NLS-1$
//		text.setText(aboutInfo);
		text.setCaret(null);
		text.setFont(getParent().getFont());
		text.setCursor(null);
		text.setBackground(textPanel.getBackground());

		//
		// Horizontal bar
		//
		Label bar = new Label(dialog, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		//
		// Panel with button(s)
		//
		Composite buttonPanel = new Composite(dialog, SWT.NONE);
		GridLayout buttonGrid = new GridLayout();
		buttonGrid.numColumns = 1;
		buttonPanel.setLayout(buttonGrid);
		buttonPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		Button btnOk = new Button(buttonPanel, SWT.PUSH);
		btnOk.setText(Messages.AboutDialog_6);
		data = new GridData();
		data.widthHint = 75;
		btnOk.setLayoutData(data);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				dialog.dispose();
			}
		});
		
		dialog.pack();
		dialog.open();
		
		while (!dialog.isDisposed()) {
			if (!getParent().getDisplay().readAndDispatch()) getParent().getDisplay().sleep();
		}
	}

	/**
	 * Closes the dialog
	 * @return boolean
	 */
	public boolean close() {
		//
		// Get rid of the image that was displayed on the left-hand side of the About dialog
		//
		if (image != null)
			image.dispose();
		return true;
	}
}
