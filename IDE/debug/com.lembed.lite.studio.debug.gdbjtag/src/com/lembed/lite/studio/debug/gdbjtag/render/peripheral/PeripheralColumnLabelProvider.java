/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial version
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.render.peripheral;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IMemoryBlockExtension;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdEnumeratedValueDMNode;
import com.lembed.lite.studio.debug.gdbjtag.preferences.PersistentPreferences;
import com.lembed.lite.studio.debug.gdbjtag.render.peripheral.PeripheralColumnInfo.ColumnType;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterFieldVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralTopVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralTreeVMNode;

/**
 * The Class PeripheralColumnLabelProvider.
 */
public class PeripheralColumnLabelProvider extends ColumnLabelProvider implements IPropertyChangeListener {

	// ------------------------------------------------------------------------

	private static final String COLOR_PREFIX = "com.lembed.lite.studio.debug.gdbjtag.peripherals.color."; //$NON-NLS-1$

	/** The Constant COLOR_READONLY. */
	public static final String COLOR_READONLY = COLOR_PREFIX + "readonly"; //$NON-NLS-1$
	
	/** The Constant COLOR_WRITEONLY. */
	public static final String COLOR_WRITEONLY = COLOR_PREFIX + "writeonly"; //$NON-NLS-1$
	
	/** The Constant COLOR_CHANGED. */
	public static final String COLOR_CHANGED = COLOR_PREFIX + "changed"; //$NON-NLS-1$
	
	/** The Constant COLOR_CHANGED_MEDIUM. */
	public static final String COLOR_CHANGED_MEDIUM = COLOR_PREFIX + "changed.medium"; //$NON-NLS-1$
	
	/** The Constant COLOR_CHANGED_LIGHT. */
	public static final String COLOR_CHANGED_LIGHT = COLOR_PREFIX + "changed.light"; //$NON-NLS-1$

	private Color fColorReadOnlyBackground;
	private Color fColorWriteOnlyBackground;
	private Color fColorChangedBackground;
	private Color fColorChangedMediumBackground;
	private Color fColorChangedLightBackground;

	private ColumnType fColumnType;
	private TreeViewer fViewer;

	private boolean fUseFadingBackground;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral column label provider.
	 *
	 * @param viewer the viewer
	 * @param fMemoryBlock the f memory block
	 * @param type the type
	 */
	public PeripheralColumnLabelProvider(TreeViewer viewer, IMemoryBlockExtension fMemoryBlock, ColumnType type) {

		// GdbActivator.log("PeripheralColumnLabelProvider() " + type);

		fViewer = viewer;
		fColumnType = type;

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		themeManager.addPropertyChangeListener(this);

		setupColors();

		fUseFadingBackground = PersistentPreferences.getPeripheralsChangedUseFadingBackground();
	}

	@Override
	public void dispose() {

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		themeManager.removePropertyChangeListener(this);
	}

	// ------------------------------------------------------------------------

	@Override
	public void propertyChange(PropertyChangeEvent event) {

		String str = event.getProperty();
		if (str.startsWith(COLOR_PREFIX)) {
			setupColors();
			fViewer.refresh();
		}
	}

	// ------------------------------------------------------------------------

	private void setupColors() {

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		ITheme theme = themeManager.getCurrentTheme();
		ColorRegistry colorRegistry = theme.getColorRegistry();

		fColorReadOnlyBackground = colorRegistry.get(COLOR_READONLY);
		fColorWriteOnlyBackground = colorRegistry.get(COLOR_WRITEONLY);
		fColorChangedBackground = colorRegistry.get(COLOR_CHANGED);
		fColorChangedMediumBackground = colorRegistry.get(COLOR_CHANGED_MEDIUM);
		fColorChangedLightBackground = colorRegistry.get(COLOR_CHANGED_LIGHT);
	}

	@Override
	public Color getBackground(Object element) {

		Color color = null;

		boolean isReadOnly = false;
		boolean isWriteOnly = false;
		boolean isReadAllowed = true;
		boolean hasChanged = false;

		int fadingLevel = 0;

		if ((element instanceof PeripheralTreeVMNode)) {

			PeripheralTreeVMNode node = (PeripheralTreeVMNode) element;
			if (!node.isPeripheral()) {

				isReadOnly = node.isReadOnly();
				isWriteOnly = node.isWriteOnly();

				isReadAllowed = node.isReadAllowed();
			}

			if (node.isRegister() || node.isField()) {
				try {
					hasChanged = node.hasValueChanged();
				} catch (DebugException e) {
					e.printStackTrace();
				}
			}

			fadingLevel = node.getFadingLevel();
		}

		// Set the colours in order of importance.
		if (isWriteOnly || !isReadAllowed) {
			color = fColorWriteOnlyBackground;
		} else if (isReadOnly) {
			color = fColorReadOnlyBackground;
		}

		// Initially was && (fColumnType == ColumnType.VALUE),
		// but was aligned with the Registers view behaviour.
		if (fUseFadingBackground) {
			if (fadingLevel == 3) {
				color = fColorChangedBackground;
			} else if (fadingLevel == 2) {
				color = fColorChangedMediumBackground;
			} else if (fadingLevel == 1) {
				color = fColorChangedLightBackground;
			}
		} else {
			if (hasChanged) {
				color = fColorChangedBackground;
			}
		}
		return color;
	}

	@Override
	public Font getFont(Object element) {

		// No special fonts in use
		return null;
	}

