package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkHubportWorkerView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JCheckBox isEnabled;

	private JLabel temp;

	private JLabel idDescription;

	private JTextField id;

	private JLabel hubidDescription;

	private JTextField hubid;

	private JButton eraseButton;

	private JButton okButton;

	private int position;

	public NetworkHubportWorkerView(EmulatorQemuMachineControl eQControl, int position) {
		super(eQControl);
		this.position = position;

		windowContent = new JPanel();

		gridLayout = new GridLayout(4, 2);

		windowContent.setLayout(gridLayout);

		isEnabled = new JCheckBox("Enable this option!");

		windowContent.add(this.isEnabled);

		temp = new JLabel();

		windowContent.add(temp);

		idDescription = new JLabel("Choose the id:");

		windowContent.add(idDescription);

		id = new JTextField();

		windowContent.add(id);

		hubidDescription = new JLabel("Choose the hubid:");

		windowContent.add(hubidDescription);

		hubid = new JTextField();

		windowContent.add(hubid);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		windowContent.add(okButton);

		windowContent.add(eraseButton);

		this.add(windowContent);

		this.setTitle("Network Hub Port");
	}

	private void rechecks() {
		// this.pack();
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

	private void buildMe(String optionString) {
		String options[] = optionString.split(",");

		for (String option : options) {
			if (option.startsWith("id=")) {
				this.id.setText(option.substring(option.indexOf("=") + 1));
			} else if (option.startsWith("hubid=")) {
				this.hubid.setText(option.substring(option.indexOf("=") + 1));
			}
		}

	}

	public JCheckBox getIsEnabled() {
		return isEnabled;
	}

	private boolean contains(String option) {
		if (option.contains("hubport")) {
			return true;
		}
		return false;
	}

	public JTextField getId() {
		return id;
	}

	public JTextField getHubid() {
		return hubid;
	}

	@Override
	public void applyView(IemultorStore store) {
		switch (position) {
		case 1:
			if (eQControl.getMachineModel().getFirstNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFirstNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getFirstNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 2:
			if (eQControl.getMachineModel().getSecondNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSecondNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getSecondNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 3:
			if (eQControl.getMachineModel().getThirdNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getThirdNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getThirdNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 4:
			if (eQControl.getMachineModel().getFourthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFourthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getFourthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 5:
			if (eQControl.getMachineModel().getFifthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFifthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getFifthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 6:
			if (eQControl.getMachineModel().getSixthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSixthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getSixthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 7:
			if (eQControl.getMachineModel().getSeventhNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSeventhNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getSeventhNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 8:
			if (eQControl.getMachineModel().getEighthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getEighthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getEighthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 9:
			if (eQControl.getMachineModel().getNinthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getNinthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getNinthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		case 10:
			if (eQControl.getMachineModel().getTenthNetworkNetdevOption() != null) {
				if (this.contains(eQControl.getMachineModel().getTenthNetworkNetdevOption())) {
					this.buildMe(eQControl.getMachineModel().getTenthNetworkNetdevOption());
					this.isEnabled.setSelected(true);
				}
			}
			break;
		default:
			break;
		}

		this.rechecks();

	}
}
