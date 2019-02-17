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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.DeviceVendor;
import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpBoard;
import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.lite.examples.ILiteExampleItem;
import com.lembed.lite.studio.device.pack.Messages;
import com.lembed.lite.studio.device.pack.ui.IHelpContextIds;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.tree.AdvisedCellLabelProvider;
import com.lembed.lite.studio.device.ui.tree.ColumnAdvisor;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;
import com.lembed.lite.studio.device.utils.AlnumComparator;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;


/**
 * Default implementation of the examples view in pack manager
 */
public class ExamplesView extends PackInstallerView {

	public static final String ID = "com.lembed.lite.studio.device.pack.ui.views.ExamplesView"; //$NON-NLS-1$

	Action fShowInstOnlyAction;

	ILiteExampleItem getRteExampleItem(Object obj) {
		if (obj instanceof ILiteExampleItem) {
			return (ILiteExampleItem) obj;
		}
		return null;
	}

	class ExamplesViewColumnAdvisor extends ColumnAdvisor {

		public ExamplesViewColumnAdvisor(ColumnViewer columnViewer) {
			super(columnViewer);
		}

		@Override
		public CellControlType getCellControlType(Object obj, int columnIndex) {
			if (columnIndex == COLBUTTON) {
				return CellControlType.BUTTON;
			}
			return CellControlType.TEXT;
		}

