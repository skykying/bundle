package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.UtilitiesView;

public class Hide_Utilities implements BaseListener {

	private JContainerView view;
	private UtilitiesView utilitiesView;
	public Hide_Utilities(JContainerView jview, UtilitiesView utilitiesView) {
		view = jview;
		view.registerListener(this);

		this.utilitiesView = utilitiesView;
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
		if (e.getActionCommand().equals("Hide_Utilities")) {
			utilitiesView.setVisible(false);
		}
	}


}
