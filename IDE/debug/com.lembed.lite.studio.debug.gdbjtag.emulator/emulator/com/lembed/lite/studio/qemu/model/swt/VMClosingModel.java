package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class VMClosingModel {

    private JSwtQemuView view;
    private EmulationControl myemulation;

    public VMClosingModel(JSwtQemuView view, EmulationControl myemulation) {
        this.view = view;
        this.myemulation = myemulation;
    }

    public boolean starts(Boolean removeAll) {

        if (removeAll) {
            while (this.view.getSizeOfJTabbedPane() > 0) {
                this.myemulation.removeAllProcesses();
                this.myemulation.closeAllEmulation();
                this.view.removeAllJPanels();
            }
        } else if (this.view.getSizeOfJTabbedPane() > 1) {
            this.myemulation.removesAProcess(this.view.getActivePanel());
            this.myemulation.close_emulation(this.view.getActivePanel());
            this.view.removeCreationNewJPanel();
        }

        return true;
    }

    public void setView(JSwtQemuView view) {
        this.view = view;
    }

    public void setEmulationControl(EmulationControl myemulation) {
        this.myemulation = myemulation;
    }
}
