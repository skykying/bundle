package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class OptionsDisplayView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel jpanel;

	private GridBagLayout gridBagLayout;

	private GridBagConstraints gridBagConstraints;

	private JRadioButton displayType;

	private JComboBox<String> displayTypeChoice;

	private JRadioButton noGraphicOption;

	private ButtonGroup groupButtons;

	private JCheckBox vgaType;

	private JComboBox<String> vgaTypeChoice;

	private JCheckBox fullScreenOption;

	private JCheckBox noframeOption;

	private JButton showVNCOptions;

	private JButton okButton, eraseButton;

	public OptionsDisplayView(EmulatorQemuMachineControl emc) {
		super(emc);

		this.jpanel = new JPanel();

		this.setTitle("Display");

		this.add(jpanel);

		gridBagLayout = new GridBagLayout();
		this.gridBagConstraints = new GridBagConstraints();

		jpanel.setLayout(gridBagLayout);

		this.displayType = new JRadioButton("Display type:");

		String[] displayTypeOptions = { "sdl", "curses", "none", "vnc" };

		this.displayTypeChoice = new JComboBox<String>(displayTypeOptions);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;

		jpanel.add(displayType, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		jpanel.add(displayTypeChoice, gridBagConstraints);

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		noGraphicOption = new JRadioButton("-nographic option!");

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jpanel.add(noGraphicOption, gridBagConstraints);

		groupButtons = new ButtonGroup();
		groupButtons.add(this.displayType);
		groupButtons.add(this.noGraphicOption);

		vgaType = new JCheckBox("Vga type:");

		String[] vgaTypeOptions = { "Cirrus Logic GD5446", "Standard VGA Card", "Vmware SVGA-II",
				"QXL Paravirtual VGA Card", "None" };

		vgaTypeChoice = new JComboBox<String>(vgaTypeOptions);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		jpanel.add(vgaType, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		jpanel.add(vgaTypeChoice, gridBagConstraints);

		fullScreenOption = new JCheckBox("Full-screen");

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		jpanel.add(fullScreenOption, gridBagConstraints);

		noframeOption = new JCheckBox("-no-frame option!");

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		jpanel.add(noframeOption, gridBagConstraints);

		showVNCOptions = new JButton("Show VNC Options!");

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		jpanel.add(showVNCOptions, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		jpanel.add(okButton, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		jpanel.add(eraseButton, gridBagConstraints);

	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
		showVNCOptions.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
		showVNCOptions.setActionCommand("showVNCOptions");
	}

	public JRadioButton getDisplayType() {
		return displayType;
	}

	public JComboBox<String> getDisplayTypeChoice() {
		return displayTypeChoice;
	}

	public JRadioButton getNoGraphicOption() {
		return noGraphicOption;
	}

	public JCheckBox getVgaType() {
		return vgaType;
	}

	public JComboBox<String> getVgaTypeChoice() {
		return vgaTypeChoice;
	}

	public JCheckBox getFullScreenOption() {
		return fullScreenOption;
	}

	public ButtonGroup getGroupButtons() {
		return groupButtons;
	}

	public JCheckBox getNoframeOption() {
		return noframeOption;
	}

	@Override
	public void applyView(IemultorStore store) {

		if (eQControl.getMachineModel().getDisplayType() != null) {
			this.displayType.setSelected(true);
			if (eQControl.getMachineModel().getDisplayType().equals("sdl")) {
				this.displayTypeChoice.setSelectedIndex(0);
			} else if (eQControl.getMachineModel().getDisplayType().equals("curses")) {
				this.displayTypeChoice.setSelectedIndex(1);
			} else if (eQControl.getMachineModel().getDisplayType().equals("none")) {
				this.displayTypeChoice.setSelectedIndex(2);
			} else if (eQControl.getMachineModel().getDisplayType().substring(0, 3).equals("vnc")) {
				this.displayTypeChoice.setSelectedIndex(3);
			}
		}

		if (eQControl.getMachineModel().getNographicOption() != null) {
			if (eQControl.getMachineModel().getNographicOption().equals("true")) {
				this.noGraphicOption.setSelected(true);
			}
		}

		if (eQControl.getMachineModel().getVgaType() != null) {
			this.vgaType.setSelected(true);
			if (eQControl.getMachineModel().getVgaType().equals("cirrus")) {
				this.vgaTypeChoice.setSelectedIndex(0);
			} else if (eQControl.getMachineModel().getVgaType().equals("std")) {
				this.vgaTypeChoice.setSelectedIndex(1);
			} else if (eQControl.getMachineModel().getVgaType().equals("vmware")) {
				this.vgaTypeChoice.setSelectedIndex(2);
			} else if (eQControl.getMachineModel().getVgaType().equals("qxl")) {
				this.vgaTypeChoice.setSelectedIndex(3);
			} else if (eQControl.getMachineModel().getVgaType().equals("none")) {
				this.vgaTypeChoice.setSelectedIndex(4);
			}
		}

		if (eQControl.getMachineModel().getFullscreenOption() != null) {
			if (eQControl.getMachineModel().getFullscreenOption().equals("true")) {
				this.fullScreenOption.setSelected(true);
			}
		}

		if (eQControl.getMachineModel().getNoFrameOption() != null) {
			if (eQControl.getMachineModel().getNoFrameOption().equals("true")) {
				this.noframeOption.setSelected(true);
			}
		}

		this.setVisible(false);

	}
}
