/*******************************************************************************
 * Copyright (c) 2017 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.tmf.analysis.xml.core.fsm.model;

import org.eclipse.tracecompass.internal.tmf.analysis.xml.core.fsm.module.IAnalysisDataContainer;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;

/**
 * A data-driven action base class
 *
 * @author Geneviève Bastien
 */
public abstract class DataDrivenAction implements IDataDrivenRuntimeObject {

    /**
     * Handle the event
     *
     * @param event
     *            The event to handle
     * @param scenarioInfo
     *            The scenario info
     * @param container
     *            The analysis data container
     */
    public abstract void eventHandle(ITmfEvent event, DataDrivenScenarioInfo scenarioInfo, IAnalysisDataContainer container);

}
