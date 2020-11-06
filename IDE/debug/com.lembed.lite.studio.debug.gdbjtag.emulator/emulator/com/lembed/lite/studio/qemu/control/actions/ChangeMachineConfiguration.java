package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class ChangeMachineConfiguration implements BaseListener {

	private JQemuView view;
	private EmulationControl emulationControl;
	private FileControl fileControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	public ChangeMachineConfiguration(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		emulationControl = null;
		fileControl = null;
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
		if (e.getActionCommand().equals("ChangeMachineConfiguration")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			if (vMConfigurationControlist.get(position) != null) {
				vMConfigurationControlist.get(position).restarts();
			} else {
				if (vMConfigurationControlist.size() <= position) {
					for (int i = vMConfigurationControlist.size(); i <= position; i++) {
						vMConfigurationControlist.add(i, null);
					}
				}
				vMConfigurationControlist.set(position,
						new VMConfigurationControl(emulationControl, view, fileControl));
				vMConfigurationControlist.get(position).starts();
				vMConfigurationControlist.get(position).restarts();
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
