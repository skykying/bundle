package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.VMClosingControl;
import com.lembed.lite.studio.qemu.control.swt.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class CloseVM implements BaseListener {

	private JSwtQemuView view;
	private EmulationControl emulationControl;
	private VMClosingControl vMClosingControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	
	public CloseVM(JSwtQemuView jview, EmulationControl emulationControl,VMClosingControl vMClosingControl) {
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
		doAction((ActionEvent) e);
	}

	private void doAction(ActionEvent e) {
		
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
			vMClosingControl.setEmulation(emulationControl);
			
			vMClosingControl.starts(false);
		}

	}


}
