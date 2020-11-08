package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.swt.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.swt.DiskCreationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.VMConfigurationControl;
import com.lembed.lite.studio.qemu.control.swt.VMCreationControl;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.swt.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class CreateNewVM implements BaseListener {

	private JSwtQemuView view;
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	private VMCreationControl vMCreationControl;
	private DiskCreationControl diskCreationControl;
	private EmulatorQemuMachineControl fileControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public CreateNewVM(JSwtQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		emulationControl = null;
		vMCreationControl = null;
		diskCreationControl = null;
		fileControl = null;
		vMConfigurationControlist = new ArrayList<VMConfigurationControl>();
		
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
		if (e.getActionCommand().equals("CreateNewVM")) {
			if (fileControl == null) {
				fileControl = new EmulatorQemuMachineControl();
			}
			if (emulationControl == null) {
				emulationControl = new EmulationControl();
			}
			if (configurationControl == null) {
				JSwtQemuView.showMessage("Please, configure the required options first!\n"
						+ "Please, configure the path of the qemu-img executable first!");
				configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
			} 
			
			if (configurationControl.getQemu_img_executable_path() == null
					|| configurationControl.getQemu_img_executable_path().getText().isEmpty()) {
				JSwtQemuView.showMessage("Please, configure the path of the qemu-img executable first!");
				configurationControl.setVisibleEnable();

			} 
			
			if (configurationControl.getDefault_virtual_machines_path().isEmpty()) {
				JSwtQemuView.showMessage("Please, configure the path of the default Virtual Machines first!");
				configurationControl.setVisibleEnable();
			} 
			
			vMCreationControl = new VMCreationControl(diskCreationControl,
						configurationControl.getQemu_img_executable_path().getText(),
						configurationControl.getDefault_virtual_machines_path(), view, fileControl, emulationControl,
						vMConfigurationControlist);
			vMCreationControl.starts();
			
		}
		
	}



}
