package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class StartEmulation implements BaseListener {

	private JQemuView view;
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public StartEmulation(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		emulationControl = null;
		new ArrayList<VMConfigurationControl>();
		String cls = LastUsedFolderModel.class.getName();
		lastUsedFolderModel = (LastUsedFolderModel) Model.loadUserConfigurationLocally(cls);
		
		cls = LastUsedFileModel.class.getName();
		lastUsedFileModel = (LastUsedFileModel) Model.loadUserConfigurationLocally(cls);
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
		 if (e.getActionCommand().equals("StartEmulation")) {
				if (emulationControl == null) {
					emulationControl = new EmulationControl(view);
				}
				if (configurationControl == null) {
					view.showMessage(
							"Please, configure the required parameters \nbefore starting the emulation with the qemu!");
					configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
				}

				if (configurationControl.getQemu_executable_path() == null) {
					configurationControl.setQemu_executable_path();
				}
				emulationControl.setExecute_before_start_qemu(
						this.JTextAreaToArrayListOfStrings(configurationControl.getExecute_before_start_qemu()));
				if (emulationControl.preruns(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
					view.showMessage("The pre-run script(s) is(are) gone.");
				}
				emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());

				if (configurationControl.getBios_vga_bios_keymaps_path() == null) {
					configurationControl.setBios_vga_bios_keymaps_path();
				}

				emulationControl.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());

				if (view.getActivePanel() == 0) {
					view.showMessage("Please, select an open virtual machine tab before starting the emulation.");
				}
				try {
					if (emulationControl.runs(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
						view.showMessage("If there were no errors so far, so the qemu should be running now!");
					} else if (!configurationControl.getIsConfigured()) {
						configurationControl.do_my_view_visible();
						view.showMessage("The qemu has not been started!\nPlease, check the configuration parameters!"
								+ "\nPlease, configure the path of the qemu executable first!");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
	}

	public List<String> JTextAreaToArrayListOfStrings(JTextArea given) {
		List<String> result = new ArrayList<String>();
		if (given != null) {
			String[] helper = given.getText().split("\n");
			for (int i = 0; i < helper.length; i++) {
				result.add(helper[i]);
			}
		}
		return result;
	}



}
