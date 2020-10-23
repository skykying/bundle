package org.panda.logicanalyzer.deviceintegration.internal;


/**
 * http://usb4java.org to replace the serial port
 */
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.deviceintegration.core.IDevice;
import org.panda.logicanalyzer.deviceintegration.core.IDeviceConfiguration;

/**
 * The device data source uses an {@link IDevice} to capture data.
 *
 */
public class ComDeviceDataSource implements IDataSource {

	/**
	 * This factory creates new {@link ComDeviceDataSource}s.
	 */
	public static class DeviceDataSourceFactory implements IDataSourceFactory {

		@Override
		public IDataSource createSource(Map<String, Object> configuration) throws CoreException {
		
			
			if (!(configuration.get("device") instanceof IDevice)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Device is mandatory in data source configuration");
				throw new CoreException(status);
			}
			if (!(configuration.get("configuration") instanceof IDeviceConfiguration)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Device configuration is mandatory in data source configuration");
				throw new CoreException(status);
			}
			if (!(configuration.get("portName") instanceof String)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Port name is mandatory in data source configuration");
				throw new CoreException(status);
			}
			if (!(configuration.get("baudRate") instanceof Integer)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Baud rate is mandatory in data source configuration");
				throw new CoreException(status);
			}

			IDevice device = (IDevice)configuration.get("device");
			device.initialize((IDeviceConfiguration) configuration.get("configuration"));

			return new ComDeviceDataSource(device, (String)configuration.get("portName"), (Integer)configuration.get("baudRate"));
		}

	}


	/**
	 * The device we're using to capture the data
	 */
	private final IDevice device;

	/**
	 * The name of the port the device is connected to
	 */
	private final String portName;

	/**
	 * The baud rate of the device connection
	 */
	private final int baudRate;


	public ComDeviceDataSource(IDevice device, String portName, int baudRate) {
		this.device = device;
		this.portName = portName;
		this.baudRate = baudRate;
	}

    
	@Override
	public IDataPacket run() throws CoreException {


		CommPortIdentifier portId;
		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Port (" + portName + ") does not exist");
			throw new CoreException(status);
		}

		if (portId == null) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unknown port " + portName);
			throw new CoreException(status);
		}

		try {
			SerialPort port = (SerialPort) portId.open(portName, 1000);
			port.setSerialPortParams(
			    baudRate,
			    SerialPort.DATABITS_8,
			    SerialPort.STOPBITS_1,
			    SerialPort.PARITY_NONE
			);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN);
			port.disableReceiveFraming();
			port.enableReceiveTimeout(100);

			InputStream inputStream = port.getInputStream();
			OutputStream outputStream = port.getOutputStream();

			try {
				showMessage("try to capture data from device ");
				return device.capture(inputStream, outputStream);
			} finally {
				port.close();
			}
		} catch (PortInUseException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Port (" + portName + ") is already in use", e);
			throw new CoreException(status);
		} catch (UnsupportedCommOperationException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unsupported operation", e);
			throw new CoreException(status);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while communicating with device", e);
			throw new CoreException(status);
		}

	}

	private void showMessage(String msg) {
		System.out.println(ComDeviceDataSource.class.getSimpleName() + " # " + msg);
	}

}
