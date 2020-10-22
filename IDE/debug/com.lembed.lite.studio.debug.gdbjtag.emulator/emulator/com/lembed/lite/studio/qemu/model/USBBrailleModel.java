package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBBrailleModel extends OptionsControl {

    private FileControl myfile;

    public USBBrailleModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getUsbBrailleOption() != null) {
            if (this.myfile.getFilemodel().getUsbBrailleOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()])) {
            super.setOption("braille",
                    OptionsEnumModel.USBBRAILLEOPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbBrailleOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBBRAILLEOPTION.getValor());
        this.myfile.getFilemodel().setUsbBrailleOption("false");
    }
}
