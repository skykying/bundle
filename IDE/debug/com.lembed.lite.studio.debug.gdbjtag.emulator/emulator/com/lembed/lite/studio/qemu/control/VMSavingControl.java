package com.lembed.lite.studio.qemu.control;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.model.VMSavingModel;
import com.lembed.lite.studio.qemu.view.internal.VMSavingView;

public class VMSavingControl {

    private VMSavingModel mymodel;

    private VMSavingView myview;

    public VMSavingControl(JPanel jPanel) {
        this.mymodel = new VMSavingModel();
        this.myview = new VMSavingView(jPanel);
    }

    public String getSavedVMPath() {
        return this.mymodel.getSavedVMPath();
    }

    public void setSavedVMPath(String savedVMPath) {
        this.mymodel.setSavedVMPath(savedVMPath);
    }

    public boolean chooseFile() {
        return this.myview.chooseFile();
    }

    public VMSavingView getMyview() {
        return myview;
    }

    public void setVMSavingViewJPanel(JPanel jPanel) {
        this.myview.setJpanel(jPanel);
    }
}
