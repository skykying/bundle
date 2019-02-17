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
/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003.
 * http://www.opnworks.com
 * Created on Apr 2, 2003 by lgauthier@opnworks.com
 */

package com.lembed.lite.studio.manager.analysis.editor.elf.ui.widgets;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.lembed.lite.studio.manager.analysis.editor.elf.Activator;



/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class ElfLabelProvider extends LabelProvider	implements ITableLabelProvider {

	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "sample";
	public static final String UNCHECKED_IMAGE  = "sample";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
	static {
		String iconPath = "icons/";
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
				Activator.class,
		                      iconPath + CHECKED_IMAGE + ".gif"
		                  )
		                 );
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
				Activator.class,
		                      iconPath + UNCHECKED_IMAGE + ".gif"
		                  )
		                 );
	}

	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	private Image getImage(boolean isSelected) {
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return  imageRegistry.get(key);
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		ElfTask task = (ElfTask) element;
		switch (columnIndex) {
		case 0:  // COMPLETED_COLUMN
			break;
		case 1 :
			result = task.getDescription();
			break;
		case 2 :
			result = task.getOwner();
			break;
		case 3 :
			result = task.getPercentComplete() + "";
			break;
		default :
			break;
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return (columnIndex == 0) ?   // COMPLETED_COLUMN?
		       getImage(((ElfTask) element).isCompleted()) :
		       null;
	}

}
