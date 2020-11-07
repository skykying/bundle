package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class RAMModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public RAMModel(EmulationControl myemulation,
            EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getMachineModel().getRamSize() == null) {
            this.setOption("128.000");
        } else {
            this.setOption(this.myfile.getMachineModel().getRamSize());
        }
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.RAMSIZE.getValor());
        this.myfile.getMachineModel().setRamSize(option);
    }
}
