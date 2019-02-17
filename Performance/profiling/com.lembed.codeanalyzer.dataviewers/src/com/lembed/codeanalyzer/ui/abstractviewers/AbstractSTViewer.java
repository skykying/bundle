/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marzia Maugeri <marzia.maugeri@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.ui.abstractviewers;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

import com.lembed.codeanalyzer.ui.listeners.STDisposeListener;
import com.lembed.codeanalyzer.ui.listeners.STHeaderListener;

/**
 * The AbstractSTViewer is a wrapper handling a viewer like TreeTable or Table.
 */
public abstract class AbstractSTViewer {

    private IDialogSettings viewerSettings = null;

    private ISTDataViewersField[] fields = null;

    private ColumnViewer viewer;

    private STDataViewersComparator comparator;

    private STDataViewersHideShowManager hideShowManager;

    /**
     * Creates a new instance of the receiver under the given parent.
     *
     * @param parent
     *            is the parent control
     * @param style is the SWT style bits to customize the style of the tree/table
     *
     */
    public AbstractSTViewer(Composite parent, int style) {
        this(parent, style, true);
    }

    /**
     * Creates a new instance of the receiver under the given parent.
     *
     * @param parent
     *            is the parent control
     * @param style is the SWT style bits to customize the style of the tree/table
     * @param init setup is a flag indicating when a customization of AbstractSTViewer needs to set up additional information
     *        useful to create the Viewer
     *
     */
    public AbstractSTViewer(Composite parent, int style, boolean init) {
        if (init) {
            init(parent, style);
        }
    }

    /*
     * Initializes this view with the given view site. A memento is passed to the view which contains a snapshot of the
     * views state from a previous session. Where possible, the view should try to recreate that state within the part
     * controls. <p> This implementation will ignore the memento and initialize the view in a fresh state. Subclasses
     * may override the implementation to perform any state restoration as needed.
     */

    /**
     * Initializes the viewers. It sets: the columns of the viewers, a viewer setting (similar to memento) a column
     * manager a viewer comparator ColumnViewerToolTipSupport an OpenListener a KeyListener a PaintListener a
     * DisposeListener the input the content provider
     * @param parent The parent composite.
     * @param style  SWT style to be used.
     */
    private void init(Composite parent, int style) {
        viewer = createViewer(parent, style);
        viewerSettings = createSTAbstractDataViewersSettings();

        fields = getAllFields();

        createColumns();
        restoreColumnOrder();

        // building columns manager
        // (needs the columns to be created first)
        STDataViewersHideShowManager manager = new STDataViewersHideShowManager(this);
        manager.restoreState(viewerSettings);
        setHideShowManager(manager);

        // building the column comparator
        // (needs the columns to be created first)
        STDataViewersComparator comparator = new STDataViewersComparator(getColumns());
        comparator.restoreState(viewerSettings);
        setComparator(comparator);
        setSortIndicators();

        IContentProvider cp = createContentProvider();

        viewer.setContentProvider(cp);
        viewer.setUseHashlookup(true);

        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        viewer.getControl().setLayoutData(data);

        viewer.setInput(null);

        ColumnViewerToolTipSupport.enableFor(viewer);

        Scrollable scrollable = (Scrollable) viewer.getControl();
        ScrollBar bar = scrollable.getVerticalBar();
        if (bar != null) {
            bar.setSelection(restoreVerticalScrollBarPosition());
        }
        bar = scrollable.getHorizontalBar();
        if (bar != null) {
            bar.setSelection(restoreHorizontalScrollBarPosition());
        }

        viewer.addOpenListener(event -> handleOpenEvent(event));

        viewer.getControl().addDisposeListener(new STDisposeListener(this));

    }

    // //////////////////////////
    // Columns manager utilities
    // //////////////////////////

    /**
     * Set this manager to be the new hide/show manager. This should only be called if the columns have been created.
     * This method should not be called by customers, it is used by the hide/show action to update the viewer.
     *
     * @param manager The new manager.
     */
    public void setHideShowManager(STDataViewersHideShowManager manager) {
        hideShowManager = manager;
        hideShowManager.updateColumns();
    }

    // //////////////////
    // Sorting utilities
    // //////////////////

    /**
     * Return the table sorter portion of the sorter.
     *
     * @return TableSorter The currently set comparator
     */
    public STDataViewersComparator getTableSorter() {
        return comparator;
    }

