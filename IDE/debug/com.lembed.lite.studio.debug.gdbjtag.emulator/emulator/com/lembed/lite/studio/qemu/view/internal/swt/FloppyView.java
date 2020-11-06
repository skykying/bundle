package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class FloppyView extends DeviceViewWithFileChooser {

    private static final long serialVersionUID = 1L;

    private JPanel jpanel;

    private JLabel floppyDiskALabel;

    private JTextField floppyDiskAText;

    private JButton floppyDiskADriveSelection,
            floppyDiskAImageSelection,
            floppyDiskAReadOnlyVirtualFAT,
            floppyDiskAReadWriteVirtualFAT;

    private JLabel floppyDiskBLabel;

    private JTextField floppyDiskBText;

    private JButton floppyDiskBDriveSelection,
            floppyDiskBImageSelection,
            floppyDiskBReadOnlyVirtualFAT,
            floppyDiskBReadWriteVirtualFAT;

    private JButton okButton, eraseButton;

    private GridLayout gridLayout;

	private String title;


    public FloppyView(FileControl myfile) {
        super(myfile,null);

        this.jpanel = new JPanel();

        this.gridLayout = new GridLayout(9, 2);

        this.setTitle("JavaQemu - Floppy Disk Options");

        this.add(jpanel);

        this.setJpanel(jpanel);

        this.jpanel.setLayout(gridLayout);

        this.floppyDiskALabel = new JLabel("Insert your Floppy Disk A option:");
        this.floppyDiskAText = new JTextField("");

        this.floppyDiskADriveSelection = new JButton("Select a Floppy Drive for the Floppy Disk A!");
        this.floppyDiskAImageSelection = new JButton("Select a Floppy Disk A Image file!");

        this.floppyDiskALabel = new JLabel("Insert your Floppy Disk A option:");

        this.floppyDiskAReadOnlyVirtualFAT = new JButton("Select a Floppy Disk A Read-Only Virtual FAT!");
        this.floppyDiskAReadWriteVirtualFAT = new JButton("Select a Floppy Disk A Read-Write Virtual FAT!");

        this.floppyDiskBText = new JTextField("");

        this.floppyDiskBDriveSelection = new JButton("Select a Floppy Drive for the Floppy Disk B!");

        this.floppyDiskBImageSelection = new JButton("Select a Floppy Disk B Image file!");

        this.floppyDiskBLabel = new JLabel("Insert your Floppy Disk B option:");

        this.floppyDiskBReadOnlyVirtualFAT = new JButton("Select a Floppy Disk B Read-Only Virtual FAT!");
        this.floppyDiskBReadWriteVirtualFAT = new JButton("Select a Floppy Disk B Read-Write Virtual FAT!");

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        this.jpanel.add(this.floppyDiskALabel);
        this.jpanel.add(this.floppyDiskAText);
        this.jpanel.add(this.floppyDiskADriveSelection);
        this.jpanel.add(this.floppyDiskAImageSelection);
        this.jpanel.add(this.floppyDiskAReadOnlyVirtualFAT);
        this.jpanel.add(this.floppyDiskAReadWriteVirtualFAT);

        this.jpanel.add(new JLabel());
        this.jpanel.add(new JLabel());

        this.jpanel.add(this.floppyDiskBLabel);
        this.jpanel.add(this.floppyDiskBText);
        this.jpanel.add(this.floppyDiskBDriveSelection);
        this.jpanel.add(this.floppyDiskBImageSelection);
        this.jpanel.add(this.floppyDiskBReadOnlyVirtualFAT);
        this.jpanel.add(this.floppyDiskBReadWriteVirtualFAT);

        this.jpanel.add(new JLabel());
        this.jpanel.add(new JLabel());

        this.jpanel.add(this.okButton);
        this.jpanel.add(this.eraseButton);

        this.add(jpanel);
    }

    private void rechecks() {
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        this.floppyDiskADriveSelection.addActionListener(listener);
        this.floppyDiskAImageSelection.addActionListener(listener);
        this.floppyDiskAReadOnlyVirtualFAT.addActionListener(listener);
        this.floppyDiskAReadWriteVirtualFAT.addActionListener(listener);

        this.floppyDiskBDriveSelection.addActionListener(listener);
        this.floppyDiskBImageSelection.addActionListener(listener);
        this.floppyDiskBReadOnlyVirtualFAT.addActionListener(listener);
        this.floppyDiskBReadWriteVirtualFAT.addActionListener(listener);

        this.eraseButton.addActionListener(listener);
        this.okButton.addActionListener(listener);
    }

    public void configureStandardMode() {
        this.floppyDiskADriveSelection.setActionCommand("floppyDiskADriveSelection");
        this.floppyDiskAImageSelection.setActionCommand("floppyDiskAImageSelection");
        this.floppyDiskAReadOnlyVirtualFAT.setActionCommand("floppyDiskAReadOnlyVirtualFAT");
        this.floppyDiskAReadWriteVirtualFAT.setActionCommand("floppyDiskAReadWriteVirtualFAT");

        this.floppyDiskBDriveSelection.setActionCommand("floppyDiskBDriveSelection");
        this.floppyDiskBImageSelection.setActionCommand("floppyDiskBImageSelection");
        this.floppyDiskBReadOnlyVirtualFAT.setActionCommand("floppyDiskBReadOnlyVirtualFAT");
        this.floppyDiskBReadWriteVirtualFAT.setActionCommand("floppyDiskBReadWriteVirtualFAT");

        this.eraseButton.setActionCommand("eraseButton");
        this.okButton.setActionCommand("okButton");
    }

    public JTextField getFloppyDiskAText() {
        return floppyDiskAText;
    }

    public JTextField getFloppyDiskBText() {
        return floppyDiskBText;
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
	public void doSave(IemultorStore store) {
        if (fileControl.getFilemodel().getFloppyDiskA() != null) {
            this.floppyDiskAText.setText(fileControl.getFilemodel()
                    .getFloppyDiskA());
        }

        if (fileControl.getFilemodel().getFloppyDiskB() != null) {
            this.floppyDiskBText.setText(fileControl.getFilemodel()
                    .getFloppyDiskB());
        }

        this.rechecks();
	}
}
