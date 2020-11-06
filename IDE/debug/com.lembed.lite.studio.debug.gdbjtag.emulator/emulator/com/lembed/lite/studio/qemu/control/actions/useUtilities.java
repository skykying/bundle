package com.lembed.lite.studio.qemu.control.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.UtilitiesView;

public class useUtilities implements BaseListener {

	private JQemuView view;
	private UtilitiesView utilitiesView;
	public useUtilities(JQemuView jview) {
		view = jview;
		view.registerListener(this);
		utilitiesView = null;
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
		if (e.getActionCommand().equals("useUtilities")) {
			if (utilitiesView == null) {
				utilitiesView = new UtilitiesView();
				utilitiesView.initialize();
				utilitiesView.configureListener(this);
				utilitiesView.configureStandardMode();
				utilitiesView.setVisible(true);
			} else {
				utilitiesView.setVisible(true);
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
