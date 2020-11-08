package com.lembed.lite.studio.qemu.view.internal.swt;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;

public abstract class DeviceBaseView extends JPanel implements IDeviceView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	EmulatorQemuMachineControl eQControl;

	public DeviceBaseView(EmulatorQemuMachineControl fc) {
		eQControl = fc;
	}

	public DeviceBaseView() {
		eQControl = null;
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