    /**
     * Set the comparator to be the new comparator. This should only be called if the viewer has been created.
     *
     * @param comparator The comparator to be used.
     */
    public void setComparator(STDataViewersComparator comparator) {
        this.comparator = comparator;
        viewer.setComparator(comparator);
        updateForNewComparator(comparator);
    }

    /**
     * Update the viewer for comparator updates
     *
     * @param comparator
     */
    private void updateForNewComparator(STDataViewersComparator comparator) {
        comparator.saveState(viewerSettings);
        viewer.refresh();
        setSortIndicators();
    }

    /**
     * Sets the sort indicator on top of target column
     */
    private void setSortIndicators() {
        Item topc = getTableSorter().getTopColumn();
        updateDirectionIndicator(topc);
    }

    // ///////////////////////
    // Save/restore utilities
    // ///////////////////////

    /**
     * Creates the container for the settings of this viewer
     *
     * @return The dialog settings.
     */
    private IDialogSettings createSTAbstractDataViewersSettings() {
        IDialogSettings settings = getDialogSettings().getSection(STDataViewersSettings.TAG_SECTION_VIEWER_STATE);
        if (settings == null) {
            settings = getDialogSettings().addNewSection(STDataViewersSettings.TAG_SECTION_VIEWER_STATE);
        }
        return settings;
    }

    /**
     * Restores the viewer's column order. Called just after the columns are created.
     */
    private void restoreColumnOrder() {
        int[] order = restoreColumnOrderSetting();
        if (order != null && order.length == fields.length) {
            setColumnOrder(order);
        }
    }

    /**
     * Restores the vertical scrollbar position
     *
     * @return the position
     */
    private int restoreVerticalScrollBarPosition() {
        if (viewerSettings == null) {
            // no settings saved
            return 0;
        }

        String position = viewerSettings.get(STDataViewersSettings.TAG_VIEWER_STATE_VERTICAL_POSITION);
        if (position == null) {
            // no vertical position saved
            return 0;
        }

        try {
            return Integer.parseInt(position);
        } catch (NumberFormatException nfe) {
            // invalid entry
            return 0;
        }
    }

    /**
     * Restores the horizontal scrollbar position
     *
     * @return the position
     */
    private int restoreHorizontalScrollBarPosition() {
        if (viewerSettings == null) {
            // no settings saved
            return 0;
        }

        String position = viewerSettings.get(STDataViewersSettings.TAG_VIEWER_STATE_HORIZONTAL_POSITION);
        if (position == null) {
            // no horizontal position saved
            return 0;
        }

        try {
            return Integer.parseInt(position);
        } catch (NumberFormatException nfe) {
            // invalid entry
            return 0;
        }
    }

