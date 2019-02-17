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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpItem;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.EDeviceHierarchyLevel;
import com.lembed.lite.studio.device.core.info.CpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.IStatusMessageListener;
import com.lembed.lite.studio.device.ui.StatusMessageListerenList;
import com.lembed.lite.studio.device.ui.tree.TreeObjectContentProvider;
import com.lembed.lite.studio.device.utils.WildCards;

/**
 * Widget to select a CMSIS device
 */
public class LiteDeviceSelectorWidget extends Composite {
	private Text text;
	Text txtSearch;
	private Label lblVendor;
	private Label lblDevice;
	private Label lblCpu;
	Combo comboFpu;
	Combo comboEndian;

	ILiteDeviceItem fDevices = null;
	ILiteDeviceItem fSelectedItem = null;
	private ICpDeviceItem fSelectedDevice = null;
	private ICpDeviceInfo fDeviceInfo = null;

	private TreeViewer treeViewer;

	// list of listeners (e.g parent widgets to monitor events)
	StatusMessageListerenList listeners = new StatusMessageListerenList();
	String fSearchString = CmsisConstants.EMPTY_STRING;
	String selectedFpu;
	String selectedEndian;

	boolean updatingControls = false;
	private Label lblMemory;
	private Link linkUrl;
	String url = CmsisConstants.EMPTY_STRING;
	private Label lblPack;
	private Label lblClock;

	public class LiteDeviceLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof ILiteDeviceItem) {
				ILiteDeviceItem item = (ILiteDeviceItem) element;
				if (item.getLevel() == EDeviceHierarchyLevel.VENDOR.ordinal()) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT);
				} else if (item.hasChildren()) {
					return CpPlugInUI.getImage(CpPlugInUI.ICON_COMPONENT_CLASS);
				}
				return CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE);
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof ILiteDeviceItem) {
				ILiteDeviceItem item = (ILiteDeviceItem) element;
				return item.getName();
			}
			return CmsisConstants.EMPTY_STRING;
		}
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public LiteDeviceSelectorWidget(Composite parent) {
		super(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.horizontalSpacing = 8;
		setLayout(gridLayout);

		Label lblDeviceLabel = new Label(this, SWT.NONE);
		lblDeviceLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_DeviceLabel);

		lblDevice = new Label(this, SWT.NONE);
		lblDevice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblCpuLabel = new Label(this, SWT.NONE);
		lblCpuLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_CPULabel);
		lblCpuLabel.setBounds(0, 0, 36, 13);

		lblCpu = new Label(this, SWT.NONE);
		lblCpu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblCpu.setBounds(0, 0, 32, 13);
		new Label(this, SWT.NONE);

		Label lblVendorLabel = new Label(this, SWT.NONE);
		lblVendorLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_VendorLabel);

		lblVendor = new Label(this, SWT.NONE);
		lblVendor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblClocklabel = new Label(this, SWT.NONE);
		lblClocklabel.setText(CpStringsUI.LiteDeviceSelectorWidget_lblClocklabel_text);

		lblClock = new Label(this, SWT.NONE);
		lblClock.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblPackLabel = new Label(this, SWT.NONE);
		lblPackLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_lblPack_text);

		lblPack = new Label(this, SWT.NONE);
		lblPack.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblMemoryLabel = new Label(this, SWT.NONE);
		lblMemoryLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_lblMemory);

		lblMemory = new Label(this, SWT.NONE);
		lblMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblUrlLabel = new Label(this, SWT.NONE);
		lblUrlLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_lblUrl);
		lblUrlLabel.setText(CmsisConstants.EMPTY_STRING);

		linkUrl = new Link(this, SWT.NONE);
		linkUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		linkUrl.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				OpenURL.open(url, getShell());
