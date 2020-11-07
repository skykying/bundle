package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.VMClosingModel;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class VMClosingControl {

    private VMClosingModel vmClosingModel;

    public VMClosingControl(JContainerView parent, EmulationControl ec) {
        this.vmClosingModel = new VMClosingModel(parent, ec);
    }

    public boolean starts(Boolean removeAll) {
        return this.vmClosingModel.starts(removeAll);
    }

    public void setView(JContainerView view) {
        this.vmClosingModel.setView(view);
    }

    public void setEmulation(EmulationControl ec) {
        this.vmClosingModel.setEmulationControl(ec);
    }
}
