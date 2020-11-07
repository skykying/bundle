package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class NetworkManagerModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public NetworkManagerModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.NETWORKNIC1OPTION.getValor());
    }

    public void buildIt(String option) {
        myfile.getMachineModel().setFirstNetworkNICOption(option);
    }
}
