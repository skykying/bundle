package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class VMClosingModel {

    private JContainerView view;
    private EmulationControl myemulation;

    public VMClosingModel(JContainerView view, EmulationControl myemulation) {
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

    public void setView(JContainerView view) {
        this.view = view;
    }

    public void setEmulationControl(EmulationControl myemulation) {
        this.myemulation = myemulation;
    }
}
