package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBKeyboardModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBKeyboardModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getMachineModel().getUsbKeyboardOption() != null) {
            if (myfile.getMachineModel().getUsbKeyboardOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()])) {
            super.setOption("keyboard",
                    OptionsEnumModel.USBKEYBOARDOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbKeyboardOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBKEYBOARDOPTION.getValor());
        this.myfile.getMachineModel().setUsbKeyboardOption("false");
    }
}
