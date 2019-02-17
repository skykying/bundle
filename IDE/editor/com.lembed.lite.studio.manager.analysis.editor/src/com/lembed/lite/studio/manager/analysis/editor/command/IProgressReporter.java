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

public interface IProgressReporter {
	/**
	 * Reports progress in checking file
	 * @param currentlyCheckedFilename filename of the currently checked file (may be null to indicate nothing has changed since last report)
	 * @param numFilesChecked number of files which are already checked (may be null to indicate nothing has changed since last report)
	 */
	void reportProgress(String currentlyCheckedFilename, Integer numFilesChecked);
}
