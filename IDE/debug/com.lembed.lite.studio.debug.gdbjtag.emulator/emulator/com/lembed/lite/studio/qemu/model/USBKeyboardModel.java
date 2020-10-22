package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBKeyboardModel extends OptionsControl {

    private FileControl myfile;

    public USBKeyboardModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getFilemodel().getUsbKeyboardOption() != null) {
            if (myfile.getFilemodel().getUsbKeyboardOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()])) {
            super.setOption("keyboard",
                    OptionsEnumModel.USBKEYBOARDOPTION.getValor());
            this.myfile.getFilemodel()
                    .setUsbKeyboardOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBKEYBOARDOPTION.getValor());
        this.myfile.getFilemodel().setUsbKeyboardOption("false");
    }
}
