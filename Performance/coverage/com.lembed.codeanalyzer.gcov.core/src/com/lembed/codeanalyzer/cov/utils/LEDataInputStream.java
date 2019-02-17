/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.cov.utils;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Little-endian implementation of DataInputStream
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class LEDataInputStream implements DataInput {

    private final DataInputStream in;
    private final byte[] buffer = new byte[8];

    /**
     * Constructor
     * @param in
     */
    public LEDataInputStream(DataInputStream in) {
        this.in = in;
    }

    @Override
    public final short readShort() throws IOException {
        in.readFully(buffer, 0, 2);
        return (short) ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        in.readFully(buffer, 0, 2);
        return ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final char readChar() throws IOException {
        in.readFully(buffer, 0, 2);
        return (char) ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final int readInt() throws IOException {
        in.readFully(buffer, 0, 4);
        return (buffer[3]) << 24 | (buffer[2] & 0xff) << 16 | (buffer[1] & 0xff) << 8 | (buffer[0] & 0xff);
    }

    @Override
    public final long readLong() throws IOException {
        in.readFully(buffer, 0, 8);
        return (long) (buffer[7]) << 56 | /* long cast needed or shift done modulo 32 */
        (long) (buffer[6] & 0xff) << 48 | (long) (buffer[5] & 0xff) << 40 | (long) (buffer[4] & 0xff) << 32
                | (long) (buffer[3] & 0xff) << 24 | (long) (buffer[2] & 0xff) << 16 | (long) (buffer[1] & 0xff) << 8
                | (buffer[0] & 0xff);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return in.readByte();
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        in.readFully(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        in.readFully(b, off, len);
    }

    @Override
    @Deprecated
    public String readLine() throws IOException {
        return in.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        return in.readUTF();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return in.skipBytes(n);
    }

    /**
     * Close this stream.
     */
    public void close() throws IOException {
        in.close();
    }

    public final long readUnsignedInt() throws IOException {
        in.readFully(buffer, 0, 4);
        return (((buffer[3]) << 24 | (buffer[2] & 0xff) << 16 | (buffer[1] & 0xff) << 8 | (buffer[0] & 0xff)) & MasksGenerator.UNSIGNED_INT_MASK);
    }

}
