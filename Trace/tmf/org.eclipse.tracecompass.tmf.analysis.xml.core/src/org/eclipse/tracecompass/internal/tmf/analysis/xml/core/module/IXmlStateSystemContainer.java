/*******************************************************************************
 * Copyright (c) 2014 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Geneviève Bastien - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.internal.tmf.analysis.xml.core.module;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.tmf.analysis.xml.core.model.TmfXmlLocation;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfAttributePool;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;

/**
 * Interface that all XML defined objects who provide, use or contain state
 * system must implement in order to use the state provider model elements in
 * {@link org.eclipse.tracecompass.internal.tmf.analysis.xml.core.model} package
 *
 * @author Geneviève Bastien
 */
public interface IXmlStateSystemContainer extends ITmfXmlTopLevelElement {

    /** Root quark, to get values at the root of the state system */
    int ROOT_QUARK = -1;
    /**
     * Error quark, value taken when a state system quark query is in error.
     *
     * FIXME: Originally in the code, the -1 was used for both root quark and
     * return errors, so it has the same value as root quark, but maybe it can
     * be changed to something else -2? A quark can never be negative
     */
    int ERROR_QUARK = -1;

    /**
     * Get the state system managed by this XML object
     *
     * @return The state system
     */
    ITmfStateSystem getStateSystem();

    /**
     * Get the list of locations defined in this top level XML element
     *
     * @return The list of {@link TmfXmlLocation}
     */
    @NonNull Iterable<@NonNull TmfXmlLocation> getLocations();

    /**
     * Get an attribute pool starting at the requested quark
     *
     * @param startNodeQuark
     *            The quark of the attribute to get the pool for
     * @return The attribute pool starting at the requested quark
     */
    default @Nullable TmfAttributePool getAttributePool(int startNodeQuark) {
        return null;
    }

    /**
     * Basic quark-retrieving method. Pass an attribute in parameter as an array of
     * strings, the matching quark will be returned. If the attribute does not
     * exist, it will add the quark to the state system if the context allows it.
     * Otherwise a negative value will be returned.
     *
     * See {@link ITmfStateSystemBuilder#getQuarkAbsoluteAndAdd(String...)}
     *
     * @param path
     *            Full path to the attribute
     * @return The quark for this attribute
     */
    default int getQuarkAbsoluteAndAdd(String... path) {
        ITmfStateSystem stateSystem = getStateSystem();
        int quark = stateSystem.optQuarkAbsolute(path);
        if (quark == ITmfStateSystem.INVALID_ATTRIBUTE && (stateSystem instanceof ITmfStateSystemBuilder)) {
            quark = ((ITmfStateSystemBuilder) stateSystem).getQuarkAbsoluteAndAdd(path);
        }
        return quark;
    }

    /**
     * Quark-retrieving method, but the attribute is queried starting from the
     * startNodeQuark. If the attribute does not exist, it will add it to the state
     * system if the context allows it. Otherwise a negative value will be returned.
     *
     * See {@link ITmfStateSystemBuilder#getQuarkRelativeAndAdd(int, String...)}
     *
     * @param startNodeQuark
     *            The quark of the attribute from which 'path' originates.
     * @param path
     *            Relative path to the attribute
     * @return The quark for this attribute
     */
    default int getQuarkRelativeAndAdd(int startNodeQuark, String... path) {
        ITmfStateSystem stateSystem = getStateSystem();
        int quark = stateSystem.optQuarkRelative(startNodeQuark, path);
        if (quark == ITmfStateSystem.INVALID_ATTRIBUTE && (stateSystem instanceof ITmfStateSystemBuilder)) {
            quark = ((ITmfStateSystemBuilder) stateSystem).getQuarkRelativeAndAdd(startNodeQuark, path);
        }
        return quark;
    }

}
