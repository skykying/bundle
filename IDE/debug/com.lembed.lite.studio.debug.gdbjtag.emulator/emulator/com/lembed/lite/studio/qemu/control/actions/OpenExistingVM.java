package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.control.VMOpeningControl;
import com.lembed.lite.studio.qemu.model.LastUsedFolderEnumModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class OpenExistingVM implements BaseListener {

	private JQemuView view;
	private EmulationControl emulationControl;
	private FileControl fileControl;
	private VMOpeningControl vMOpeningControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	private LastUsedFolderModel lastUsedFolderModel;
	
	public OpenExistingVM(JQemuView jview) {
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
		doAction((BaseEvent) e);
	}


	private void doAction(BaseEvent e) {
		if (e.getActionCommand().equals("OpenExistingVM")) {
			if (emulationControl == null) {
				emulationControl = new EmulationControl(view);
			}
			if (fileControl == null) {
				fileControl = new FileControl(view.getMyUntitledJPanel(), view);
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
				if (fileControl.getFilemodel().readXML(fileControl.getFileview().getChoice())) {
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
								new VMConfigurationControl(emulationControl, view, fileControl));
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
