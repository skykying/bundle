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
package com.lembed.lite.studio.manager.analysis.editor.linker.preferences.tasktag;

/**
 * Change listener used by <code>ListDialogField</code> and <code>CheckedListDialogField</code>
 */
public interface IListAdapter<T> {
    
    /**
     * A button from the button bar has been pressed.
     */
    void customButtonPressed(ListDialogField<T> field, int index);
    
    /**
     * The selection of the list has changed.
     */ 
    void selectionChanged(ListDialogField<T> field);
    
    /**
     * En entry in the list has been double clicked
     */
    void doubleClicked(ListDialogField<T> field);   

}