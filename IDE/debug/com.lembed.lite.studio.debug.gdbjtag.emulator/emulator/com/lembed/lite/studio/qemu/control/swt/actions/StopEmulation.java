package com.lembed.lite.studio.qemu.control.swt.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class StopEmulation implements BaseListener {

	private JContainerView view;
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	public StopEmulation(JContainerView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		emulationControl = null;
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
		if (e.getActionCommand().equals("StopEmulation")) {
			if (emulationControl == null) {
				emulationControl = new EmulationControl(view);
			}
			if (emulationControl.warns()) {
				if (emulationControl.stops(view.getActivePanel())) {
					view.showMessage("The Qemu is not running now!");
				}

				if (configurationControl != null) {
					emulationControl.setExecute_after_stop_qemu(
							this.JTextAreaToArrayListOfStrings(configurationControl.getExecute_after_stop_qemu()));
					if (emulationControl.postruns(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
						view.showMessage("The post-run script(s) is(are) gone.");
					}
				}
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
