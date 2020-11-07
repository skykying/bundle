package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class NameModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public NameModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);

        this.myfile = myfile;

        if (this.myfile.getMachineModel().getNameOption() != null) {
            this.setOption(this.myfile.getMachineModel().getNameOption());
        }
    }

    public void setOption(String option) {
        super.setOption(option,
                OptionsEnumModel.NAMEOPTION.getValor());
        this.myfile.getMachineModel()
                .setNameOption(option);
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setNameOption("");
    }
}
