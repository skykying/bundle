package com.lembed.lite.studio.qemu.control;

import com.lembed.lite.studio.qemu.model.VMOpeningModel;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.VMOpeningView;

public class VMOpeningControl {

    private VMOpeningModel mymodel;
    private VMOpeningView myview;

    public VMOpeningControl(JQemuView view, EmulationControl myemulation, FileControl myfile) {
        this.mymodel = new VMOpeningModel(myemulation, myfile);
        this.myview = new VMOpeningView(view, this.mymodel.getMyfile().getFilemodel().getMachineName());
    }

    public boolean starts(FileControl myfile) {
        this.mymodel.starts();
        this.myview.setChosenMachineName(myfile.getFilemodel().getMachineName());
        this.myview.starts(this.mymodel.getDiskImagePath(),
                myfile.getFilemodel().getSecondHardDiskOption(),
                myfile.getFilemodel().getThirdHardDiskOption(),
                myfile.getFilemodel().getFourthHardDiskOption(),
                myfile.getFilemodel().getRamSize());
        return true;
    }

    public void setView(JQemuView view) {
        this.myview.setView(view);
    }

    public void setMyemulation(EmulationControl myemulation) {
        this.mymodel.setMyemulation(myemulation);
    }

    public void setMyfile(FileControl myfile) {
        this.mymodel.setMyfile(myfile);
    }
}
