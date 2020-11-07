package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.RAMModel;

public class RAMControl {

    private RAMModel ramModel;

    public RAMControl(EmulationControl ec,
            EmulatorQemuMachineControl emc) {
        this.ramModel = new RAMModel(ec, emc);
    }

    public void setOption(String option) {
        this.ramModel.setOption(option);
    }

    public String getOption() {
        return this.ramModel.getOption();
    }
}
