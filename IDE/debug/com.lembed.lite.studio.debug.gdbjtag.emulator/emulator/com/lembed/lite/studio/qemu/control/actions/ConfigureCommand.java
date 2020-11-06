package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class ConfigureCommand implements BaseListener {

	private JQemuView view;
	private ConfigurationControl configurationControl;
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;

	public ConfigureCommand(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;

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
		
		 if (e.getActionCommand().equals("ConfigureCommand")) {
			if (configurationControl == null) {
				configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
			} else {
				configurationControl.do_my_view_visible();
			}
		}

	}



}