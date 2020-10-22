package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.CDROMModel;
import com.lembed.lite.studio.qemu.view.internal.CDROMView;

public class CDROMControl implements ActionListener {

    private CDROMModel cdRomModel;
    private CDROMView cdRomView;
    private FileControl fileControl;

    public CDROMControl(EmulationControl myemulation, FileControl myfile) {
        cdRomModel = new CDROMModel(myemulation, myfile);
        cdRomView = new CDROMView(myfile);
        fileControl = myfile;
        if (fileControl.getFilemodel().getCdrom() != null) {
            cdRomModel.setCdromOption(cdRomView.getCdromText().getText(), "");
        }
    }

    public void starts() {
        cdRomView.configureListener(this);
        cdRomView.configureStandardMode();
    }

    public void change_my_visibility(Boolean value) {
        cdRomView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            if (!cdRomView.getCdromText().getText().isEmpty()) {
                cdRomView.getCdromText().setText("");
            }

            cdRomModel.setCdromOption("", "image");
            fileControl.getFilemodel().setCdrom("");

            cdRomView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (!cdRomView.getCdromText().getText().equals(cdRomView.getChoice())) {
                cdRomModel.setCdromOption(cdRomView.getCdromText().getText(), "");
                fileControl.getFilemodel().setCdrom(cdRomView.getCdromText().getText());
            }
            cdRomView.setVisible(false);
        } else if (e.getActionCommand().equals("cdromDriveSelection")) {
            if (cdRomView.chooseCDROMDrives()) {
                cdRomView.getCdromText().setText(cdRomView.getChoice());
                cdRomModel.setCdromOption(cdRomView.getChoice(), "drive");
                fileControl.getFilemodel().setCdrom(cdRomView.getChoice());
            }
        } else if (e.getActionCommand().equals("diskImageSelection")) {
            if (cdRomView.chooseCDROMFiles()) {
                cdRomView.getCdromText().setText(cdRomView.getChoice());
                cdRomModel.setCdromOption(cdRomView.getChoice(), "image");
                fileControl.getFilemodel().setCdrom(cdRomView.getChoice());
            }
        }
    }
}