    /**
     * Restore the order of the columns using the dialog settings
     *
     * @return column order
     */
    private int[] restoreColumnOrderSetting() {
        if (viewerSettings == null) {
            // no settings saved
            return null;
        }

        String[] columnOrder = viewerSettings.getArray(STDataViewersSettings.TAG_VIEWER_STATE_COLUMN_ORDER);
        if (columnOrder == null) {
            // no column order saved
            return null;
        }

        int n = columnOrder.length;
        if (n != getAllFields().length) {
            // bad column count
            return null;
        }

        try {
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                int val = Integer.parseInt(columnOrder[i]);
                values[i] = val;
            }
            return values;
        } catch (NumberFormatException nfe) {
            // invalid entry
            return null;
        }
    }

    /**
     * Used to save the state of the viewer before its disposal
     *
     */
    public void saveState() {
        if (viewerSettings == null) {
            viewerSettings = getDialogSettings().getSection(STDataViewersSettings.TAG_SECTION_VIEWER_STATE);
        }
        if (viewerSettings == null) {
            viewerSettings = getDialogSettings().addNewSection(STDataViewersSettings.TAG_SECTION_VIEWER_STATE);
        }

        // save column order
        int[] columnOrder = getColumnOrder();
        String[] columnOrderStrings = new String[columnOrder.length];
        for (int i = 0; i < columnOrder.length; i++) {
            columnOrderStrings[i] = columnOrder[i] + "";
        }
        viewerSettings.put(STDataViewersSettings.TAG_VIEWER_STATE_COLUMN_ORDER, columnOrderStrings);

        // save hide show manager
        if (getHideShowManager() != null) {
            getHideShowManager().saveState(viewerSettings);
        }

        // save sort
        if (getTableSorter() != null) {
            getTableSorter().saveState(viewerSettings);
        }

        // save vertical position
        Scrollable scrollable = (Scrollable) viewer.getControl();
        ScrollBar bar = scrollable.getVerticalBar();
        int position = (bar != null) ? bar.getSelection() : 0;
        viewerSettings.put(STDataViewersSettings.TAG_VIEWER_STATE_VERTICAL_POSITION, position);

        // save horizontal position
        bar = scrollable.getHorizontalBar();
        position = (bar != null) ? bar.getSelection() : 0;
        viewerSettings.put(STDataViewersSettings.TAG_VIEWER_STATE_HORIZONTAL_POSITION, position);
    }

    // //////////////////////////
    // Listeners for this viewer
    // //////////////////////////

    /**
     * Creates the header listener used by the columns to check when their header is selected (used by sorting). This
     * method is called at column creation.
     *
     * @return the new header listener
     */
    protected SelectionListener createHeaderListener() {
        return new STHeaderListener(this);
    }

    // ////////////////////////
    // Getters for this viewer
    // ////////////////////////

    /**
     * Get the viewer's settings
     *
     * @return the IDialogSettings used to save the viewer's settings
     */
    public IDialogSettings getViewerSettings() {
        return viewerSettings;
    }

    /**
     * Get the wrapped viewer
     *
     * @return the JFace viewer wrapped in this ST viewer
     */
    public ColumnViewer getViewer() {
        return viewer;
    }

    /**
     * Get the input of the viewer
     *
     * @return the inputed object
     */
    public Object getInput() {
        return viewer.getInput();
    }

    /**
     * Get the hideShowManager that manages the columns hiding and width.
     *
     * @return the hideShowManager
     */
    public STDataViewersHideShowManager getHideShowManager() {
        return hideShowManager;
    }

    // ///////////////////////////////////////////////////////////
    // Methods that can be overridden by customers when
    // subclassing AbstractSTTreeViewer and AbstractSTTableViewer
    // ///////////////////////////////////////////////////////////

    /**
     * Handle the open event, called each time the viewer is opened
     * <p>
     * Subclasses may override it.
     * </p>
     *
     * @param event Unused event.
     */
    protected abstract void handleOpenEvent(OpenEvent event);

    // ///////////////////////////////////////////////
    // Abstract methods to implement when subclassing
    // AbstractSTTreeViewer or AbstractSTTableViewer
    // ///////////////////////////////////////////////

    /**
     * Gets the fields (e.g. the columns description) of that viewer
     *
     * <p>
     * This is where you should define the columns of your viewers and the data they are going to display (label
     * provider).
     * </p>
     *
     * @return the fields of that viewer
     */
    public abstract ISTDataViewersField[] getAllFields();

    /**
     * Creates the content provider used by the viewer. This method is called once at viewer initialization.
     *
     * @return a new content provider
     */
    protected abstract IContentProvider createContentProvider();

    /**
     * Permit to provide the sort dialog dialogSettings (used to persist the state of the sort dialog)
     * <p>
     * This implementation is generally like:
     * </p>
     * <p>
     * <code>protected IDialogSettings getDialogSettings() {<br>
     * return </code>PLUGINActivator<code>.getDefault().getDialogSettings();<br>
     * } </code>
     * </p>
     * <p>
     * Note that your plugin has to be an <code>AbstractUIPlugin</code> in order to provide the
     * <code>getDialogSettings()</code> method.
     * </p>
     * <p>
     * If your plugin implements MANY view or viewers, this method should return a particular section of the plugin
     * dialog setting:
     * </p>
     * <p>
     * <code>
     * protected IDialogSettings getDialogSettings() <br>
     * IDialogSettings settings = </code>PLUGINActivator<code>.getDefault().getDialogSettings().getSection(</code>
     * SECTION_NAME<code>); <br>
     *     if (settings == null) { <br>
     *         settings = </code>PLUGINActivator<code>.getDefault().getDialogSettings().addNewSection(</code>SECTION_NAME
     * <code>);<br>
     *     }<br>
     *     return settings;
     * </code>
     * </p>
     * <p>
     * Note that if you use multiple instantiated views (not singleton) or many views with the same viewer, using the
     * code above they will all have the same dialog settings thus the last one which is closed will save the state for
     * all the others.<br>
     * If you want to avoid that you can add a view-dependent SECTION_NAME parameter to the constructor of the VIEWER in
     * the VIEW class and then use it in the <code>getDialogSettings()</code> method. Here is an example:
     * </p>
     * <p>
     * In the view class (extending <code>AbstractSTDataView</code>):
     * </p>
     * <p>
     * <code>
     * private static final String SETTINGS_SECTION = </code>SECTION_NAME<code>;<br>
     * <br>
     * protected AbstractSTViewer createAbstractSTViewer(Composite parent) {<br>
     * return new MyViewer(parent, SETTINGS_SECTION);<br>
     * }
     * </code>
     * </p>
     * <p>
     * In the viewer class (extending <code>AbstractST{Tree|Table}Viewer</code>):
     * </p>
     * <p>
     * <code>
     * private final String settingsSection;<br>
     * <br>
     * public CallHierarchyViewer(Composite parent, String settingsSection) {<br>
     * super(parent);<br>
     * this.settingsSection = settingsSection;<br>
     * }<br>
     * <br>
     * protected IDialogSettings getDialogSettings() <br>
     * IDialogSettings settings = </code>PLUGINActivator<code>.getDefault().getDialogSettings().getSection(</code>
     * this.settingsSection<code>); <br>
     * if (settings == null) { <br>
     * settings = </code>PLUGINActivator<code>.getDefault().getDialogSettings().addNewSection(</code>
     * this.settingsSection<code>);<br>
     * }<br>
     * return settings;
     * </code>
     * </p>
     *
     * @return the IDialogSettings used to store/load the dialog state
     */
    public abstract IDialogSettings getDialogSettings();

    // //////////////////////////////////////////////////////////////
    // These following methods are intended to be implemented in
    // AbstractSTTreeViewer and AbstractSTTableViewer because
    // they are different for the two types of viewers.
    // They are not intended to be implemented by customer.
    // //////////////////////////////////////////////////////////////

    // Viewer creation utilities

    /**
     * The method called to create the wrapped control (TreeViewer, TableViewer)
     * @param parent The composite to be parent.
     * @param style  The SWT style to be used.
     * @return The newly created viewer.
     *
     */
    protected abstract ColumnViewer createViewer(Composite parent, int style);

    /**
     * Creates the columns in the control.
     *
     */
    protected abstract void createColumns();

    protected CellLabelProvider createColumnLabelProvider(Item column) {
        return new STOwnerDrawLabelProvider(column);
    }

    // Columns utilities

    /**
     * Update the direction indicator as column is now the primary column.
     *
     * @param column
     *            the column that has to be the sorted column
     */
    public abstract void updateDirectionIndicator(Item column);

    /**
     * Get the wrapped viewer's columns order. Used to get the columns order since the TreeViewer and the TableViewer
     * don't share the same API to get the columns.
     *
     * @return the columns order of the viewer
     */
    public abstract int[] getColumnOrder();

    /**
     * Set the wrapped viewer's columns order. Used to set the columns order since the TreeViewer and the TableViewer
     * don't share the same API to get the columns.
     * @param order The new column order.
     *
     */
    protected abstract void setColumnOrder(int[] order);

    /**
     * Get the wrapped viewer's columns. Used get the columns list since the TreeViewer and the TableViewer don't share
     * the same API to get the columns.
     *
     * @return the columns of the viewer
     */
    public abstract Item[] getColumns();

    /**
     * Get the wrapped viewer's column index for a given column. Used get the columns list since the TreeViewer and the
     * TableViewer don't share the same API to get the columns.
     *
     * @param column The column whose index is looked for.
     *
     * @return the index of the column in the viewer
     */
    public abstract int getColumnIndex(Item column);

    /**
     * Get the width of the target column of the viewer
     *
     * @param column
     *            A column under the form of an Item object which is the common superclass of TreeColumn and TableColumn
     *
     * @return The width of the column
     */
    public abstract int getColumnWidth(Item column);

    /**
     * Set the width of the target column of the viewer
     *
     * @param column
     *            A column under the form of an Item object which is the common superclass of TreeColumn and TableColumn
     * @param width
     *            The new width
     */
    public abstract void setColumnWidth(Item column, int width);

    /**
     * Set the resizable state of the target column of the viewer
     *
     * @param column
     *            A column under the form of an Item object which is the common superclass of TreeColumn and TableColumn
     * @param resizable
     *            The new resizable state
     */
    public abstract void setColumnResizable(Item column, boolean resizable);

}
