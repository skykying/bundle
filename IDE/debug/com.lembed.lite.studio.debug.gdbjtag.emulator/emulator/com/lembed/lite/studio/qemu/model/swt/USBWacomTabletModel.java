package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBWacomTabletModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBWacomTabletModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getMachineModel().getUsbWacomTabletOption() != null) {
            if (myfile.getMachineModel().getUsbWacomTabletOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()])) {
            super.setOption("wacom-tablet",
                    OptionsEnumModel.USBWACOMTABLETOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbWacomTabletOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBWACOMTABLETOPTION.getValor());
        this.myfile.getMachineModel().setUsbWacomTabletOption("false");
    }
}
