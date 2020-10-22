package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBWacomTabletModel extends OptionsControl {

    private FileControl myfile;

    public USBWacomTabletModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getFilemodel().getUsbWacomTabletOption() != null) {
            if (myfile.getFilemodel().getUsbWacomTabletOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()])) {
            super.setOption("wacom-tablet",
                    OptionsEnumModel.USBWACOMTABLETOPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbWacomTabletOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBWACOMTABLETOPTION.getValor());
        this.myfile.getFilemodel().setUsbWacomTabletOption("false");
    }
}
