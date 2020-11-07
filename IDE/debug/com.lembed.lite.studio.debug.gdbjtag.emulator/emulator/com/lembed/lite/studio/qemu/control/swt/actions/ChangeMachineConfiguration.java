package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class ChangeMachineConfiguration implements BaseListener {

	private JContainerView view;
	private EmulationControl emulationControl;
	private FileControl fileControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	public ChangeMachineConfiguration(JContainerView jview, EmulationControl emulationControl, FileControl fileControl) {
		view = jview;
		view.registerListener(this);
		this.emulationControl = emulationControl;
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


}
