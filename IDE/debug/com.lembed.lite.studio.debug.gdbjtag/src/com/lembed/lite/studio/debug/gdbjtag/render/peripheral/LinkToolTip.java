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

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

/**
 * The Class LinkToolTip.
 */
public class LinkToolTip extends ColumnViewerToolTipSupport {

	// ------------------------------------------------------------------------

	/**
	 * Enable for.
	 *
	 * @param viewer the viewer
	 * @param style the style
	 * @param listener the listener
	 */
	public static final void enableFor(ColumnViewer viewer, int style, ILinkToolTipListener listener) {

		new LinkToolTip(viewer, style, false, listener);
	}

	// ------------------------------------------------------------------------

	private ILinkToolTipListener fListener;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new link tool tip.
	 *
	 * @param viewer the viewer
	 * @param style the style
	 * @param manualActivation the manual activation
	 * @param listener the listener
	 */
	protected LinkToolTip(ColumnViewer viewer, int style, boolean manualActivation, ILinkToolTipListener listener) {
		super(viewer, style, manualActivation);

		fListener = listener;
	}


	@Override
    protected Composite createToolTipContentArea(Event event, Composite parent) {

		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);

		Color fgColor = getForegroundColor(event);
		Color bgColor = getBackgroundColor(event);
		Font font = getFont(event);
		Link link = new Link(composite, 0);
		link.setFont(font);
		link.setBackground(bgColor);
		link.setForeground(fgColor);

		link.addListener(SWT.Selection, new Listener() {

			@Override
            public void handleEvent(Event ev) {
				if (fListener != null)
					fListener.linkSelected(ev.text);
			}
		});
		link.setText(getText(event));
		return composite;
	}

	@Override
    public boolean isHideOnMouseDown() {
		return false;
	}

}
