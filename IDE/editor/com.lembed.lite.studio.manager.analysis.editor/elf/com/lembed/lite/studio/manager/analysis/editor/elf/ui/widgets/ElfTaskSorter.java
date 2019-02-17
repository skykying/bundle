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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorter for the TableViewerExample that displays items of type 
 * <code>ExampleTask</code>.
 * The sorter supports three sort criteria:
 * <p>
 * <code>DESCRIPTION</code>: Task description (String)
 * </p>
 * <p>
 * <code>OWNER</code>: Task Owner (String)
 * </p>
 * <p>
 * <code>PERCENT_COMPLETE</code>: Task percent completed (int).
 * </p>
 */
public class ElfTaskSorter extends ViewerSorter {

	/**
	 * Constructor argument values that indicate to sort items by 
	 * description, owner or percent complete.
	 */
	public final static int DESCRIPTION 		= 1;
	public final static int OWNER 				= 2;
	public final static int PERCENT_COMPLETE 	= 3;

	// Criteria that the instance uses 
	private int criteria;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or 
	 *   <code>TYPE</code>
	 */
	public ElfTaskSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	/* (non-Javadoc)
	 * Method declared on ViewerSorter.
	 */
	public int compare(Viewer viewer, Object o1, Object o2) {

		ElfTask task1 = (ElfTask) o1;
		ElfTask task2 = (ElfTask) o2;

		switch (criteria) {
			case DESCRIPTION :
				return compareDescriptions(task1, task2);
			case OWNER :
				return compareOwners(task1, task2);
			case PERCENT_COMPLETE :
				return comparePercentComplete(task1, task2);
			default:
				return 0;
		}
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks
	 * based on the percent completed.
	 *
	 * @param task1
	 * @param task2
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	private int comparePercentComplete(ElfTask task1, ElfTask task2) {
		int result = task1.getPercentComplete() - task2.getPercentComplete();
		result = result < 0 ? -1 : (result > 0) ? 1 : 0;  
		return result;
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks
	 * based on the description.
	 *
	 * @param task1 the first task element to be ordered
	 * @param resource2 the second task element to be ordered
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	protected int compareDescriptions(ElfTask task1, ElfTask task2) {
		return collator.compare(task1.getDescription(), task2.getDescription());
	}

	/**
	 * Returns a number reflecting the collation order of the given tasks
	 * based on their owner.
	 *
	 * @param resource1 the first resource element to be ordered
	 * @param resource2 the second resource element to be ordered
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	protected int compareOwners(ElfTask task1, ElfTask task2) {
		return collator.compare(task1.getOwner(), task2.getOwner());
	}

	/**
	 * Returns the sort criteria of this this sorter.
	 *
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}
