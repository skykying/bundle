package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.VMConfigurationControl;
import com.lembed.lite.studio.qemu.control.swt.VMOpeningControl;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderEnumModel;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.swt.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class OpenExistingVM implements BaseListener {

	private JSwtQemuView view;
	private EmulationControl emulationControl;
	private EmulatorQemuMachineControl fileControl;
	private VMOpeningControl vMOpeningControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	private LastUsedFolderModel lastUsedFolderModel;
	
	public OpenExistingVM(JSwtQemuView jview) {
		view = jview;
		view.registerListener(this);
		
		emulationControl = null;
		fileControl = null;
		vMOpeningControl = null;
		
		vMConfigurationControlist = new ArrayList<VMConfigurationControl>();
		String cls = LastUsedFolderModel.class.getName();
		lastUsedFolderModel = (LastUsedFolderModel) Model.loadUserConfigurationLocally(cls);

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
		if (e.getActionCommand().equals("OpenExistingVM")) {
			if (emulationControl == null) {
				emulationControl = new EmulationControl();
			}
			if (fileControl == null) {
				fileControl = new EmulatorQemuMachineControl();
			}
			
			if (vMOpeningControl == null) {
				vMOpeningControl = new VMOpeningControl(view, emulationControl, fileControl);
			}
			
			fileControl.getFileview().setFileExtension(".xml");
			fileControl.getFileview().setChoosertitle("Choose an existing JavaQemu VM file!");
			fileControl.getFileview().setFileDescription("XML - JavaQemu VM Files");
			fileControl.getFileview().setCurrentDirectory(
					lastUsedFolderModel.getLastUsedFolder(LastUsedFolderEnumModel.OPENEXISTINGVM.getValor()));
			if (fileControl.getFileview().chooseFile()) {
				if (fileControl.getMachineModel().readXML(fileControl.getFileview().getChoice())) {
					lastUsedFolderModel.setLastUsedFolder(LastUsedFolderEnumModel.OPENEXISTINGVM.getValor(),
							fileControl.getFileview().getChooser().getCurrentDirectory().getAbsolutePath());

					vMOpeningControl.starts(fileControl);
					int position;
					if (view.getActivePanel() == 0) {
						position = view.getSizeOfJTabbedPane();
					} else {
						position = view.getActivePanel();
					}
					if (vMConfigurationControlist.size() <= position) {
						for (int i = vMConfigurationControlist.size(); i <= position; i++) {
							vMConfigurationControlist.add(i, null);
						}
					}
					if (vMConfigurationControlist.get(position) == null) {
						vMConfigurationControlist.set(position,
								new VMConfigurationControl(emulationControl, fileControl));
						vMConfigurationControlist.get(position).starts();
						vMConfigurationControlist.get(position).getMyName().updateMe();
					}
				} else {
					view.showMessage("Please, select a valid file (*.xml)!");
				}
			}
		}
		
	}




}
