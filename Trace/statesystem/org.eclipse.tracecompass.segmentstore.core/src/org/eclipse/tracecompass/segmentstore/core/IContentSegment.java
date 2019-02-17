/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.segmentstore.core;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A segment with fields
 *
 * @author Matthew Khouzam
 * @since 2.0
 */
public interface IContentSegment extends ISegment {

    /**
     * Get the content, like an event
     *
     * @return a Map of key values
     */
    Map<@NonNull String, @NonNull ?> getContent();
}
