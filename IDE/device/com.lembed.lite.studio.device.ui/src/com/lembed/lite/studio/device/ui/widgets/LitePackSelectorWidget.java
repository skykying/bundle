/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.ui.widgets;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.core.lite.packs.ILitePack;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackFamily;
import com.lembed.lite.studio.device.core.lite.packs.ILitePackItem;
import com.lembed.lite.studio.device.core.lite.packs.LitePack;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.tree.AdvisedCellLabelProvider;
import com.lembed.lite.studio.device.ui.tree.AdvisedEditingSupport;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;

/**
 * Tree widget to select pack versions
 */
public class LitePackSelectorWidget extends LiteWidget {
	protected final static String[] VERSION_MODES = new String[] { CpStrings.Latest, CpStrings.Fixed,
			CpStrings.Excluded };

	static final Color GREEN = new Color(Display.getCurrent(), CpPlugInUI.GREEN);
	static final Color YELLOW = new Color(Display.getCurrent(), CpPlugInUI.YELLOW);

	static final String[] PACK_ICONS = new String[] { CpPlugInUI.ICON_PACKAGE, CpPlugInUI.ICON_PACKAGE_EMPTY,
			CpPlugInUI.ICON_PACKAGE_GREY, CpPlugInUI.ICON_PACKAGE_RED, CpPlugInUI.ICON_PACKAGES,
			CpPlugInUI.ICON_PACKAGES_EMPTY, CpPlugInUI.ICON_PACKAGES_GREY, CpPlugInUI.ICON_PACKAGES_RED };
	public static final int ICON_INDEX_EMPTY = 1;
	public static final int ICON_INDEX_GREY = 2;
	public static final int ICON_INDEX_RED = 3;
	public static final int ICON_INDEX_PACKAGES = 4;

	TreeViewer viewer = null; // the Tree Viewer
	private static final int COLPACK = 0;
	private static final int COLSEL = 1;
	private static final int COLVERSION = 2;
	private static final int COLDESCR = 3;

	public LitePackSelectorWidget() {
	}

	public ILitePackItem getLitePackItem(Object obj) {
		if (obj instanceof ILitePackItem) {
			return (ILitePackItem) obj;
		}
		return null;
	}

	public ILitePackFamily getLitePackFamily(Object obj) {
		if (obj instanceof ILitePackFamily) {
			return (ILitePackFamily) obj;
		}
		return null;
	}

	public LitePack getLitePack(Object obj) {
		if (obj instanceof LitePack) {
			return (LitePack) obj;
		}
		return null;
	}

	public int getIconIndex(ILitePackItem item) {
		int index = 0;
		if (!item.isInstalled()) {
			if (item.isUsed()) {
				index = ICON_INDEX_RED;
			} else {
				index = ICON_INDEX_EMPTY;
			}
		} else if (item.isExcluded()) {
			index = ICON_INDEX_GREY;
		}

		if (item instanceof ILitePackFamily) {
			index += ICON_INDEX_PACKAGES;
		}
		return index;
	};

