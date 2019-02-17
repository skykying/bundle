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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpGenerator;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.lite.ILiteModel;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentBundle;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentClass;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentGroup;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.core.lite.components.LiteMoreClass;
import com.lembed.lite.studio.device.core.lite.components.LiteSelectedDeviceClass;
import com.lembed.lite.studio.device.generic.ITreeObject;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.LaunchGenerator;
import com.lembed.lite.studio.device.ui.tree.AdvisedCellLabelProvider;
import com.lembed.lite.studio.device.ui.tree.AdvisedEditingSupport;
import com.lembed.lite.studio.device.ui.tree.OverlayImage;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;
import com.lembed.lite.studio.device.ui.tree.OverlayImage.OverlayPos;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;

/**
 * This class displays the component tree for selection.
 *
 */
public class LiteComponentSelectorWidget extends LiteWidget {

	// constants for column number
	static final int COLSWCOMP = 0;
	static final int COLSEL = 1;
	static final int COLVARIANT = 2;
	static final int COLVENDOR = 3;
	static final int COLVERSION = 4;
	static final int COLDESCR = 5;

	private Action expandAll;
	private Action collapseAll;
	private Action expandAllSelected;

	protected Map<String, LaunchGeneratorAction> laGeneratorActions = new HashMap<>();

	TreeViewer viewer = null; // the Tree Viewer

	static final Color GREEN = new Color(Display.getCurrent(), CpPlugInUI.GREEN);
	static final Color YELLOW = new Color(Display.getCurrent(), CpPlugInUI.YELLOW);

	private List<String> selItemKeyPath = null;

	/**
	 * Return the effective component
	 */
	public ILiteComponentItem getComponentItem(Object obj) {
		if (obj instanceof ILiteComponentItem) {
			return ((ILiteComponentItem) obj).getEffectiveItem();
		}
		return null;
	}

	public void launchGenerator(ICpGenerator gen, String launchType) {
		if (gen == null) {
			return;
		}
		LaunchGenerator.launch(gen, getModelController().getConfigurationInfo(), launchType);
	}

	/**
	 * Action to launch a generator for selected item
	 */
	public class LaunchGeneratorAction extends Action {
		protected String fLaunchType;

		public LaunchGeneratorAction(String launchType) {
			fLaunchType = launchType;
		}

		public String getLaunchType() {
			return fLaunchType;
		}

		@Override
		public void run() {
			launchGenerator(getSelectedGenerator(), getLaunchType());
		}
	}

	/**
	 * Column label provider for LiteComponentTreeWidget
	 */
	public class LiteComponentColumnAdvisor extends LiteColumnAdvisor {
		/**
		 * Constructs advisor for a viewer
		 * 
		 * @param columnViewer
		 *            ColumnViewer on which the advisor is installed
		 */
		public LiteComponentColumnAdvisor(ColumnViewer columnViewer) {
			super(columnViewer);
		}

		/**
		 * Return true if the tree item (RTE component) at column index contains
		 * a check box
		 * 
		 * @param obj
		 * @return true if tree item at columnIndex is check box
		 */
		private CellControlType getSelectionControlType(Object obj) {
			ILiteComponentItem item = getComponentItem(obj);
			if (item != null) {
				if (item instanceof ILiteComponent) {
					int count = ((ILiteComponent) item).getMaxInstanceCount();
					if (count == 1) {
						return CellControlType.INPLACE_CHECK;
					} else if (count > 1) {
						return CellControlType.INPLACE_SPIN;
					}
				}
			}
			return CellControlType.NONE;
		}

