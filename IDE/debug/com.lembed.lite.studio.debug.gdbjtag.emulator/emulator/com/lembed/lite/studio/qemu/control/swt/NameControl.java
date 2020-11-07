package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.NameModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class NameControl {

    private JContainerView jview;
    private NameModel nameModel;

    public NameControl(EmulationControl ec, EmulatorQemuMachineControl emc, JContainerView view) {
        jview = view;
        nameModel = new NameModel(ec, emc);
    }

    public void updateMe() {
        if (this.jview.getSelectedPanel().getTitle() != null) {
            if (!this.jview.getSelectedPanel().getTitle().isEmpty()) {
                this.nameModel.setOption(this.jview.getSelectedPanel().getTitle());
            } else {
                this.nameModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
            }
        } else {
            this.nameModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
        }
    }
}