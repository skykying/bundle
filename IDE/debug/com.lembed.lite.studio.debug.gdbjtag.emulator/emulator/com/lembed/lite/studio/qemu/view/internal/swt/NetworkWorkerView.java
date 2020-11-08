package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkWorkerView extends DeviceBaseView{

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JCheckBox isEnabled;

	private JButton nicOptions;

	private JButton userOption;

	private JButton tapOption;

	private JButton bridgeOption;

	private JButton tcpSocketOption;

	private JButton udpSocketOption;

	private JButton vdeOption;

	private JButton hubportOption;

	private JButton dumpOption;

	private JButton disableButton;

	private JButton okButton;

	private JLabel temp[];
	private String title;

	private int position;

	private EmulatorQemuMachineControl myfile;

	public NetworkWorkerView(EmulatorQemuMachineControl myfile, int position) {
		super();
		this.myfile = myfile;
		this.position = position;

		windowContent = new JPanel();

		gridLayout = new GridLayout(7, 2);

		windowContent.setLayout(gridLayout);

		isEnabled = new JCheckBox("Enable these options!");

		windowContent.add(this.isEnabled);

		this.temp = new JLabel[2];
		for (int i = 0; i < this.temp.length; i++) {
			this.temp[i] = new JLabel();
		}

		windowContent.add(this.temp[0]);

		nicOptions = new JButton("Use the nic option!");

		userOption = new JButton("Use the user option!");

		windowContent.add(this.nicOptions);

		windowContent.add(this.userOption);

		tapOption = new JButton("Use the tap option!");

		bridgeOption = new JButton("Use the bridge option!");

		windowContent.add(this.tapOption);

		windowContent.add(this.bridgeOption);

		tcpSocketOption = new JButton("Use the tcp socket option!");

		udpSocketOption = new JButton("Use the udp socket option!");

		windowContent.add(this.tcpSocketOption);

		windowContent.add(this.udpSocketOption);

		vdeOption = new JButton("Use the vde option!");

		windowContent.add(this.vdeOption);

		hubportOption = new JButton("Use the hubport option!");

		windowContent.add(this.hubportOption);

		dumpOption = new JButton("Use the dump option!");

		windowContent.add(this.dumpOption);

		windowContent.add(this.temp[1]);

		okButton = new JButton("OK");

		disableButton = new JButton("Disable");

		windowContent.add(okButton);

		windowContent.add(disableButton);

		setTitle("Network Option");

		add(windowContent);

	}

	private void rechecks() {
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		disableButton.addActionListener(listener);
		okButton.addActionListener(listener);
		nicOptions.addActionListener(listener);
		userOption.addActionListener(listener);
		tapOption.addActionListener(listener);
		bridgeOption.addActionListener(listener);
		tcpSocketOption.addActionListener(listener);
		udpSocketOption.addActionListener(listener);
		vdeOption.addActionListener(listener);
		hubportOption.addActionListener(listener);
		dumpOption.addActionListener(listener);
	}

	public void configureStandardMode() {
		disableButton.setActionCommand("disableButton");
		okButton.setActionCommand("okButton");
		nicOptions.setActionCommand("nicOptions");
		userOption.setActionCommand("userOption");
		tapOption.setActionCommand("tapOption");
		bridgeOption.setActionCommand("bridgeOption");
		tcpSocketOption.setActionCommand("tcpSocketOption");
		udpSocketOption.setActionCommand("udpSocketOption");
		vdeOption.setActionCommand("vdeOption");
		hubportOption.setActionCommand("hubportOption");
		dumpOption.setActionCommand("dumpOption");
	}

	public JCheckBox getIsEnabled() {
		return isEnabled;
	}

	@Override
	public void setTitle(String string) {
		title = string;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void applyView(IemultorStore store) {
		switch (position) {
		case 1:
			if (myfile.getMachineModel().getFirstNetworkNICOption() != null
					|| (myfile.getMachineModel().getFirstNetworkExtraOption() != null
							|| myfile.getMachineModel().getFirstNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			} else if (myfile.getMachineModel().getFirstNetworkNICOption() == null
					&& myfile.getMachineModel().getFirstNetworkExtraOption() == null
					&& myfile.getMachineModel().getFirstNetworkNetdevOption() == null
					&& myfile.getMachineModel().getSecondNetworkNICOption() == null
					&& myfile.getMachineModel().getSecondNetworkExtraOption() == null
					&& myfile.getMachineModel().getSecondNetworkNetdevOption() == null
					&& myfile.getMachineModel().getThirdNetworkNICOption() == null
					&& myfile.getMachineModel().getThirdNetworkExtraOption() == null
					&& myfile.getMachineModel().getThirdNetworkNetdevOption() == null
					&& myfile.getMachineModel().getFourthNetworkNICOption() == null
					&& myfile.getMachineModel().getFourthNetworkExtraOption() == null
					&& myfile.getMachineModel().getFourthNetworkNetdevOption() == null
					&& myfile.getMachineModel().getFifthNetworkNICOption() == null
					&& myfile.getMachineModel().getFifthNetworkExtraOption() == null
					&& myfile.getMachineModel().getFifthNetworkNetdevOption() == null
					&& myfile.getMachineModel().getSixthNetworkNICOption() == null
					&& myfile.getMachineModel().getSixthNetworkExtraOption() == null
					&& myfile.getMachineModel().getSixthNetworkNetdevOption() == null
					&& myfile.getMachineModel().getSeventhNetworkNICOption() == null
					&& myfile.getMachineModel().getSeventhNetworkExtraOption() == null
					&& myfile.getMachineModel().getSeventhNetworkNetdevOption() == null
					&& myfile.getMachineModel().getEighthNetworkNICOption() == null
					&& myfile.getMachineModel().getEighthNetworkExtraOption() == null
					&& myfile.getMachineModel().getEighthNetworkNetdevOption() == null
					&& myfile.getMachineModel().getNinthNetworkNICOption() == null
					&& myfile.getMachineModel().getNinthNetworkExtraOption() == null
					&& myfile.getMachineModel().getNinthNetworkNetdevOption() == null
					&& myfile.getMachineModel().getTenthNetworkNICOption() == null
					&& myfile.getMachineModel().getTenthNetworkExtraOption() == null
					&& myfile.getMachineModel().getTenthNetworkNetdevOption() == null) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 2:
			if (myfile.getMachineModel().getSecondNetworkNICOption() != null
					|| (myfile.getMachineModel().getSecondNetworkExtraOption() != null
							|| myfile.getMachineModel().getSecondNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 3:
			if (myfile.getMachineModel().getThirdNetworkNICOption() != null
					|| (myfile.getMachineModel().getThirdNetworkExtraOption() != null
							|| myfile.getMachineModel().getThirdNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 4:
			if (myfile.getMachineModel().getFourthNetworkNICOption() != null
					|| (myfile.getMachineModel().getFourthNetworkExtraOption() != null
							|| myfile.getMachineModel().getFourthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 5:
			if (myfile.getMachineModel().getFifthNetworkNICOption() != null
					|| (myfile.getMachineModel().getFifthNetworkExtraOption() != null
							|| myfile.getMachineModel().getFifthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 6:
			if (myfile.getMachineModel().getSixthNetworkNICOption() != null
					|| (myfile.getMachineModel().getSixthNetworkExtraOption() != null
							|| myfile.getMachineModel().getSixthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 7:
			if (myfile.getMachineModel().getSeventhNetworkNICOption() != null
					|| (myfile.getMachineModel().getSeventhNetworkExtraOption() != null
							|| myfile.getMachineModel().getSeventhNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 8:
			if (myfile.getMachineModel().getEighthNetworkNICOption() != null
					|| (myfile.getMachineModel().getEighthNetworkExtraOption() != null
							|| myfile.getMachineModel().getEighthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 9:
			if (myfile.getMachineModel().getNinthNetworkNICOption() != null
					|| (myfile.getMachineModel().getNinthNetworkExtraOption() != null
							|| myfile.getMachineModel().getNinthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		case 10:
			if (myfile.getMachineModel().getTenthNetworkNICOption() != null
					|| (myfile.getMachineModel().getTenthNetworkExtraOption() != null
							|| myfile.getMachineModel().getTenthNetworkNetdevOption() != null)) {
				this.isEnabled.setSelected(true);
			}
			break;
		default:
			break;
		}

		this.rechecks();

	}
}
