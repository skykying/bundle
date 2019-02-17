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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Class that plays the role of the domain model in the TableViewerExample
 * In real life, this class would access a persistent store of some kind.
 * 
 */

public class ElfTaskList {

	private final int COUNT = 10;
	private Vector tasks = new Vector(COUNT);
	private Set changeListeners = new HashSet();

	// Combo box choices
	static final String[] OWNERS_ARRAY = { "?", "Nancy", "Larry", "Joe" };
	
	/**
	 * Constructor
	 */
	public ElfTaskList() {
		super();
		this.initData();
	}
	
	/*
	 * Initialize the table data.
	 * Create COUNT tasks and add them them to the 
	 * collection of tasks
	 */
	private void initData() {
		ElfTask task;
		for (int i = 0; i < COUNT; i++) {
			task = new ElfTask("Task "  + i);
			task.setOwner(OWNERS_ARRAY[i % 3]);
			tasks.add(task);
		}
	};

	/**
	 * Return the array of owners   
	 */
	public String[] getOwners() {
		return OWNERS_ARRAY;
	}
	
	/**
	 * Return the collection of tasks
	 */
	public Vector getTasks() {
		return tasks;
	}
	
	/**
	 * Add a new task to the collection of tasks
	 */
	public void addTask() {
		ElfTask task = new ElfTask("New task");
		tasks.add(tasks.size(), task);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ITaskListViewer) iterator.next()).addTask(task);
	}

	/**
	 * @param task
	 */
	public void removeTask(ElfTask task) {
		tasks.remove(task);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ITaskListViewer) iterator.next()).removeTask(task);
	}

	/**
	 * @param task
	 */
	public void taskChanged(ElfTask task) {
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ITaskListViewer) iterator.next()).updateTask(task);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(ITaskListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(ITaskListViewer viewer) {
		changeListeners.add(viewer);
	}

}
