/*******************************************************************************
 * Copyright (c) 2017 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.tmf.analysis.xml.core.fsm.compile;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.analysis.xml.core.module.TmfXmlStrings;

/**
 * This class is a container for compilation units that may be used by other
 * elements during analysis compilation. It is passed to objects during
 * compilation and elements can be set and retrieved.
 *
 * @author Geneviève Bastien
 */
public class AnalysisCompilationData {

    private final Map<String, TmfXmlLocationCu> fLocations = new HashMap<>();
    private final Map<String, String> fDefinedValues = new HashMap<>();
    private final Map<String, TmfXmlMappingGroupCu> fMappingGroups = new HashMap<>();

    /**
     * Add a location compilation unit with a given ID to the analysis data
     *
     * @param id
     *            The ID of the location
     * @param location
     *            The location compilation unit
     */
    public void addLocation(String id, TmfXmlLocationCu location) {
        fLocations.put(id, location);
    }

    /**
     * Get a location compilation unit for a given ID
     *
     * @param locationId
     *            The ID of the location to get
     * @return The location compilation unit or <code>null</code> if it is not
     *         available
     */
    public @Nullable TmfXmlLocationCu getLocation(String locationId) {
        return fLocations.get(locationId);
    }

    /**
     * Add a defined value to the analysis data. A defined value allows to map a
     * constant to some corresponding value.
     *
     * @param id
     *            The constant defining this value
     * @param definedValue
     *            The value to map to the ID
     */
    public void addDefinedValue(String id, String definedValue) {
        fDefinedValues.put(id, definedValue.intern());
    }

    /**
     * Get the defined value associated with a constant
     *
     * @param constant
     *            The constant defining this value
     * @return The actual value corresponding to this constant, or <code>null</code>
     *         if the constant is not defined
     */
    private @Nullable String getDefinedValue(String constant) {
        return fDefinedValues.get(constant);
    }

    /**
     * Get the actual value of a string. If the string is a defined value it will
     * return the value it maps to, otherwise, the string is returned as is.
     *
     * @param string
     *            The string to resolve
     * @return The resolved string or <code>null</code> if the value should be
     *         mapped to something but the value is unavailable.
     */
    public @Nullable String getStringValue(String string) {
        String value = string;
        if (value.startsWith(TmfXmlStrings.VARIABLE_PREFIX)) {
            /* search the attribute in the map without the fist character $ */
            value = getDefinedValue(value.substring(1));
        }
        return value;
    }

    /**
     * Add a mapping group compilation unit to the analysis data
     *
     * @param id
     *            The ID of the mapping group
     * @param mappingGroup
     *            The mapping group compilation unit
     */
    public void addMappingGroup(String id, TmfXmlMappingGroupCu mappingGroup) {
        fMappingGroups.put(id, mappingGroup);
    }

    /**
     * Get the mapping group compilation unit for a given ID
     *
     * @param groupId
     *            The ID of the group to retrieve
     * @return The mapping group
     */
    public @Nullable TmfXmlMappingGroupCu getMappingGroup(String groupId) {
        return fMappingGroups.get(groupId);
    }
}
