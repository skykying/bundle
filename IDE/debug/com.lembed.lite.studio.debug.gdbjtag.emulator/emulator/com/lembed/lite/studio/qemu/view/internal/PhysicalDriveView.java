package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;

public class PhysicalDriveView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel physicalDriveLabel;

    private JComboBox<String> physicalDriveNumber;

    private JButton cancelButton;

    private JButton okButton;

    public PhysicalDriveView(FileControl myfile, int position) {
        super();

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

        this.setContentPane(windowContent);

        this.setTitle("JavaQemu - Physical Drive Choice");

        if (position == 1) {
            if (myfile.getFilemodel().getFirstHardDiskOption() != null) {
                if (myfile.getFilemodel().getFirstHardDiskOption()
                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                    this.physicalDriveNumber
                            .setSelectedIndex(Integer
                                    .parseInt(myfile
                                            .getFilemodel()
                                            .getFirstHardDiskOption()
                                            .substring(
                                                    ("\\" + "\\" + "." + "\\"
                                                    + "PhysicalDrive")
                                                    .length())) + 1);
                }
            }
        } else if (position == 2) {
            if (myfile.getFilemodel().getSecondHardDiskOption() != null) {
                if (myfile.getFilemodel().getSecondHardDiskOption()
                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                    this.physicalDriveNumber
                            .setSelectedIndex(Integer
                                    .parseInt(myfile
                                            .getFilemodel()
                                            .getSecondHardDiskOption()
                                            .substring(
                                                    ("\\" + "\\" + "." + "\\"
                                                    + "PhysicalDrive")
                                                    .length())) + 1);
                }
            }
        } else if (position == 3) {
            if (myfile.getFilemodel().getThirdHardDiskOption() != null) {
                if (myfile.getFilemodel().getThirdHardDiskOption()
                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                    this.physicalDriveNumber
                            .setSelectedIndex(Integer
                                    .parseInt(myfile
                                            .getFilemodel()
                                            .getThirdHardDiskOption()
                                            .substring(
                                                    ("\\" + "\\" + "." + "\\"
                                                    + "PhysicalDrive")
                                                    .length())) + 1);
                }
            }
        } else if (position == 4) {
            if (myfile.getFilemodel().getFourthHardDiskOption() != null) {
                if (myfile.getFilemodel().getFourthHardDiskOption()
                        .startsWith("\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                    this.physicalDriveNumber
                            .setSelectedIndex(Integer
                                    .parseInt(myfile
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

    private void rechecks() {
        this.pack();
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
}
