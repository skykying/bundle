package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class ImageView extends DeviceViewWithFileChooser {

	private static final long serialVersionUID = 1L;

	private JPanel jpanel;

	private JButton mtdblockChooser;

	private JTextField mtdblock;

	private JButton sdChooser;

	private JTextField sd;

	private JButton pflashChooser;

	private JTextField pflash;

	private JButton okButton, eraseButton;

	private GridLayout gridLayout;

	public ImageView(EmulatorQemuMachineControl myfile) {
		super(myfile, null);

		this.jpanel = new JPanel();

		this.gridLayout = new GridLayout(4, 2);

		this.setTitle("Image");

		this.add(jpanel);

		this.setJpanel(jpanel);

		this.jpanel.setLayout(gridLayout);

		mtdblockChooser = new JButton("Choose the on-board Flash memory image:");

		this.jpanel.add(this.mtdblockChooser);

		this.mtdblock = new JTextField();

		this.jpanel.add(this.mtdblock);

		this.sdChooser = new JButton("Choose the SecureDigital card image:");

		this.jpanel.add(this.sdChooser);

		this.sd = new JTextField();

		this.jpanel.add(this.sd);

		this.pflashChooser = new JButton("Choose the parallel flash image:");

		this.jpanel.add(this.pflashChooser);

		this.pflash = new JTextField();

		this.jpanel.add(this.pflash);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		this.jpanel.add(this.okButton);
		this.jpanel.add(this.eraseButton);

	}

	private void rechecks() {
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
		mtdblockChooser.addActionListener(listener);
		sdChooser.addActionListener(listener);
		pflashChooser.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
		mtdblockChooser.setActionCommand("mtdblockChooser");
		sdChooser.setActionCommand("sdChooser");
		pflashChooser.setActionCommand("pflashChooser");
	}

	public JTextField getMtdblock() {
		return mtdblock;
	}

	public JTextField getSd() {
		return sd;
	}

	public JTextField getPflash() {
		return pflash;
	}

	@Override
	public void applyView(IemultorStore store) {
		if (eQControl.getMachineModel().getMtdblockOption() != null) {
			this.mtdblock.setText(eQControl.getMachineModel().getMtdblockOption());
		}

		if (eQControl.getMachineModel().getSdOption() != null) {
			this.sd.setText(eQControl.getMachineModel().getSdOption());
		}

		if (eQControl.getMachineModel().getPflashOption() != null) {
			this.pflash.setText(eQControl.getMachineModel().getPflashOption());
		}

		this.rechecks();

	}
}
