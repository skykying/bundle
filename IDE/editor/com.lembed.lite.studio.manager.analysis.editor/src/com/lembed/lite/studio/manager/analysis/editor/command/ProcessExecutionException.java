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

public class ProcessExecutionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085940287202256905L;

	public static ProcessExecutionException newException(String cmdLine, Throwable cause) {
		StringBuffer errorMsg = new StringBuffer();
		errorMsg.append("Error executing '").append(cmdLine).append("' due to error: ").append(cause.getLocalizedMessage());
		errorMsg.append(" Maybe more information is available in the console view.");
		return new ProcessExecutionException(errorMsg.toString(), cause);
	}
	
	public ProcessExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessExecutionException(String message) {
		super(message);
	}
	
}
