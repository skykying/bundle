package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class ChangeMachineName implements BaseListener {

	private JQemuView view;
	private FileControl fileControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	public ChangeMachineName(JQemuView jview,FileControl fileControl) {
		view = jview;
		view.registerListener(this);
		this.fileControl = fileControl;
		vMConfigurationControlist = new ArrayList<VMConfigurationControl>();

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
		 if (e.getActionCommand().equals("ChangeMachineName")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			String machineName = view.getInputMessage("Please, type a name for VM:");
			if (machineName != null) {
				if (!machineName.isEmpty()) {
					fileControl.getFilemodel().setMachineName(machineName);
					view.changeNameJPanel(machineName);
					vMConfigurationControlist.get(position).getMyName().updateMe();
				} else {
					view.showMessage("Please, type a valid name for VM!");
				}
			}
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
