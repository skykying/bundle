package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.ProcessModel;
import com.lembed.lite.studio.qemu.view.internal.swt.EmulationView;

public class ProcessControl {

    private ProcessModel myModel;
    private EmulationView myView;

    public ProcessControl(Process myProcess, String machineName, EmulationView myView, String qemuPathDir) {
        this.myView = myView;
        this.myModel = new ProcessModel(myProcess, machineName, qemuPathDir, myView);
    }

    public ProcessModel getMyModel() {
        return this.myModel;
    }

    public String getMachineName() {
        return this.myModel.getMachineName();
    }

    public EmulationView getMyView() {
        return myView;
    }

    public void run() throws InterruptedException {
        this.myModel.runMaster();
    }
}
