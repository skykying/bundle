package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.DiskCreationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class Cancel_CreateNewDiskImageFile implements BaseListener {

	private JQemuView view;
	private DiskCreationControl diskCreationControl;
	public Cancel_CreateNewDiskImageFile(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		diskCreationControl = null;
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
		if (e.getActionCommand().equals("Cancel_CreateNewDiskImageFile")) {
			diskCreationControl.unsetBoxSelections();
			diskCreationControl.change_visibility(false);
		}
	}

	public List<String> JTextAreaToArrayListOfStrings(JTextArea given) {
		List<String> result = new ArrayList<String>();
		if (given != null) {
			String[] helper = given.getText().split("\n");
			for (int i = 0; i < helper.length; i++) {
				result.add(helper[i]);
			}
		}
		return result;
	}

}