	/**
	 * Content provider for LiteValidateWidget tree
	 */
	public class LitePackProvider extends TreeObjectContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == getModelController()) {
				return getModelController().getLitePackCollection().getChildArray();
			}
			return super.getElements(inputElement);
		}
	}

	/**
	 * Column label provider for LitePackSelectorWidget
	 */
	public class LitePackSelectorColumnAdvisor extends LiteColumnAdvisor {
		/**
		 * Constructs advisor for a viewer
		 * 
		 * @param columnViewer
		 *            ColumnViewer on which the advisor is installed
		 */
		public LitePackSelectorColumnAdvisor(ColumnViewer columnViewer) {
			super(columnViewer);
		}

		@Override
		public CellControlType getCellControlType(Object obj, int columnIndex) {
			ILitePackItem item = getLitePackItem(obj);
			if (item == null) {
				return CellControlType.NONE;
			}
			ILitePackFamily packFamily = getLitePackFamily(obj);
			switch (columnIndex) {
			case COLSEL:
				if (packFamily != null) {
					return CellControlType.MENU;
				}
				return CellControlType.CHECK;
			case COLDESCR:
				if (packFamily != null) {
					String url = item.getUrl();
					if (url != null && !url.isEmpty()) {
						return CellControlType.URL;
					}
				}
				break;
			case COLPACK:
			case COLVERSION:
			default:
				break;
			}

			return CellControlType.TEXT;
		}

		@Override
		public boolean getCheck(Object obj, int columnIndex) {
			if (getCellControlType(obj, columnIndex) == CellControlType.CHECK) {
				ILitePack pack = getLitePack(obj);
				boolean check = pack.isSelected();
				return check;
			}
			return false;
		}

		@Override
		public String getString(Object obj, int index) {
			ILitePackItem item = getLitePackItem(obj);
			if (item != null) {
				String id = item.getId();
				ILitePackFamily packFamily = getLitePackFamily(obj);
				switch (index) {
				case COLPACK: {
					if (packFamily != null) {
						return id;
					}
					return item.getVersion();
				}
				case COLSEL:
					if (packFamily != null) {
						int i = (int) getCurrentSelectedIndex(obj, index);
						return VERSION_MODES[i];
					}
					break;
				case COLVERSION:
					if (packFamily != null) {
						return packFamily.getVersion();
					}
					break;
				case COLDESCR:
					if (packFamily != null || !item.isInstalled()) {
						return item.getDescription();
					}
				default:
					break;
				}
			}
			return CmsisConstants.EMPTY_STRING;
		}

		@Override
		public long getCurrentSelectedIndex(Object element, int columnIndex) {
			if (columnIndex == COLSEL) {
				ILitePackFamily packFamily = getLitePackFamily(element);
				if (packFamily != null) {
					return packFamily.getVersionMatchMode().ordinal();
				}
			}
			return -1;
		}

		@Override
		public String[] getStringArray(Object obj, int columnIndex) {
			if (columnIndex == COLSEL) {
				ILitePackFamily packFamily = getLitePackFamily(obj);
				if (packFamily != null) {
					return VERSION_MODES;
				}
			}
			return null;
		}

		@Override
		public boolean canEdit(Object obj, int columnIndex) {
			if (columnIndex == COLSEL) {
				return isEnabled(obj, columnIndex);
			}
			return false;
		}

		@Override
		public boolean isEnabled(Object obj, int columnIndex) {
			if (columnIndex == COLSEL) {
				if (getModelController().getLitePackCollection().isUseAllLatestPacks()) {
					return false;
				}
				ILitePack pack = getLitePack(obj);
				if (pack != null) {
					return pack.getVersionMatchMode() != EVersionMatchMode.LATEST;
				}
				return getLitePackFamily(obj) != null;
			}
			return true;
		}

		@Override
		public Image getImage(Object obj, int columnIndex) {
			ILitePackItem item = getLitePackItem(obj);
			if (item != null) {
				if (columnIndex == 0) {
					int iconIndex = getIconIndex(item);
					Image baseImage = CpPlugInUI.getImage(PACK_ICONS[iconIndex]);
					return getOverlayImage(baseImage, obj, columnIndex);
				}
				switch (columnIndex) {
				case COLVERSION:
				case COLSEL:
					break;
				default:
					break;
				}
			}
			return null;
		}

		@Override
		public String getUrl(Object obj, int columnIndex) {
			if (columnIndex == COLDESCR) {
				ILitePackFamily item = getLitePackFamily(obj);
				if (item != null) {
					return item.getUrl();
				}
			}
			return null;
		}

		private Image getOverlayImage(Image baseImage, Object obj, int columnIndex) {
			return baseImage;
		}

		@Override
		public String getTooltipText(Object obj, int columnIndex) {
			ILitePackItem item = getLitePackItem(obj);
			if (item == null) {
				return null;
			}

			switch (columnIndex) {
			case COLPACK:
				return item.getDescription();
			case COLVERSION:
				return null; // TODO
			case COLDESCR:
				String url = item.getUrl();
				if (url != null && !url.isEmpty()) {
					return url;
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		public void setCheck(Object element, int columnIndex, boolean newVal) {
			if (getCellControlType(element, columnIndex) == CellControlType.CHECK) {
				ILitePack pack = getLitePack(element);
				if (pack != null) {
					fModelController.selectPack(pack, newVal);
				}
			}
		}

		@Override
		public void setString(Object obj, int columnIndex, String newVal) {
			if (columnIndex != COLSEL) {
				return;
			}
			ILitePackFamily packFamily = getLitePackFamily(obj);
			if (packFamily == null) {
				return;
			}
			EVersionMatchMode mode = EVersionMatchMode.fromString(newVal);
			packFamily.setVersionMatchMode(mode);
			fModelController.setVesrionMatchMode(packFamily, mode);
		}

		@Override
		public Color getBgColor(Object obj, int columnIndex) {
			if (columnIndex != COLSEL) {
				return null;
			}
			ILitePackItem item = getLitePackItem(obj);
			if (item != null && item.isUsed()) {
				Device device = Display.getCurrent();
				if (!item.isInstalled()) {
					return device.getSystemColor(SWT.COLOR_RED);
				} else if (!item.isSelected()) {
					return YELLOW;
				}
				return GREEN;
			}
			return null;
		}

	} /// end of ColumnAdviser

	@Override
	public Composite createControl(Composite parent) {
		Tree tree = new Tree(parent, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tree.setHeaderVisible(true);
		viewer = new TreeViewer(tree);
		ColumnViewerToolTipSupport.enableFor(viewer);

		// Tree item name
		TreeViewerColumn column0 = new TreeViewerColumn(viewer, SWT.LEFT);
		tree.setLinesVisible(true);
		column0.getColumn().setText(CpStrings.Pack);
		column0.getColumn().setWidth(180);
		fColumnAdvisor = new LitePackSelectorColumnAdvisor(viewer);
		column0.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, COLPACK));
		AdvisedCellLabelProvider col0LabelProvider = new AdvisedCellLabelProvider(fColumnAdvisor, COLPACK);
		// workaround jface bug: first owner-draw column is not correctly
		// painted when column is resized
		col0LabelProvider.setOwnerDrawEnabled(false);
		column0.setLabelProvider(col0LabelProvider);

		// Check/menu box for selection
		TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.LEFT);
		tree.setLinesVisible(true);
		column1.getColumn().setText(CpStrings.Selection);
		column1.getColumn().setWidth(100);
		column1.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, COLSEL));
		column1.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, COLSEL));

		// Version
		TreeViewerColumn column2 = new TreeViewerColumn(viewer, SWT.LEFT);
		column2.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Version);
		column2.getColumn().setWidth(70);
		column2.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, COLVERSION));
		column2.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, COLVERSION));

		// Description/URL
		TreeViewerColumn column3 = new TreeViewerColumn(viewer, SWT.LEFT);
		column3.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Description);
		column3.getColumn().setWidth(400);
		column3.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, COLDESCR));
		column3.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, COLDESCR));

		viewer.setContentProvider(new LitePackProvider());

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;
		tree.setLayoutData(gridData);

		// hookContextMenu();
		return tree;
	}

	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		if (model != null) {
			viewer.setInput(fModelController);
		} else {
			viewer.setInput(null);
		}
		update();
	}

	@Override
	public void handle(LiteEvent event) {
	}

	@Override
	public void refresh() {
		viewer.refresh();
	}

	@Override
	public void update() {
		refresh();
	}

	@Override
	public Composite getFocusWidget() {
		return viewer.getTree();
	}

}
