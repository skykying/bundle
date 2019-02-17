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
package com.lembed.lite.studio.manager.analysis.editor.elf.ui;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;

public class ElfItemView extends ViewPart {

	private TableViewer viewer;
	private ParserResult _adapters;

	public ElfItemView(ParserResult content){
		_adapters = content;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent, _adapters);
	}

	private void createViewer(Composite parent,ParserResult _adapters2) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer, _adapters2);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public TableViewer getViewer() {
		return viewer;
	}
	
	public void setInput(Object input){
		viewer.setInput(input);
	}
	
	public void createColumns(final Composite parent,ParserResult result){
		createColumns(parent,viewer,result);
	}
	
	public void createColumns(final Composite parent, final TableViewer viewer,ParserResult result){		
		int childColumn = result.size();
		List<Integer> bound = result.bound();
		String[] title = result.getTitles();

		for(int index = 0; index < childColumn;index++ ){
			TableViewerColumn col = createTableViewerColumn(title[index],bound.get(index), index, viewer);
			final int i = index;
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ParserResult p = (ParserResult) element;
					List<String> values = p.get_values();
					return values.get(i);				
				}
			});
		}
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber,final TableViewer viewer) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
