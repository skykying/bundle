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
package com.lembed.lite.studio.dataviewers.abstractviewers;

/**
 * This interface contains the constants used during an export action
 */
@SuppressWarnings("javadoc")
public interface STDataViewersCSVExporterConstants {
   
    String TAG_SECTION_CSV_EXPORTER = "csv_exporter_section"; //$NON-NLS-1$
    String TAG_EXPORTER_SEPARATOR = "csv_exporter_separator"; //$NON-NLS-1$
    String TAG_EXPORTER_CHILD_MARKER = "csv_exporter_child_marker"; //$NON-NLS-1$
    String TAG_EXPORTER_LAST_CHILD_MARKER = "csv_exporter_last_child_marker"; //$NON-NLS-1$
    String TAG_EXPORTER_CHILD_LINK = "csv_exporter_child_link"; //$NON-NLS-1$
    String TAG_EXPORTER_NO_CHILD_LINK = "csv_exporter_no_child_link"; //$NON-NLS-1$
    String TAG_EXPORTER_OUTPUT_FILE_PATH = "csv_exporter_output_file_path"; //$NON-NLS-1$
    String TAG_EXPORTER_LEAF_MARKER = "csv_exporter_leaf_marker"; //$NON-NLS-1$
    String TAG_EXPORTER_NODE_MARKER = "csv_exporter_node_marker"; //$NON-NLS-1$
    String TAG_EXPORTER_EXPAND_ALL = "csv_exporter_expand_all"; //$NON-NLS-1$
    String TAG_EXPORTER_SHOW_HIDDEN_COLUMNS = "csv_exporter_show_hidden_columns"; //$NON-NLS-1$
    String TAG_EXPORTER_TREE_PREFIX = "csv_exporter_tree_prefix"; //$NON-NLS-1$

    String DEFAULT_EXPORTER_SEPARATOR = ";"; //$NON-NLS-1$
    String DEFAULT_EXPORTER_CHILD_MARKER = "+-"; //$NON-NLS-1$
    String DEFAULT_EXPORTER_LAST_CHILD_MARKER = "+-"; //$NON-NLS-1$
    String DEFAULT_EXPORTER_CHILD_LINK = "| "; //$NON-NLS-1$
    String DEFAULT_EXPORTER_NO_CHILD_LINK = "  "; //$NON-NLS-1$
    String DEFAULT_EXPORTER_OUTPUT_FILE_PATH = "./export.csv"; //$NON-NLS-1$
    String DEFAULT_EXPORTER_LEAF_MARKER = ""; //$NON-NLS-1$
    String DEFAULT_EXPORTER_NODE_MARKER = ""; //$NON-NLS-1$
    boolean DEFAULT_EXPORTER_EXPAND_ALL = true;
    boolean DEFAULT_EXPORTER_SHOW_HIDDEN_COLUMNS = true;
    boolean DEFAULT_EXPORTER_TREE_PREFIX = true;
}
