package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class CDROMView extends DeviceViewWithFileChooser {

	private static final long serialVersionUID = 1L;

	private JPanel jpanel;
	private JLabel cdromLabel;
	private JTextField cdromText;
	private JButton cdromDriveSelection, diskImageSelection;
	private JButton okButton, eraseButton;
	private GridLayout gridLayout;

	private String title;

	public CDROMView(EmulatorQemuMachineControl myfile) {
		super(myfile, null);

		jpanel = new JPanel();
		this.gridLayout = new GridLayout(3, 2);

		this.setJpanel(jpanel);
		this.jpanel.setLayout(gridLayout);

		cdromLabel = new JLabel("Insert your CDROM option:");
		cdromText = new JTextField("");
		cdromDriveSelection = new JButton("Select a CDROM Drive!");
		diskImageSelection = new JButton("Select a CDROM image file!");

		okButton = new JButton("OK");
		eraseButton = new JButton("Erase");

		jpanel.add(this.cdromLabel);
		jpanel.add(this.cdromText);
		jpanel.add(this.cdromDriveSelection);
		jpanel.add(this.diskImageSelection);
		jpanel.add(this.okButton);
		jpanel.add(this.eraseButton);

		setTitle("CDROM");
		this.add(jpanel);

	}

	private void rechecks() {

		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		cdromDriveSelection.addActionListener(listener);
		diskImageSelection.addActionListener(listener);
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
	}

	public void configureStandardMode() {
		cdromDriveSelection.setActionCommand("cdromDriveSelection");
		diskImageSelection.setActionCommand("diskImageSelection");
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
	}

	public JTextField getCdromText() {
		return cdromText;
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
		if (eQControl.getMachineModel().getCdrom() != null) {
			cdromText.setText(eQControl.getMachineModel().getCdrom());
		}

		this.rechecks();
	}
}
