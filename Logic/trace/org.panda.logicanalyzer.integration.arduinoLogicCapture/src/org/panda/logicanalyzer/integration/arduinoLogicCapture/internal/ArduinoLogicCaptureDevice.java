package org.panda.logicanalyzer.integration.arduinoLogicCapture.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;
import org.panda.logicanalyzer.core.util.ArrayBackedFrame;
import org.panda.logicanalyzer.core.util.DataPacket;
import org.panda.logicanalyzer.deviceintegration.core.IDevice;
import org.panda.logicanalyzer.deviceintegration.core.IDeviceConfiguration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Our device implementation which constitutes the bridge between the logic
 * analyzer and the Arduino.
 */
public class ArduinoLogicCaptureDevice implements IDevice {

	/**
	 * The clock speed of the target device in Hz. Set by
	 * {@link #initialize(IDeviceConfiguration)}
	 */
	private int clockSpeed;

	/**
	 * True if we're supposed to run in state change mode, false if in clocked
	 * mode. Set by {@link #initialize(IDeviceConfiguration)}
	 */
	private boolean stateChangeMode;

	/**
	 * The trigger level. Set by {@link #initialize(IDeviceConfiguration)}
	 */
	private byte level;

	/**
	 * The time in seconds to wait while capturing
	 */
	private int captureTime;

	/**
	 * The prescaler used during capturing
	 */
	private int prescaler;

	@Override
	public IDataPacket capture(InputStream in, OutputStream out) throws CoreException, IOException {

		showMessage("capture begin");

		List<IFrame> frames = new LinkedList<IFrame>();
		boolean done = false;
		String buffer = "";

		buffer += "15000142 12554489 56785621 45789635 25786352 25895642 32123678 55784232 55553621 44451223 11784526 88135483";
		buffer += "\n";

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// who cares
		}

		if (buffer.endsWith("\n")) {
			String[] frame = buffer.split(" ");

			if (frame.length == 2) {
				try {
					long time = Long.parseLong(frame[0].trim(), 16) * ((1000000000L /  clockSpeed) * prescaler);
					int capture = Integer.parseInt(frame[1].trim(), 16);

					boolean[] data = new boolean[8];
					for (int i = 0; i < data.length; i++) {
						data[i] = ((capture >> i) & 0x01) > 0;
					}

					frames.add(new ArrayBackedFrame(time, data));
				} catch (NumberFormatException e) {
					// what a pitty
				}
			}

			buffer = "";
		}


		showMessage("capture end" + frames.toString());
		return new DataPacket(frames);
	}

	public IDataPacket bcapture(InputStream in, OutputStream out) throws CoreException, IOException {

		// wait for first sign of life
		if (!timedWaitingFor(in, "ok.", 10000)) {
			IStatus status = new Status(IStatus.ERROR, ArduinoLogicCaptureDeviceConfigurationManager.PLUGIN_ID, "Timeout while initializing device");
			throw new CoreException(status);
		}

		// set the mode
		out.write('m');
		out.write(stateChangeMode ? 's' : 'c');

		// set the level
		out.write(level);

		// ping the device to ensure it's ready
		out.write('p');
		out.flush();
		if (!timedWaitingFor(in, "pong", 10000)) {
			IStatus status = new Status(IStatus.ERROR, ArduinoLogicCaptureDeviceConfigurationManager.PLUGIN_ID, "Timeout while initializing device");
			throw new CoreException(status);
		}

		// start tracing
		out.write('t');

		// wating until tracing is finished
		timedWaitingFor(in, "tracing finished", captureTime * 1000);

		// get our dump
		out.write('d');

		// wait for the trace: line
		if (!timedWaitingFor(in, "Trace:", 10000)) {
			IStatus status = new Status(IStatus.ERROR, ArduinoLogicCaptureDeviceConfigurationManager.PLUGIN_ID, "Timeout while waiting for trace");
			throw new CoreException(status);
		}


		List<IFrame> frames = new LinkedList<IFrame>();
		boolean done = false;
		String buffer = "";
		while (!done) {
			if (in.available() > 0) {
				buffer += String.valueOf((char)in.read());

				if (buffer.endsWith("ZZZZ Z")) {
					done = true;
				} else if (buffer.endsWith("\n")) {
					String[] frame = buffer.split(" ");

					if (frame.length == 2) {
						try {
							long time = Long.parseLong(frame[0].trim(), 16) * ((1000000000L /  clockSpeed) * prescaler);
							int capture = Integer.parseInt(frame[1].trim(), 16);

							boolean[] data = new boolean[8];
							for (int i = 0; i < data.length; i++) {
								data[i] = ((capture >> i) & 0x01) > 0;
							}

							frames.add(new ArrayBackedFrame(time, data));
						} catch (NumberFormatException e) {
							// what a pitty
						}
					}

					buffer = "";
				}
			}
		}

		return new DataPacket(frames);
	}

	/**
	 * This method searches on the input stream for the given pattern. If the
	 * pattern is not found in time, false is returned (true otherwise).
	 *
	 * @param in The stream to search on
	 * @param pattern The pattern to look for
	 * @param timeout The time to wait at max
	 * @throws IOException In case reading from the stream fails
	 */
	private boolean timedWaitingFor(InputStream in, String pattern, int timeout) throws IOException {
		String buffer = "";

		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < timeout) {
			if (in.available() > 0) {
				buffer += String.valueOf((char)in.read());
				if (buffer.endsWith(pattern)) return true;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// who cares
			}
		}

		return false;
	}

	@Override
	public void initialize(IDeviceConfiguration configuration) throws CoreException {
		captureTime = Integer.parseInt((String) configuration.getProperty("analyzer.time"));
		clockSpeed = Integer.parseInt((String) configuration.getProperty("analyzer.clockSpeed"));
		stateChangeMode = "stateChange".equals(configuration.getProperty("analyzer.mode"));
		level = ((String) configuration.getProperty("analyzer.level")).getBytes()[0];
		prescaler = Integer.parseInt((String) configuration.getProperty("analyzer.prescaler"));
	}

	private void showMessage(String msg) {
		System.out.println(ArduinoLogicCaptureDevice.class.getSimpleName() + " # " + msg);
	}

}
