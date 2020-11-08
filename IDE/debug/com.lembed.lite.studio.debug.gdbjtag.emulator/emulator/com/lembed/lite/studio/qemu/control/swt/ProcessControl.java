package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.ProcessModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.EmulationView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class ProcessControl implements BaseControl {

	private ProcessModel processModel;
	private EmulationView emulationView;

	public ProcessControl(Process myProcess, String machineName, EmulationView view, String qemuPathDir) {
		this.emulationView = view;
		this.processModel = new ProcessModel(myProcess, machineName, qemuPathDir, view);
	}

	public ProcessModel getMyModel() {
		return this.processModel;
	}

	public String getMachineName() {
		return this.processModel.getMachineName();
	}

	public EmulationView getMyView() {
		return emulationView;
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(emulationView);
		
		return list;
	}

	public void run() throws InterruptedException {
		this.processModel.runMaster();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
