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

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;

public class EditorProcessResultHandler implements ExecuteResultHandler {

	private boolean isRunning = true;
	private int exitValue = 0;
	private ExecuteException exception = null;

	public synchronized void onProcessComplete(int exitValue) {
		isRunning = false;
		this.exitValue = exitValue;
	}

	public synchronized void onProcessFailed(ExecuteException exception) {
		isRunning = false;
		this.exception = exception;
	}

	public synchronized int getExitValue() throws ExecuteException {
		if (exception != null) {
			throw exception;
		}
		return exitValue;
	}

	public synchronized boolean isRunning() {
		return isRunning;
	}
}
