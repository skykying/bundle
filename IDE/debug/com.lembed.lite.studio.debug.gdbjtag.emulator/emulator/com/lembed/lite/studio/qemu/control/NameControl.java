package com.lembed.lite.studio.qemu.control;

import com.lembed.lite.studio.qemu.model.NameModel;
import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.JContainerView;

public class NameControl {

    private JContainerView myView;
    private NameModel myModel;

    public NameControl(EmulationControl myemulation, FileControl myfile, JContainerView view) {
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
