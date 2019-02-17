package org.panda.logicanalyzer.integration.sump.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.integration.sump.Activator;
import org.panda.logicanalyzer.integration.sump.configuration.DeviceConfiguration;

/**
 * The device communicator builds the bridge between this application and the device itself.
 */
public class DeviceCommunicator {

	/**
	 * The output stream to the device
	 */
	private final OutputStream out;

	/**
	 * The input stream from the device
	 */
	private final InputStream in;

	/**
	 * The configuration of the enclosing device
	 */
	private final DeviceConfiguration configuration;

	/**
	 * Use the static factory method instead
	 * @param configuration The configuration of the enclosing device
	 * @param port The port we'll use for communicating
	 * @param inputStream The input stream from the device
	 * @param outputStream The output stream to the device
	 */
	public DeviceCommunicator(DeviceConfiguration configuration, OutputStream outputStream, InputStream inputStream) {
		this.configuration = configuration;
		out = outputStream;
		in = inputStream;
	}

	/**
	 * Sends a command to the device
	 *
	 * @param opcode The command to send
	 * @throws CoreException In case something goes wrong
	 */
	public void sendCommand(int opcode) throws CoreException {
		try {
			out.write((byte)opcode);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while communicating with device", e);
			throw new CoreException(status);
		}
	}

	/**
	 * Sends a command to the device
	 *
	 * @param opcode The command to send
	 * @param data The parameter of the opcode sent
	 * @throws CoreException In case something goes wrong
	 */
	public void sendCommand(int opcode, int data) throws CoreException {
		byte[] raw = new byte[5];
		int mask = 0xff;
		int shift = 0;

		raw[0] = (byte)opcode;
		for (int i = 1; i < 5; i++) {
			raw[i] = (byte)((data & mask) >> shift);
			mask = mask << 8;
			shift += 8;
		}

		String debugCmd = "";
		for (int j = 0; j < 5; j++) {
			for (int i = 7; i >= 0; i--) {
				if ((raw[j] & (1 << i)) != 0)
					debugCmd += "1";
				else
					debugCmd += "0";
			}
			debugCmd += " ";
		}
		System.out.println(debugCmd);

		try {
			out.write(raw);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while communicating with device", e);
			throw new CoreException(status);
		}
	}

	/**
	 * Reads a sample from the device.
	 *
	 * @param amountOfChannels The amount of channels we used during sampling
	 * @return The read sample
	 * @throws CoreException In case something goes wrong
	 */
	public int readSample(int amountOfChannels) throws CoreException {
		int v, value = 0;

		try {
			// wait for the first byte
			for (int i = 0; i < 10000 && in.available() <= 0; i++) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// we don't really care
				}
			}

			for (int i = 0; i < amountOfChannels / 8; i++) {
				if (configuration.getChannelGroupEnablement()[i]) {
					v = in.read();
				} else {
					v = 0;
				}
				if (v < 0 || Thread.interrupted()) {
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Data readout has been interrupted");
					throw new CoreException(status);
				}
				value |= v << (8 * i);
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while communicating with device", e);
			throw new CoreException(status);
		}

		return value;
	}

	/**
	 * Reads an integer value from the device.
	 *
	 * @return the integer value read
	 * @throws CoreException In case something goes wrong
	 */
	public int readInteger() throws CoreException {
		int value = 0;

		try {
			for (int i = 0; i < 4; i++) {
				int v = in.read();
				if (v < 0 || Thread.interrupted()) {
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Data readout has been interrupted");
					throw new CoreException(status);
				}
				value |= v << (8 * i);
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while communicating with device", e);
			throw new CoreException(status);
		}

		return value;
	}

}
