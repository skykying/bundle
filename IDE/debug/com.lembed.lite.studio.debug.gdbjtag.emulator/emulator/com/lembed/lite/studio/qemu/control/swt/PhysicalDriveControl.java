package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.PhysicalDriveModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.PhysicalDriveView;

public class PhysicalDriveControl implements BaseControl {

	private PhysicalDriveView myview;
	private PhysicalDriveModel mymodel;
	private HardDiskControl myhd;
	private int position;

	public PhysicalDriveControl(HardDiskControl myhd, EmulatorQemuMachineControl myfile, int position) {
		this.myview = new PhysicalDriveView(myfile, position);
		this.myview.configureListener(this);
		this.myview.configureStandardMode();
		this.mymodel = new PhysicalDriveModel();
		this.myhd = myhd;
		this.position = position;
	}

	public void change_my_visibility(boolean value) {
		this.myview.setVisible(value);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(myview);
		
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancelButton")) {
			this.mymodel.unsetOption();
			this.myhd.setPhysicalDriveChoice(position, this.mymodel.getOption());
			this.change_my_visibility(false);
		} else if (e.getActionCommand().equals("okButton")) {
			this.mymodel.setOption((String) this.myview.getPhysicalDriveNumber().getSelectedItem());
			this.myhd.setPhysicalDriveChoice(position, this.mymodel.getOption());
			this.change_my_visibility(false);
		}
	}
}
