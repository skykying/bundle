package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import com.lembed.lite.studio.qemu.control.DiskCreationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class OK_CreateNewDiskImageFile implements BaseListener {

	private JQemuView view;
	private DiskCreationControl diskCreationControl;
	public OK_CreateNewDiskImageFile(JQemuView jview, DiskCreationControl diskCreationControl) {
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
		if (e.getActionCommand().equals("OK_CreateNewDiskImageFile")) {
			diskCreationControl.setSizeMB(diskCreationControl.getDiskImageSize());
			diskCreationControl.setFileName(diskCreationControl.getDiskName().getText(),
					(String) diskCreationControl.getDiskExtension().getSelectedItem());
			try {
				diskCreationControl.runsThisIfTrue();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				diskCreationControl.showsOutput();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			if (diskCreationControl.getMessage().contains("Formatting")
					|| diskCreationControl.getMessage().contains("Creating")) {
				view.showMessage("The disk image was created!");
			}

			diskCreationControl.unsetBoxSelections();
			diskCreationControl.change_visibility(false);
		}
	}



}