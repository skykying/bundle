package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.model.VMSavingModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.VMSavingView;

public class VMSavingControl implements BaseControl {

	private VMSavingModel vMSavingModel;

	private VMSavingView vMSavingView;

	public VMSavingControl(JPanel jPanel) {
		this.vMSavingModel = new VMSavingModel();
		this.vMSavingView = new VMSavingView(jPanel);
	}

	public String getSavedVMPath() {
		return this.vMSavingModel.getSavedVMPath();
	}

	public void setSavedVMPath(String savedVMPath) {
		this.vMSavingModel.setSavedVMPath(savedVMPath);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(vMSavingView);
		
		return list;
	}

	public boolean chooseFile() {
		return this.vMSavingView.chooseFile();
	}

	public VMSavingView getView() {
		return vMSavingView;
	}

	public void setVMSavingViewJPanel(JPanel jPanel) {
		this.vMSavingView.setJpanel(jPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
