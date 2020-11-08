package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkHostfwdUserWorkerView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JLabel hostfwdDescription;

	private JTextArea hostfwd;

	private JButton add;

	private JLabel connectionTypeDescription;

	private JComboBox<String> connectionType;

	private JLabel hostaddrDescription;

	private JTextField hostaddr;

	private JLabel hostportDescription;

	private JTextField hostport;

	private JLabel guestaddrDescription;

	private JTextField guestaddr;

	private JLabel guestportDescription;

	private JTextField guestport;

	private JLabel temp;

	private JTextField option;

	private JButton remove;

	private JButton eraseButton;

	private JButton okButton;

	private int position;

	public NetworkHostfwdUserWorkerView(EmulatorQemuMachineControl eQControl, int position) {
		super(eQControl);
		this.position = position;

		windowContent = new JPanel();

		gridLayout = new GridLayout(9, 2);

		windowContent.setLayout(gridLayout);

		hostfwdDescription = new JLabel("'hostfwd' Option(s):");

		windowContent.add(hostfwdDescription);

		hostfwd = new JTextArea("", 3, 30);
		hostfwd.setLineWrap(true);
		hostfwd.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(this.hostfwd);

		windowContent.add(scrollPane);

		this.add = new JButton("Add the following option:");

		windowContent.add(this.add);

		this.temp = new JLabel();

		windowContent.add(this.temp);

		connectionTypeDescription = new JLabel("Choose the connection type:");

		windowContent.add(this.connectionTypeDescription);

		String[] connections = { "TCP", "UDP" };

		connectionType = new JComboBox<String>(connections);

		windowContent.add(this.connectionType);

		hostaddrDescription = new JLabel("Choose the host address:");

		windowContent.add(this.hostaddrDescription);

		hostaddr = new JTextField();

		windowContent.add(this.hostaddr);

		hostportDescription = new JLabel("Choose the host port:");

		windowContent.add(this.hostportDescription);

		hostport = new JTextField();

		windowContent.add(hostport);

		guestaddrDescription = new JLabel("Choose the guest address:");

		windowContent.add(guestaddrDescription);

		guestaddr = new JTextField();

		windowContent.add(this.guestaddr);

		guestportDescription = new JLabel("Choose the guest port:");

		windowContent.add(this.guestportDescription);

		guestport = new JTextField();

		windowContent.add(guestport);

		this.remove = new JButton("Remove a option:");

		windowContent.add(this.remove);

		this.option = new JTextField();

		windowContent.add(this.option);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		windowContent.add(okButton);

		windowContent.add(eraseButton);

		this.add(windowContent);

		this.setTitle("JavaQemu - Network User Choice");

	}

	private void rechecks() {
		// this.pack();
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
		add.addActionListener(listener);
		remove.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
		add.setActionCommand("add");
		remove.setActionCommand("remove");
	}

	public void buildMe(String option) {
		this.hostfwd.append(option + "\n");
	}

	public void buildTextArea(String optionString) {
		String[] options = optionString.split(",");
		for (String option : options) {
			if (option.startsWith("hostfwd=")) {
				this.hostfwd.append(option.substring(option.indexOf("=") + 1) + "\n");
			}
		}
	}

	public void removeMe(String option) {
		String options[] = hostfwd.getText().split("\n");
		hostfwd.setText("");
		for (String anOption : options) {
			if (!anOption.equals(option)) {
				hostfwd.append(anOption + "\n");
			}
		}
	}

	private boolean contains(String option) {
		if (option.contains("user")) {
			return true;
		}
		return false;
	}

	public JTextArea getHostfwd() {
		return hostfwd;
	}

	public JComboBox<String> getConnectionType() {
		return connectionType;
	}

	public JTextField getHostaddr() {
		return hostaddr;
	}

	public JTextField getHostport() {
		return hostport;
	}

	public JTextField getGuestaddr() {
		return guestaddr;
	}

	public JTextField getGuestport() {
		return guestport;
	}

	public JTextField getOption() {
		return option;
	}

	@Override
	public void applyView(IemultorStore store) {

		switch (position) {
		case 1:
			if (eQControl.getMachineModel().getFirstNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFirstNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getFirstNetworkExtraOption());
				}
			}
			break;
		case 2:
			if (eQControl.getMachineModel().getSecondNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSecondNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getSecondNetworkExtraOption());
				}
			}
			break;
		case 3:
			if (eQControl.getMachineModel().getThirdNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getThirdNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getThirdNetworkExtraOption());
				}
			}
			break;
		case 4:
			if (eQControl.getMachineModel().getFourthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFourthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getFourthNetworkExtraOption());
				}
			}
			break;
		case 5:
			if (eQControl.getMachineModel().getFifthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getFifthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getFifthNetworkExtraOption());
				}
			}
			break;
		case 6:
			if (eQControl.getMachineModel().getSixthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSixthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getSixthNetworkExtraOption());
				}
			}
			break;
		case 7:
			if (eQControl.getMachineModel().getSeventhNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getSeventhNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getSeventhNetworkExtraOption());
				}
			}
			break;
		case 8:
			if (eQControl.getMachineModel().getEighthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getEighthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getEighthNetworkExtraOption());
				}
			}
			break;
		case 9:
			if (eQControl.getMachineModel().getNinthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getNinthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getNinthNetworkExtraOption());
				}
			}
			break;
		case 10:
			if (eQControl.getMachineModel().getTenthNetworkExtraOption() != null) {
				if (this.contains(eQControl.getMachineModel().getTenthNetworkExtraOption())) {
					this.buildTextArea(eQControl.getMachineModel().getTenthNetworkExtraOption());
				}
			}
			break;
		default:
			break;
		}

		this.rechecks();

	}
}
