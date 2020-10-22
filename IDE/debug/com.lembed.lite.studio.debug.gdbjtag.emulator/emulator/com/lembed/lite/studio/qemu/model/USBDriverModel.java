package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBDriverModel extends OptionsControl {

    private FileControl myfile;

    public USBDriverModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getFilemodel().getUsbDriverOption() != null) {
            if (myfile.getFilemodel().getUsbDriverOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()])) {
            super.setOption("",
                    OptionsEnumModel.USBDRIVEROPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbDriverOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getFilemodel().setUsbDriverOption("false");
    }
}
