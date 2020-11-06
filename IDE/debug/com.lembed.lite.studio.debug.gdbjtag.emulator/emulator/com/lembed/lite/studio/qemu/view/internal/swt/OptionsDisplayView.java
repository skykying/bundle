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

import com.lembed.lite.studio.qemu.control.FileControl;
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

    public OptionsDisplayView(FileControl myfile) {
    	super(myfile);
    	
        this.jpanel = new JPanel();

        this.setTitle("JavaQemu - Display Options");

        this.add(jpanel);

        gridBagLayout = new GridBagLayout();
        this.gridBagConstraints = new GridBagConstraints();

        jpanel.setLayout(gridBagLayout);

        this.displayType = new JRadioButton("Display type:");

        String[] displayTypeOptions = {"sdl", "curses", "none", "vnc"};

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

        String[] vgaTypeOptions = {"Cirrus Logic GD5446", "Standard VGA Card", "Vmware SVGA-II", "QXL Paravirtual VGA Card", "None"};

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
	public void doSave(IemultorStore store) {

        if (fileControl.getFilemodel().getDisplayType() != null) {
            this.displayType.setSelected(true);
            if (fileControl.getFilemodel().getDisplayType().equals("sdl")) {
                this.displayTypeChoice.setSelectedIndex(0);
            } else if (fileControl.getFilemodel().getDisplayType().equals("curses")) {
                this.displayTypeChoice.setSelectedIndex(1);
            } else if (fileControl.getFilemodel().getDisplayType().equals("none")) {
                this.displayTypeChoice.setSelectedIndex(2);
            } else if (fileControl.getFilemodel().getDisplayType().substring(0, 3).equals("vnc")) {
                this.displayTypeChoice.setSelectedIndex(3);
            }
        }

        if (fileControl.getFilemodel().getNographicOption() != null) {
            if (fileControl.getFilemodel().getNographicOption().equals("true")) {
                this.noGraphicOption.setSelected(true);
            }
        }

        if (fileControl.getFilemodel().getVgaType() != null) {
            this.vgaType.setSelected(true);
            if (fileControl.getFilemodel().getVgaType().equals("cirrus")) {
                this.vgaTypeChoice.setSelectedIndex(0);
            } else if (fileControl.getFilemodel().getVgaType().equals("std")) {
                this.vgaTypeChoice.setSelectedIndex(1);
            } else if (fileControl.getFilemodel().getVgaType().equals("vmware")) {
                this.vgaTypeChoice.setSelectedIndex(2);
            } else if (fileControl.getFilemodel().getVgaType().equals("qxl")) {
                this.vgaTypeChoice.setSelectedIndex(3);
            } else if (fileControl.getFilemodel().getVgaType().equals("none")) {
                this.vgaTypeChoice.setSelectedIndex(4);
            }
        }

        if (fileControl.getFilemodel().getFullscreenOption() != null) {
            if (fileControl.getFilemodel().getFullscreenOption().equals("true")) {
                this.fullScreenOption.setSelected(true);
            }
        }

        if (fileControl.getFilemodel().getNoFrameOption() != null) {
            if (fileControl.getFilemodel().getNoFrameOption().equals("true")) {
                this.noframeOption.setSelected(true);
            }
        }

//        this.pack();
        this.setVisible(false);
		
	}
}
