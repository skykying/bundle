package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class MonitorView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JLabel monitorLabel;

	private JComboBox<String> monitor;

	private JLabel qmpLabel;

	private JComboBox<String> qmp;

	private JButton eraseButton;

	private JButton okButton;

	// Association "option" - "Description".
	private HashMap<String, String> falseOptions;

	public MonitorView(EmulatorQemuMachineControl emc) {
		super(emc);

		windowContent = new JPanel();

		gridLayout = new GridLayout(3, 2);

		windowContent.setLayout(gridLayout);

		monitorLabel = new JLabel("Redirect the monitor to host device:");

		windowContent.add(monitorLabel);

		String[] hostDeviceOptions = { "", "Graphical Mode", "Non Graphical Mode" };

		this.monitor = new JComboBox<String>(hostDeviceOptions);

		windowContent.add(monitor);

		qmpLabel = new JLabel("Redirect the monitor to host device, in 'control' mode:");

		windowContent.add(qmpLabel);

		this.qmp = new JComboBox<String>(hostDeviceOptions);

		windowContent.add(this.qmp);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		windowContent.add(okButton);

		windowContent.add(eraseButton);

		this.add(windowContent);

		this.setTitle("Monitor");
	}

	private void rechecks() {
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
	}

	public JComboBox<String> getMonitor() {
		return monitor;
	}

	public JComboBox<String> getQmp() {
		return qmp;
	}

	@Override
	public void applyView(IemultorStore store) {
		this.falseOptions = new HashMap<String, String>();
		falseOptions.put("", "");
		falseOptions.put("vc", "Graphical Mode");
		falseOptions.put("stdio", "Non Graphical Mode");

		if (eQControl.getMachineModel().getMonitorOption() != null) {
			this.monitor.setSelectedItem(this.falseOptions.get(eQControl.getMachineModel().getMonitorOption()));
		}

		if (eQControl.getMachineModel().getQmpOption() != null) {
			this.qmp.setSelectedItem(this.falseOptions.get(eQControl.getMachineModel().getQmpOption()));
		}

		this.rechecks();

	}
}
