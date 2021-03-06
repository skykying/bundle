/*******************************************************************************
 * Copyright (c) 2016 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.pack.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.events.ILiteEventListener;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.item.ICmsisItem;
import com.lembed.lite.studio.device.pack.CpInstallerPlugInUI;
import com.lembed.lite.studio.device.pack.Messages;
import com.lembed.lite.studio.device.pack.ui.PackInstallerViewController;
import com.lembed.lite.studio.device.ui.CpPlugInUI;

/**
 * Base class for all the views in pack manager perspective
 */
public abstract class PackInstallerView extends ViewPart implements ILiteEventListener, ISelectionListener {

	protected static final int COLBUTTON = 1;
	protected static final int COLURL = 1;

	protected PackInstallerViewController fViewController;

	protected FilteredTree fTree = null;
	protected TreeViewer fViewer = null;


	protected ViewerFilter[] fViewFilters = null;
	PatternFilter fPatternFilter = null;

	Action fHelpAction = null;
	Action fRemoveSelection = null;
	Action fExpandAction = null;
	Action fExpandItemAction = null;
	Action fCollapseAction = null;
	Action fCollapseItemAction = null;
	Action fDoubleClickAction = null;
	Action fShowPackProperties = null;


	static ICpItem getCpItem(Object obj) {
		if (obj instanceof ICpItem) {
			return (ICpItem)obj;
		}
		return null;
	}


	public PackInstallerView() {
		fViewController = CpInstallerPlugInUI.getViewController();
	}


	@Override
	public void dispose() {
		//viewController.selectionChanged(this, null);
		fViewController.removeListener(this);
		fViewController = null;

		if(getSite() != null && getSite().getPage() != null) {
			getSite().getPage().removeSelectionListener(this);
		}
		super.dispose();
	}

	public ICpPackInstaller getPackInstaller() {
		if(CpPlugIn.getPackManager() == null) {
			return null;
		}
		return CpPlugIn.getPackManager().getPackInstaller();
	}

	protected abstract String getHelpContextId();
	protected abstract void createTreeColumns();
	protected abstract void refresh();

	protected boolean isExpandable() {
		return true;
	}

	protected boolean hasManagerCommands() {
		return false;
	}

	public boolean isFilterSource() {
		return false;
	}


	public boolean isFilterClient() {
		return !isFilterSource();
	}

	protected ICmsisItem getSelectedItem() {
		IStructuredSelection sel = (IStructuredSelection)fViewer.getSelection();
		if(sel.size() == 1) {
			Object o = sel.getFirstElement();
			if(o instanceof ICmsisItem ){
				return (ICmsisItem)o;
			}
		}
		return null;
	}

	protected void hookViewSelection() {
		IWorkbenchPartSite site = getSite();
		if(site != null) {
			if(isFilterClient() || isFilterSource()) {
				site.setSelectionProvider(fViewer);
			}
			if(site.getPage() != null) {
				site.getPage().addSelectionListener(this);
			}
		}
	}