	@Override
	public Color getForeground(Object element) {

		return null;
	}

	@Override
	public Image getImage(Object element) {

		if (fColumnType == ColumnType.REGISTER) {
			if ((element instanceof PeripheralTreeVMNode)) {

				// Get the image from the named file.
				String name = ((PeripheralTreeVMNode) element).getImageName();
				return GdbActivator.getInstance().getImage(name);
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {

		if (element instanceof PeripheralTreeVMNode) {

			PeripheralTreeVMNode treeNode = (PeripheralTreeVMNode) element;

			switch (fColumnType) {

			case REGISTER:
				try {
					// Add a space to separate from the image
					String str = " " + treeNode.getName(); //$NON-NLS-1$
					return str;

				} catch (DebugException e) {
				}

                //$FALL-THROUGH$
            case ADDRESS:
				return treeNode.getDisplayAddress();

			case VALUE:
				return treeNode.getDisplayValue();
            case _DESCRIPTION:
            case _IMAGE:
            case _OFFSET:
            case _SIZE:
            case _TYPE:

			default:
				break;
			}
		}
		return null;
	}

	@Override
	public String getToolTipText(Object element) {

		StringBuilder sb = new StringBuilder();
		if (element instanceof PeripheralTreeVMNode) {

			PeripheralTreeVMNode treeNode = (PeripheralTreeVMNode) element;

			sb.append(treeNode.getDisplayNodeType());

			switch (fColumnType) {

			case REGISTER:
				try {
					appendText(sb, "", treeNode.getName()); //$NON-NLS-1$
				} catch (DebugException e) {
				}
				String description = "\"" + treeNode.getDescription() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
				appendText(sb, "", description); //$NON-NLS-1$
				if (treeNode instanceof PeripheralTopVMNode) {
					appendText(sb, "Version=", ((PeripheralTopVMNode) treeNode).getDisplayVersion()); //$NON-NLS-1$
					appendText(sb, "Group=", ((PeripheralTopVMNode) treeNode).getDisplayGroupName()); //$NON-NLS-1$
				}
				break;

			case ADDRESS:
				if (treeNode.isField()) {
					appendText(sb, "Range=", treeNode.getDisplayOffset()); //$NON-NLS-1$
				} else {
					appendText(sb, "Offset=", treeNode.getDisplayOffset()); //$NON-NLS-1$
				}
				appendText(sb, "Size=", treeNode.getDisplaySize()); //$NON-NLS-1$
				break;

			case VALUE:
				if (treeNode.isPeripheral() || treeNode.isCluster()) {
					return null; // No value tooltip for groups
				}
				if (treeNode instanceof PeripheralRegisterVMNode) {
					appendText(sb, "Reset=", ((PeripheralRegisterVMNode) treeNode).getDisplayResetValue()); //$NON-NLS-1$
				}
				break;
            case _DESCRIPTION:
            case _IMAGE:
            case _OFFSET:
            case _SIZE:
            case _TYPE:

			default:
				break;
			}
			appendText(sb, "", getDisplayAccess(treeNode)); //$NON-NLS-1$
			appendText(sb, "", getDisplayReadAction(treeNode)); //$NON-NLS-1$

			switch (fColumnType) {

			case VALUE:
				if (treeNode instanceof PeripheralRegisterFieldVMNode
				        && (((PeripheralRegisterFieldVMNode) treeNode).isEnumeration())) {

					// For enumerations, add the enumeration description.
					SvdEnumeratedValueDMNode node = ((PeripheralRegisterFieldVMNode) treeNode)
					                                .getEnumeratedValueDMNode();
					if (node == null) {
						break;
					}

					appendText(sb, "Enumeration=", "\"" + node.getDescription() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				break;
            case ADDRESS:
            case REGISTER:
            case _DESCRIPTION:
            case _IMAGE:
            case _OFFSET:
            case _SIZE:
            case _TYPE:

			default:
				break;
			}
		}
		if (sb.length() != 0) {
			return sb.toString();
		}
		return null;
	}

	/**
	 * Gets the display access.
	 *
	 * @param element the element
	 * @return the display access
	 */
	public String getDisplayAccess(PeripheralTreeVMNode element) {

		String str = element.getAccess();
		if ("read-only".equals(str)) { //$NON-NLS-1$
			return "Read only"; //$NON-NLS-1$
		} else if ("write-only".equals(str)) { //$NON-NLS-1$
			return "Write only"; //$NON-NLS-1$
		} else if ("read-write".equals(str)) { //$NON-NLS-1$
			return "Read/Write"; //$NON-NLS-1$
		} else if ("writeOnce".equals(str)) { //$NON-NLS-1$
			return "Write once"; //$NON-NLS-1$
		} else if ("read-writeOnce".equals(str)) { //$NON-NLS-1$
			return "Read/Write once"; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Gets the display read action.
	 *
	 * @param element the element
	 * @return the display read action
	 */
	public String getDisplayReadAction(PeripheralTreeVMNode element) {

		String str = element.getReadAction();
		return str;
	}

	private static void appendText(StringBuilder sb, String name, String value) {

		if ((value == null) || (value.trim().isEmpty())) {
			return;
		}
		if (sb.length() > 0) {
			if (sb.indexOf(":") == -1) { //$NON-NLS-1$
				sb.append(": "); //$NON-NLS-1$
			} else {
				sb.append(", "); //$NON-NLS-1$
			}
		}
		sb.append(name);
		sb.append(value.trim());
	}

	// ------------------------------------------------------------------------
}
