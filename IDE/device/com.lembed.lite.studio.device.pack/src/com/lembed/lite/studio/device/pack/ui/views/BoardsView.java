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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
import com.lembed.lite.studio.device.core.data.ICpBoard;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.lite.boards.ILiteBoardDeviceItem;
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
import com.lembed.lite.studio.device.utils.VersionComparator;


/**
 * Default implementation of the boards view in pack manager
 */
public class BoardsView extends PackInstallerView {

	public static final String ID = "com.lembed.lite.studio.device.pack.ui.views.BoardsView"; //$NON-NLS-1$

	static final String ALL_BOARDS = Messages.BoardsView_AllBoards;
	private static final String MOUNTED_DEVICES = CmsisConstants.MOUNTED_DEVICES;
	private static final String COMPATIBLE_DEVICES = CmsisConstants.COMPATIBLE_DEVICES;

	ILiteBoardDeviceItem getBoardDeviceTreeItem(Object obj) {
		if (obj instanceof ILiteBoardDeviceItem) {
			return (ILiteBoardDeviceItem) obj;
		}
		return null;
	}

	class BoardViewContentProvider extends TreeObjectContentProvider {

		private DevicesView.DeviceViewContentProvider deviceViewContentProvider = new DevicesView.DeviceViewContentProvider();
		private Map<ILiteDeviceItem, ILiteBoardDeviceItem> deviceToBoardMap = new HashMap<>();

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			ILiteBoardDeviceItem liteBoardDeviceItem = getBoardDeviceTreeItem(parentElement);
			if (liteBoardDeviceItem != null) {
				if (ALL_BOARDS.equals(liteBoardDeviceItem.getName())) { // All boards
					return liteBoardDeviceItem.getChildArray();
				}
				// Normal board
				Collection<ILiteDeviceItem> children = new LinkedList<>();
				ILiteDeviceItem mountedDevices = liteBoardDeviceItem.getMountedDevices();
				if (mountedDevices != null) {
					children.add(mountedDevices);
				}
				deviceToBoardMap.put(mountedDevices, liteBoardDeviceItem);

				ILiteDeviceItem compatibleDevices = liteBoardDeviceItem.getCompatibleDevices();
				if (compatibleDevices != null) {
					children.add(compatibleDevices);
				}
				deviceToBoardMap.put(compatibleDevices, liteBoardDeviceItem);

				return children.toArray();
			} else if (parentElement instanceof ILiteDeviceItem) {
				return deviceViewContentProvider.getChildren(parentElement);
			}

