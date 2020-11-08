package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.VMOpeningModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.VMOpeningView;

public class VMOpeningControl implements BaseControl {

	private VMOpeningModel mymodel;
	private VMOpeningView myview;

	public VMOpeningControl(JSwtQemuView view, EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
		this.mymodel = new VMOpeningModel(myemulation, myfile);
		this.myview = new VMOpeningView(view, this.mymodel.getMyfile().getMachineModel().getMachineName());
	}

	public boolean starts(EmulatorQemuMachineControl myfile) {
		this.mymodel.starts();
		this.myview.setChosenMachineName(myfile.getMachineModel().getMachineName());
		this.myview.starts(this.mymodel.getDiskImagePath(), myfile.getMachineModel().getSecondHardDiskOption(),
				myfile.getMachineModel().getThirdHardDiskOption(), myfile.getMachineModel().getFourthHardDiskOption(),
				myfile.getMachineModel().getRamSize());
		return true;
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(myview);
		
		return list;
	}

	public void setView(JSwtQemuView view) {
		this.myview.setView(view);
	}

	public void setMyemulation(EmulationControl myemulation) {
		this.mymodel.setMyemulation(myemulation);
	}

	public void setMyfile(EmulatorQemuMachineControl myfile) {
		this.mymodel.setMyfile(myfile);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
