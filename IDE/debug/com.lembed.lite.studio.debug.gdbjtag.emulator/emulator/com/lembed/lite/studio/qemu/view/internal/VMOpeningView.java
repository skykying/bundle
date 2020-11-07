package com.lembed.lite.studio.qemu.view.internal;

import com.lembed.lite.studio.qemu.view.JContainerView;

public class VMOpeningView {

    private JContainerView parent;

    private JPanelCreationView view;

    private String chosenMachineName;

    public VMOpeningView(JContainerView parent, String chosenMachineName) {
        this.view = null;
        this.parent = parent;
        this.chosenMachineName = chosenMachineName;
    }

    public boolean starts(String diskImagePath, String secondDiskImagePath,
            String thirdDiskImagePath, String fourthDiskImagePath,
            String ramSize) {
        this.view = (JPanelCreationView) parent.makeVMPanel(chosenMachineName);
        if (ramSize != null) {
            if (!ramSize.isEmpty()) {
                view.setRamSize(ramSize);
            }
        } else {
            this.view.setRamSize("128");
        }
        this.parent.addCreationNewJPanel(view, chosenMachineName);
        return true;
    }

    public void setView(JContainerView parent) {
        this.parent = parent;
    }

    public void setChosenMachineName(String chosenMachineName) {
        this.chosenMachineName = chosenMachineName;
    }
}
