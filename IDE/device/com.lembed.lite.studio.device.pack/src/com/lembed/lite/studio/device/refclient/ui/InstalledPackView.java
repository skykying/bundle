/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Eclipse Project - generation from template   
* ARM Ltd and ARM Germany GmbH - application-specific implementation
*******************************************************************************/
package com.lembed.lite.studio.device.refclient.ui;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpBoard;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.data.ICpPackFamily;
import com.lembed.lite.studio.device.core.events.ILiteEventListener;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;

/**
 * This sample view class is an example how to show raw data of installed packs
 * <p>
 */

public class InstalledPackView extends ViewPart implements ILiteEventListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.lembed.lite.studio.device.refclient.ui.InstalledPackView"; //$NON-NLS-1$

	TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action expandAction;
	private Action collapseAction;
	Action doubleClickAction;

	ICpItem getCpItem(Object obj) {
		if (obj instanceof ICpItem) {
			return (ICpItem) obj;
		}
		return null;
	}

	class PackViewContentProvider extends TreeObjectContentProvider {

		public Object getParent(Object child) {
			ICpItem item = getCpItem(child);
			if (item != null) {
				return item.getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof ICpDeviceItem) {
				ICpDeviceItem item = (ICpDeviceItem) parent;
				if (item.getDeviceItems() != null)
					return item.getDeviceItems().toArray();
				ICpItem props = item.getEffectiveProperties(""); //$NON-NLS-1$
				if (props != null)
					return props.getChildArray();
			}

			return super.getChildren(parent);
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof ICpDeviceItem) {
				ICpDeviceItem item = (ICpDeviceItem) parent;
				if (item.getDeviceItems() != null)
					return !item.getDeviceItems().isEmpty();
				ICpItem props = item.getEffectiveProperties(""); //$NON-NLS-1$
				if (props != null)
					return props.hasChildren();
			}
			return super.hasChildren(parent);
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(Object obj, int index) {
			ICpItem item = getCpItem(obj);
			if (item != null) {
				switch (index) {
				case 0:
					return item.getTag();
				case 1:
					return item.attributes().toString();
				case 2:
					return item.getText();
				default:
					break;
				}
			}
			return CmsisConstants.EMPTY_STRING;
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			if (index == 0)
				return getImage(obj);
			return null;
		}

		@Override
		public Image getImage(Object obj) {
			ICpItem item = getCpItem(obj);
			if (item == null) {
				return null;
			}
			if (obj instanceof ICpPack) {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_PACKAGE);
			} else if (obj instanceof ICpPackFamily) {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_PACKAGES);
			} else if (obj instanceof ICpComponent) {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT);
			} else if (obj instanceof ICpFile) {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_FILE);
			} else if (obj instanceof ICpBoard) {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_BOARD);
			} else if (obj instanceof ICpDeviceItem) {
				ICpDeviceItem di = (ICpDeviceItem) obj;
				if (di.getDeviceItems() == null) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE);
				}
				return CpPlugInUI.getImage(CpPlugInUI.ICON_FOLDER);
			}
			switch (item.getTag()) {
			case CmsisConstants.COMPONENTS_TAG:
				return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_CLASS);
			case CmsisConstants.DEVICES_TAG:
				return CpPlugInUI.getImage(CpPlugInUI.ICON_FOLDER);
			case CmsisConstants.ALGORITHM_TAG:
				return CpPlugInUI.getImage(CpPlugInUI.ICON_FILE);
			case CmsisConstants.BOOK_TAG:
				return CpPlugInUI.getImage(CpPlugInUI.ICON_BOOK);
			case CmsisConstants.COMPATIBLE_DEVICE_TAG:
			case CmsisConstants.MOUNTED_DEVICE_TAG:
				return CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE);
			default:
				break;
			}

			return CpPlugInUI.getImage(CpPlugInUI.ICON_ITEM);
		}
	}

	/**
	 * The constructor.
	 */
	public InstalledPackView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		viewer = new TreeViewer(tree);

		TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
		tree.setLinesVisible(true);
		column0.setText(CpStringsUI.PackView_Tag);
		column0.setWidth(300);

		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		tree.setLinesVisible(true);
		column1.setText(CpStringsUI.PackView_Attributes);
		column1.setWidth(200);

		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setText(CpStringsUI.PackView_Text);
		column2.setWidth(500);

		drillDownAdapter = new DrillDownAdapter(viewer);

		viewer.setContentProvider(new TreeObjectContentProvider());// new
																	// PackViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		refresh();

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), ID);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		CpPlugIn.addLiteListener(this);
	}

	protected void refresh() {
		if (CpPlugIn.getDefault() == null){
			return;
		}
		ICpPackManager packManager = CpPlugIn.getPackManager();
		if (packManager != null){
			ICpPackCollection packs = packManager.getInstalledPacks();
			if(packs!=null){
				viewer.setInput(packs);
			}			
		}else{
			viewer.setInput(null);
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				InstalledPackView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(expandAction);
		manager.add(collapseAction);
	}

	void fillContextMenu(IMenuManager manager) {
		manager.add(expandAction);
		manager.add(collapseAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(expandAction);
		manager.add(collapseAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		expandAction = new Action() {
			public void run() {
				if (viewer == null)
					return;
				viewer.expandAll();
			}
		};

		expandAction.setText(CpStringsUI.ExpandAll);
		expandAction.setToolTipText(CpStringsUI.ExpandAllNodes);
		expandAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_EXPAND_ALL));

		collapseAction = new Action() {
			public void run() {
				if (viewer == null)
					return;
				viewer.collapseAll();
			}
		};
		collapseAction.setText(CpStringsUI.CollapseAll);
		collapseAction.setToolTipText(CpStringsUI.CollapseAllNodes);
		collapseAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_COLLAPSE_ALL));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj instanceof ICpItem) {
					ICpItem item = (ICpItem) obj;
					String s = item.getName();
					s += "\n"; //$NON-NLS-1$
					Attributes a = new Attributes();
					Map<String, String> m = item.getEffectiveAttributes(null);
					a.setAttributes(m);
					s += a.toString();
					showMessage(s);
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "PackView", //$NON-NLS-1$
				message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void handle(LiteEvent event) {
		if (event.getTopic().equals(LiteEvent.PACKS_RELOADED))
			refresh();
	}

	@Override
	public void dispose() {
		CpPlugIn.removeLiteListener(this);
		super.dispose();
	}

}