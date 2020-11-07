package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.VMOpeningModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.swt.VMOpeningView;

public class VMOpeningControl {

    private VMOpeningModel mymodel;
    private VMOpeningView myview;

    public VMOpeningControl(JContainerView view, EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.mymodel = new VMOpeningModel(myemulation, myfile);
        this.myview = new VMOpeningView(view, this.mymodel.getMyfile().getMachineModel().getMachineName());
    }

    public boolean starts(EmulatorQemuMachineControl myfile) {
        this.mymodel.starts();
        this.myview.setChosenMachineName(myfile.getMachineModel().getMachineName());
        this.myview.starts(this.mymodel.getDiskImagePath(),
                myfile.getMachineModel().getSecondHardDiskOption(),
                myfile.getMachineModel().getThirdHardDiskOption(),
                myfile.getMachineModel().getFourthHardDiskOption(),
                myfile.getMachineModel().getRamSize());
        return true;
    }

    public void setView(JContainerView view) {
        this.myview.setView(view);
    }

    public void setMyemulation(EmulationControl myemulation) {
        this.mymodel.setMyemulation(myemulation);
    }

    public void setMyfile(EmulatorQemuMachineControl myfile) {
        this.mymodel.setMyfile(myfile);
    }
}
