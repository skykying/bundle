package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBTabletModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBTabletModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getMachineModel().getUsbTabletOption() != null) {
            if (myfile.getMachineModel().getUsbTabletOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()])) {
            super.setOption("tablet",
                    OptionsEnumModel.USBTABLETOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbTabletOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBTABLETOPTION.getValor());
        this.myfile.getMachineModel().setUsbTabletOption("false");
    }
}
