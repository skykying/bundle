package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMSavingControl;
import com.lembed.lite.studio.qemu.model.LastUsedFolderEnumModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class SaveVM implements BaseListener {

	private JContainerView view;
	private FileControl fileControl;
	private VMSavingControl vMSavingControl;
	private LastUsedFolderModel lastUsedFolderModel;
	
	public SaveVM(JContainerView jview, FileControl fileControl) {
		view = jview;
		view.registerListener(this);
		
		
		this.fileControl = fileControl;

		vMSavingControl = null;
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
		if (e.getActionCommand().equals("SaveVM")) {
			if (vMSavingControl == null) {
				vMSavingControl = new VMSavingControl(view.getSelectedPanel());
			} 
			vMSavingControl.setVMSavingViewJPanel(view.getSelectedPanel());

			vMSavingControl.getView().setCurrentDirectory(
					lastUsedFolderModel.getLastUsedFolder(LastUsedFolderEnumModel.SAVEEXISTINGVM.getValor()));

			if (vMSavingControl.chooseFile()) {
				String result = vMSavingControl.getView().getChoice();
				if (result.length() < 5) {
					result += ".xml";
				} else if (!result.substring(result.length() - 4).equals(".xml")) {
					result += ".xml";
				}
				vMSavingControl.setSavedVMPath(result);

				lastUsedFolderModel.setLastUsedFolder(LastUsedFolderEnumModel.SAVEEXISTINGVM.getValor(),
						vMSavingControl.getView().getChooser().getCurrentDirectory().getAbsolutePath());

				fileControl.getFilemodel().saveToXML(vMSavingControl.getSavedVMPath());
			}
		}
	}

}
