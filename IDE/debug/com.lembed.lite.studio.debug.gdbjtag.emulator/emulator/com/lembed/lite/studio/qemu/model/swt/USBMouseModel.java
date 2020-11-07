package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBMouseModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBMouseModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getUsbMouseOption() != null) {
            if (this.myfile.getMachineModel().getUsbMouseOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()])) {
            super.setOption("mouse",
                    OptionsEnumModel.USBMOUSEOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbMouseOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBMOUSEOPTION.getValor());
        this.myfile.getMachineModel().setUsbMouseOption("false");
    }
}
