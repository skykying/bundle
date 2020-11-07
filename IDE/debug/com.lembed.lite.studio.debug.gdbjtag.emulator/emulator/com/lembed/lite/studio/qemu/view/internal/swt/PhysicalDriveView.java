package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class PhysicalDriveView extends DeviceBaseView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;
    private JLabel physicalDriveLabel;
    private JComboBox<String> physicalDriveNumber;
    private JButton cancelButton;
    private JButton okButton;


	private int position;

    public PhysicalDriveView(EmulatorQemuMachineControl emc, int position) {
        super(emc);

        this.position = position;

        windowContent = new JPanel();

        gridLayout = new GridLayout(2, 2);

        windowContent.setLayout(gridLayout);

        physicalDriveLabel = new JLabel("Choose the physical drive number:");

        windowContent.add(physicalDriveLabel);

        String[] physicalDriveNumberOptions = {"", "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "10"};

        this.physicalDriveNumber = new JComboBox<String>(
                physicalDriveNumberOptions);

        windowContent.add(physicalDriveNumber);

        okButton = new JButton("OK");

        cancelButton = new JButton("Cancel");

        windowContent.add(cancelButton);

        windowContent.add(okButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Physical Drive Choice");
    }

    private void rechecks() {
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
        okButton.addActionListener(listener);
    }

    public void configureStandardMode() {
        cancelButton.setActionCommand("cancelButton");
        okButton.setActionCommand("okButton");
    }

    public JComboBox<String> getPhysicalDriveNumber() {
        return physicalDriveNumber;
    }
    

	@Override
	public void applyView(IemultorStore store) {
		 if (position == 1) {
	            if (eQControl.getMachineModel().getFirstHardDiskOption() != null) {
	                if (eQControl.getMachineModel().getFirstHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(eQControl
	                                            .getMachineModel()
	                                            .getFirstHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 2) {
	            if (eQControl.getMachineModel().getSecondHardDiskOption() != null) {
	                if (eQControl.getMachineModel().getSecondHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(eQControl
	                                            .getMachineModel()
	                                            .getSecondHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 3) {
	            if (eQControl.getMachineModel().getThirdHardDiskOption() != null) {
	                if (eQControl.getMachineModel().getThirdHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(eQControl
	                                            .getMachineModel()
	                                            .getThirdHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 4) {
	            if (eQControl.getMachineModel().getFourthHardDiskOption() != null) {
	                if (eQControl.getMachineModel().getFourthHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(eQControl
	                                            .getMachineModel()
	                                            .getFourthHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        }

	        this.rechecks();
		
	}
}
