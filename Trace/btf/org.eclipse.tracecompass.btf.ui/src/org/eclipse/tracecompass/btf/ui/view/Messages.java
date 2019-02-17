/*******************************************************************************
 * Copyright (c) 2014, 2015 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Geneviève Bastien - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.btf.ui.view;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used in the LTTng kernel CPU usage view and viewers.
 *
 * @author Geneviève Bastien
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {

    private static final String BUNDLE_NAME = Messages.class.getName();

    public static String BtfComposite_ColumnPercent;
    public static String BtfComposite_ColumnProcess;
    public static String BtfComposite_ColumnTID;
    public static String BtfComposite_ColumnTime;
    public static String BtfComposite_TextPercent;
    public static String BtfComposite_TextTime;
    public static String BtfComposite_ColumnLegend;
    public static String BtfView_Title;
    public static String BtfXYViewer_CpuYAxis;
    public static String BtfXYViewer_TimeXAxis;
    public static String BtfXYViewer_Title;
    public static String BtfXYViewer_Total;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
