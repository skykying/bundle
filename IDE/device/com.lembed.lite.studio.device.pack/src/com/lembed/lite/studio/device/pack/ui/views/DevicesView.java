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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.enums.EDeviceHierarchyLevel;
import com.lembed.lite.studio.device.core.info.CpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.item.ICmsisMapItem;
import com.lembed.lite.studio.device.pack.Messages;
import com.lembed.lite.studio.device.pack.ui.IHelpContextIds;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.tree.AdvisedCellLabelProvider;
import com.lembed.lite.studio.device.ui.tree.ColumnAdvisor;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;
import com.lembed.lite.studio.device.utils.AlnumComparator;


/**
 * Default implementation of the devices view in pack manager
 */
public class DevicesView extends PackInstallerView {

	public static final String ID = "com.lembed.lite.studio.device.pack.ui.views.DevicesView"; //$NON-NLS-1$

	static ILiteDeviceItem getDeviceTreeItem(Object obj) {
		if (obj instanceof ILiteDeviceItem) {
			return (ILiteDeviceItem)obj;
		}
		return null;
	}
	
	static boolean stopAtCurrentLevel(ILiteDeviceItem liteDeviceItem) {
		ILiteDeviceItem firstChild = liteDeviceItem.getFirstChild();
		if (firstChild == null || firstChild.getLevel() == EDeviceHierarchyLevel.PROCESSOR.ordinal()) {
			return true;
		}
		return false;
	}

	static class DeviceViewContentProvider extends TreeObjectContentProvider {

		@Override
		public Object getParent(Object child) {
			ILiteDeviceItem liteDeviceItem = getDeviceTreeItem(child);
			if(liteDeviceItem != null) {
				return liteDeviceItem.getParent();
			}
			return null;
		}

		@Override
		public Object [] getChildren(Object parent) {
			ILiteDeviceItem liteDeviceItem = getDeviceTreeItem(parent);
			if(liteDeviceItem != null) {
				if(!liteDeviceItem.hasChildren() || stopAtCurrentLevel(liteDeviceItem)) {
					return null;
				}
				return liteDeviceItem.getChildArray();
			}
			return super.getChildren(parent);
		}

		@Override
		public boolean hasChildren(Object parent) {
			ILiteDeviceItem liteDeviceItem = getDeviceTreeItem(parent);
			if(liteDeviceItem != null) {
				if (stopAtCurrentLevel(liteDeviceItem)) {
					return false;
				}
				return liteDeviceItem.hasChildren();
			}
			return super.hasChildren(parent);
		}
	}

	static class DevicesViewLabelProvider extends ColumnLabelProvider {

		@Override
		public String getText(Object obj) {
			ILiteDeviceItem liteDeviceItem = getDeviceTreeItem(obj);
			if (liteDeviceItem != null) {
				// added spaces at last of text as a workaround to show the complete text in the views
				return removeColon(liteDeviceItem.getName()) + ' ';
			}
			return CmsisConstants.EMPTY_STRING;
		}

		private String removeColon(String string) {
			if (string.indexOf(':') != -1) {
				return string.substring(0, string.indexOf(':'));
			}
			return string;
		}

