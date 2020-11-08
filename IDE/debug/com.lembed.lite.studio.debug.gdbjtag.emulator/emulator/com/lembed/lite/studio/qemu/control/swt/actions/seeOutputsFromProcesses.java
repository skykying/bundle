package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class seeOutputsFromProcesses implements BaseListener {

	private JContainerView view;
	private EmulationControl emulationControl;


	public seeOutputsFromProcesses(JContainerView jview, EmulationControl emulationControl) {
		view = jview;
		view.registerListener(this);
		this.emulationControl = emulationControl;
	}

	public void starts() {
		view.setVisible(true);
		view.configureStandardMode();
	}

	@Override
	public void actionPerformed(BaseEvent e) {
		doAction(e);
	}

	private void doAction(ActionEvent e) {
		if (e.getActionCommand().equals("seeOutputsFromProcesses")) {
			try {
				if (emulationControl == null) {
					emulationControl = new EmulationControl(view);
				}
				emulationControl.showsMessages();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doAction((ActionEvent) e);
	}


}
