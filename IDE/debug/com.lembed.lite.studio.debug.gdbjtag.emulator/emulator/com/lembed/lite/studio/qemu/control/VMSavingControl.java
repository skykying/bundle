package com.lembed.lite.studio.qemu.control;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.model.VMSavingModel;
import com.lembed.lite.studio.qemu.view.internal.VMSavingView;

public class VMSavingControl {

    private VMSavingModel vMSavingModel;

    private VMSavingView vMSavingView;

    public VMSavingControl(JPanel jPanel) {
        this.vMSavingModel = new VMSavingModel();
        this.vMSavingView = new VMSavingView(jPanel);
    }

    public String getSavedVMPath() {
        return this.vMSavingModel.getSavedVMPath();
    }

    public void setSavedVMPath(String savedVMPath) {
        this.vMSavingModel.setSavedVMPath(savedVMPath);
    }

    public boolean chooseFile() {
        return this.vMSavingView.chooseFile();
    }

    public VMSavingView getView() {
        return vMSavingView;
    }

    public void setVMSavingViewJPanel(JPanel jPanel) {
        this.vMSavingView.setJpanel(jPanel);
    }
}
