package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.KernelBootModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.SpecificBootView;

public class SpecificBootControl implements BaseControl {

	private SpecificBootView myview;
	private KernelBootModel mykernel;

	public SpecificBootControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
		this.myview = new SpecificBootView(myfile);
		this.myview.configureListener(this);
		this.myview.configureStandardMode();
		this.mykernel = new KernelBootModel(myemulation, myfile);
	}

	public void setVisible(boolean value) {
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
		if (e.getActionCommand().equals("eraseButton")) {
			if (!this.myview.getKernel().getText().isEmpty()) {
				this.myview.getKernel().setText("");
			}
			this.mykernel
					.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KERNELBOOTOPTION.getValor()]);
			this.myview.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			if (!this.myview.getKernel().getText().isEmpty()) {
				this.mykernel.setOption(this.myview.getKernel().getText());
			} else {
				this.mykernel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KERNELBOOTOPTION.getValor()]);
			}
			this.myview.setVisible(false);
		} else if (e.getActionCommand().equals("chooseKernel")) {
			this.myview.setChoosertitle("Choose a file as kernel image!");
			this.myview.setFileDescription("File - Kernel Image");
			if (this.myview.chooseAnyFile()) {
				this.myview.getKernel().setText(this.myview.getChoice());
			}
		}
	}

}
