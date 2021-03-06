package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.FloppyModel;
import com.lembed.lite.studio.qemu.view.internal.FloppyView;

public class FloppyControl implements ActionListener {

    private FloppyModel mymodel;
    private FloppyView myview;
    private FileControl myfile;

    public FloppyControl(EmulationControl myemulation, FileControl myfile) {
        this.mymodel = new FloppyModel(myemulation, myfile);
        this.myview = new FloppyView(myfile);
        this.myfile = myfile;
        if (myfile.getFilemodel().getFloppyDiskA() != null) {
            this.mymodel.setFloppyDiskAOption(this.myview.getFloppyDiskAText()
                    .getText(), "");
        }
        if (myfile.getFilemodel().getFloppyDiskB() != null) {
            this.mymodel.setFloppyDiskBOption(this.myview.getFloppyDiskBText()
                    .getText(), "");
        }
    }

    public void starts() {
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
    }

    public void change_my_visibility(Boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {

            this.myview.getFloppyDiskAText().setText("");
            this.mymodel.setFloppyDiskAOption("", "image");
            this.myfile.getFilemodel().setFloppyDiskA("");

            this.myview.getFloppyDiskBText().setText("");
            this.mymodel.setFloppyDiskBOption("", "image");
            this.myfile.getFilemodel().setFloppyDiskB("");

            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (!this.myview.getFloppyDiskAText().getText()
                    .equals(this.myview.getChoice())) {
                this.mymodel.setFloppyDiskAOption(this.myview.getFloppyDiskAText()
                        .getText(), "");
                this.myfile.getFilemodel().setFloppyDiskA(
                        this.myview.getFloppyDiskAText().getText());
            }
            if (!this.myview.getFloppyDiskBText().getText()
                    .equals(this.myview.getChoice())) {
                this.mymodel.setFloppyDiskBOption(this.myview.getFloppyDiskBText()
                        .getText(), "");
                this.myfile.getFilemodel().setFloppyDiskB(
                        this.myview.getFloppyDiskBText().getText());
            }
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("floppyDiskADriveSelection")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskAText().setText(this.myview.getChoice());
                this.mymodel.setFloppyDiskAOption(this.myview.getChoice(), "drive");
                this.myfile.getFilemodel().setFloppyDiskA(this.myview.getChoice());
            }
        } else if (e.getActionCommand().equals("floppyDiskAImageSelection")) {
            if (this.myview.chooseFloppyFiles()) {
                this.myview.getFloppyDiskAText().setText(this.myview.getChoice());
                this.mymodel.setFloppyDiskAOption(this.myview.getChoice(), "image");
                this.myfile.getFilemodel().setFloppyDiskA(this.myview.getChoice());
            }
        } else if (e.getActionCommand().equals("floppyDiskAReadOnlyVirtualFAT")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskAText().setText(this.convertFatReadOnly(this.myview.getChoice()));
                this.mymodel.setFloppyDiskAOption(this.convertFatReadOnly(this.myview.getChoice()), "drive");
                this.myfile.getFilemodel().setFloppyDiskA(this.convertFatReadOnly(this.myview.getChoice()));
            }
        } else if (e.getActionCommand().equals("floppyDiskAReadWriteVirtualFAT")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskAText().setText(this.convertFatReadWrite(this.myview.getChoice()));
                this.mymodel.setFloppyDiskAOption(this.convertFatReadWrite(this.myview.getChoice()), "drive");
                this.myfile.getFilemodel().setFloppyDiskA(this.convertFatReadWrite(this.myview.getChoice()));
            }
        } else if (e.getActionCommand().equals("floppyDiskBDriveSelection")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskBText().setText(this.myview.getChoice());
                this.mymodel.setFloppyDiskBOption(this.myview.getChoice(), "drive");
                this.myfile.getFilemodel().setFloppyDiskB(this.myview.getChoice());
            }
        } else if (e.getActionCommand().equals("floppyDiskBImageSelection")) {
            if (this.myview.chooseFloppyFiles()) {
                this.myview.getFloppyDiskBText().setText(this.myview.getChoice());
                this.mymodel.setFloppyDiskBOption(this.myview.getChoice(), "image");
                this.myfile.getFilemodel().setFloppyDiskB(this.myview.getChoice());
            }
        } else if (e.getActionCommand().equals("floppyDiskBReadOnlyVirtualFAT")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskBText().setText(this.convertFatReadOnly(this.myview.getChoice()));
                this.mymodel.setFloppyDiskBOption(this.convertFatReadOnly(this.myview.getChoice()), "drive");
                this.myfile.getFilemodel().setFloppyDiskB(this.convertFatReadOnly(this.myview.getChoice()));
            }
        } else if (e.getActionCommand().equals("floppyDiskBReadWriteVirtualFAT")) {
            if (this.myview.chooseFloppyDrives()) {
                this.myview.getFloppyDiskBText().setText(this.convertFatReadWrite(this.myview.getChoice()));
                this.mymodel.setFloppyDiskBOption(this.convertFatReadWrite(this.myview.getChoice()), "drive");
                this.myfile.getFilemodel().setFloppyDiskB(this.convertFatReadWrite(this.myview.getChoice()));
            }
        }
    }

    public String convertFatReadOnly(String basis) {
        return "fat:floppy:" + basis;
    }

    public String convertFatReadWrite(String basis) {
        return "fat:floppy:rw:" + basis;
    }
}
