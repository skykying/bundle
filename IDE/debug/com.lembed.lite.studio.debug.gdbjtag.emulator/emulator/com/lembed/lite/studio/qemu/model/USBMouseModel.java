package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBMouseModel extends OptionsControl {

    private FileControl myfile;

    public USBMouseModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getUsbMouseOption() != null) {
            if (this.myfile.getFilemodel().getUsbMouseOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()])) {
            super.setOption("mouse",
                    OptionsEnumModel.USBMOUSEOPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbMouseOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBMOUSEOPTION.getValor());
        this.myfile.getFilemodel().setUsbMouseOption("false");
    }
}
