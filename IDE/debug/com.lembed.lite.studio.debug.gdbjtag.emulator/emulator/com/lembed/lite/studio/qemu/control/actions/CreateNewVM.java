package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.DiskCreationControl;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.control.VMCreationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class CreateNewVM implements BaseListener {

	private JQemuView view;
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	private VMCreationControl vMCreationControl;
	private DiskCreationControl diskCreationControl;
	private FileControl fileControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public CreateNewVM(JQemuView jview) {
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
		doAction((BaseEvent) e);
	}


	private void doAction(BaseEvent e) {
		if (e.getActionCommand().equals("CreateNewVM")) {
			if (fileControl == null) {
				fileControl = new FileControl(view.getMyUntitledJPanel(), view);
			}
			if (emulationControl == null) {
				emulationControl = new EmulationControl(view);
			}
			if (configurationControl == null) {
				view.showMessage("Please, configure the required options first!\n"
						+ "Please, configure the path of the qemu-img executable first!");
				configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
			} 
			
			if (configurationControl.getQemu_img_executable_path() == null
					|| configurationControl.getQemu_img_executable_path().getText().isEmpty()) {
				view.showMessage("Please, configure the path of the qemu-img executable first!");
				configurationControl.do_my_view_visible();

			} 
			
			if (configurationControl.getDefault_virtual_machines_path().isEmpty()) {
				view.showMessage("Please, configure the path of the default Virtual Machines first!");
				configurationControl.do_my_view_visible();
			} 
			
			vMCreationControl = new VMCreationControl(diskCreationControl,
						configurationControl.getQemu_img_executable_path().getText(),
						configurationControl.getDefault_virtual_machines_path(), view, fileControl, emulationControl,
						vMConfigurationControlist);
			vMCreationControl.starts();
			
		}
		
	}



}
