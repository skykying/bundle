package com.lembed.lite.studio.qemu.model.swt;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class MonitorModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    // Association "Description" - "Option".
    private static HashMap<String, String> trueOptions;

    // Association "Option" - "Description".
    private static HashMap<String, String> falseOptions;

    static {
        trueOptions = new HashMap<String, String>();
        trueOptions.put("", "");
        trueOptions.put("Graphical Mode", "vc");
        trueOptions.put("Non Graphical Mode", "stdio");

        falseOptions = new HashMap<String, String>();
        falseOptions.put("", "");
        falseOptions.put("vc", "Graphical Mode");
        falseOptions.put("stdio", "Non Graphical Mode");
    }

    public MonitorModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getMonitorOption() != null) {
            this.setOption(falseOptions.get(this.myfile.getMachineModel().getMonitorOption()));
        }
    }

    public void setOption(String option) {
        super.setOption(trueOptions.get(option),
                OptionsEnumModel.MONITOROPTION.getValor());
        this.myfile.getMachineModel()
                .setMonitorOption(trueOptions.get(option));
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setMonitorOption("");
    }
}
