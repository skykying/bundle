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

/**
 * The Class PeripheralColumnInfo.
 */
public class PeripheralColumnInfo {

    // ------------------------------------------------------------------------

    /**
     * The Enum ColumnType.
     */
    public static enum ColumnType {

        /** The address. */
        ADDRESS,
        /** The description. */
        _DESCRIPTION,
        /** The image. */
        _IMAGE,
        /** The offset. */
        _OFFSET,
        /** The register. */
        REGISTER,
        /** The size. */
        _SIZE,
        /** The type. */
        _TYPE,
        /** The value. */
        VALUE;
    }

    // ------------------------------------------------------------------------

    /** The header. */
    public String header;

    /** The type. */
    public ColumnType type;

    /** The weight. */
    public int weight;

    /** The sortable. */
    public boolean sortable;

    // ------------------------------------------------------------------------

    /**
     * Instantiates a new peripheral column info.
     *
     * @param header
     *            the header
     * @param weight
     *            the weight
     * @param type
     *            the type
     */
    public PeripheralColumnInfo(String header, int weight, ColumnType type) {
        this(header, weight, type, false);
    }

    /**
     * Instantiates a new peripheral column info.
     *
     * @param header
     *            the header
     * @param weight
     *            the weight
     * @param type
     *            the type
     * @param sortable
     *            the sortable
     */
    public PeripheralColumnInfo(String header, int weight, ColumnType type, boolean sortable) {

        this.header = header;
        this.weight = weight;
        this.type = type;
        this.sortable = sortable;
    }

    // ------------------------------------------------------------------------
}
