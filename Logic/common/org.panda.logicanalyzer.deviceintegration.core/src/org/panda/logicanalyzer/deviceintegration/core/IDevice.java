package org.panda.logicanalyzer.deviceintegration.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

import org.eclipse.core.runtime.CoreException;

/**
 * Classes implementing this interface serve as devices, thus providing
 * functionality which is integrated into the logic analyzer.
 *
 *
 *
 */
public interface IDevice {

	/**
	 * Initializes the device. If the configuration should be invalid, a core
	 * exception may be thrown.
	 *
	 * @param configuration The configuration of the device
	 */
	public void initialize(IDeviceConfiguration configuration) throws CoreException;

	/**
	 * Starts the capturing progress. This method must block until the complete
	 * data is received (including waiting for a possible trigger).
	 *
	 * @param in The input stream coming from the device
	 * @param out The output stream going to the device
	 * @return The captured data packet
	 * @throws CoreException In case of a problem
	 * @throws IOException An error occured during device communication
	 */
	public IDataPacket capture(InputStream in, OutputStream out) throws CoreException, IOException;

}
