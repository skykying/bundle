package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBBrailleModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public USBBrailleModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getUsbBrailleOption() != null) {
            if (this.myfile.getMachineModel().getUsbBrailleOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()])) {
            super.setOption("braille",
                    OptionsEnumModel.USBBRAILLEOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbBrailleOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBBRAILLEOPTION.getValor());
        this.myfile.getMachineModel().setUsbBrailleOption("false");
    }
}
