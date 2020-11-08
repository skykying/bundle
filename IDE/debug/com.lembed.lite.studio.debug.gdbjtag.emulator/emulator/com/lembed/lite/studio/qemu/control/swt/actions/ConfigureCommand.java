package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class ConfigureCommand implements BaseListener {

	private JSwtQemuView view;
	private ConfigurationControl configurationControl;

	public ConfigureCommand(JSwtQemuView jview, ConfigurationControl configurationControl) {
		view = jview;
		view.registerListener(this);
		this.configurationControl = configurationControl;
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
		
		 if (e.getActionCommand().equals("ConfigureCommand")) {
			configurationControl.do_my_view_visible();
		}

	}



}
