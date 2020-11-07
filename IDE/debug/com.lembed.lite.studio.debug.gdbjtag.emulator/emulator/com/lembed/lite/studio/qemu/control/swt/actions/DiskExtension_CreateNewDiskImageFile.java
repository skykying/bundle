package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.DiskCreationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class DiskExtension_CreateNewDiskImageFile implements BaseListener {

	private JContainerView view;
	private DiskCreationControl diskCreationControl;
	public DiskExtension_CreateNewDiskImageFile(JContainerView jview, DiskCreationControl diskCreationControl) {
		view = jview;
		view.registerListener(this);
		this.diskCreationControl = diskCreationControl;
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
		if (e.getActionCommand().equals("DiskExtension_CreateNewDiskImageFile")) {
			diskCreationControl.addsComponent((String) diskCreationControl.getDiskExtension().getSelectedItem());
		}
		
	}



}
