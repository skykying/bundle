package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.internal.swt.UtilitiesView;

public class UseUtilities implements BaseListener {

//	private JSwtQemuView view;
	private UtilitiesView utilitiesView;
	
	
	public UseUtilities() {
//		view = jview;
		utilitiesView = null;
	}

	public void starts() {
//		view.setVisible(true);
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
		if (utilitiesView == null) {
			utilitiesView = new UtilitiesView();
			utilitiesView.initialize();
			utilitiesView.setVisible(true);
		}
		
		if (e.getActionCommand().equals("useUtilities")) {
			{
				utilitiesView.setVisible(true);
			}
		}
		
		if (e.getActionCommand().equals("Hide_Utilities")) {
			utilitiesView.setVisible(false);
		}
	}




}
