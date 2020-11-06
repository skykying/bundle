package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.VMClosingControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class CloseVM implements BaseListener {

	private JQemuView view;
	private EmulationControl emulationControl;
	private VMClosingControl vMClosingControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	
	public CloseVM(JQemuView jview, EmulationControl emulationControl,VMClosingControl vMClosingControl) {
		view = jview;
		view.registerListener(this);
		this.emulationControl = emulationControl;
		this.vMClosingControl = vMClosingControl;
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
		
		if (e.getActionCommand().equals("CloseVM")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			
			vMConfigurationControlist.remove(position);
			if (vMClosingControl == null) {
				vMClosingControl = new VMClosingControl(view, emulationControl);
			} 
			
			vMClosingControl.setView(view);
			vMClosingControl.setMyemulation(emulationControl);
			
			vMClosingControl.starts(false);
	
		}

	}


}
