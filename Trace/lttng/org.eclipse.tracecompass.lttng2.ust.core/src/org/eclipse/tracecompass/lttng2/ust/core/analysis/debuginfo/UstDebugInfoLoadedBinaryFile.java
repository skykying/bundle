/*******************************************************************************
 * Copyright (c) 2015 EfficiOS Inc., Alexandre Montplaisir
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.lttng2.ust.core.analysis.debuginfo;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Simple extension to {@link UstDebugInfoBinaryFile} that adds the base address at
 * which the given binary or library is loaded.
 *
 * @author Alexandre Montplaisir
 * @noreference Meant to be used internally by the analysis only
 */
public class UstDebugInfoLoadedBinaryFile extends UstDebugInfoBinaryFile {

    private final long fBaseAddress;

    /**
     * Constructor
     *
     * @param baseAddress
     *            The base address at which the binary or library is loaded
     * @param filePath
     *            The binary's path on the filesystem.
     * @param buildId
     *            The binary's unique buildID (in base16 form).
     * @param debugLink
     *            Path to the binary's separate debug info.
     * @param isPic
     *            Whether the code in the binary is position-independent.
     */
    public UstDebugInfoLoadedBinaryFile(long baseAddress, String filePath,
            @Nullable String buildId, @Nullable String debugLink,
            boolean isPic) {
        super(filePath, buildId, debugLink, isPic);
        this.fBaseAddress = baseAddress;
    }

    /**
     * Return the base address at which the object is loaded.
     *
     * @return The base address
     */
    public long getBaseAddress() {
        return fBaseAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fBaseAddress);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(getClass().equals(obj.getClass()))) {
            return false;
        }

        if (!super.equals(obj)) {
            return false;
        }

        UstDebugInfoLoadedBinaryFile other = (UstDebugInfoLoadedBinaryFile) obj;

        return Objects.equals(fBaseAddress, other.fBaseAddress);
    }
}