		@Override
		public boolean isEnabled(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.BUTTON) {
				ICpPackInstaller packInstaller = getPackInstaller();
				if(packInstaller == null) {
					return false;
				}
				ILiteExampleItem example = getRteExampleItem(obj);
				if (example != null) {
					ICpExample e = example.getExample();
					if (e == null || packInstaller.isProcessing(e.getPackId())) {
						return false;
					} else {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public Image getImage(Object obj, int columnIndex) {
			if (getCellControlType(obj,	columnIndex) == CellControlType.BUTTON) {
				switch (getString(obj, columnIndex)) {
					case CmsisConstants.BUTTON_COPY :
						return CpPlugInUI.getImage(CpPlugInUI.ICON_LITE);
					case CmsisConstants.BUTTON_INSTALL :
						return CpPlugInUI.getImage(CpPlugInUI.ICON_RTE_INSTALL);
					default :
						break;
				}
			}
			return null;
		}

		@Override
		public String getString(Object obj, int index) {
			if (getCellControlType(obj, index) == CellControlType.BUTTON) {
				ILiteExampleItem item = getRteExampleItem(obj);
				if (item != null) {
					if (item.getExample().getPack().getPackState() == PackState.INSTALLED) {
						return CmsisConstants.BUTTON_COPY;
					}
					return CmsisConstants.BUTTON_INSTALL;
				}
			}
			return CmsisConstants.EMPTY_STRING;
		}

		@Override
		public String getTooltipText(Object obj, int columnIndex) {
			if (getCellControlType(obj,
					columnIndex) == CellControlType.BUTTON) {
				ILiteExampleItem item = getRteExampleItem(obj);
				if (item != null) {
					if (item.getExample().getPack()
							.getPackState() != PackState.INSTALLED) {
						StringBuilder str = new StringBuilder(
								Messages.ExamplesView_CopyExampleInstallPack)
										.append(item.getExample().getPackId());
						return str.toString();
					}
					return constructExampleTooltipText(item.getExample());
				}
			}
			return null;
		}

		@Override
		protected void handleMouseUp(MouseEvent e) {
			Point pt = new Point(e.x, e.y);
			ViewerCell cell = getViewer().getCell(pt);

			if (cell == null) {
				return;
			}

			int colIndex = cell.getColumnIndex();
			Object element = cell.getElement();
			if (getCellControlType(element, colIndex) != CellControlType.BUTTON
					|| !isEnabled(element, colIndex)
					|| !isButtonPressed(element, colIndex)) {
				return;
			}

			ILiteExampleItem example = getRteExampleItem(element);
			if (example != null) {
				switch (getString(element, colIndex)) {
					case CmsisConstants.BUTTON_COPY :
						ICpExample cpExample = example.getExample();
						copyExample(cpExample);
						DeviceUIUtils.clearReadOnly(ResourcesPlugin
								.getWorkspace().getRoot().getLocation()
								.append(DeviceUIUtils.extractBaseFileName(
										cpExample.getFolder()))
								.toFile(), CmsisConstants.EMPTY_STRING);
						break;
					case CmsisConstants.BUTTON_INSTALL :
						ICpPackInstaller packInstaller = getPackInstaller();
						if(packInstaller != null) {
							packInstaller.installPack(example.getExample().getPackId());
						}
					default :
						break;
				}
			}

			setButtonPressed(null, COLBUTTON, null);
			this.control.redraw();
		}

	} /// end of ColumnAdviser

	void copyExample(ICpExample cpExample) {
		if(fViewController == null) {
			return;
		}
		fViewController.copyExample(cpExample);
	}

	String constructExampleTooltipText(ICpExample example) {
		ICpBoard b = example.getBoard();
		String tooltip = CmsisConstants.EMPTY_STRING;
		if(b != null) {
			String line1 = NLS.bind(Messages.ExamplesView_Board, b.getName(), b.getVendor());
			StringBuilder lb2 = new StringBuilder(Messages.ExamplesView_Device);
			for (ICpItem device : b.getMountedDevices()) {
				String vendorName = DeviceVendor
						.getOfficialVendorName(device.getVendor());
				String deviceName = CmsisConstants.EMPTY_STRING;
				if (device.hasAttribute(CmsisConstants.DFAMILY)) {
					deviceName = device.getAttribute(CmsisConstants.DFAMILY);
				} else if (device.hasAttribute(CmsisConstants.DSUBFAMILY)) {
					deviceName = device.getAttribute(CmsisConstants.DSUBFAMILY);
				} else if (device.hasAttribute(CmsisConstants.DNAME)) {
					deviceName = device.getAttribute(CmsisConstants.DNAME);
				} else if (device.hasAttribute(CmsisConstants.DVARIANT)) {
					deviceName = device.getAttribute(CmsisConstants.DVARIANT);
				}
				if (!deviceName.isEmpty()) {
					lb2.append(deviceName).append(" (").append(vendorName) //$NON-NLS-1$
					.append("), "); //$NON-NLS-1$
				}
			}
			if (lb2.lastIndexOf(",") >= 0) { //$NON-NLS-1$
				lb2.deleteCharAt(lb2.lastIndexOf(",")); //$NON-NLS-1$
			}
			String line2 = lb2.append(System.lineSeparator()).toString();
			tooltip = line1 + line2;
		}
		String line3 = NLS.bind(Messages.ExamplesView_Pack,	example.getPackId());
		String line4 = example.getDescription();
		return tooltip + line3 + line4;
	}

	class ExampleTreeColumnComparator extends TreeColumnComparator {

		private final AlnumComparator alnumComparator;

		public ExampleTreeColumnComparator(TreeViewer viewer,
				ColumnAdvisor advisor) {
			super(viewer, advisor);
			alnumComparator = new AlnumComparator(false, false);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {

			Tree tree = treeViewer.getTree();
			if (getColumnIndex() != 0) {
				return super.compare(viewer, e1, e2);
			}

			ICpExample cp1 = ((ILiteExampleItem) e1).getExample();
			ICpExample cp2 = ((ILiteExampleItem) e2).getExample();

			int result = alnumComparator.compare(cp1.getId(), cp2.getId());
			return tree.getSortDirection() == SWT.DOWN ? -result : result;
		}
	}

	public ExamplesView() {
	}

	@Override
	public void createTreeColumns() {
		fTree.setInitialText(Messages.ExamplesView_SearchExample);

		// ------ Start Setting ALL Columns for the Examples View
		// ------ First Column
		TreeViewerColumn column0 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column0.getColumn().setText(CmsisConstants.EXAMPLE_TITLE);
		column0.getColumn().setWidth(300);
		column0.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ICpExample e = ((ILiteExampleItem) element).getExample();

				return e.getId();
			}

			@Override
			public String getToolTipText(Object element) {
				ICpExample e = ((ILiteExampleItem) element).getExample();

				return constructExampleTooltipText(e);
			}
		});

		// ------ Second Column
		TreeViewerColumn column1 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column1.getColumn().setText(CmsisConstants.ACTION_TITLE);
		column1.getColumn().setWidth(90);
		ExamplesViewColumnAdvisor columnAdvisor = new ExamplesViewColumnAdvisor(fViewer);
		column1.setLabelProvider(new AdvisedCellLabelProvider(columnAdvisor, COLBUTTON));

		// ------ Third Column
		TreeViewerColumn column2 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column2.getColumn().setText(CmsisConstants.DESCRIPTION_TITLE);
		column2.getColumn().setWidth(400);
		column2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object obj) {
				ILiteExampleItem example = getRteExampleItem(obj);
				if (example != null) {
					return example.getExample().getDescription();
				}
				return null;
			}
		});
		// ------ End Setting ALL Columns for the Examples View

		fViewer.setContentProvider(new TreeObjectContentProvider());
		fViewer.setComparator(new ExampleTreeColumnComparator(fViewer, columnAdvisor));
	}

	@Override
	protected String getHelpContextId() {
		return IHelpContextIds.EXAMPLES_VIEW;
	}

	@Override
	protected boolean isExpandable() {
		return false;
	}

	@Override
	protected boolean hasManagerCommands() {
		return true;
	}
	
	@Override
	protected void refresh() {
		if (CpPlugIn.getDefault() == null) {
			return;
		}
		ICpPackManager packManager = CpPlugIn.getPackManager();
		if (packManager != null) {
			fViewer.setInput(packManager.getExamples());
		} else {
			fViewer.setInput(null);
		}
	}

	@Override
	protected void makeActions() {
		fShowInstOnlyAction = new Action(
				Messages.ExamplesView_OnlyShowInstalledPack,
				IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				fViewController.getFilter().setInstalledOnly(fShowInstOnlyAction.isChecked());
				fViewer.setFilters(fViewFilters);
				fViewer.setSelection(null);
				if (fShowInstOnlyAction.isChecked()) {
					fShowInstOnlyAction.setImageDescriptor(CpPlugInUI
							.getImageDescriptor(CpPlugInUI.ICON_CHECKED));
				} else {
					fShowInstOnlyAction.setImageDescriptor(CpPlugInUI
							.getImageDescriptor(CpPlugInUI.ICON_UNCHECKED));
				}
			}
		};
		fShowInstOnlyAction	.setToolTipText(Messages.ExamplesView_OnlyShowInstalledPack);
		fShowInstOnlyAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_UNCHECKED));
		fShowInstOnlyAction.setEnabled(true);

		super.makeActions();
	}


	@Override
	protected void fillLocalPullDown(IMenuManager manager) {
		manager.add(fShowInstOnlyAction);
		manager.add(new Separator());
		super.fillLocalPullDown(manager);
	}

	@Override
	protected void fillLocalToolBar(IToolBarManager manager) {
		ActionContributionItem aci = new ActionContributionItem(fShowInstOnlyAction);
		aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		manager.add(aci);
		manager.add(new Separator());
		super.fillLocalToolBar(manager);
	}

}