		@Override
		public CellControlType getCellControlType(Object obj, int columnIndex) {
			ILiteComponentItem item = getComponentItem(obj);
			int minItems = 0;
			if (item == null) {
				return CellControlType.NONE;
			}
			Collection<String> strings = null;
			switch (columnIndex) {
			case COLSWCOMP:
				break;
			case COLSEL:
				return getSelectionControlType(obj);
			case COLVARIANT:
				strings = item.getVariantStrings();
				minItems = 1;
				break;
			case COLVENDOR:
				strings = item.getVendorStrings();
				minItems = 1;
				break;
			case COLVERSION:
				strings = item.getVersionStrings();
				break;
			case COLDESCR:
				String url = item.getUrl();
				if (url != null && !url.isEmpty()) {
					return CellControlType.URL;
				}
				break;
			default:
				break;
			}

			if (strings != null && strings.size() > minItems) {
				return CellControlType.MENU;
			}

			return CellControlType.TEXT;
		}

		@Override
		public boolean getCheck(Object obj, int columnIndex) {
			if (columnIndex != COLSEL) {
				return false;
			}
			if (getSelectionControlType(obj) == CellControlType.CHECK
					|| getSelectionControlType(obj) == CellControlType.INPLACE_CHECK) {
				ILiteComponentItem item = getComponentItem(obj);
				if (item instanceof ILiteComponent) {
					boolean check = item.isSelected();
					return check;
				}
			}
			return false;
		}

