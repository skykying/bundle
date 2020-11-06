package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.VMClosingControl;
import com.lembed.lite.studio.qemu.control.VMConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class ExitCommand implements BaseListener {

	private JQemuView view;
	private EmulationControl emulationControl;
	private VMClosingControl vMClosingControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	public ExitCommand(JQemuView jview, EmulationControl emulationControl) {
		view = jview;
		view.registerListener(this);
		this.emulationControl = emulationControl;
		vMClosingControl = null;
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
		if (e.getActionCommand().equals("ExitCommand")) {
			// System.exit(0);
			boolean runFurther = view.getSizeOfJTabbedPane() > 1;
			for (int i = 1; i < view.getSizeOfJTabbedPane(); i++) {
				vMConfigurationControlist.remove(i);
			}

			if (runFurther) {
				if (vMClosingControl == null) {
					vMClosingControl = new VMClosingControl(view, emulationControl);
				} else {
					vMClosingControl.setView(view);
					vMClosingControl.setMyemulation(emulationControl);
				}
				vMClosingControl.starts(true);
			}

			view.dispose();

			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				int n = 0;

				@Override
				public void run() {

					if (++n == 2) {
						timer.cancel();
						System.exit(0);
					}
				}
			}, 1000, 1000);
		} 

	}

}
