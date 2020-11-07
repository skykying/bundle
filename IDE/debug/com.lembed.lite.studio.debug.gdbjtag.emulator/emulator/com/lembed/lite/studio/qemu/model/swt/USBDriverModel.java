package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBDriverModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBDriverModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getMachineModel().getUsbDriverOption() != null) {
            if (myfile.getMachineModel().getUsbDriverOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()])) {
            super.setOption("",
                    OptionsEnumModel.USBDRIVEROPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbDriverOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setUsbDriverOption("false");
    }
}
