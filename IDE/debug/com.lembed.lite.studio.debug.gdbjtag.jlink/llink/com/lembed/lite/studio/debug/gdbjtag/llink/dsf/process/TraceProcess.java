/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.llink.dsf.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;


/**
 * Synthetic process (does not have a system process), used to get the
 * semihosting output from the J-Link GDB server.
 */
public class TraceProcess extends Process implements Runnable {

	boolean fRunningSD;
	Thread fThread = null;

	Socket fSocket;
	boolean connectIsTimeOut = true;

	private int count = 5;
	ExecutorService executor;

	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Boolean socketStarted = true;

	/**
	 * Size of buffer for processing data received from remote endpoint.
	 */
	protected static final int BUFFER_SIZE = 2048;

	private static final int SOCKET_TIME_OUT = 10 * 1000;

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
	
	/** The output stream. */
	protected OutputStream fOutputStream;
	
	/** The error stream. */
	protected InputStream fErrorStream;

	/** The pipe in. */
	protected PipedInputStream fPipeIn;
	
	/** The pipe out. */
	protected PipedOutputStream fPipeOut;

	private String fHost;
	private int fPort;

	/** fake input stream, never has data to read */
	protected class NullInputStream extends InputStream {

		private boolean fIsOpened = true;
		private Thread finThread = null;

		@Override
		public int read() throws IOException {

			if (!fIsOpened) {
				return -1;
			}

			try {
				finThread = Thread.currentThread();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				
				DevicePlugin.log("NullInputStream.read() interrupted"); //$NON-NLS-1$
			}
			return 0;
		}

		@Override
        public int available() {
			return 0;
		}

		@Override
        public void close() throws IOException {

			DevicePlugin.log("NullInputStream.close() " + Thread.currentThread()); //$NON-NLS-1$

			if (fIsOpened) {
				super.close();
				fIsOpened = false;
				if (finThread != null) {

					DevicePlugin.log("NullInputStream.close() interrupt " + Thread.currentThread() + " " + finThread); //$NON-NLS-1$ //$NON-NLS-2$
					finThread.interrupt();
				}
			}
		}
	}

	/** Fake output stream, ignores all writes. */
	protected class NullOutputStream extends OutputStream {

		@Override
		public void write(int arg) throws IOException {
		}

	}

