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
 * 
 */

package com.lembed.lite.studio.manager.analysis.editor.elf.ui.widgets;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the 
 * tableViewer
 */

public class ElfCellModifier implements ICellModifier {
	private ElfTableViewerEx tableViewerEx;
	private String[] columnNames;
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public ElfCellModifier(ElfTableViewerEx elfTableViewerEx) {
		super();
		this.tableViewerEx = elfTableViewerEx;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {

		// Find the index of the column
		int columnIndex = tableViewerEx.getColumnNames().indexOf(property);

		Object result = null;
		ElfTask task = (ElfTask) element;

		switch (columnIndex) {
			case 0 : // COMPLETED_COLUMN 
				result = new Boolean(task.isCompleted());
				break;
			case 1 : // DESCRIPTION_COLUMN 
				result = task.getDescription();
				break;
			case 2 : // OWNER_COLUMN 
				String stringValue = task.getOwner();
				String[] choices = tableViewerEx.getChoices(property);
				int i = choices.length - 1;
				while (!stringValue.equals(choices[i]) && i > 0)
					--i;
				result = new Integer(i);					
				break;
			case 3 : // PERCENT_COLUMN 
				result = task.getPercentComplete() + "";
				break;
			default :
				result = "";
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	

		// Find the index of the column 
		int columnIndex	= tableViewerEx.getColumnNames().indexOf(property);
			
		TableItem item = (TableItem) element;
		ElfTask task = (ElfTask) item.getData();
		String valueString;

		switch (columnIndex) {
			case 0 : // COMPLETED_COLUMN 
			    task.setCompleted(((Boolean) value).booleanValue());
				break;
			case 1 : // DESCRIPTION_COLUMN 
				valueString = ((String) value).trim();
				task.setDescription(valueString);
				break;
			case 2 : // OWNER_COLUMN 
				valueString = tableViewerEx.getChoices(property)[((Integer) value).intValue()].trim();
				if (!task.getOwner().equals(valueString)) {
					task.setOwner(valueString);
				}
				break;
			case 3 : // PERCENT_COLUMN
				valueString = ((String) value).trim();
				if (valueString.length() == 0)
					valueString = "0";
				task.setPercentComplete(Integer.parseInt(valueString));
				break;
			default :
			}
		tableViewerEx.getTaskList().taskChanged(task);
	}
}
