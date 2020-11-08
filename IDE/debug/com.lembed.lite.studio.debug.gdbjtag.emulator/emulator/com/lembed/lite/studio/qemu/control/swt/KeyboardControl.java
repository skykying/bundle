package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.KeyboardModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.KeyboardView;

public class KeyboardControl implements BaseControl {

	private KeyboardModel keyboardModel;
	private KeyboardView keyboardView;

	public KeyboardControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
		this.keyboardView = new KeyboardView(emc);
		this.keyboardView.configureListener(this);
		this.keyboardView.configureStandardMode();
		this.keyboardModel = new KeyboardModel(ec, emc);
	}

	public void setVisible(boolean value) {
		this.keyboardView.setVisible(value);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(keyboardView);
		
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] topts = VMConfigurationModel.getTagsOptions();

		if (e.getActionCommand().equals("eraseButton")) {

			if (this.keyboardView.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
				this.keyboardView.getKeyboardLayoutLanguage().setSelectedIndex(0);
			}

			this.keyboardModel.unsetOption(topts[OptionsEnumModel.KEYBOARDOPTION.getValor()]);

			this.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			int i = keyboardView.getKeyboardLayoutLanguage().getSelectedItem().toString().indexOf(":") + 2;
			String sel = this.keyboardView.getKeyboardLayoutLanguage().getSelectedItem().toString().substring(i);

			if (this.keyboardView.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
				keyboardModel.setOption(sel);
			} else {
				this.keyboardModel.unsetOption(topts[OptionsEnumModel.KEYBOARDOPTION.getValor()]);
			}

			this.setVisible(false);
		}
	}

}
