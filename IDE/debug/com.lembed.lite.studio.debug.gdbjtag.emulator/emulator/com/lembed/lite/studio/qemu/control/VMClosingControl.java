package com.lembed.lite.studio.qemu.control;

import com.lembed.lite.studio.qemu.model.VMClosingModel;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class VMClosingControl {

    private VMClosingModel mymodel;

    public VMClosingControl(JContainerView view, EmulationControl myemulation) {
        this.mymodel = new VMClosingModel(view, myemulation);
    }

    public boolean starts(Boolean removeAll) {
        return this.mymodel.starts(removeAll);
    }

    public void setView(JContainerView view) {
        this.mymodel.setView(view);
    }

    public void setEmulation(EmulationControl myemulation) {
        this.mymodel.setMyemulation(myemulation);
    }
}
