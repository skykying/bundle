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
package com.lembed.lite.studio.manager.analysis.editor.command;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Wrapper around a console window, which can output an existing InputSteam.
 * 
 */
public class EditorConsole implements IEditorConsole {

	private static final String NAME = "Build Analysis";
	private final MessageConsole messageConsole;
	private static EditorConsole INSTANCE = null;

	public synchronized static EditorConsole getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EditorConsole();
		}
		return INSTANCE;
	}

	public EditorConsole() {
		messageConsole = findMessageConsole(NAME);
	}

	private static MessageConsole findMessageConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}


	public OutputStream getConsoleOutputStream(boolean isError) {
		final MessageConsoleStream output = messageConsole.newMessageStream();
		output.setActivateOnWrite(false);

		final int colorId;
		if (!isError) {
			colorId = SWT.COLOR_BLACK;
		} else {
			colorId = SWT.COLOR_RED;
		}

		/* we must set the color in the UI thread */
		Runnable runnable = new Runnable() {
			public void run() {
				org.eclipse.swt.graphics.Color color = Display.getCurrent().getSystemColor(colorId);
				output.setColor(color);
			}
		};
		Display.getDefault().syncExec(runnable);

		return output;
	}

	public void print(String line) throws IOException {
		final MessageConsoleStream output = messageConsole.newMessageStream();
		output.print(line);
		output.close();
	}

	
	public void println(String line) throws IOException {
		final MessageConsoleStream output = messageConsole.newMessageStream();
		output.println(line);
		output.close();
	}


	public void show() {

		Runnable runnable = new Runnable() {
			public void run() {
				// this should only be called from GUI thread
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window == null) {
					log("Could not show console because there is no active workbench window");
					return;
				}
				IWorkbenchPage page = window.getActivePage();
				if (page == null) {
					log("Could not show console because there is no active page");
					return;
				}
				try {
					IConsoleView view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
					view.display(messageConsole);
				} catch (PartInitException e) {
					log("Could not show console");
				}

			}

		};
		Display.getDefault().asyncExec(runnable);
	}

	private void log(String string) {

	}
}
