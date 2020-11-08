package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.DiskCreationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class CreateNewDiskImageFile implements BaseListener {

	private JSwtQemuView view;
	private ConfigurationControl configurationControl;
	private DiskCreationControl diskCreationControl;

	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public CreateNewDiskImageFile(JSwtQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		diskCreationControl = null;

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
		doAction((ActionEvent) e);
	}


	private void doAction(ActionEvent e) {
		if (e.getActionCommand().equals("CreateNewDiskImageFile")) {
			if (diskCreationControl == null) {
				diskCreationControl = new DiskCreationControl("0", this);
			}
			
			if (configurationControl == null) {
				view.showMessage("Please, configure the required options first!"
						+ "\nPlease, configure the path of the default Virtual Machines first!");
				configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
			} 
			
			if (configurationControl.getDefault_virtual_machines_path().isEmpty()) {
				view.showMessage("Please, configure the path of the default Virtual Machines first!");
				configurationControl.do_my_view_visible();
			} else {
				diskCreationControl
						.setDefault_virtual_machines_path(configurationControl.getDefault_virtual_machines_path());
				if (configurationControl.getQemu_img_executable_path() == null) {
					view.showMessage("Please, configure the path of the qemu-img executable first!");
					configurationControl.do_my_view_visible();
				} else {
					diskCreationControl.setPathQemu_img(configurationControl.getQemu_img_executable_path().getText());
					diskCreationControl.change_visibility(true);
				}

			}
		} 
	}


}
