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

public interface IEditorConsole {

	public abstract OutputStream getConsoleOutputStream(boolean isError);

	public abstract void print(String line) throws IOException;

	public abstract void println(String line) throws IOException;

	/**
	 * Shows the console view (in an async way, this method may be called from non UI-thread)
	 */
	public abstract void show();

}
