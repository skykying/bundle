/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.stlink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;

/**
 * Synthetic process (does not have a system process), used to get the
 * semihosting output from the J-Link GDB server.
 */
public class SemihostingProcess extends Process implements Runnable {

	boolean fRunning = false;
	Thread fThread = null;

	Socket fSocket;

	boolean isConnected = false;
	private int count = 50;

	/**
	 * Size of buffer for processing data received from remote endpoint.
	 */
	protected static final int BUFFER_SIZE = 2048;

	/**
	 * Holds raw bytes received from the remote endpoint, prior to any TELNET
	 * protocol processing.
	 */
	protected byte[] fRawBytes = new byte[BUFFER_SIZE];

	/**
	 * This field holds a reference to an {@link InputStream} object used to receive
	 * data from the remote endpoint.
	 */
	protected InputStream fInputStream;
	protected OutputStream fOutputStream;
	protected InputStream fErrorStream;

	// fake input stream, never has data to read
	protected class NullInputStream extends InputStream {

		private boolean fIsOpened = true;
		private Thread fThread = null;

		@Override
		public int read() throws IOException {

			if (!fIsOpened) {
				return -1;
			}

			try {
				fThread = Thread.currentThread();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				{
					DevicePlugin.log("NullInputStream.read() interrupted");
				}
			}
			return 0;
		}

		public int available() {
			return 0;
		}

		public void close() throws IOException {

			DevicePlugin.log("NullInputStream.close() " + Thread.currentThread());

			if (fIsOpened) {
				super.close();
				fIsOpened = false;
				if (fThread != null) {

					DevicePlugin.log("NullInputStream.close() interrupt " + Thread.currentThread() + " " + fThread);
					fThread.interrupt();
				}
			}
		}
	}

	// Fake output stream, ignores all writes.
	protected class NullOutputStream extends OutputStream {

		@Override
		public void write(int arg) throws IOException {
		}

	}

	protected PipedInputStream fPipeIn;
	protected PipedOutputStream fPipeOut;

	private String fHost;
	private int fPort;

	public SemihostingProcess(String host, int port) {

		DevicePlugin.log("SSemihostingProcess(" + host + "," + port + ") " + this);

		fHost = host;
		fPort = port;

		fRunning = false;

		fInputStream = new NullInputStream();
		fErrorStream = new NullInputStream();
		fOutputStream = new NullOutputStream();

		fPipeOut = new PipedOutputStream();

		try {
			fPipeIn = new PipedInputStream(fPipeOut);
		} catch (IOException e) {
			DevicePlugin.log(e);
		}
	}

	@Override
	public void destroy() {

		DevicePlugin.log("SSemihostingProcess.destroy() " + Thread.currentThread() + " " + fThread);

		if (fRunning) {

			if (fThread != null && fThread != Thread.currentThread()) {
				fThread.interrupt();

				DevicePlugin.log("SSSemihostingProcess.destroy() after interrupt");
			}

			try {
				if (fSocket != null && !fSocket.isClosed()) {

					// Do not close(), it is too brutal.
					// socket.close();

					if (fSocket != null && !fSocket.isInputShutdown()) {

						DevicePlugin.log("SSSemihostingProcess.destroy() before shutdownInput");
						if (fSocket.isConnected()) {
							fSocket.shutdownInput();
						}

					}
					if (fSocket != null && !fSocket.isOutputShutdown()) {

						DevicePlugin.log("SSSemihostingProcess.destroy() before shutdownOutput");
						if (fSocket.isConnected()) {
							fSocket.shutdownOutput();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		DevicePlugin.log("SSSemihostingProcess.destroy() return");
	}

	@Override
	public int exitValue() {
		if (fRunning) {
			throw new IllegalThreadStateException();
		}
		return 0;
	}

	@Override
	public InputStream getErrorStream() {
		return fErrorStream;
	}

	@Override
	public InputStream getInputStream() {
		return fPipeIn;
	}

	@Override
	public OutputStream getOutputStream() {
		return fOutputStream;
	}

	@Override
	public int waitFor() throws InterruptedException {

		DevicePlugin.log("SSSemihostingProcess.waitFor() " + Thread.currentThread() + " will wait for " + fThread);

		fThread.join();

		DevicePlugin.log("SSSemihostingProcess.waitFor() return " + Thread.currentThread());
		return 0;
	}

	public void run() {

		DevicePlugin.log("SSSemihostingProcess.run() " + Thread.currentThread());

		
		
		fRunning = true;
		int nTimeout = 10 * 1000;
		int i = 50;
		// wait max 10sec (50x200ms)
		while (i > 0) {
			InetSocketAddress address = new InetSocketAddress(fHost, fPort);
			fSocket = new Socket();
			try {
				fSocket.connect(address, nTimeout);
				break;
			} catch (IOException e1) {
				--i;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i == 0) {
				// cannot connect, no way to go further
				
				DevicePlugin.log("cannot connect, no way to go further.");
				
//				fRunning = false;
				return;
			}
		}

		
		while(!isConnected) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 0) {
				DevicePlugin.log("SSemihostingProcess.run() not vvvvvvvv++++++++");
				return;
			}
		}

		DevicePlugin.log("SSemihostingProcess.run() not +++++++++++++++");

		fRunning = true;
		try {

			fInputStream = fSocket.getInputStream();
			fOutputStream = fSocket.getOutputStream();
			fSocket.setKeepAlive(true);

			while (fSocket.isConnected() & fRunning) {
				if (!process()) {
					break;
				}

			}
		} catch (IOException e) {
			DevicePlugin.log(e);
		} finally {
			clean();
			fRunning = false;
		}

		DevicePlugin.log("SSemihostingProcess.run() completed ");
	}

	private boolean process() throws IOException {
		int nRawBytes;
		try {
			nRawBytes = fInputStream.read(fRawBytes);
		} catch (SocketException e) {
			nRawBytes = -1; // EOS
			// STlinkPlugin.log(e);
		}

		if (nRawBytes == -1) {
			// End of input on inputStream.

			// Announce to the user that the remote endpoint has closed
			// the connection.
			DevicePlugin.log("SSemihostingProcess.run() Connection closed by the GDB server.");
			fPipeOut.write("Connection closed by the GDB server.".getBytes());

			return false;
		} else {
			if (nRawBytes > 0) {
				fPipeOut.write(fRawBytes, 0, nRawBytes);
			}
		}
		return true;
	}

	private void clean() {
		try {
			fPipeOut.close();
		} catch (IOException e1) {
			;
		}

		try {
			fErrorStream.close();
		} catch (IOException e1) {
			;
		}

		try {
			fSocket.close();
			fSocket = null;
		} catch (IOException e) {
			;
		}

		try {
			fInputStream.close();
		} catch (NullPointerException e) {
			;
		} catch (IOException e) {
			;
		}
	}

	private boolean connect() {
		boolean connectIsOk = false;
		final int nTimeout = 10 * 1000;

		InetSocketAddress address = new InetSocketAddress(fHost, fPort);
		fSocket = new Socket();

		try {
			fSocket.connect(address, nTimeout);
			connectIsOk = true;
		} catch (IOException e1) {
			connectIsOk = false;
		}

		return connectIsOk;
	}

	/**
	 * start semihosting process.
	 */
	public void submit() {

		DevicePlugin.log("SSemihostingProcess.submit() " + Thread.currentThread());

		fThread = new Thread(this);
		fThread.setName("Semihosting and SWV fake process");
		fThread.setDaemon(true);
		fThread.setPriority(Thread.MIN_PRIORITY);
		fThread.start();
	}

}
