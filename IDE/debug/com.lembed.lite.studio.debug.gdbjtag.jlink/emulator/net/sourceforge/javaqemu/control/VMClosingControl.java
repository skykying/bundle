package net.sourceforge.javaqemu.control;

import net.sourceforge.javaqemu.model.VMClosingModel;
import net.sourceforge.javaqemu.view.JQemuView;

public class VMClosingControl {

    private VMClosingModel mymodel;

    public VMClosingControl(JQemuView view, EmulationControl myemulation) {
        this.mymodel = new VMClosingModel(view, myemulation);
    }

    public boolean starts(Boolean removeAll) {
        return this.mymodel.starts(removeAll);
    }

    public void setView(JQemuView view) {
        this.mymodel.setView(view);
    }

    public void setMyemulation(EmulationControl myemulation) {
        this.mymodel.setMyemulation(myemulation);
    }
}