//			}
//		});
		new Label(this, SWT.NONE);

		Label lblFpuLabel = new Label(this, SWT.NONE);
		lblFpuLabel.setText(CpStringsUI.LiteDeviceSelectorWidget_FPULabel);
		lblFpuLabel.setBounds(0, 0, 38, 13);
		comboFpu = new Combo(this, SWT.READ_ONLY);
		comboFpu.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (updatingControls) {
					return;
				}
				int index = comboFpu.getSelectionIndex();
				selectedFpu = fpuIndexToString(index);
			}
		});
		comboFpu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);

		Label lblSearch = new Label(this, SWT.NONE);
		lblSearch.setText(CpStringsUI.LiteDeviceSelectorWidget_SearchLabel);

		txtSearch = new Text(this, SWT.BORDER);
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setSearchText(txtSearch.getText());
			}
		});
		txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtSearch.setToolTipText(CpStringsUI.LiteDeviceSelectorWidget_SearchTooltip);
		new Label(this, SWT.NONE);

		Label lblEndian = new Label(this, SWT.NONE);
		lblEndian.setText(CpStringsUI.LiteDeviceSelectorWidget_Endian);
		lblEndian.setBounds(0, 0, 37, 13);

		comboEndian = new Combo(this, SWT.READ_ONLY);
		comboEndian.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (updatingControls) {
					return;
				}
				selectedEndian = adjustEndianString(comboEndian.getText());
			}
		});
		comboEndian.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);

		treeViewer = new TreeViewer(this, SWT.BORDER);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleTreeSelectionChanged(event);
			}
		});
		Tree tree = treeViewer.getTree();
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_tree.minimumWidth = 240;
		tree.setLayoutData(gd_tree);

		treeViewer.setContentProvider(new TreeObjectContentProvider());
		treeViewer.setLabelProvider(new LiteDeviceLabelProvider());
		treeViewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (fSearchString.isEmpty() || fSearchString.equals("*")) { //$NON-NLS-1$
					return true;
				}
				if (element instanceof ILiteDeviceItem) {
					String s = CmsisConstants.EMPTY_STRING;
					if (!fSearchString.startsWith("*")) { //$NON-NLS-1$
						s = "*"; //$NON-NLS-1$
					}
					s += fSearchString;
					if (!s.endsWith("*")) { //$NON-NLS-1$
						s += "*"; //$NON-NLS-1$
					}
					// first check if parent elements (vendor, family, etc.
					// match the pattern)
					ILiteDeviceItem deviceItem = (ILiteDeviceItem) element;
					for (ILiteDeviceItem parent = deviceItem.getParent(); parent != null; parent = parent.getParent()) {
						if (WildCards.matchNoCase(s, parent.getName())) {
							return true;
						}
					}
					return deviceItem.getFirstItem(s) != null;
				}
				return false;
			}

		});

		text = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_text.widthHint = 240;
		gd_text.minimumWidth = 200;
		text.setLayoutData(gd_text);
		text.setEditable(false);

		setTabList(new Control[] { txtSearch, tree, comboFpu, comboEndian });

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				fSelectedItem = null;
				fDevices = null;
				listeners.removeAllListeners();
				listeners = null;
			}
		});

	}

	boolean setSearchText(String s) {
		// Search must be a substring of the existing value
		if (fSearchString != s) {
			fSearchString = s;
			ILiteDeviceItem deviceItem = null;
			if (!fSearchString.isEmpty() && !fSearchString.equals("*")) { //$NON-NLS-1$
				String pattern = fSearchString;
				if (!pattern.endsWith("*")) //$NON-NLS-1$
				{
					pattern += "*"; //$NON-NLS-1$
				}
				if (fSelectedItem != null && fSelectedItem.getFirstItem(pattern) == fSelectedItem) {
					deviceItem = fSelectedItem;
				} else {
					deviceItem = fDevices.getFirstItem(pattern);
				}
			}
			refreshTree();
			if (deviceItem != null) {
				selectItem(deviceItem);
				treeViewer.setExpandedState(deviceItem, true);
			}
		}
		return false;
	}

	private void refreshTree() {
		treeViewer.refresh();
	}

	/**
	 * Sets collection of the available devices
	 * 
	 * @param devices
	 *            ILiteDeviceItem root of device tree
	 */
	public void setDevices(ILiteDeviceItem devices) {
		this.fDevices = devices;
		treeViewer.setInput(devices);
	}

	/**
	 * Returns selected device item
	 * 
	 * @return selected ILiteDeviceItem
	 */
	public ILiteDeviceItem getSelectedDeviceItem() {
		if (fSelectedDevice != null) {
			return fSelectedItem;
		}		
		
		return null;
	}

	protected void handleTreeSelectionChanged(SelectionChangedEvent event) {
		ILiteDeviceItem selectedItem = getSelectedItem();
		if (selectedItem == fSelectedItem) {
			return;
		}

		fSelectedItem = selectedItem;
		updateDeviceInfo();
		updateControls();
	}

	protected void updateControls() {
		updatingControls = true;
		String description = CmsisConstants.EMPTY_STRING;
		String vendorName = CmsisConstants.EMPTY_STRING;
		String deviceName = CmsisConstants.EMPTY_STRING;
		String cpu = CmsisConstants.EMPTY_STRING;
		String clock = CmsisConstants.EMPTY_STRING;
		String pack = CmsisConstants.EMPTY_STRING;
		String mem = CmsisConstants.EMPTY_STRING;
		url = CmsisConstants.EMPTY_STRING;
		String urlText = CmsisConstants.EMPTY_STRING;

		String message = null;

		if (fSelectedItem != null) {
			ILiteDeviceItem vendorItem = fSelectedItem.getVendorItem();
			if (vendorItem != null) {
				vendorName = vendorItem.getName();
			}
			if (fSelectedItem.isDevice()) {
				deviceName = fSelectedItem.getName();
				url = fSelectedItem.getUrl();
				if (!url.isEmpty()) {
//					urlText = "<a href=\"" + url + "\">" + url + "</a>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			description = fSelectedItem.getDescription();
		} else {
			message = "Device is not installed. Please install the pack first, or select a new device"; //$NON-NLS-1$
		}

		if (fDeviceInfo != null) {
			clock = fDeviceInfo.getClockSummary();
			pack = fDeviceInfo.getPackId();
			cpu = "ARM " + fDeviceInfo.getAttribute(CmsisConstants.DCORE); //$NON-NLS-1$
			mem = fDeviceInfo.getMemorySummary();
			// this happens in the import process, when the device is not
			// installed
			if (deviceName.isEmpty()) {
				deviceName = fDeviceInfo.getDeviceName();
			}
			if (vendorName.isEmpty()) {
				vendorName = fDeviceInfo.getVendor();
			}
			if (url.isEmpty()) {
				url = fDeviceInfo.getUrl();
				if (!url.isEmpty()) {
//					urlText = "<a href=\"" + url + "\">" + url + "</a>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
		} else {
			message = CpStringsUI.LiteDeviceSelectorWidget_NoDeviceSelected;
		}

		lblVendor.setText(vendorName);
		lblDevice.setText(deviceName);
		text.setText(description);
		lblCpu.setText(cpu);
		lblClock.setText(clock);
		lblPack.setText(pack);
		linkUrl.setText(urlText);
		linkUrl.setToolTipText(url);
		lblMemory.setText(mem);

		updateFpu();
		updateEndian();

		updateStatus(message);
		updatingControls = false;
	}

	private void updateDeviceInfo() {
		if (fSelectedItem != null && fSelectedItem.isDevice()) {
			fSelectedDevice = fSelectedItem.getDevice();
			fDeviceInfo = null;
			ICpItem props = fSelectedItem.getEffectiveProperties();
			if (props != null) {
				fDeviceInfo = new CpDeviceInfo(null, fSelectedItem);
			}
		} else {
			fSelectedDevice = null;
			fDeviceInfo = null;
		}
	}

	/**
	 * Returns ILiteDeviceItem currently selected in the tree
	 */
	private ILiteDeviceItem getSelectedItem() {
		IStructuredSelection sel = (IStructuredSelection) treeViewer.getSelection();
		if (sel.size() == 1) {
			Object o = sel.getFirstElement();
			if (o instanceof ILiteDeviceItem) {
				return (ILiteDeviceItem) o;
			}
		}
		return null;
	}

	private void updateEndian() {
		String endian = null;
		if (fDeviceInfo != null) {
			endian = fDeviceInfo.getAttribute(CmsisConstants.DENDIAN);
		}

		String[] endianStrings = getEndianStrings(endian);
		comboEndian.setItems(endianStrings);

		endian = adjustEndianString(selectedEndian);
		int index = comboEndian.indexOf(endian);
		if (index < 0 || index >= endianStrings.length) {
			index = 0;
		}

		comboEndian.select(index);
		comboEndian.setEnabled(endianStrings.length > 1);
	}

	private String[] getEndianStrings(String endian) {
		if (endian != null) {
			switch (endian) {
			case CmsisConstants.BIGENDIAN:
				return new String[] { CmsisConstants.BIGENDIAN };
			case CmsisConstants.CONFIGENDIAN:
			case "*": //$NON-NLS-1$
				return new String[] { CmsisConstants.LITTLENDIAN, CmsisConstants.BIGENDIAN };
			case CmsisConstants.LITTLENDIAN:
			default:
				return new String[] { CmsisConstants.LITTLENDIAN };
			}
		}
		return new String[] { CmsisConstants.EMPTY_STRING };
	}

	String adjustEndianString(String endian) {
		if (endian != null) {
			if (endian.equals(CmsisConstants.BIGENDIAN) || endian.equals(CmsisConstants.LITTLENDIAN)) {
				return endian;
			}
		}
		return CmsisConstants.LITTLENDIAN;
	}

	private void updateFpu() {
		String fpu = null;
		if (fDeviceInfo != null) {
			fpu = fDeviceInfo.getAttribute(CmsisConstants.DFPU);
		}

		String[] fpuStrings = getFpuStrings(fpu);
		int index = fpuStringToIndex(selectedFpu);
		if (index < 0 || index >= fpuStrings.length) {
			index = fpuStringToIndex(fpu);
		}
		if (index < 0) {
			index = 0;
		}

		comboFpu.setItems(fpuStrings);
		comboFpu.select(index);
		comboFpu.setEnabled(fpuStrings.length > 1);
	}

	private String[] getFpuStrings(String fpu) {
		if (fpu != null) {
			switch (fpu) {
			case "1": //$NON-NLS-1$
			case CmsisConstants.FPU:
			case CmsisConstants.SP_FPU:
				return new String[] { CpStringsUI.LiteDeviceSelectorWidget_none,
						CpStringsUI.LiteDeviceSelectorWidget_SinglePrecision };
			case CmsisConstants.DP_FPU:
				return new String[] { CpStringsUI.LiteDeviceSelectorWidget_none,
						CpStringsUI.LiteDeviceSelectorWidget_SinglePrecision,
						CpStringsUI.LiteDeviceSelectorWidget_DoublePrecision };
			default:
				return new String[] { CpStringsUI.LiteDeviceSelectorWidget_none };
			}
		}
		return new String[] { CmsisConstants.EMPTY_STRING };
	}

	String fpuIndexToString(int index) {
		switch (index) {
		case 1:
			return CmsisConstants.SP_FPU;
		case 2:
			return CmsisConstants.DP_FPU;
		default:
			break;
		}
		return CmsisConstants.NO_FPU;
	}

	private int fpuStringToIndex(String fpu) {
		if (fpu != null) {
			switch (fpu) {
			case "1": //$NON-NLS-1$
			case CmsisConstants.FPU:
			case CmsisConstants.SP_FPU:
				return 1;
			case CmsisConstants.DP_FPU:
				return 2;
			default:
				return 0;
			}
		}
		return -1;
	}

	private void updateStatus(String message) {
		// notify listeners
		listeners.notifyListeners(message);
	}

	public void addListener(IStatusMessageListener listener) {
		listeners.addListener(listener);
	}

	public void removeListener(IStatusMessageListener listener) {
		listeners.removeListener(listener);
	}

	/**
	 * Returns device info for selected device
	 * 
	 * @return ICpDeviceInfo
	 */
	public ICpDeviceInfo getDeviceInfo() {
		if (fDeviceInfo != null) {
			int index = comboFpu.getSelectionIndex();
			String fpu = fpuIndexToString(index);
			fDeviceInfo.attributes().setAttribute(CmsisConstants.DFPU, fpu);

			String endian = comboEndian.getText();
			if (endian == null || endian.isEmpty()) {
				endian = CmsisConstants.LITTLENDIAN;
			}

			fDeviceInfo.attributes().setAttribute(CmsisConstants.DENDIAN, endian);
		}
		return fDeviceInfo;
	}

	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		fDeviceInfo = deviceInfo;
		if (fDeviceInfo != null) {
			selectedFpu = fDeviceInfo.getAttribute(CmsisConstants.DFPU);
			selectedEndian = fDeviceInfo.getAttribute(CmsisConstants.DENDIAN);
			// set initial selection
			ILiteDeviceItem item = fDevices.findItem(fDeviceInfo.attributes());
			if (item != null) {
				selectItem(item);
			} else {
				String message = NLS.bind(CpStringsUI.LiteDeviceSelectorWidget_DeviceNotFound,
						CpItem.getDeviceName(deviceInfo.attributes()));
				updateStatus(message);
			}
			updateControls();
		} else {
			updateStatus(CpStringsUI.LiteDeviceSelectorWidget_NoDeviceSelected);
		}
	}

	/**
	 * Selects given device item in the tree
	 * 
	 * @param item
	 *            device item to select
	 */
	public void selectItem(ILiteDeviceItem item) {
		if (item != null) {
			Object[] path = item.getHierachyPath();
			TreePath tp = new TreePath(path);
			TreeSelection ts = new TreeSelection(tp);
			treeViewer.setSelection(ts, true);
		}
	}

}
