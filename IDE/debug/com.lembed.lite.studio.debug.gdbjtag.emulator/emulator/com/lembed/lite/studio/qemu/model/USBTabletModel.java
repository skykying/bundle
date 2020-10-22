package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBTabletModel extends OptionsControl {

    private FileControl myfile;

    public USBTabletModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getFilemodel().getUsbTabletOption() != null) {
            if (myfile.getFilemodel().getUsbTabletOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()])) {
            super.setOption("tablet",
                    OptionsEnumModel.USBTABLETOPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbTabletOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBTABLETOPTION.getValor());
        this.myfile.getFilemodel().setUsbTabletOption("false");
    }
}
