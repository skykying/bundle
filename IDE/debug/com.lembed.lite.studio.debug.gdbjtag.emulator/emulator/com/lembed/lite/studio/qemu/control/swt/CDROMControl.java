package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.CDROMModel;
import com.lembed.lite.studio.qemu.view.internal.swt.CDROMView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class CDROMControl implements BaseControl {

	private CDROMModel cdRomModel;
	private CDROMView cdRomView;
	private EmulatorQemuMachineControl fileControl;

	public CDROMControl(EmulationControl myemulation, EmulatorQemuMachineControl emc) {
		cdRomModel = new CDROMModel(myemulation, emc);
		cdRomView = new CDROMView(emc);
		fileControl = emc;
		if (fileControl.getMachineModel().getCdrom() != null) {
			cdRomModel.setCdromOption(cdRomView.getCdromText().getText(), "");
		}
	}

	public void starts() {
		cdRomView.configureListener(this);
		cdRomView.configureStandardMode();
	}

	public void setVisible(Boolean value) {
		cdRomView.setVisible(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {
			if (!cdRomView.getCdromText().getText().isEmpty()) {
				cdRomView.getCdromText().setText("");
			}

			cdRomModel.setCdromOption("", "image");
			fileControl.getMachineModel().setCdrom("");

			cdRomView.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			if (!cdRomView.getCdromText().getText().equals(cdRomView.getChoice())) {
				cdRomModel.setCdromOption(cdRomView.getCdromText().getText(), "");
				fileControl.getMachineModel().setCdrom(cdRomView.getCdromText().getText());
			}
			cdRomView.setVisible(false);
		} else if (e.getActionCommand().equals("cdromDriveSelection")) {
			if (cdRomView.chooseCDROMDrives()) {
				cdRomView.getCdromText().setText(cdRomView.getChoice());
				cdRomModel.setCdromOption(cdRomView.getChoice(), "drive");
				fileControl.getMachineModel().setCdrom(cdRomView.getChoice());
			}
		} else if (e.getActionCommand().equals("diskImageSelection")) {
			if (cdRomView.chooseCDROMFiles()) {
				cdRomView.getCdromText().setText(cdRomView.getChoice());
				cdRomModel.setCdromOption(cdRomView.getChoice(), "image");
				fileControl.getMachineModel().setCdrom(cdRomView.getChoice());
			}
		}
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {

		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(cdRomView);
		
		return list;
	}
}
