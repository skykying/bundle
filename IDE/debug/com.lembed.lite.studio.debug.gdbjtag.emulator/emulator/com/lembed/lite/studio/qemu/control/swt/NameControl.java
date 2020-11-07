package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.NameModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.JQemuView;

public class NameControl {

    private JQemuView myView;
    private NameModel myModel;

    public NameControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile, JQemuView view) {
        myView = view;
        myModel = new NameModel(myemulation, myfile);
    }

    public void updateMe() {
        if (this.myView.getSelectedPanel().getTitle() != null) {
            if (!this.myView.getSelectedPanel().getTitle().isEmpty()) {
                this.myModel.setOption(this.myView.getSelectedPanel().getTitle());
            } else {
                this.myModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
            }
        } else {
            this.myModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
        }
    }
}
