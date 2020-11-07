package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class KernelBootModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public KernelBootModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getKernelBootOption() != null) {
            if (!this.myfile.getMachineModel().getKernelBootOption().isEmpty()) {
                this.setOption(this.myfile.getMachineModel().getKernelBootOption());
            }
        }
    }

    public void setOption(String option) {
        this.myfile.getMachineModel()
                .setKernelBootOption(option);
        option = UsabilityModel.fixPath(option);
        super.setOption(option,
                OptionsEnumModel.KERNELBOOTOPTION.getValor());
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setKernelBootOption("");
    }
}
