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
package com.lembed.lite.studio.manager.analysis.editor.hex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;


/**
 * The Class HexManager.
 */
public class HexManager implements IPropertyChangeListener {

	
	private static Map<RGB, Color> sharedColors = new HashMap<>();
	
	private static Map<FontData, Font> fonts = new HashMap<>();
	
	
	/**
	 * The constructor.
	 */
	public HexManager() {
		super();
	}
	
	@Override
    public void propertyChange(PropertyChangeEvent event) {
	}
	

	/**
	 * This method is called upon plug-in activation.
	 *
	 * @param context the context
	 */
	public void start(BundleContext context) {
	
	}

	/**
	 * This method is called when the plug-in is stopped.
	 *
	 * @param context the context
	 */
	public void stop(BundleContext context) {
		disposeColors();
		disposeFonts();		
	}

	/**
	 * Writes a status to plugin log
	 * @param status IStatus
	 */
	public static void log(IStatus status) {
		
	}

	/**
	 * Writes a message to plugin log
	 * @param severity int 
	 * @param message  String
	 */
	public static void log(int severity, String message) {
		Status status = new Status(severity, HexEditorConstants.PLUGIN_ID, IStatus.OK, message, null);
		log(status);
	}

	/**
	 * Writes a message to plugin log
	 * @param message  String
	 */
	public static void log(String message) {
		log(IStatus.INFO, message);
	}

	/**
	 * Writes an exception to plugin log
	 * @param e Throwable
	 */
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, HexEditorConstants.PLUGIN_ID, IStatus.ERROR, "EhepPlugin.internalErrorOccurred", e)); //$NON-NLS-1$
	}

	/**
	 * Returns the color for the specified rgb on the device. The colors
	 * created by this call are cached and disposed at bundle stop time. 
	 * @param rgb the rgb value, must not be null
	 * @return the color
	 */
	public static Color getColor(RGB rgb) {
		Assert.isNotNull(rgb);
		
		Device device = Display.getCurrent();
		if (device == null)
			device = Display.getDefault();
		Color color = sharedColors.get(rgb);
		if (color == null)
			sharedColors.put(rgb, color = new Color(device, rgb));
		return color;
	}
	
	/**
	 * Gets the font.
	 *
	 * @param data the data
	 * @return the font
	 */
	public static Font getFont(FontData data) {
		Assert.isNotNull(data);
		
		Device device = Display.getCurrent();
		if (device == null)
			device = Display.getDefault();
		Font font = fonts.get(data);
		if (font == null)
			fonts.put(data, font = new Font(device, data));
		return font;
	}

	private static void disposeColors() {
		for (Iterator<Color> it = sharedColors.values().iterator(); it.hasNext();) {
			it.next().dispose();
		} // for
	}

	private static void disposeFonts() {
		for (Iterator<Font> it = fonts.values().iterator(); it.hasNext();) {
			it.next().dispose();
		}
	}
}
