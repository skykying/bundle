package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.RAMModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class RAMControl implements BaseControl {

	private RAMModel ramModel;

	public RAMControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
		this.ramModel = new RAMModel(ec, emc);
	}

	public void setOption(String option) {
		this.ramModel.setOption(option);
	}

	public String getOption() {
		return this.ramModel.getOption();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
	
		
		return list;
	}
}
