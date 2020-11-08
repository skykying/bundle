package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.FloppyModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.FloppyView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class FloppyControl implements BaseControl {

	private FloppyModel floppyModel;
	private FloppyView floppyView;
	private EmulatorQemuMachineControl eQctl;

	public FloppyControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
		this.floppyModel = new FloppyModel(ec, emc);
		this.floppyView = new FloppyView(emc);
		this.eQctl = emc;
		if (emc.getMachineModel().getFloppyDiskA() != null) {
			this.floppyModel.setFloppyDiskAOption(this.floppyView.getFloppyDiskAText().getText(), "");
		}
		if (emc.getMachineModel().getFloppyDiskB() != null) {
			this.floppyModel.setFloppyDiskBOption(this.floppyView.getFloppyDiskBText().getText(), "");
		}
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(floppyView);
		
		return list;
	}

	public void starts() {
		this.floppyView.configureListener(this);
		this.floppyView.configureStandardMode();
	}

	public void setVisible(Boolean value) {
		this.floppyView.setVisible(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {

			this.floppyView.getFloppyDiskAText().setText("");
			this.floppyModel.setFloppyDiskAOption("", "image");
			this.eQctl.getMachineModel().setFloppyDiskA("");

			this.floppyView.getFloppyDiskBText().setText("");
			this.floppyModel.setFloppyDiskBOption("", "image");
			this.eQctl.getMachineModel().setFloppyDiskB("");

			this.floppyView.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			if (!this.floppyView.getFloppyDiskAText().getText().equals(this.floppyView.getChoice())) {
				this.floppyModel.setFloppyDiskAOption(this.floppyView.getFloppyDiskAText().getText(), "");
				this.eQctl.getMachineModel().setFloppyDiskA(this.floppyView.getFloppyDiskAText().getText());
			}
			if (!this.floppyView.getFloppyDiskBText().getText().equals(this.floppyView.getChoice())) {
				this.floppyModel.setFloppyDiskBOption(this.floppyView.getFloppyDiskBText().getText(), "");
				this.eQctl.getMachineModel().setFloppyDiskB(this.floppyView.getFloppyDiskBText().getText());
			}
			this.floppyView.setVisible(false);
		} else if (e.getActionCommand().equals("floppyDiskADriveSelection")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskAText().setText(this.floppyView.getChoice());
				this.floppyModel.setFloppyDiskAOption(this.floppyView.getChoice(), "drive");
				this.eQctl.getMachineModel().setFloppyDiskA(this.floppyView.getChoice());
			}
		} else if (e.getActionCommand().equals("floppyDiskAImageSelection")) {
			if (this.floppyView.chooseFloppyFiles()) {
				this.floppyView.getFloppyDiskAText().setText(this.floppyView.getChoice());
				this.floppyModel.setFloppyDiskAOption(this.floppyView.getChoice(), "image");
				this.eQctl.getMachineModel().setFloppyDiskA(this.floppyView.getChoice());
			}
		} else if (e.getActionCommand().equals("floppyDiskAReadOnlyVirtualFAT")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskAText().setText(this.convertFatReadOnly(this.floppyView.getChoice()));
				this.floppyModel.setFloppyDiskAOption(this.convertFatReadOnly(this.floppyView.getChoice()), "drive");
				this.eQctl.getMachineModel().setFloppyDiskA(this.convertFatReadOnly(this.floppyView.getChoice()));
			}
		} else if (e.getActionCommand().equals("floppyDiskAReadWriteVirtualFAT")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskAText().setText(this.convertFatReadWrite(this.floppyView.getChoice()));
				this.floppyModel.setFloppyDiskAOption(this.convertFatReadWrite(this.floppyView.getChoice()), "drive");
				this.eQctl.getMachineModel().setFloppyDiskA(this.convertFatReadWrite(this.floppyView.getChoice()));
			}
		} else if (e.getActionCommand().equals("floppyDiskBDriveSelection")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskBText().setText(this.floppyView.getChoice());
				this.floppyModel.setFloppyDiskBOption(this.floppyView.getChoice(), "drive");
				this.eQctl.getMachineModel().setFloppyDiskB(this.floppyView.getChoice());
			}
		} else if (e.getActionCommand().equals("floppyDiskBImageSelection")) {
			if (this.floppyView.chooseFloppyFiles()) {
				this.floppyView.getFloppyDiskBText().setText(this.floppyView.getChoice());
				this.floppyModel.setFloppyDiskBOption(this.floppyView.getChoice(), "image");
				this.eQctl.getMachineModel().setFloppyDiskB(this.floppyView.getChoice());
			}
		} else if (e.getActionCommand().equals("floppyDiskBReadOnlyVirtualFAT")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskBText().setText(this.convertFatReadOnly(this.floppyView.getChoice()));
				this.floppyModel.setFloppyDiskBOption(this.convertFatReadOnly(this.floppyView.getChoice()), "drive");
				this.eQctl.getMachineModel().setFloppyDiskB(this.convertFatReadOnly(this.floppyView.getChoice()));
			}
		} else if (e.getActionCommand().equals("floppyDiskBReadWriteVirtualFAT")) {
			if (this.floppyView.chooseFloppyDrives()) {
				this.floppyView.getFloppyDiskBText().setText(this.convertFatReadWrite(this.floppyView.getChoice()));
				this.floppyModel.setFloppyDiskBOption(this.convertFatReadWrite(this.floppyView.getChoice()), "drive");
				this.eQctl.getMachineModel().setFloppyDiskB(this.convertFatReadWrite(this.floppyView.getChoice()));
			}
		}
	}

	public String convertFatReadOnly(String basis) {
		return "fat:floppy:" + basis;
	}

	public String convertFatReadWrite(String basis) {
		return "fat:floppy:rw:" + basis;
	}
}
