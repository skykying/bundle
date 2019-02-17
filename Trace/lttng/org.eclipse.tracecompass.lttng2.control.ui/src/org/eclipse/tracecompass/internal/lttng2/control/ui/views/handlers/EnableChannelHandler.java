/**********************************************************************
 * Copyright (c) 2012, 2014 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Bernd Hufmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.tracecompass.internal.lttng2.control.ui.views.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tracecompass.internal.lttng2.control.core.model.TraceEnablement;
import org.eclipse.tracecompass.internal.lttng2.control.ui.views.model.impl.TraceDomainComponent;

/**
 * <p>
 * Command handler implementation to enable one or more trace channels per session and domain.
 * </p>
 *
 * @author Bernd Hufmann
 */
public class EnableChannelHandler extends ChangeChannelStateHandler {

    // ------------------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------------------

    @Override
    protected TraceEnablement getNewState() {
        return TraceEnablement.ENABLED;
    }

    // ------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------

    @Override
    protected void changeState(TraceDomainComponent domain, List<String> channelNames, IProgressMonitor monitor) throws ExecutionException {
        domain.enableChannels(channelNames, null, monitor);
    }
}
