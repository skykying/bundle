package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.ImageModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.ImageView;

public class ImageControl implements BaseControl {

    private ImageModel imageModel;
    private ImageView imageView;

    public ImageControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.imageModel = new ImageModel(myemulation, myfile);
        this.imageView = new ImageView(myfile);
    }

    public void starts() {
        this.imageView.configureListener(this);
        this.imageView.configureStandardMode();
    }

    public void setVisible(Boolean value) {
        this.imageView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.imageView.getMtdblock().setText("");
            this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()]);

            this.imageView.getSd().setText("");
            this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()]);

            this.imageView.getPflash().setText("");
            this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()]);
            this.imageView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (!this.imageView.getMtdblock().getText().isEmpty()) {
                this.imageModel.setMtdblock(this.imageView.getMtdblock().getText());
                this.imageModel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()]);
            } else {
                this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()]);
            }
            if (!this.imageView.getSd().getText().isEmpty()) {
                this.imageModel.setSd(this.imageView.getSd().getText());
                this.imageModel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()]);
            } else {
                this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()]);
            }
            if (!this.imageView.getPflash().getText().isEmpty()) {
                this.imageModel.setPflash(this.imageView.getPflash().getText());
                this.imageModel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()]);
            } else {
                this.imageModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()]);
            }
            this.imageView.setVisible(false);
        } else if (e.getActionCommand().equals("mtdblockChooser")) {
            this.imageView.setChoosertitle("Choose a file as the on-board Flash memory image!");
            this.imageView.setFileDescription("File - on-board Flash memory image");
            if (this.imageView.chooseAnyFile()) {
                this.imageView.getMtdblock().setText(this.imageView.getChoice());
            }
        } else if (e.getActionCommand().equals("sdChooser")) {
            this.imageView.setChoosertitle("Choose a file as the SecureDigital card image!");
            this.imageView.setFileDescription("File - SecureDigital card image");
            if (this.imageView.chooseAnyFile()) {
                this.imageView.getSd().setText(this.imageView.getChoice());
            }
        } else if (e.getActionCommand().equals("pflashChooser")) {
            this.imageView.setChoosertitle("Choose a file as the parallel flash image!");
            this.imageView.setFileDescription("File - parallel flash image");
            if (this.imageView.chooseAnyFile()) {
                this.imageView.getPflash().setText(this.imageView.getChoice());
            }
        }
    }

}
