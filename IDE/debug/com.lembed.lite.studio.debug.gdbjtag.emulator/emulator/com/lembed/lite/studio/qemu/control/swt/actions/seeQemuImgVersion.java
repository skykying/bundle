package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.model.UtilitiesModel;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.UtilitiesView;

public class seeQemuImgVersion implements BaseListener {

	private JContainerView view;
	private ConfigurationControl configurationControl;
	private UtilitiesView utilitiesView;
	private UtilitiesModel utilitiesModel;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public seeQemuImgVersion(JContainerView jview, UtilitiesView utilitiesView) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;

		this.utilitiesView = utilitiesView;
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
		doAction(e);
	}

	private void doAction(ActionEvent e) {
		if (e.getActionCommand().equals("seeQemuImgVersion")) {
			if (utilitiesModel == null) {
				try {
					if (configurationControl != null) {
						if (configurationControl.getQemu_img_executable_path() != null) {
							utilitiesModel = new UtilitiesModel(
									Runtime.getRuntime()
											.exec(configurationControl.getQemu_img_executable_path().getText()),
									utilitiesView, null);
							if (!utilitiesModel.inheritIO_Output_QemuImg()) {
								while (utilitiesModel.isRunning()) {

								}
								utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
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
					utilitiesModel.setMyprocess(
							Runtime.getRuntime().exec(configurationControl.getQemu_img_executable_path().getText()));
					utilitiesModel.setQemuPathDir(null);
					if (!utilitiesModel.inheritIO_Output_QemuImg()) {
						while (utilitiesModel.isRunning()) {

						}
						utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}



}
