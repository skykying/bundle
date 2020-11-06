package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.model.UtilitiesModel;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.UtilitiesView;

public class seeQemuVersion implements BaseListener {

	private JQemuView view;
	private ConfigurationControl configurationControl;
	private UtilitiesView utilitiesView;
	private UtilitiesModel utilitiesModel;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public seeQemuVersion(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		new ArrayList<VMConfigurationControl>();
		utilitiesView = null;
		utilitiesModel = null;

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
		if (e.getActionCommand().equals("seeQemuVersion")) {
			if (utilitiesModel == null) {
				try {
					if (configurationControl != null) {
						if (configurationControl.getQemu_executable_path() != null) {
							utilitiesModel = new UtilitiesModel(
									Runtime.getRuntime()
											.exec(configurationControl.getQemu_executable_path().getText()
													+ " -version"),
									utilitiesView,
									getQemuPathDir(configurationControl.getQemu_executable_path().getText()));
							if (!utilitiesModel.inheritIO_Output_Qemu()) {
								while (utilitiesModel.isRunning()) {

								}
								if (!utilitiesModel.readsFile()) {
									utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
								}
							}
						} else {
							view.showMessage("Please, configure the required options first!"
									+ "\nPlease, configure the path of the default Virtual Machines first!");
							configurationControl.do_my_view_visible();
						}
					} else {
						view.showMessage("Please, configure the required options first!"
								+ "\nPlease, configure the path of the default Virtual Machines first!");
						configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					utilitiesModel.setMyprocess(Runtime.getRuntime()
							.exec(configurationControl.getQemu_executable_path().getText() + " -version"));
					utilitiesModel
							.setQemuPathDir(getQemuPathDir(configurationControl.getQemu_executable_path().getText()));
					if (!utilitiesModel.inheritIO_Output_Qemu()) {
						while (utilitiesModel.isRunning()) {

						}
						if (!utilitiesModel.readsFile()) {
							utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}



	private String getQemuPathDir(String qemuPath) {
		String extension = checks_extension(qemuPath);
		int position = qemuPath.lastIndexOf(extension);
		return qemuPath.substring(0, position);
	}

	private String checks_extension(String path) {
		String result = "";
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '/') {
				result = "/";
				break;
			}
			if (path.charAt(i) == '\\') {
				result = "\\";
				break;
			}
		}
		return result;
	}

}
