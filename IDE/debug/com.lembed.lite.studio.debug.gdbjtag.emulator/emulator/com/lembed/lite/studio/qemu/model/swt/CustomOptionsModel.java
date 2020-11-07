package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class CustomOptionsModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public CustomOptionsModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getCustomOptions() != null) {
            this.setOption(this.myfile.getMachineModel().getCustomOptions().replace("\n", " "));
        }
    }

    public void setOption(String option) {
        super.setOption(option,
                OptionsEnumModel.CUSTOMOPTIONS.getValor());
    }

    public void setFileOption(String option) {
        this.myfile.getMachineModel().setCustomOptions(option);
    }
}
