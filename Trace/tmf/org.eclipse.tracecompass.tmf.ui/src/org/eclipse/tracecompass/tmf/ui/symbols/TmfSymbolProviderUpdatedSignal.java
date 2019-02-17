/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.tmf.ui.symbols;

import org.eclipse.tracecompass.tmf.core.signal.TmfSignal;

/**
 * Symbol provider is updated. It will be fired when the symbol provider loads a
 * new mapping. This should be listened to by all views and viewers that need to
 * display items using the symbol map.
 * <p>
 * FIXME: move to core when possible
 *
 * @author Matthew Khouzam
 * @since 2.2
 * @deprecated Use the class with same name in the
 *             org.eclipse.tracecompass.analysis.profiling.ui plugin
 */
@Deprecated
public class TmfSymbolProviderUpdatedSignal extends TmfSignal {
    /**
     * Constructor
     *
     * @param source
     *            the symbol source
     */
    public TmfSymbolProviderUpdatedSignal(Object source) {
        super(source);
    }
}
