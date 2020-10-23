package org.panda.logicanalyzer.ui.pipeline;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IFilter;
import org.panda.logicanalyzer.core.pipeline.IPipeline;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Executes a pipeline and displays the progress using the progress monitor
 *
 */
public class ProgressMonitorPipelineExecutor implements IRunnableWithProgress {

	/**
	 * The pipeline we're about to execute
	 */
	private final IPipeline pipeline;

	public ProgressMonitorPipelineExecutor(IPipeline pipeline) {
		this.pipeline = pipeline;
	}

	/**
     * Dumps the specified device and its sub devices.
     * 
     * @param device
     *            The USB device to dump.
     * @param level
     *            The indentation level.
     */
    public static void dump(UsbDevice device, int level)
    {
        for (int i = 0; i < level; i += 1) {
            System.out.print("  ");
        }
        
        //System.out.println(device);
        if (device.isUsbHub())
        {
            final UsbHub hub = (UsbHub) device;
            for (UsbDevice child: (List<UsbDevice>) hub.getAttachedUsbDevices())
            {
            	
                dump(child, level + 1);
            }
        }else {
        	try {
				dumpName(device);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (UsbException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
        }
    }
    
    private static void dumpName(final UsbDevice device)
            throws UnsupportedEncodingException, UsbException
        {
            // Read the string descriptor indices from the device descriptor.
            // If they are missing then ignore the device.
            final UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            final byte iManufacturer = desc.iManufacturer();
            final byte iProduct = desc.iProduct();
            if (iManufacturer == 0 || iProduct == 0) return;

            // Dump the device name
            System.out.println(device.getString(iManufacturer) + " & "
                + device.getString(iProduct));
        }
    

    public static void dusb() throws UsbException
    {
        UsbServices services = UsbHostManager.getUsbServices();
        dump(services.getRootUsbHub(), 0);
    }
    
	//@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		monitor.beginTask("Executing pipeline", IProgressMonitor.UNKNOWN);
		
		try {
			dusb();
		} catch (UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			if (pipeline == null) {
				showMessage("pipeline is null");
			}

			showMessage("run stage 1");

			/**
			 * the DataPacket contain many frames captured by hardware, and transfered by GNU serial port library(CDC),
			 * the getSource of the pipeline just get the DataSource instance init by source select wizard dialog,
			 * the run method will use CommPort dirver to established the hardware and use the instanced device "capture"
			 * method the pipeline the data, once the hardware "done" flag triggerd, the "run" method will return.
			 */
			IDataPacket packet = pipeline.getSource().run();

			showMessage("run stage 2");

			/**
			 * middleware to do the filter on the pipeline
			 */
			for (IFilter filter : pipeline.getFilter()) {
				packet = filter.process(packet);
				showMessage("filter = " + filter.toString());
			}

			showMessage("run stage 3 " + pipeline.getSinks().size());

			/**
			 * the DataSink will pull the data and draw it on special editpart.
			 * the DataSink accept method will keep the data and open editpart and draw the data on it.
			 */
			for (IDataSink sink : pipeline.getSinks()) {

				if (sink == null) {
					showMessage("sink is null");
				} else {
					sink.accept(packet);
				}

				showMessage("sink = " + sink.getClass().getSimpleName());
			}

			showMessage("run stage 4");

		} catch (CoreException e) {
			showMessage("run stage 5");
			throw new InvocationTargetException(e);
		} finally {
			monitor.done();
		}
	}

	private void showMessage(String msg) {
		System.out.println(ProgressMonitorPipelineExecutor.class.getSimpleName() + " # " + msg);
	}

}
