package net.sourceforge.javaqemu.control;

import net.sourceforge.javaqemu.model.NameModel;
import net.sourceforge.javaqemu.model.OptionsEnumModel;
import net.sourceforge.javaqemu.model.VMConfigurationModel;
import net.sourceforge.javaqemu.view.JQemuView;

public class NameControl {

    private JQemuView myView;
    private NameModel myModel;

    public NameControl(EmulationControl myemulation, FileControl myfile, JQemuView view) {
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
