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
/*
 * Created on 05.12.2003
 *
 */
package com.lembed.lite.studio.manager.analysis.editor.map;

import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @author luelljoc
 * 
 * From EPIC Perl editor
 *
 */
public interface IDocumentIdleListener
{
    /**
     * This notification occurs on the Display thread within a configured
     * time period after the viewed document has changed, provided that
     * the viewer is visible at that time. If the document has not changed
     * or the viewer is not visible, the notification may, but does not have
     * to occur.
     */
	public void editorIdle(ISourceViewer viewer);
}