			return super.getChildren(parentElement);

		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof ILiteBoardDeviceItem) { // Board Node
				return ((ILiteBoardDeviceItem) element).getParent();
			} else if (element instanceof ILiteDeviceItem) {
				ILiteDeviceItem item = (ILiteDeviceItem) element;
				if (MOUNTED_DEVICES.equals(item.getName()) || COMPATIBLE_DEVICES.equals(item.getName())) {
					return deviceToBoardMap.get(item);
				}
				return item.getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof ILiteDeviceItem) {
				return deviceViewContentProvider.hasChildren(element);
			}
			return getChildren(element).length > 0;
		}

	}

	class BoardsViewLabelProvider extends ColumnLabelProvider {

		private DevicesView.DevicesViewLabelProvider devicesViewLabelProvider = new DevicesView.DevicesViewLabelProvider();

		@Override
		public String getText(Object element) {
			if (element instanceof ILiteBoardDeviceItem) {
				ILiteBoardDeviceItem bdItem = (ILiteBoardDeviceItem) element;
				// added spaces at last of text as a workaround to show the complete text in the views
				if (ALL_BOARDS.equals(bdItem.getName()) || MOUNTED_DEVICES.equals(bdItem.getName())
						|| COMPATIBLE_DEVICES.equals(bdItem.getName())) {
					return bdItem.getName() + ' ';
				}
				return getBoardTitle(bdItem.getBoard()) + ' ';
			} else if (element instanceof ILiteDeviceItem) {
				return devicesViewLabelProvider.getText(element);
			}
			return CmsisConstants.EMPTY_STRING;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof ILiteBoardDeviceItem) {

				ILiteBoardDeviceItem bdItem = (ILiteBoardDeviceItem) element;
				if (ALL_BOARDS.equals(bdItem.getName()) || MOUNTED_DEVICES.equals(bdItem.getName())
						|| COMPATIBLE_DEVICES.equals(bdItem.getName())) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_CLASS);
				}

				if (packInstalledAndContainsBoard(((ILiteBoardDeviceItem) element).getBoard())) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_BOARD);
				}
				return CpPlugInUI.getImage(CpPlugInUI.ICON_BOARD_GREY);
			} else if (element instanceof ILiteDeviceItem) {
				return devicesViewLabelProvider.getImage(element);
			} else {
				return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_CLASS);
			}
		}

		private boolean packInstalledAndContainsBoard(ICpBoard board) {
			if (board == null) {
				return false;
			}
			return board.getPack().getPackState() == PackState.INSTALLED
					|| board.getPack().getPackState() == PackState.GENERATED;
		}

		@Override
		public String getToolTipText(Object obj) {
			if (obj instanceof ILiteBoardDeviceItem) {
				ILiteBoardDeviceItem board = (ILiteBoardDeviceItem) obj;
				if (board.getBoard() != null) {
					return NLS.bind(Messages.BoardsView_AvailableInPack, board.getBoard().getPackId());
				}
			} else if (obj instanceof ILiteDeviceItem) {
				return devicesViewLabelProvider.getToolTipText(obj);
			}
			return null;
		}
	}

	class BoardsViewColumnAdvisor extends ColumnAdvisor {

		private DevicesView.DevicesViewColumnAdvisor devicesViewColumnAdvisor = new DevicesView.DevicesViewColumnAdvisor(null);

		public BoardsViewColumnAdvisor(ColumnViewer columnViewer) {
			super(columnViewer);
		}

		@Override
		public CellControlType getCellControlType(Object obj, int columnIndex) {
			if (columnIndex == COLURL) {
				ILiteBoardDeviceItem item = getBoardDeviceTreeItem(obj);
				if (item != null && item.getBoard() != null) {
					return CellControlType.URL;
				} else if (obj instanceof ILiteDeviceItem) {
					return devicesViewColumnAdvisor.getCellControlType(obj, columnIndex);
				}
			}
			return CellControlType.TEXT;
		}

		@Override
		public String getString(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteBoardDeviceItem item = getBoardDeviceTreeItem(obj);
				if (item != null) {
					if (item.getMountedDevices() != null) {
						return item.getMountedDevices().getFirstChildKey();
					} else if (item.getCompatibleDevices() != null) {
						return item.getCompatibleDevices().getFirstChildKey();
					} else if (item.getBoard() != null) {
						return item.getBoard().getName();
					}
				} else if (obj instanceof ILiteDeviceItem) {
					return devicesViewColumnAdvisor.getString(obj, columnIndex);
				}
			} else if (columnIndex == COLURL) {
				ILiteBoardDeviceItem item = getBoardDeviceTreeItem(obj);
				if (item != null) {
					if (ALL_BOARDS.equals(item.getName())) {
						int nrofBoards = item.getChildCount();
						return nrofBoards + Messages.BoardsView_Boards;
					}
				} else if (obj instanceof ILiteDeviceItem) {
					return devicesViewColumnAdvisor.getString(obj, columnIndex);
				}
			}
			return null;
		}

		@Override
		public String getUrl(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteBoardDeviceItem item = getBoardDeviceTreeItem(obj);
				if (item != null) {
					return item.getUrl();
				} else if (obj instanceof ILiteDeviceItem) {
					return devicesViewColumnAdvisor.getTooltipText(obj, columnIndex);
				}
			}
			return null;
		}

		@Override
		public String getTooltipText(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.URL) {
				ILiteBoardDeviceItem item = getBoardDeviceTreeItem(obj);
				if (item != null) {
					return item.getUrl();
				} else if (obj instanceof ILiteDeviceItem) {
					return devicesViewColumnAdvisor.getTooltipText(obj, columnIndex);
				}
			}
			return null;
		}

	}

	class BoardTreeColumnComparator extends TreeColumnComparator {

		private final AlnumComparator alnumComparator;
		private VersionComparator versionComparator;

		public BoardTreeColumnComparator(TreeViewer viewer, ColumnAdvisor advisor) {
			super(viewer, advisor);
			alnumComparator = new AlnumComparator(false);
			versionComparator = new VersionComparator(false);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {

			Tree tree = treeViewer.getTree();
			if (getColumnIndex() != 0) {
				return super.compare(viewer, e1, e2);
			}

			int result = 0;
			if ((e1 instanceof ILiteBoardDeviceItem) && (e2 instanceof ILiteBoardDeviceItem)) {

				ILiteBoardDeviceItem cp1 = (ILiteBoardDeviceItem) e1;
				ILiteBoardDeviceItem cp2 = (ILiteBoardDeviceItem) e2;

				String title1 = getBoardTitle(cp1.getBoard());
				String title2 = getBoardTitle(cp2.getBoard());

				result = versionComparator.compare(title1, title2);

			} else if ((e1 instanceof ILiteDeviceItem) && (e2 instanceof ILiteDeviceItem)) {
				ILiteDeviceItem d1 = (ILiteDeviceItem) e1;
				ILiteDeviceItem d2 = (ILiteDeviceItem) e2;
				if (MOUNTED_DEVICES.equals(d1.getName()) || COMPATIBLE_DEVICES.equals(d1.getName())) {
					return 0;
				}
				result = alnumComparator.compare(d1.getName(), d2.getName());
			}
			return tree.getSortDirection() == SWT.DOWN ? -result : result;
		}
	}

	String getBoardTitle(ICpBoard cpBoard) {
		if (cpBoard == null) {
			return CmsisConstants.EMPTY_STRING;
		}
		String boardTitle = cpBoard.getAttribute(CmsisConstants.NAME);
		if (!cpBoard.getAttribute(CmsisConstants.REVISION).isEmpty()) {
			boardTitle += " (" + cpBoard.getAttribute(CmsisConstants.REVISION) + ')'; //$NON-NLS-1$
		}
		return boardTitle;
	}

	public BoardsView() {
	}
	
	@Override
	protected String getHelpContextId() {
		return IHelpContextIds.BOARDS_VIEW;
	}

	@Override
	public boolean isFilterSource() {
		return true;
	}

	@Override
	public void createTreeColumns() {

		TreeViewerColumn column0 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column0.getColumn().setText(CmsisConstants.BOARD_TITLE);
		column0.getColumn().setWidth(200);
		column0.setLabelProvider(new BoardsViewLabelProvider());

		TreeViewerColumn column1 = new TreeViewerColumn(fViewer, SWT.LEFT);
		column1.getColumn().setText(CmsisConstants.SUMMARY_TITLE);
		column1.getColumn().setWidth(300);
		BoardsViewColumnAdvisor columnAdvisor = new BoardsViewColumnAdvisor(fViewer);
		column1.setLabelProvider(new AdvisedCellLabelProvider(columnAdvisor, COLURL));

		fViewer.setContentProvider(new BoardViewContentProvider());
		fViewer.setComparator(new BoardTreeColumnComparator(fViewer, columnAdvisor));
		fViewer.setAutoExpandLevel(2);
	}

	@Override
	protected void refresh() {
		if (CpPlugIn.getDefault() == null) {
			return;
		}
		ICpPackManager packManager = CpPlugIn.getPackManager();
		if (packManager != null && packManager.getBoards() != null) {
			ICmsisMapItem<ILiteBoardDeviceItem> root = new CmsisMapItem<>();
			ILiteBoardDeviceItem allBoardRoot = packManager.getLiteBoardDevices();
			root.addChild(allBoardRoot);
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
