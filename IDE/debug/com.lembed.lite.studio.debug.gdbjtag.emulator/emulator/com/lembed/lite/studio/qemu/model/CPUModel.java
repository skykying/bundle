package com.lembed.lite.studio.qemu.model;

import javax.swing.JComboBox;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class CPUModel extends OptionsControl {

    private String cpuidFlags;
    private String cpuModel;

    public CPUModel(EmulationControl myemulation) {
        super(myemulation);
        cpuModel = "";
        cpuidFlags = "";
    }

    public void buildIt() {
        if (!cpuModel.isEmpty()) {
            super.setOption(cpuModel + cpuidFlags, OptionsEnumModel.CPUOPTION.getValor());
        } else {
            super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.CPUOPTION.getValor()]);
        }
    }

    public void buildCPUIDFlags(String[] mycpuidflagsoptions) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < mycpuidflagsoptions.length; i++) {
            if (!mycpuidflagsoptions[i].isEmpty()) {
                result.append(",+").append(mycpuidflagsoptions[i]);
            }
        }
        cpuidFlags = result.toString();
    }

    public void buildCPUModel(JComboBox<String> cpuModels) {
        if (cpuModels.getSelectedItem().toString().indexOf(":") != -1) {
        	
        	int index = cpuModels.getSelectedItem().toString().indexOf(":") + 2;
            cpuModel = cpuModels.getSelectedItem().toString().substring(index);
        } else {
            cpuModel = "";
        }
    }

    public String getCpuidFlags() {
        return cpuidFlags;
    }

    public String getCpuModel() {
        return cpuModel;
    }
}