		@Override
		public boolean hasSuffixButton(Object obj, int columnIndex) {
			if (columnIndex == COLSEL && getSelectionControlType(obj) == CellControlType.INPLACE_CHECK) {
				ILiteComponentItem item = getComponentItem(obj);
				if (getGenerator(item) != null) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isEnabled(Object obj, int columnIndex) {
			ILiteComponentItem item = getComponentItem(obj);
			if (columnIndex == COLSEL || columnIndex == COLSWCOMP || columnIndex == COLDESCR) {
				return true;
			}
			if (item instanceof ILiteComponent) {
				ILiteComponent liteComponent = (ILiteComponent) item;
				if (liteComponent.isBootStrap()) {
					return true;
				} else if (liteComponent.isGenerated()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isSuffixButtonEnabled(Object obj, int columnIndex) {
			if (columnIndex == COLSEL && hasSuffixButton(obj, columnIndex)) {
				return getCheck(obj, columnIndex);
			}
			return super.isSuffixButtonEnabled(obj, columnIndex);
		}

		@Override
		public String getString(Object obj, int index) {
			ILiteComponentItem item = getComponentItem(obj);
			if (item != null) {
				switch (index) {
				case COLSWCOMP: {
					String label = item.getEffectiveName();
					return label;
				}
				case COLSEL:
					if (getSelectionControlType(obj) == CellControlType.INPLACE_SPIN) {
						return Integer.toString(((ILiteComponent) item).getSelectedCount());
					}
					break;
				case COLVARIANT:
					return item.getActiveVariant();
				case COLVENDOR:
					return item.getActiveVendor();
				case COLVERSION:
					return item.getActiveVersion(); // active variant
				case COLDESCR:
					return item.getDescription();
				default:
					break;
				}
			}
			return CmsisConstants.EMPTY_STRING;
		}

		@Override
		public long getCurrentSelectedIndex(Object element, int columnIndex) {
			int index = -1;
			ILiteComponentItem item = getComponentItem(element);

			if (item != null) {
				switch (columnIndex) {
				case COLSEL:
					if (getSelectionControlType(element) == CellControlType.INPLACE_SPIN) {
						index = ((ILiteComponent) item).getSelectedCount();
					}
					break;
				case COLVARIANT:
					index = DeviceUIUtils.indexOf(item.getVariantStrings(), item.getActiveVariant());
					break;
				case COLVENDOR:
					index = DeviceUIUtils.indexOf(item.getVendorStrings(), item.getActiveVendor());
					break;
				case COLVERSION:
					index = DeviceUIUtils.indexOf(item.getVersionStrings(), item.getActiveVersion());
					break;
				default:
					break;
				}
			}

			return index;
		}

		@Override
		public long getMaxCount(Object obj, int columnIndex) {
			if (columnIndex == COLSEL && obj instanceof ILiteComponent) {
				ILiteComponent c = (ILiteComponent) (obj);
				return c.getMaxInstanceCount();
			}
			return 0;
		}

		@Override
		public String[] getStringArray(Object obj, int columnIndex) {
			String[] strings = null;
			ILiteComponentItem item = getComponentItem(obj);
			if (item != null) {
				switch (columnIndex) {
				case COLSWCOMP:
					break;
				case COLSEL:
					if (getSelectionControlType(obj) == CellControlType.INPLACE_SPIN) {
						strings = getSelectionStrings(obj);
					}
					break;
				case COLVARIANT:
					strings = item.getVariantStrings().toArray(new String[0]);
					break;
				case COLVENDOR:
					strings = item.getVendorStrings().toArray(new String[0]);
					break;
				case COLVERSION:
					strings = item.getVersionStrings().toArray(new String[0]);
					break;
				case COLDESCR:
				default:
					break;
				}
			}
			return strings;
		}

		@Override
		public boolean canEdit(Object obj, int columnIndex) {
			Collection<String> strings = null;
			ILiteComponentItem item = getComponentItem(obj);
			int minValues = 0;
			if (!isEnabled(obj, columnIndex)) {
				return false;
			}
			if (item != null) {
				switch (columnIndex) {
				case COLSWCOMP:
					break;
				case COLSEL:
					CellControlType ct = getSelectionControlType(obj);
					return ct == CellControlType.CHECK || ct == CellControlType.INPLACE_SPIN
							|| ct == CellControlType.INPLACE_CHECK;
				case COLVARIANT:
					strings = item.getVariantStrings();
					minValues = 1;
					break;
				case COLVENDOR:
					strings = item.getVendorStrings();
					minValues = 1;
					break;
				case COLVERSION:
					strings = item.getVersionStrings();
					break;
				case COLDESCR:
				default:
					break;
				}
			}

			if (strings != null) {
				return strings.size() > minValues;
			}
			return false;
		}

		@Override
		public Image getImage(Object obj, int columnIndex) {
			ILiteComponentItem item = getComponentItem(obj);
			if (item != null) {
				if (columnIndex == 0) {
					EEvaluationResult res = getModelController().getEvaluationResult(item);
					Image baseImage = null;
					if (item instanceof LiteSelectedDeviceClass) {
						if (res.isFulfilled()) {
							baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE);
						} else {
							baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_DEPRDEVICE);
						}
					} else if (item instanceof LiteMoreClass) {
						baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_PACKAGES_FILTER);
					} else if (item instanceof ILiteComponentClass) {
						baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_LITE);
					} else if (item instanceof ILiteComponentGroup) {
						baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_GROUP);
					} else if (item instanceof ILiteComponent) {
						ILiteComponent c = (ILiteComponent) item;
						ICpComponentInfo ci = c.getActiveCpComponentInfo();
						if (ci != null && ci.getComponent() == null) {
							if (c.getMaxInstanceCount() > 1) {
								baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_MULTICOMPONENT_ERROR);
							} else {
								baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_ERROR);
							}

						} else {
							if (c.getMaxInstanceCount() > 1) {
								baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_MULTICOMPONENT);
							} else {
								baseImage = CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT);
							}
						}
					}
					return baseImage;
				}
				switch (columnIndex) {
				case COLSEL:
				case COLSWCOMP:
				case COLVARIANT:
					break;
				case COLVENDOR:
					break;
				case COLVERSION:
					if (item.getActiveVersion() != null && !item.getActiveVersion().isEmpty()
							&& !item.isUseLatestVersion()) {
						return CpPlugInUI.getImage(CpPlugInUI.ICON_PIN);
					}
					break;
				case COLDESCR:
				default:
					break;
				}
			}
			return null;
		}

		@Override
		public Image getSuffixButtonImage(Object obj, int columnIndex) {
			if (columnIndex == COLSEL) {
				if (isSuffixButtonEnabled(obj, columnIndex))
					return CpPlugInUI.getImage(CpPlugInUI.ICON_RUN);
				return CpPlugInUI.getImage(CpPlugInUI.ICON_RUN_GREY);
			}
			return null;
		}

		@Override
		public String getTooltipText(Object obj, int columnIndex) {
			ILiteComponentItem item = getComponentItem(obj);
			if (item == null) {
				return null;
			}

			switch (columnIndex) {
			case COLSWCOMP:
				return item.getDescription();
			case COLVERSION:
				String ver = item.getActiveVersion();
				if (ver == null || ver.isEmpty()) {
					return null;
				}
				String tt;
				if (item.isUseLatestVersion()) {
					tt = CpStringsUI.LiteComponentTreeWidget_UseLatestVersion;
				} else {
					tt = CpStringsUI.LiteComponentTreeWidget_StickToFixedVersion;
				}

				tt += ": "; //$NON-NLS-1$

				return tt + ver;
			case COLDESCR:
				String url = item.getUrl();
				if (url != null && !url.isEmpty()) {
					return url;
				}
				break;
			case COLSEL:
			case COLVARIANT:
			case COLVENDOR:
			default:
				break;
			}
			return null;
		}

		@Override
		public String getUrl(Object obj, int columnIndex) {
			if (columnIndex == COLDESCR) {
				ILiteComponentItem item = getComponentItem(obj);
				if (item != null) {
					return item.getUrl();
				}
			}
			return null;
		}

		@Override
		public void setCheck(Object element, int columnIndex, boolean newVal) {
			if (columnIndex != COLSEL) {
				return;
			}
			if (getSelectionControlType(element) == CellControlType.CHECK
					|| getSelectionControlType(element) == CellControlType.INPLACE_CHECK) {
				ILiteComponentItem item = getComponentItem(element);

				/**
				 * the entry of selection and viewer refresh loop, when component item is 
				 * selected, all view will be refresh, and the dependencys will be resoloved
				 * the output will be display in LiteValidateWidget.
				 */
				getModelController().selectComponent((ILiteComponent) item, newVal ? 1 : 0);
			}
		}

		/**
		 * @param obj
		 * @return a string array containing indexes starting from 0 to max
		 *         instance count of the RTE component
		 */
		private String[] getSelectionStrings(Object obj) {
			String[] strings = null;
			if (getSelectionControlType(obj) == CellControlType.INPLACE_SPIN) {
				int count = ((ILiteComponent) obj).getMaxInstanceCount();
				strings = new String[count + 1];
				for (int i = 0; i <= count; ++i) {
					strings[i] = Integer.toString(i);
				}
			}
			return strings;
		}

		@Override
		public void setString(Object obj, int columnIndex, String newVal) {
			ILiteComponentItem item = getComponentItem(obj);

			if (item == null || getModelController() == null || newVal == null) {
				return;
			}
			switch (columnIndex) {
			case COLVARIANT:
				getModelController().selectActiveVariant(item, newVal);
				break;
			case COLVENDOR:
				getModelController().selectActiveVendor(item, newVal);
				break;
			case COLVERSION:
				getModelController().selectActiveVersion(item, newVal);
				break;
			default:
				return;
			}
			viewer.update(item, null);
		}

		@Override
		public void setCurrentSelectedIndex(Object obj, int columnIndex, long newVal) {
			ILiteComponentItem item = getComponentItem(obj);
			if (item == null || getModelController() == null) {
				return;
			}
			if (columnIndex == COLSEL && getSelectionControlType(item) == CellControlType.INPLACE_SPIN) {
				getModelController().selectComponent((ILiteComponent) item, (int) newVal);
			}

		}

		@Override
		public Color getBgColor(Object obj, int columnIndex) {
			if (columnIndex != COLSEL) {
				return null;
			}
			ILiteComponentItem item = getComponentItem(obj);
			if (item != null) {
				Device device = Display.getCurrent();
				EEvaluationResult res = getModelController().getEvaluationResult(item);
				switch (res) {
				case UNDEFINED:
					break;
				case CONFLICT:
				case ERROR:
				case FAILED:
				case INCOMPATIBLE:
				case INCOMPATIBLE_API:
				case INCOMPATIBLE_BUNDLE:
				case INCOMPATIBLE_VARIANT:
				case INCOMPATIBLE_VENDOR:
				case INCOMPATIBLE_VERSION:
				case MISSING:
				case MISSING_GPDSC:
				case MISSING_API:
				case MISSING_BUNDLE:
				case MISSING_VARIANT:
				case MISSING_VENDOR:
				case MISSING_VERSION:
				case UNAVAILABLE:
				case UNAVAILABLE_PACK:
					return device.getSystemColor(SWT.COLOR_RED);
				case IGNORED:
					if (!item.isSelected()) {
						break;
					}
				case FULFILLED:
					return GREEN;

				case INACTIVE:
				case INSTALLED:
				case SELECTABLE:
					return YELLOW;
				default:
					break;
				}
			}
			return null;
		}

		@Override
		public String getDefaultString(Object obj, int columnIndex) {
			if (columnIndex != COLVERSION) {
				return null;
			}
			ILiteComponentItem item = getComponentItem(obj);
			if (item == null) {
				return null;
			}
			return item.getDefaultVersion();
		}

		@Override
		public boolean isDefault(Object obj, int columnIndex) {
			if (columnIndex != COLVERSION) {
				return false;
			}
			ILiteComponentItem item = getComponentItem(obj);
			if (item == null) {
				return false;
			}
			return item.isUseLatestVersion();
		}

		@Override
		protected void executeSuffixButtonAction(Object element, int colIndex, Point pt) {
			ILiteComponentItem item = getComponentItem(element);
			if (item == null) {
				return;
			}
			launchGenerator(getGenerator(item), null);
		}

	} /// end of LiteColumnAdvisor

	/**
	 * Content provider for RTEComponentTreeWidget
	 */
	public class LiteComponentContentProvider extends TreeObjectContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == getModelController()) {
				return getModelController().getComponents().getChildArray();
			}
			return getChildren(inputElement);
		}

		@Override
		public Object getParent(Object child) {
			ILiteComponentItem item = getComponentItem(child);
			if (item != null) {
				return item.getEffectiveParent();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			ILiteComponentItem item = getComponentItem(parent);
			if (item != null) {
				return item.getEffectiveChildArray();
			}
			return ITreeObject.EMPTY_OBJECT_ARRAY;
		}

		@Override
		public boolean hasChildren(Object parent) {
			ILiteComponentItem item = getComponentItem(parent);
			if (item != null) {
				return item.hasEffectiveChildren();
			}
			return false;
		}
	}

	/**
	 * Set current configuration for this component tree widget
	 * 
	 * @param configuration
	 *            A RTE configuration that contains RTE component
	 */
	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		if (viewer != null) {
			viewer.setInput(model);
		}
	}

	@Override
	public Composite createControl(Composite parent) {

		Tree tree = new Tree(parent, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		viewer = new TreeViewer(tree);
		ColumnViewerToolTipSupport.enableFor(viewer);
		fColumnAdvisor = new LiteComponentColumnAdvisor(viewer);

		// Tree item name
		TreeViewerColumn column0 = new TreeViewerColumn(viewer, SWT.LEFT);
		column0.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_SoftwareComponents);
		column0.getColumn().setWidth(180);
		column0.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 0));
		AdvisedCellLabelProvider col0LabelProvider = new AdvisedCellLabelProvider(fColumnAdvisor, 0);
		// workaround jface bug: first owner-draw column is not correctly
		// painted when column is resized
		col0LabelProvider.setOwnerDrawEnabled(false);
		column0.setLabelProvider(col0LabelProvider);

		// Check box for selection
		TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.LEFT);
		column1.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Sel);
		column1.getColumn().setWidth(35);
		column1.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 1));
		column1.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, 1));

		// Variant
		TreeViewerColumn column2 = new TreeViewerColumn(viewer, SWT.LEFT);
		column2.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Variant);
		column2.getColumn().setWidth(110);
		column2.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 2));
		column2.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, 2));

		// Vendor
		TreeViewerColumn column3 = new TreeViewerColumn(viewer, SWT.LEFT);
		column3.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Vendor);
		column3.getColumn().setWidth(110);
		column3.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 3));
		column3.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, 3));

		// Version
		TreeViewerColumn column4 = new TreeViewerColumn(viewer, SWT.LEFT);
		column4.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Version);
		column4.getColumn().setWidth(70);
		column4.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 4));
		column4.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, 4));

		// Description/URL
		TreeViewerColumn column5 = new TreeViewerColumn(viewer, SWT.LEFT);
		column5.getColumn().setText(CpStringsUI.LiteComponentTreeWidget_Description);
		column5.getColumn().setWidth(400);
		column5.setEditingSupport(new AdvisedEditingSupport(viewer, fColumnAdvisor, 5));
		column5.setLabelProvider(new AdvisedCellLabelProvider(fColumnAdvisor, 5));

		LiteComponentContentProvider rteContentProvider = new LiteComponentContentProvider();
		viewer.setContentProvider(rteContentProvider);
		viewer.addSelectionChangedListener(event -> handleTreeSelectionChanged(event));

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;
		tree.setLayoutData(gridData);

		if (getModelController() != null) {
			viewer.setInput(getModelController());
		}
		hookContextMenu();
		return tree;
	}

	protected ILiteComponentItem getSelectedItem() {
		if (viewer != null) {
			IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
			if (sel != null) {
				return getComponentItem(sel.getFirstElement());
			}
		}
		return null;
	}

	ICpGenerator getSelectedGenerator() {
		return getGenerator(getSelectedItem());
	}

	ICpGenerator getGenerator(ILiteComponentItem item) {
		if (item == null) {
			return null;
		}
		ICpComponent cp = item.getActiveCpComponent();
		if (cp == null) {
			return null;
		}
		return cp.getGenerator();
	}

	/**
	 * @param event
	 */
	protected void handleTreeSelectionChanged(SelectionChangedEvent event) {
		if (getModelController() == null) {
			return;
		}
		ILiteComponentItem c = getSelectedItem();
		if (c != null) {
			selItemKeyPath = c.getKeyPath();
		}
	}

	@Override
	public void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
	}

	/**
	 * Refresh completely the tree viewer.
	 */
	@Override
	public void update() {
		refresh();

		if (viewer == null || getModelController() == null) {
			return;
		}
		if (selItemKeyPath == null || selItemKeyPath.isEmpty()) {
			return;
		}

		ILiteComponentItem item = getModelController().getComponents().findChild(selItemKeyPath, false);
		if (item != null) {
			showComponentItem(item);
		}
	}

	@Override
	public void handle(LiteEvent event) {
		if (event.getTopic().equals(LiteEvent.COMPONENT_SHOW)) {
			showComponentItem((ILiteComponentItem) event.getData());
			return;
		} else if (event.getTopic().equals(LiteEvent.COMPONENT_SELECTION_MODIFIED)) {
			refresh();
		} else {
			super.handle(event);
		}
	}

	/**
	 * Highlights given item expanding parent nodes if needed
	 * 
	 * @param item
	 *            Component item to select
	 */
	public void showComponentItem(ILiteComponentItem item) {
		if (viewer == null) {
			return;
		}
		if (item == null) {
			return;
		}

		ILiteComponent c = item.getParentComponent();
		if (c != null) {
			// if supplied item has parent component, highlight it
			item = c;
		}
		ILiteComponentBundle b = item.getParentBundle();
		if (b != null && !b.isActive()) {
			// if bundle is not active, highlight bundle's parent (component
			// class)
			item = b.getParent();
		}

		if (item == null) {
			return;
		}

		if (item == getSelectedItem()) {
			return;
		}

		Object[] path = item.getEffectiveHierachyPath();
		if (path.length == 0) {
			return;
		}
		TreePath tp = new TreePath(path);
		TreeSelection ts = new TreeSelection(tp);

		viewer.setSelection(ts, true);

	}

	/**
	 * Return the tree viewer embedded in this widget
	 * 
	 * @return
	 */
	public TreeViewer getViewer() {
		return viewer;
	}

	private void hookContextMenu() {

		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		makeActions();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(manager -> fillContextMenu(menuMgr));
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
	}

	void addGeneratorActions(IMenuManager manager) {
		ICpGenerator gen = getSelectedGenerator();
		if (gen == null){
			return;
		}
		Collection<String> types = gen.getAvailableTypes();
		if (types == null || types.isEmpty()){
			return;
		}
		String genName = gen.getId();
		int n = 0;
		for (String type : types) {
			Action a = laGeneratorActions.get(type);
			if (a == null){
				continue;
			}
			String text = CpStringsUI.Launch + ' ' + genName + ' ' + '(' + type + ')';
			a.setText(text);
			manager.add(a);
			n++;
		}

		if (n > 0){
			manager.add(new Separator());
		}
	}

	void fillContextMenu(IMenuManager manager) {
		addGeneratorActions(manager);
		manager.add(expandAll);
		manager.add(collapseAll);
		manager.add(expandAllSelected);
	}

	protected void makeActions() {
		expandAll = new Action() {
			@Override
			public void run() {
				if (viewer == null) {
					return;
				}
				viewer.expandAll();
			}
		};

		expandAll.setText(CpStringsUI.ExpandAll);
		expandAll.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_EXPAND_ALL));

		collapseAll = new Action() {
			@Override
			public void run() {
				if (viewer == null) {
					return;
				}
				viewer.collapseAll();
			}
		};
		collapseAll.setText(CpStringsUI.CollapseAll);
		collapseAll.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_COLLAPSE_ALL));

		expandAllSelected = new Action() {
			@Override
			public void run() {
				if (viewer == null) {
					return;
				}
				ILiteModel model = getModelController();
				if (model != null) {
					viewer.getTree().setRedraw(false);
					ISelection prevSel = viewer.getSelection();
					Collection<ILiteComponent> selectedComponents = model.getSelectedComponents();
					for (ILiteComponent comp : selectedComponents) {
						Object[] path = comp.getEffectiveHierachyPath();
						TreePath tp = new TreePath(path);
						TreeSelection ts = new TreeSelection(tp);
						viewer.setSelection(ts, false);
					}
					viewer.setSelection(prevSel, true);
					viewer.getTree().setRedraw(true);
				}
			}
		};
		expandAllSelected.setText(CpStringsUI.LiteManagerWidget_ExpandAllSelected);

		OverlayImage overlayImage = new OverlayImage(
				CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_EXPAND_ALL).createImage(),
				CpPlugInUI.getImageDescriptor(CpPlugInUI.CHECKEDOUT_OVR).createImage(), OverlayPos.TOP_RIGHT);
		expandAllSelected.setImageDescriptor(overlayImage);

		for (String type : CmsisConstants.LAUNCH_TYPES) {
			LaunchGeneratorAction lga = new LaunchGeneratorAction(type);
			laGeneratorActions.put(type, lga);
		}
	}

	@Override
	public Composite getFocusWidget() {
		return viewer.getTree();
	}

}
