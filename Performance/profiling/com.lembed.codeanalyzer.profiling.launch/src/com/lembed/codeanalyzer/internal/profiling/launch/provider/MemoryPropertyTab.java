/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Red Hat initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.internal.profiling.launch.provider;


public class MemoryPropertyTab extends AbstractProviderPropertyTab {

    public MemoryPropertyTab() {
        super();
    }

    @Override
    protected String getType() {
        return "memory"; //$NON-NLS-01$
    }

    @Override
    protected String getPrefPageId() {
        return "com.lembed.codeanalyzer.profiling.provider.MemoryPreferencePage"; //$NON-NLS-1$
    }


}
