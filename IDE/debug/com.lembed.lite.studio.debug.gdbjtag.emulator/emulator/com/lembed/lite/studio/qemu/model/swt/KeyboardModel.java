package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class KeyboardModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public KeyboardModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getMachineModel().getKeyboardLayoutLanguage() != null) {
            this.setOption(this.myfile.getMachineModel().getKeyboardLayoutLanguage());
        }
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.KEYBOARDOPTION.getValor());
        this.myfile.getMachineModel().setKeyboardLayoutLanguage(option);
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setKeyboardLayoutLanguage("");
    }
}