		@Override
		public Image getImage(Object obj){
			ILiteDeviceItem liteDeviceItem = getDeviceTreeItem(obj);
			if(liteDeviceItem != null) {
				if (liteDeviceItem.getLevel() == EDeviceHierarchyLevel.VENDOR.ordinal()) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT);
				} else if (liteDeviceItem.hasChildren() && !stopAtCurrentLevel(liteDeviceItem)) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_CLASS);
				} else if (packInstalledAndContainsDevice(liteDeviceItem)) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE);
				} else {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_DEPRDEVICE);
				}
			}

			return  null;
		}

		private boolean packInstalledAndContainsDevice(ILiteDeviceItem liteDeviceItem) {
			ILiteDeviceItem deviceItem;
			if (liteDeviceItem.getDevice() == null) {
				ILiteDeviceItem parent = getClosestParentRteDeviceItem(liteDeviceItem);
				
				if(parent == null){
					return false;
				}
				
				deviceItem = parent.findItem(liteDeviceItem.getName(), liteDeviceItem.getVendorName(), false);
				if (deviceItem == null) {
					return false;
				}
			} else {
				deviceItem = liteDeviceItem;
			}
			return deviceItem.getDevice().getPack().getPackState() == PackState.INSTALLED ||
					deviceItem.getDevice().getPack().getPackState() == PackState.GENERATED;
		}

		@Override
		public String getToolTipText(Object obj) {
			ILiteDeviceItem item = getDeviceTreeItem(obj);
			ILiteDeviceItem parent = getClosestParentRteDeviceItem(item);
			ILiteDeviceItem liteDeviceItem;
			if (parent == item) {
				liteDeviceItem = item;
			} else {
				liteDeviceItem = parent.findItem(item.getName(), item.getVendorName(), false);
			}
			if(liteDeviceItem != null && liteDeviceItem.getDevice() != null) {
				return NLS.bind(Messages.DevicesView_AvailableInPack, liteDeviceItem.getDevice().getPackId());
			}
			return null;
		}

		private ILiteDeviceItem getClosestParentRteDeviceItem(ILiteDeviceItem item) {
			ILiteDeviceItem parent = item;
			while (parent != null && parent.getAllDeviceNames().isEmpty()) {
				parent = parent.getParent();
			}
			return parent;
		}
	}

	static class DevicesViewColumnAdvisor extends ColumnAdvisor {

		public DevicesViewColumnAdvisor(ColumnViewer columnViewer) {
			super(columnViewer);
		}

		@Override
		public CellControlType getCellControlType(Object obj, int columnIndex) {
			if (columnIndex == COLURL) {
				ILiteDeviceItem item = getDeviceTreeItem(obj);
				if (item != null) {
					if (item.getLevel() == EDeviceHierarchyLevel.VARIANT.ordinal()
							|| (item.getLevel() == EDeviceHierarchyLevel.DEVICE.ordinal()
							&& stopAtCurrentLevel(item))) {
						return CellControlType.URL;
					}
				}
			}
			return CellControlType.TEXT;
		}

		@Override
		public String getString(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteDeviceItem item = getDeviceTreeItem(obj);
				ICpDeviceInfo deviceInfo = new CpDeviceInfo(null, item);
				return deviceInfo.getSummary();
			} else if (columnIndex == COLURL) {
				ILiteDeviceItem item = getDeviceTreeItem(obj);
				int nrofDevices = item.getAllDeviceNames().size();
				if (nrofDevices == 1) {
					return Messages.DevicesView_1Device;
				} else if (nrofDevices == 0) {
					return Messages.DevicesView_Processor;
				} else {
					return nrofDevices + Messages.DevicesView_Devices;
				}
			}
			return null;
		}

		@Override
		public String getUrl(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteDeviceItem item = getDeviceTreeItem(obj);
				return item.getUrl();
			}
			return null;
		}

		@Override
		public String getTooltipText(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteDeviceItem item = getDeviceTreeItem(obj);
				return item.getUrl();
			}
			return null;
		}

	}

	class DeviceTreeColumnComparator extends TreeColumnComparator {

		private final AlnumComparator alnumComparator;

		public DeviceTreeColumnComparator(TreeViewer viewer, ColumnAdvisor advisor) {
			super(viewer, advisor);
			alnumComparator = new AlnumComparator(false, false);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Tree tree = fViewer.getTree();
			int index = getColumnIndex();
			if (index != 0) {
				return super.compare(viewer, e1, e2);
			}

			int result = 0;
			ColumnLabelProvider colLabelProvider = (ColumnLabelProvider) treeViewer.getLabelProvider(index);
			String str1 = colLabelProvider.getText(e1);
			String str2 = colLabelProvider.getText(e2);
			result = alnumComparator.compare(str1, str2);

			return tree.getSortDirection() == SWT.DOWN ? -result : result;
		}
	}

	public DevicesView() {
	}

	@Override
	public boolean isFilterSource() {
		return true;
	}
	
	@Override
	protected String getHelpContextId() {
		return IHelpContextIds.DEVICES_VIEW;
	}

	
	@Override
	public void createTreeColumns() {

		TreeViewerColumn column0 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column0.getColumn().setText(CmsisConstants.DEVICE_TITLE);
		column0.getColumn().setWidth(200);
		column0.setLabelProvider(new DevicesViewLabelProvider());

		TreeViewerColumn column1 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column1.getColumn().setText(CmsisConstants.SUMMARY_TITLE);
		column1.getColumn().setWidth(300);
		DevicesViewColumnAdvisor columnAdvisor = new DevicesViewColumnAdvisor(fViewer);
		column1.setLabelProvider(new AdvisedCellLabelProvider(columnAdvisor, COLURL));

		fViewer.setContentProvider(new DeviceViewContentProvider());
		fViewer.setComparator(new DeviceTreeColumnComparator(fViewer, columnAdvisor));
		fViewer.setAutoExpandLevel(2);
	}

	protected void refresh() {
		if(CpPlugIn.getDefault() == null) {
			return;
		}
		ICpPackManager packManager = CpPlugIn.getPackManager();
		if(packManager != null) {
			ICmsisMapItem<ILiteDeviceItem> root = new CmsisMapItem<>();
			ILiteDeviceItem allDevices = packManager.getDevices();
			root.addChild(allDevices);
			if (!fViewer.getControl().isDisposed()) {
				fViewer.setInput(root);
			}
		} else {
			if (!fViewer.getControl().isDisposed()) {
				fViewer.setInput(null);
			}
		}
	}
}
