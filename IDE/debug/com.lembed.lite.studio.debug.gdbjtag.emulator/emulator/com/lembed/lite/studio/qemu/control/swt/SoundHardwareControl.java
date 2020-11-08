package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.SoundHardwareModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.SoundHardwareView;

public class SoundHardwareControl implements BaseControl {

	private SoundHardwareModel mymodel;
	private SoundHardwareView myview;

	public SoundHardwareControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
		this.myview = new SoundHardwareView(myfile);
		this.myview.configureListener(this);
		this.myview.configureStandardMode();
		this.mymodel = new SoundHardwareModel(myemulation, myfile);
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

			if (this.myview.getSoundHardware().getSelectedIndex() != 0) {
				this.mymodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SOUNDHARDWAREOPTION.getValor()]);
				this.myview.getSoundHardware().setSelectedIndex(0);
			}

			this.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {

			if (this.myview.getSoundHardware().getSelectedIndex() != 0) {
				this.mymodel.setOption((String) this.myview.getSoundHardware().getSelectedItem());
			} else {
				this.mymodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SOUNDHARDWAREOPTION.getValor()]);
			}

			this.setVisible(false);
		}
	}

}
