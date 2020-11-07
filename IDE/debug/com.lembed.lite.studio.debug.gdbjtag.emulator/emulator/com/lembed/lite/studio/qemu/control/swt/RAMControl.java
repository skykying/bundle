package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.RAMModel;

public class RAMControl {

    private RAMModel mymodel;

    public RAMControl(EmulationControl myemulation,
            EmulatorQemuMachineControl myfile) {
        this.mymodel = new RAMModel(myemulation, myfile);
    }

    public void setOption(String option) {
        this.mymodel.setOption(option);
    }

    public String getOption() {
        return this.mymodel.getOption();
    }
}
