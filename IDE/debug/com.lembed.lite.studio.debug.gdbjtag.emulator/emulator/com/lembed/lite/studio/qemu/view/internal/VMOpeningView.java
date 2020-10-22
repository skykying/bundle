package com.lembed.lite.studio.qemu.view.internal;

import com.lembed.lite.studio.qemu.view.JQemuView;

public class VMOpeningView {

    private JQemuView parent;

    private JPanelCreationView view;

    private String chosenMachineName;

    public VMOpeningView(JQemuView parent, String chosenMachineName) {
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

    public void setView(JQemuView parent) {
        this.parent = parent;
    }

    public void setChosenMachineName(String chosenMachineName) {
        this.chosenMachineName = chosenMachineName;
    }
}
