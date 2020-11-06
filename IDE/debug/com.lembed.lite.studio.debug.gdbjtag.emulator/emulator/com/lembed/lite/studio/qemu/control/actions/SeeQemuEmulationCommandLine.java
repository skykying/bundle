package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class SeeQemuEmulationCommandLine implements BaseListener {

	private JQemuView view;
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	public SeeQemuEmulationCommandLine(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		emulationControl = null;

	}

	public void starts() {
		view.setVisible(true);
		view.configureStandardMode();
	}

	@Override
	public void actionPerformed(BaseEvent e) {
		doAction(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doAction((BaseEvent) e);
	}

	private void doAction(BaseEvent e) {
		if (e.getActionCommand().equals("SeeQemuEmulationCommandLine")) {
			if (emulationControl == null) {
				emulationControl = new EmulationControl(view);

				if (configurationControl.getQemu_executable_path() != null) {
					emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());
				}

				if (configurationControl.getBios_vga_bios_keymaps_path() != null) {
					emulationControl
							.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());
				}
			} else if (configurationControl != null) {
				if (configurationControl.getQemu_executable_path() != null) {
					emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());
				}

				if (configurationControl.getBios_vga_bios_keymaps_path() != null) {
					emulationControl
							.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());
				}
			}

			if (view.getActivePanel() == 0) {
				view.showMessage(
						"Please, select an open virtual machine tab before seeing the Qemu emulation command line.");
			} else if (!emulationControl.getFullCommandLine(view.getActivePanel()).isEmpty()) {
				view.showMessage("The Qemu emulation command line is:\n"
						+ emulationControl.getFullCommandLine(view.getActivePanel()) + "\n!");
			} else {
				view.showMessage("Sorry! There is no Qemu emulation command line, or it is empty!");
			}
		}
	}

}
