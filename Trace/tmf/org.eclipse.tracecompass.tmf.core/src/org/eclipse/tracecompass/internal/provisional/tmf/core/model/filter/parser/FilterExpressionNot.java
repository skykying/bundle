/*******************************************************************************
* Copyright (c) 2018 Ericsson
*
* All rights reserved. This program and the accompanying materials are
* made available under the terms of the Eclipse Public License v1.0 which
* accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.eclipse.tracecompass.internal.provisional.tmf.core.model.filter.parser;

import java.util.Map;
import java.util.Queue;

/**
 * This class implement a filter expression negation that could be tested against an
 * input
 *
 * @author Jean-Christian Kouame
 *
 */
public class FilterExpressionNot extends FilterExpression {

    /**
     * Constructor
     *
     * @param elements
     *            The list of element representing this experession
     *
     */
    public FilterExpressionNot(Queue<Object> elements) {
        super(elements);
    }

    @Override
    public boolean test(Map<String, String> data) {
        return !super.test(data);
    }
}
