package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;
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

    public PhysicalDriveView(FileControl myfile, int position) {
        super(myfile);

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
	public void doSave(IemultorStore store) {
		 if (position == 1) {
	            if (fileControl.getFilemodel().getFirstHardDiskOption() != null) {
	                if (fileControl.getFilemodel().getFirstHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(fileControl
	                                            .getFilemodel()
	                                            .getFirstHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 2) {
	            if (fileControl.getFilemodel().getSecondHardDiskOption() != null) {
	                if (fileControl.getFilemodel().getSecondHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(fileControl
	                                            .getFilemodel()
	                                            .getSecondHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 3) {
	            if (fileControl.getFilemodel().getThirdHardDiskOption() != null) {
	                if (fileControl.getFilemodel().getThirdHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(fileControl
	                                            .getFilemodel()
	                                            .getThirdHardDiskOption()
	                                            .substring(
	                                                    ("\\" + "\\" + "." + "\\"
	                                                    + "PhysicalDrive")
	                                                    .length())) + 1);
	                }
	            }
	        } else if (position == 4) {
	            if (fileControl.getFilemodel().getFourthHardDiskOption() != null) {
	                if (fileControl.getFilemodel().getFourthHardDiskOption()
	                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
	                    this.physicalDriveNumber
	                            .setSelectedIndex(Integer
	                                    .parseInt(fileControl
	                                            .getFilemodel()
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
