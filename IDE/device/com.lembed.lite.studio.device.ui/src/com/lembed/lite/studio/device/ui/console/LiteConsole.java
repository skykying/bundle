/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.ui.console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.events.ILiteEventListener;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.preferences.CpUIPreferenceConstants;

/**
 * Console to display RTE messages from LiteProject
 *
 */
public class LiteConsole extends MessageConsole implements IPropertyChangeListener, ILiteEventListener {

	public static final String CONSOLE_TYPE = "com.lembed.lite.studio.device.lite.console"; //$NON-NLS-1$
	public static final String BASE_NAME = CpStringsUI.LiteConsole_BaseName;
	public static final String PACK_MANAGER_CONSOLE_NAME = CpStringsUI.LiteConsole_PackManagerConsoleName;
	public static final int OUTPUT = 0;
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	public static final int STREAM_COUNT = 4;

	private Map<Integer, MessageConsoleStream> fStreams = new HashMap<Integer, MessageConsoleStream>();

	public LiteConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, CONSOLE_TYPE, imageDescriptor, true);
		updateBackGround();
		CpPlugInUI.addPreferenceStoreListener(this);
		initStreams();
	}

	private void initStreams() {
		asyncExec(() -> {
			for (int i = 0; i < STREAM_COUNT; i++) {
				getStream(i);
			}
		});
	}

	@Override
	protected void dispose() {
		super.dispose();
		CpPlugInUI.removePreferenceStoreListener(this);
		CpPlugIn.removeLiteListener(this);
		for (MessageConsoleStream stream : fStreams.values()) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fStreams.clear();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (!property.startsWith(CpUIPreferenceConstants.CONSOLE_PREFIX)) {
			return;
		}
		if (property.startsWith(CpUIPreferenceConstants.CONSOLE_COLOR_PREFIX)) {
			int streamType = getStreamType(property);
			MessageConsoleStream stream = fStreams.get(streamType);
			if (stream != null) {
				updateStreamColor(stream, property);
			}
		} else if (property.equals(CpUIPreferenceConstants.CONSOLE_OPEN_ON_OUT)) {
			IPreferenceStore store = CpPlugInUI.getDefault().getPreferenceStore();
			boolean activateOnWrite = store.getBoolean(CpUIPreferenceConstants.CONSOLE_OPEN_ON_OUT);
			for (MessageConsoleStream stream : fStreams.values()) {
				stream.setActivateOnWrite(activateOnWrite);
			}
		} else if (property.equals(CpUIPreferenceConstants.CONSOLE_BG_COLOR)) {
			updateBackGround();
		}
	}

	private void updateBackGround() {
		asyncExec(() -> {
			IPreferenceStore store = CpPlugInUI.getDefault().getPreferenceStore();
			RGB rgb = PreferenceConverter.getColor(store, CpUIPreferenceConstants.CONSOLE_BG_COLOR);
			setBackground(new Color(Display.getCurrent(), rgb));
		});
	}

	MessageConsoleStream getStream(int streamType) {
		MessageConsoleStream stream = fStreams.get(streamType);
		if (stream == null) {
			stream = newMessageStream();
			initStream(stream, streamType);
			fStreams.put(streamType, stream);
		}
		return stream;
	}

	private void initStream(MessageConsoleStream stream, int streamType) {
		IPreferenceStore store = CpPlugInUI.getDefault().getPreferenceStore();
		boolean activateOnWrite = store.getBoolean(CpUIPreferenceConstants.CONSOLE_OPEN_ON_OUT);
		stream.setActivateOnWrite(activateOnWrite);
		updateStreamColor(stream, getColorPreferenceConstant(streamType));
	}

	private void updateStreamColor(MessageConsoleStream stream, String preferenceConstant) {
		stream.setColor(new Color(Display.getCurrent(), getStreamColor(preferenceConstant)));
	}

	private RGB getStreamColor(String preferenceConstant) {
		IPreferenceStore store = CpPlugInUI.getDefault().getPreferenceStore();
		return PreferenceConverter.getColor(store, preferenceConstant);
	}

	private String getColorPreferenceConstant(int streamType) {
		switch (streamType) {
		case INFO:
			return CpUIPreferenceConstants.CONSOLE_INFO_COLOR;
		case WARNING:
			return CpUIPreferenceConstants.CONSOLE_WARNING_COLOR;
		case ERROR:
			return CpUIPreferenceConstants.CONSOLE_ERROR_COLOR;
		case OUTPUT:
		default:
			return CpUIPreferenceConstants.CONSOLE_OUT_COLOR;
		}
	}

	private int getStreamType(String preferenceConstant) {
		switch (preferenceConstant) {
		case CpUIPreferenceConstants.CONSOLE_INFO_COLOR:
			return INFO;
		case CpUIPreferenceConstants.CONSOLE_WARNING_COLOR:
			return WARNING;
		case CpUIPreferenceConstants.CONSOLE_ERROR_COLOR:
			return ERROR;
		case CpUIPreferenceConstants.CONSOLE_OUT_COLOR:
		default:
			return OUTPUT;
		}
	}

	/**
	 * Outputs the message to specified console stream
	 * 
	 * @param streamType
	 *            stream type: OUTPUT, INFO, ERROR
	 * @param msg
	 *            message to output
	 */
	public void output(int streamType, String msg) {
		MessageConsoleStream stream = getStream(streamType);
		stream.println(msg);
	}

	public void output(final String message) {
		output(OUTPUT, message);
	}

	public void outputInfo(final String message) {
		output(INFO, message);
	}

	public void outputWarning(final String message) {
		output(WARNING, message);
	}

	public void outputError(final String message) {
		output(ERROR, message);
	}

	/**
	 * Opens LiteConsole for given project
	 * 
	 * @param project
	 *            IProject to open console for
	 * @return LiteConsole
	 */
	public static LiteConsole openConsole(IProject project) {
		String name = null;
		if (project != null) {
			name = project.getName();
		}
		return openConsole(name);
	}

	/**
	 * Opens LiteConsole for given project name
	 * 
	 * @param projectName
	 *            name of the project to open console for
	 * @return LiteConsole
	 */
	synchronized public static LiteConsole openConsole(String projectName) {
		// add it if necessary
		String consoleName = BASE_NAME;
		if (projectName != null && !projectName.isEmpty() && !projectName.equals(BASE_NAME)) {
			consoleName += " [" + projectName + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		LiteConsole liteConsole = null;
		LiteConsole liteBaseConsole = null;
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		if (consoles != null) {
			for (IConsole console : consoles) {
				if (!CONSOLE_TYPE.equals(console.getType())) {
					continue;
				}
				if (consoleName.equals(BASE_NAME)) {
					liteConsole = (LiteConsole) console;
					break;
				}
				String name = console.getName();
				if (consoleName.equals(name)) {
					liteConsole = (LiteConsole) console;
					break;
				} else if (BASE_NAME.equals(name)) {
					liteBaseConsole = (LiteConsole) console;
				}
			}
		}
		if (liteConsole == null && liteBaseConsole != null) {
			liteConsole = liteBaseConsole;
			if (!consoleName.equals(BASE_NAME)) {
				liteConsole.setName(consoleName);
			}
		} else if (liteConsole == null) {
			ImageDescriptor imageDescriptor = CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_RTE_CONSOLE);
			liteConsole = new LiteConsole(consoleName, imageDescriptor);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { liteConsole });
		}

		if (CpPlugInUI.getDefault().getPreferenceStore().getBoolean(CpUIPreferenceConstants.CONSOLE_OPEN_ON_OUT)) {
			showConsole(liteConsole);
		}
		return liteConsole;
	}

	synchronized public static LiteConsole openPackManagerConsole() {
		LiteConsole liteConsole = null;
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		if (consoles != null) {
			for (IConsole console : consoles) {
				if (!CONSOLE_TYPE.equals(console.getType())) {
					continue;
				}
				if (PACK_MANAGER_CONSOLE_NAME.equals(console.getName())) {
					liteConsole = (LiteConsole) console;
					break;
				}
			}
		}
		if (liteConsole == null) {
			ImageDescriptor imageDescriptor = CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_RTE_CONSOLE);
			liteConsole = new LiteConsole(PACK_MANAGER_CONSOLE_NAME, imageDescriptor);
			CpPlugIn.addLiteListener(liteConsole);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { liteConsole });
		}

		if (CpPlugInUI.getDefault().getPreferenceStore().getBoolean(CpUIPreferenceConstants.CONSOLE_OPEN_ON_OUT)) {
			showConsole(liteConsole);
		}
		return liteConsole;
	}

	synchronized public static void showConsole(final LiteConsole console) {
		asyncExec(() -> ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console));
	}

	protected static void asyncExec(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	@Override
	public void handle(LiteEvent event) {
		asyncExec(() -> {
			String topic = event.getTopic();
			if (topic.startsWith(LiteEvent.PRINT)) {
				String message = (String) event.getData();
				switch (topic) {
				case LiteEvent.PRINT_OUTPUT:
					output(message);
					break;
				case LiteEvent.PRINT_INFO:
					outputInfo(message);
					break;
				case LiteEvent.PRINT_WARNING:
					outputWarning(message);
					break;
				case LiteEvent.PRINT_ERROR:
					outputError(message);
					break;
				default:
					break;
				}
			} else if (LiteEvent.GPDSC_LAUNCH_ERROR.equals(topic)) {
				String message = (String) event.getData();
				outputError(message);
			}
		});
	}

}
