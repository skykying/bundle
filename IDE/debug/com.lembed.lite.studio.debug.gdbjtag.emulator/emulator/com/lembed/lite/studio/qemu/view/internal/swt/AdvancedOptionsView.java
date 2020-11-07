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

public class AdvancedOptionsView extends JPanel implements IDeviceView{

    private static final long serialVersionUID = 1L;

    private JPanel jpanel;

    private GridLayout gridLayout;

    private JLabel temp[];

    private JCheckBox win2kHackOption;

    private JCheckBox noAcpiOption;

    private JLabel nameDescription;

    private JTextField name;

    private JCheckBox snapshotOption;

    private JCheckBox noFdBootchkOption;

    private JCheckBox noHpetOption;

    private JButton okButton, eraseButton;

	private String title;

	private EmulatorQemuMachineControl myfile;

    public AdvancedOptionsView(EmulatorQemuMachineControl myfile) {
    	this.myfile = myfile;
        this.jpanel = new JPanel();

        this.gridLayout = new GridLayout(7, 2);

        jpanel.setLayout(gridLayout);

        this.temp = new JLabel[8];
        for (int i = 0; i < this.temp.length; i++) {
            this.temp[i] = new JLabel();
        }

        win2kHackOption = new JCheckBox("Enable win2kHack Option!");

        jpanel.add(win2kHackOption);
        jpanel.add(temp[0]);

        noAcpiOption = new JCheckBox("Disable ACPI Support!");

        jpanel.add(noAcpiOption);
        jpanel.add(temp[1]);

        nameDescription = new JLabel("Guest name:");

        name = new JTextField();

        jpanel.add(nameDescription);

        jpanel.add(name);

        snapshotOption = new JCheckBox("Enable Snapshot Option!");

        jpanel.add(snapshotOption);
        jpanel.add(this.temp[2]);

        noFdBootchkOption = new JCheckBox("Disable boot signature checking for floppy disks in BIOS!");

        jpanel.add(noFdBootchkOption);
        jpanel.add(this.temp[3]);

        noHpetOption = new JCheckBox("Disable HPET Support!");

        jpanel.add(noHpetOption);
        jpanel.add(this.temp[4]);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        jpanel.add(okButton);

        jpanel.add(eraseButton);
        
        this.setTitle("JavaQemu - Advanced Options");

        this.add(jpanel);

        this.setVisible(false);
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
    }

    public JCheckBox getWin2kHackOption() {
        return win2kHackOption;
    }

    public JCheckBox getNoAcpiOption() {
        return noAcpiOption;
    }

    public JTextField getNameContents() {
        return name;
    }

    public JCheckBox getSnapshotOption() {
        return snapshotOption;
    }

    public JCheckBox getNoFdBootchkOption() {
        return noFdBootchkOption;
    }

    public JCheckBox getNoHpetOption() {
        return noHpetOption;
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
        if (myfile.getMachineModel().getWin2khackOption() != null) {
            if (myfile.getMachineModel().getWin2khackOption().equals("true")) {
                this.win2kHackOption.setSelected(true);
            }
        }

        if (myfile.getMachineModel().getNoacpiOption() != null) {
            if (myfile.getMachineModel().getNoacpiOption().equals("true")) {
                this.noAcpiOption.setSelected(true);
            }
        }

        if (myfile.getMachineModel().getNameOption() != null) {
            this.name.setText(myfile.getMachineModel().getNameOption());
        }

        if (myfile.getMachineModel().getSnapshotOption() != null) {
            if (myfile.getMachineModel().getSnapshotOption().equals("true")) {
                this.snapshotOption.setSelected(true);
            }
        }

        if (myfile.getMachineModel().getNoFdBootchkOption() != null) {
            if (myfile.getMachineModel().getNoFdBootchkOption().equals("true")) {
                this.noFdBootchkOption.setSelected(true);
            }
        }

        if (myfile.getMachineModel().getNoHpetOption() != null) {
            if (myfile.getMachineModel().getNoHpetOption().equals("true")) {
                this.noHpetOption.setSelected(true);
            }
        }
		
	}
}
