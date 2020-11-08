package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkDnssearchUserWorkerView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JLabel dnssearchDescription;

	private JTextArea dnssearch;

	private JButton add;

	private JTextField text;

	private JTextField option;

	private JButton remove;

	private JButton eraseButton;

	private JButton okButton;

	private int position;

	public NetworkDnssearchUserWorkerView(EmulatorQemuMachineControl emc, int position) {
		super(emc);

		this.position = position;

		windowContent = new JPanel();

		gridLayout = new GridLayout(4, 2);

		windowContent.setLayout(gridLayout);

		dnssearchDescription = new JLabel("'dnssearch' Option(s):");

		windowContent.add(dnssearchDescription);

		dnssearch = new JTextArea("", 3, 30);
		dnssearch.setLineWrap(true);
		dnssearch.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(this.dnssearch);

		windowContent.add(scrollPane);

		this.option = new JTextField();

		this.add = new JButton("Add the option:");

		windowContent.add(this.add);

		this.text = new JTextField();

		windowContent.add(this.text);

		this.remove = new JButton("Remove the option:");

		windowContent.add(this.remove);

		windowContent.add(this.option);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		windowContent.add(okButton);

		windowContent.add(eraseButton);

		this.add(windowContent);

		this.setTitle("Network User");

	}

	private void rechecks() {
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
		this.dnssearch.append(option + "\n");
	}

	public void buildTextArea(String optionString) {
		String[] options = optionString.split(",");
		for (String option : options) {
			if (option.startsWith("dnssearch=")) {
				this.dnssearch.append(option.substring(option.indexOf("=") + 1) + "\n");
			}
		}
	}

	public void removeMe(String option) {
		String options[] = dnssearch.getText().split("\n");
		dnssearch.setText("");
		for (String anOption : options) {
			if (!anOption.equals(option)) {
				dnssearch.append(anOption + "\n");
			}
		}
	}

	private boolean contains(String option) {
		if (option.contains("user")) {
			return true;
		}
		return false;
	}

	public JTextArea getDnssearch() {
		return dnssearch;
	}

	public JTextField getOption() {
		return option;
	}

	public JTextField getText() {
		return text;
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
