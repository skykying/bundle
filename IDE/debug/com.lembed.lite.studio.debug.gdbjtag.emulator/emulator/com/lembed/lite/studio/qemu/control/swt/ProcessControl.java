package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.ProcessModel;
import com.lembed.lite.studio.qemu.view.internal.swt.EmulationView;

public class ProcessControl {

    private ProcessModel processModel;
    private EmulationView emulationView;

    public ProcessControl(Process myProcess, String machineName, EmulationView myView, String qemuPathDir) {
        this.emulationView = myView;
        this.processModel = new ProcessModel(myProcess, machineName, qemuPathDir, myView);
    }

    public ProcessModel getMyModel() {
        return this.processModel;
    }

    public String getMachineName() {
        return this.processModel.getMachineName();
    }

    public EmulationView getMyView() {
        return emulationView;
    }

    public void run() throws InterruptedException {
        this.processModel.runMaster();
    }
}
