/*******************************************************************************
 * Copyright (c) 2017 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.tmf.ui.viewers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.graphics.Image;

/**
 * Provides a legend image. With a name, desired height and width, an image is
 * created.
 *
 * @author Yonni Chen
 * @since 3.2
 */
public interface ILegendImageProvider {

    /**
     * Returns an image that represents the legend.
     *
     * @param imageHeight
     *            Desired image height
     * @param imageWidth
     *            Desired image width
     * @param name
     *            Name associated with a legend image
     * @return A legend image
     */
    public Image getLegendImage(int imageHeight, int imageWidth, @NonNull String name);
}
