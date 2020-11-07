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

public class VNCDisplayView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel jpanel;

	private JButton okButton, eraseButton;

	private GridLayout gridLayout;

	private JTextField iPAddressField;

	private JLabel iPAddressLabel;

	private JTextField tcpPortField;

	private JLabel tcpPortLabel;

	private JCheckBox reverseOption;

	private JCheckBox passwordOption;

	private JCheckBox lossyOption;

	private JCheckBox nonAdaptiveOption;

	private JLabel temp[];

	public VNCDisplayView(EmulatorQemuMachineControl emc) {
		super(emc);
		this.jpanel = new JPanel();

		this.gridLayout = new GridLayout(7, 2);

		this.setTitle("JavaQemu - VNC Display Options");

		okButton = new JButton("OK");
		eraseButton = new JButton("Erase");
		iPAddressLabel = new JLabel("IP Address: ");
		tcpPortLabel = new JLabel("TCP Port: 5900 + ");
		iPAddressField = new JTextField();
		tcpPortField = new JTextField();

		jpanel.setLayout(gridLayout);

		jpanel.add(iPAddressLabel);
		jpanel.add(iPAddressField);
		jpanel.add(tcpPortLabel);
		jpanel.add(tcpPortField);

		this.reverseOption = new JCheckBox("reverse");
		this.passwordOption = new JCheckBox("password");
		this.lossyOption = new JCheckBox("lossy");
		this.nonAdaptiveOption = new JCheckBox("non-adaptive");

		this.temp = new JLabel[4];

		for (int i = 0; i < this.temp.length; i++) {
			this.temp[i] = new JLabel();
		}

		jpanel.add(this.reverseOption);
		jpanel.add(this.temp[0]);
		jpanel.add(this.passwordOption);
		jpanel.add(this.temp[1]);
		jpanel.add(this.lossyOption);
		jpanel.add(this.temp[2]);
		jpanel.add(this.nonAdaptiveOption);
		jpanel.add(this.temp[3]);
		jpanel.add(okButton);
		jpanel.add(eraseButton);

		add(jpanel);
	}

	private void rechecks() {
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton2");
		okButton.setActionCommand("okButton2");
	}

	public JTextField getiPAddressField() {
		return iPAddressField;
	}

	public JTextField getTcpPortField() {
		return tcpPortField;
	}

	public void setiPAddressField(String iPAddressField) {
		this.iPAddressField.setText(iPAddressField);
	}

	public void setTcpPortField(String tcpPortField) {
		this.tcpPortField.setText(tcpPortField);
	}

	public JCheckBox getReverseOption() {
		return reverseOption;
	}

	public JCheckBox getPasswordOption() {
		return passwordOption;
	}

	public JCheckBox getLossyOption() {
		return lossyOption;
	}

	public JCheckBox getNonAdaptiveOption() {
		return nonAdaptiveOption;
	}

	@Override
	public void applyView(IemultorStore store) {
		String dispType = eQControl.getMachineModel().getDisplayType();
		if (dispType != null) {
			if (dispType.substring(0, 3).equals("vnc")) {
				this.iPAddressField.setText(dispType.substring(4, dispType.indexOf(":")));
				if (dispType.contains(",")) {
					this.tcpPortField.setText(dispType.substring(dispType.indexOf(":") + 1, dispType.indexOf(",")));
				} else {
					this.tcpPortField.setText(dispType.substring(dispType.indexOf(":") + 1));
				}
				if (dispType.contains(",")) {
					String[] otherOptions = dispType.substring(dispType.indexOf(",") + 1).split(",");
					for (String option : otherOptions) {
						if (option.equals("reverse")) {
							this.reverseOption.setSelected(true);
						} else if (option.equals("password")) {
							this.passwordOption.setSelected(true);
						} else if (option.equals("lossy")) {
							this.lossyOption.setSelected(true);
						} else if (option.equals("non-adaptive")) {
							this.nonAdaptiveOption.setSelected(true);
						}
					}
				}
			}
		}

		this.rechecks();

	}
}