	@Override
	public void setFocus() {
		fViewer.getControl().setFocus();
	}


	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(part == this && fViewController !=null ) {
			fViewController.selectionChanged(part, selection);
		}
	}

	protected void handleFilterChanged() {
		if(!isFilterClient()) {
			return;
		}
		if (!fTree.getFilterControl().isDisposed()) {
			fTree.getFilterControl().setText(CmsisConstants.EMPTY_STRING);
		}

		if (!fViewer.getControl().isDisposed()) {
			fViewer.setFilters(fViewFilters);
			fViewer.setSelection(null);
		}
	}


	@Override
	public void createPartControl(Composite parent) {
		createViewFilters();

		fTree = new FilteredTree(parent,
				SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL,
				fPatternFilter, true);

		fViewer = fTree.getViewer();
		Tree tree = fViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		createTreeColumns();
		refresh();

		ColumnViewerToolTipSupport.enableFor(fViewer);
		setHelpContext();
		hookViewSelection();
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		if(fViewFilters != null) {
			fViewer.setFilters(fViewFilters);
		}
		fViewController.addListener(this);
		updateOnlineState();
	}

	protected void createViewFilters() {
		fPatternFilter = new PatternFilter() {
			@Override
			protected boolean isLeafMatch(final Viewer viewer, final Object element) {
				TreeViewer treeViewer = (TreeViewer) viewer;
				boolean isMatch = false;
				ColumnLabelProvider labelProvider = (ColumnLabelProvider) treeViewer.getLabelProvider(0);
				String labelText = labelProvider.getText(element);
				isMatch |= wordMatches(labelText);
				return isMatch;
			}
		};
		fPatternFilter.setIncludeLeadingWildcard(true);
		if(isFilterClient()) {
			fViewFilters = new ViewerFilter[]{fPatternFilter, fViewController.getFilter()};
		}

	}


	protected void setHelpContext() {
		// Create the help context id for the viewer's control
		IWorkbench wb = PlatformUI.getWorkbench();
		if(wb == null) {
			return;
		}
		IWorkbenchHelpSystem hs = wb.getHelpSystem();
		if(hs == null) {
			return;
		}
		hs.setHelp(fViewer.getControl(), getHelpContextId());
	}


	protected void makeActions() {
		fHelpAction = new Action(Messages.Help, IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				fViewer.getControl().notifyListeners(SWT.Help, new Event());
			}
		};
		fHelpAction.setToolTipText(Messages.PackInstallerView_Help);
		fHelpAction.setImageDescriptor(	CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_HELP));

		fShowPackProperties = new Action() {
			@Override
			public void run() {
				fViewController.showPackProperties(fViewer.getSelection()); // the item should be already selected
			}
		};
		fShowPackProperties.setText(Messages.PacksView_ShowPacksOutline);
		fShowPackProperties.setToolTipText(Messages.PacksView_ShowPacksOutline);
		fShowPackProperties.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_DETAILS));

		if(isFilterSource()) {
			fRemoveSelection = new Action() {

				@Override
				public void run() {
					// Empty search text and selection
					fViewer.setSelection(null);
					fTree.getFilterControl().setText(CmsisConstants.EMPTY_STRING);
				}
			};

			fRemoveSelection.setText(Messages.DevicesView_RemoveSelection);
			fRemoveSelection.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_REMOVE_ALL));
			fRemoveSelection.setToolTipText(Messages.DevicesView_RemoveSelection);
		}


		if(!isExpandable()) {
			return;
		}
		// expandAction
		fExpandAction = new Action() {
			@Override
			public void run() {
				if(fViewer == null) {
					return;
				}
				fViewer.expandAll();
			}
		};


		fExpandAction.setText(Messages.ExpandAll);
		fExpandAction.setToolTipText(Messages.ExpandAllNodes);
		fExpandAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_EXPAND_ALL));

		fExpandItemAction = new Action() {
			@Override
			public void run() {
				if(fViewer == null) {
					return;
				}
				ISelection selection = fViewer.getSelection();
				if (selection == null) {
					return;
				}
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				fViewer.expandToLevel(obj, AbstractTreeViewer.ALL_LEVELS);
			}
		};
		fExpandItemAction.setText(Messages.ExpandSelected);
		fExpandItemAction.setToolTipText(Messages.ExpandSelectedNode);
		fExpandItemAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_EXPAND_ALL));

		// collapseAction
		fCollapseAction = new Action() {
			@Override
			public void run() {
				if(fViewer == null) {
					return;
				}
				fViewer.collapseAll();
			}
		};
		fCollapseAction.setText(Messages.CollapseAll);
		fCollapseAction.setToolTipText(Messages.CollapseAllNodes);
		fCollapseAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_COLLAPSE_ALL));

		fCollapseItemAction = new Action() {
			@Override
			public void run() {
				if(fViewer == null) {
					return;
				}
				ISelection selection = fViewer.getSelection();
				if (selection == null) {
					return;
				}
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				fViewer.collapseToLevel(obj, AbstractTreeViewer.ALL_LEVELS);
			}
		};
		fCollapseItemAction.setText(Messages.CollapseSelected);
		fCollapseItemAction.setToolTipText(Messages.CollapseSelectedNode);
		fCollapseItemAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_COLLAPSE_ALL));

		fHelpAction = new Action(Messages.Help, IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				fViewer.getControl().notifyListeners(SWT.Help, new Event());
			}
		};
		fHelpAction.setToolTipText(Messages.PackInstallerView_Help);
		fHelpAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_HELP));

		fDoubleClickAction = new Action() {
			@Override
			public void run() {
				ISelection selection = fViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (fViewer.getExpandedState(obj)) {
					fViewer.collapseToLevel(obj, AbstractTreeViewer.ALL_LEVELS);
				} else if (fViewer.isExpandable(obj)) {
					fViewer.expandToLevel(obj, 1);
				}
			}
		};

		fViewer.addDoubleClickListener(event -> fDoubleClickAction.run());
	}


	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	protected void fillLocalPullDown(IMenuManager manager) {
		if(isExpandable()) {
			manager.add(fExpandAction);
			manager.add(fCollapseAction);
		}
		manager.add(fHelpAction);
		manager.add(new Separator());

		if(isFilterSource()) {
			manager.add(fRemoveSelection);
		}

		if(hasManagerCommands()) {
			PackInstallerViewUtils.addManagementCommandsToLocalToolBar(this, manager);
		}
	}

	protected void fillLocalToolBar(IToolBarManager manager) {
		if(isExpandable()) {
			manager.add(fExpandAction);
			manager.add(fCollapseAction);
		}
		manager.add(fHelpAction);
		manager.add(new Separator());

		if(isFilterSource()) {
			manager.add(fRemoveSelection);
		}
		if(hasManagerCommands())  {
			PackInstallerViewUtils.addManagementCommandsToLocalToolBar(this, manager);
		}
	}

	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(manager -> fillContextMenu(manager));
		Menu menu = menuMgr.createContextMenu(fViewer.getControl());
		fViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, fViewer);
	}


	protected void fillContextMenu(IMenuManager manager) {
		if(isExpandable()) {
			ICmsisItem item = getSelectedItem();
			if (item == null ) {
				manager.add(fExpandAction);
				manager.add(fCollapseAction);
			} else {
				if(fViewer.isExpandable(item)) {
					boolean isExpanded = fViewer.getExpandedState(item);
					if(!isExpanded) {
						manager.add(fExpandItemAction);
					} else {
						manager.add(fCollapseItemAction);
					}
				}
			}
		}
		ICpPack pack = PackInstallerViewController.getPackFromSelection(fViewer.getSelection());
		if(pack != null && (isFilterSource() || isFilterClient())) {
			manager.add(new Separator());
			manager.add(fShowPackProperties);
		}

		if(isFilterSource()) {
			manager.add(new Separator());
			manager.add(fRemoveSelection);
		}
	}


	@Override
	public void handle(LiteEvent event) {
		String topic = event.getTopic();
		switch(topic) {
			case PackInstallerViewController.INSTALLER_UI_FILTER_CHANGED:
				if(isFilterClient()) {
					Display.getDefault().asyncExec(() -> handleFilterChanged());
				}
				return;
			case LiteEvent.PACK_OLNLINE_STATE_CHANGED:
				updateOnlineState(); // already in UI thread
				return;
		}
		Display.getDefault().asyncExec(() -> handleRteEvent(event));
	}

	protected void updateOnlineState() {
		if(getViewSite() == null || getViewSite().getActionBars() == null) {
			return;
		}
		IStatusLineManager mgr = getViewSite().getActionBars().getStatusLineManager();
		if(mgr == null) {
			return;
		}
		if (isFilterClient() && fViewer != null && !fViewer.getControl().isDisposed()) {
			fViewer.refresh();
		}
	}


	protected void handleRteEvent(LiteEvent event) {
		switch (event.getTopic()) {
			case LiteEvent.PACKS_RELOADED:
				refresh();
				break;
			case LiteEvent.PACK_INSTALL_JOB_FINISHED:
			case LiteEvent.PACK_REMOVE_JOB_FINISHED:
			case LiteEvent.PACK_DELETE_JOB_FINISHED:
				fViewer.refresh();
				break;
			default:
				return;
		}
	}

}