	/**
	 * Instantiates a new trace process.
	 *
	 * @param host the host
	 * @param port the port
	 */
	public TraceProcess(String host, int port) {

		DevicePlugin.log("TraceProcess(" + host + "," + port + ") " + this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		fHost = host;
		fPort = port;

		connectIsTimeOut = true;
		fRunningSD = false;

		fInputStream = new NullInputStream();
		fErrorStream = new NullInputStream();
		fOutputStream = new NullOutputStream();

		fPipeOut = new PipedOutputStream();

		try {
			fPipeIn = new PipedInputStream(fPipeOut);
		} catch (IOException e) {
			DevicePlugin.log(e);
		}

		executor = Executors.newSingleThreadExecutor();
	}

	@Override
	public void destroy() {

		DevicePlugin.log("TraceProcess.destroy() " + Thread.currentThread() + " " + fThread); //$NON-NLS-1$ //$NON-NLS-2$

		lock.lock();
		if (fRunningSD) {

			if (fThread != null && fThread != Thread.currentThread()) {
				fThread.interrupt();

				DevicePlugin.log("STraceProcess.destroy() after interrupt"); //$NON-NLS-1$
			}

			try {
				if (fSocket != null && !fSocket.isClosed()) {

					// Do not close(), it is too brutal.
					// socket.close();

					if (fSocket != null && !fSocket.isInputShutdown()) {

						DevicePlugin.log("STraceProcess.destroy() before shutdownInput"); //$NON-NLS-1$
						if (fSocket.isConnected()) {
							fSocket.shutdownInput();
						}

					}
					if (fSocket != null && !fSocket.isOutputShutdown()) {

						DevicePlugin.log("STraceProcess.destroy() before shutdownOutput"); //$NON-NLS-1$
						if (fSocket.isConnected()) {
							fSocket.shutdownOutput();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		lock.unlock();
		
		if (executor != null) {
			executor.shutdown();
		}
		DevicePlugin.log("STraceProcess.destroy() return"); //$NON-NLS-1$
	}

	@Override
	public int exitValue() {
		lock.lock();

		try {
			while (socketStarted) {
				condition.await();
			}
			if (fRunningSD) {
				throw new IllegalThreadStateException();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
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

		DevicePlugin.log("STraceProcess.waitFor() " + Thread.currentThread() + " will wait for " + fThread); //$NON-NLS-1$ //$NON-NLS-2$

		fThread.join();

		DevicePlugin.log("STraceProcess.waitFor() return " + Thread.currentThread()); //$NON-NLS-1$
		return 0;
	}

	private boolean connect() {
		boolean connectIsOk = false;

		InetSocketAddress address = new InetSocketAddress(fHost, fPort);
		fSocket = new Socket();

		try {
			fSocket.connect(address, SOCKET_TIME_OUT);
			connectIsOk = true;
		} catch (IOException e1) {
			connectIsOk = false;
		}

		return connectIsOk;
	}

	private void execSocket() {
		try {
			String future = executor.submit(new Callable<String>() {
				@Override
                public String call() throws Exception {

					while (count > 0) {
						if (connect()) {
							connectIsTimeOut = false;
						} else {
							connectIsTimeOut = true;
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						count--;
						DevicePlugin.log("TraceProcess.run() not !!!!!!!!!!!"); //$NON-NLS-1$
					}
					return "fault"; //$NON-NLS-1$
				}
			}).get();
			DevicePlugin.log(future);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {

		DevicePlugin.log("STraceProcess.run() " + Thread.currentThread()); //$NON-NLS-1$

		fRunningSD = true;
		execSocket();

		lock.lock();
		if (connectIsTimeOut) {
			DevicePlugin.log("TraceProcess.run() not +++++++++++++++ 111"); //$NON-NLS-1$
			fRunningSD = false;
			socketStarted = false;
			condition.signal();
		}

		lock.unlock();
		if (!fRunningSD) {
			return;
		}

		DevicePlugin.log("TraceProcess.run() not +++++++++++++++ 22"); //$NON-NLS-1$

		fRunningSD = true;
		try {

			fInputStream = fSocket.getInputStream();
			fOutputStream = fSocket.getOutputStream();
			fSocket.setKeepAlive(true);

			while (fSocket.isConnected() & fRunningSD) {
				if (!process()) {
					break;
				}
			}
		} catch (IOException e) {
			DevicePlugin.log(e);
		} finally {
			clean();
			fRunningSD = false;
		}

		DevicePlugin.log("TraceProcess.run() completed "); //$NON-NLS-1$
	}

	private boolean process() throws IOException {
		int nRawBytes;
		try {
			nRawBytes = fInputStream.read(fRawBytes);
		} catch (SocketException e) {
			nRawBytes = -1; // EOS
			// DevicePlugin.log(e);
		}

		if (nRawBytes == -1) {
			// End of input on inputStream.

			// Announce to the user that the remote endpoint has closed
			// the connection.
			DevicePlugin.log("TraceProcess.run() Connection closed by the GDB server."); //$NON-NLS-1$
			fPipeOut.write("Connection closed by the GDB server.".getBytes()); //$NON-NLS-1$

			return false;
		}
        if (nRawBytes > 0) {
        	fPipeOut.write(fRawBytes, 0, nRawBytes);
        }
		return true;
	}

	private void clean() {
		try {
			fPipeOut.close();
		} catch (IOException e1) {
			
		}

		try {
			fErrorStream.close();
		} catch (IOException e1) {
			
		}

		try {
			fSocket.close();
			fSocket = null;
		} catch (IOException e) {
			
		}

		try {
			fInputStream.close();
		} catch (NullPointerException e) {
			
		} catch (IOException e) {
			
		}
	}

	/**
	 * start semihosting process.
	 */
	public void submit() {

		DevicePlugin.log("TraceProcess.submit() " + Thread.currentThread()); //$NON-NLS-1$

		fThread = new Thread(this);
		fThread.setName("Trace process"); //$NON-NLS-1$
		fThread.setDaemon(true);
		fThread.setPriority(Thread.MIN_PRIORITY+1);
		fThread.start();
	}

}
