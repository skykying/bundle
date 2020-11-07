package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class MemoryModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public MemoryModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getMemPathOption() != null) {
            if (!this.myfile.getMachineModel().getMemPathOption().isEmpty()) {
                this.buildIt(this.myfile.getMachineModel().getMemPathOption());
            }
        }

        if (this.myfile.getMachineModel().getMemPreallocOption() != null) {
            if (!this.myfile.getMachineModel().getMemPreallocOption().isEmpty()) {
                if (this.myfile.getMachineModel().getMemPreallocOption().equals("true")) {
                    this.buildIt();
                }
            }
        }
    }

    public void buildIt(String path) {
        if (!path.isEmpty()) {
            path = UsabilityModel.fixPath(path);
            this.setOption(path);
            this.myfile.getMachineModel().setMemPathOption(path);
        } else {
            this.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMORYPATHOPTION.getValor()]);
        }
    }

    public void buildIt() {
        this.setOption("");
        this.myfile.getMachineModel().setMemPreallocOption("true");
    }

    private void setOption(String option) {
        if (!option.isEmpty()) {
            super.setOption(option,
                    OptionsEnumModel.MEMORYPATHOPTION.getValor());
        } else {
            super.setOption(option, OptionsEnumModel.MEMPREALLOC.getValor());
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMORYPATHOPTION.getValor()])) {
            this.myfile.getMachineModel().setMemPathOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMPREALLOC.getValor()])) {
            this.myfile.getMachineModel().setMemPreallocOption("");
        }
    }
}
