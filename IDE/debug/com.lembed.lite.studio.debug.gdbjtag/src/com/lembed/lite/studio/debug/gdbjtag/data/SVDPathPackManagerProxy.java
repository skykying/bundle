/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     LEMBED - initial for LiteSTUDIO
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.data;

import com.lembed.lite.studio.debug.core.data.ISVDPathPackManager;

import java.util.Collection;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.DeviceVendor;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.item.ICmsisMapItem;


/**
 * The Class SVDPathPackManagerProxy.
 *
 * @author "Keven Mealamu"
 */
public class SVDPathPackManagerProxy implements ISVDPathPackManager {


	// ------------------------------------------------------------------------

	/** The fg instance. */
	private static SVDPathPackManagerProxy fgInstance;

	/**
	 * Gets the single instance of SVDPathPackManagerProxy.
	 *
	 * @return single instance of SVDPathPackManagerProxy
	 */
	public static SVDPathPackManagerProxy getInstance() {

		if (fgInstance == null) {
			fgInstance = new SVDPathPackManagerProxy();
		}
		return fgInstance;
	}


	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.debug.core.data.ISVDPathPackManager#getSVDAbsolutePathById(java.lang.String, java.lang.String)
	 */
	// ------------------------------------------------------------------------
	@Override
	public IPath getSVDAbsolutePathById(String deviceVendorId, String deviceName) {

		ICpPackManager manager = CpPlugIn.getPackManager();
		ILiteDeviceItem devices = manager.getDevices();
		if(devices == null) {
			return null;
		}
		
		ICmsisMapItem<ILiteDeviceItem> root = new CmsisMapItem<>();
		root.addChild(devices);
		
		ILiteDeviceItem dti = null;
		Collection<? extends ILiteDeviceItem> children = root.getChildren();
		String deviceVendorName = DeviceVendor.getVendorName(deviceVendorId);
		if(children == null) {
			return null;
		}
		
		for(ILiteDeviceItem child : children){			
			dti = child.findItem(deviceName, deviceVendorName, true);
		}
		
		if(dti != null){
			String name = dti.getDevice().getProcessorName();
			ICpDebugConfiguration debugConf = dti.getDevice().getDebugConfiguration(name);
			String svd = debugConf.getSvdFile();
			IPath path =new Path(svd);
			return path;		
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.lembed.lite.studio.debug.core.data.ISVDPathPackManager#getSVDAbsolutePathByName(java.lang.String, java.lang.String)
	 */
	@Override
	public IPath getSVDAbsolutePathByName(String deviceVendorName, String deviceName) {

		CpPlugIn.getPackManager().getCmsisPackRootDirectory();
		ICpPackManager manager = CpPlugIn.getPackManager();
		ILiteDeviceItem devices = manager.getDevices();
		
		ICmsisMapItem<ILiteDeviceItem> root = new CmsisMapItem<>();
		root.addChild(devices);
		
		ILiteDeviceItem dti = null;
		Collection<? extends ILiteDeviceItem> children = root.getChildren();		
		for(ILiteDeviceItem child : children){			
			dti = child.findItem(deviceName, deviceVendorName, true);
		}
		
		if(dti != null){
			String name = dti.getDevice().getProcessorName();
			ICpDebugConfiguration debugConf = dti.getDevice().getDebugConfiguration(name);
			String svd = debugConf.getSvdFile();
			IPath path =new Path(svd);
			return path;		
		}
		
		return null;
	}
	
}
