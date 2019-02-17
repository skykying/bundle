/*******************************************************************************
 * Copyright (c) 2010 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.internal.gprof.parser;

import java.io.DataInput;
import java.io.IOException;

public class CallGraphDecoder64 extends CallGraphDecoder{

    public CallGraphDecoder64(GmonDecoder decoder) {
        super(decoder);
    }

    @Override
    protected long readAddress(DataInput stream) throws IOException {
        return stream.readLong();
    }

}
