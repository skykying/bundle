package com.lembed.lite.studio.qemu.view.internal.swt;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;

public abstract class DeviceBaseView extends JPanel implements IDeviceView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	FileControl fileControl;
	
	public DeviceBaseView(FileControl fc) {
		fileControl = fc;
	}

	public DeviceBaseView() {
		fileControl = null;
	}
	
	@Override
	public void setTitle(String string) {
		title = string;
	}

	@Override
	public String getTitle() {
		return title;
	}


}
