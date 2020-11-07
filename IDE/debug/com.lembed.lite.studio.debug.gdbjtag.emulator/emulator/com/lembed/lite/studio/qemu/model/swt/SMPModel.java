package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class SMPModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    public SMPModel(EmulationControl myemulation,
            EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getSmpCpusNumber() != null
                || this.myfile.getMachineModel().getSmpCoresNumber() != null
                || this.myfile.getMachineModel().getSmpThreadsNumber() != null
                || this.myfile.getMachineModel().getSmpSocketsNumber() != null
                || this.myfile.getMachineModel().getSmpCpusMaxNumber() != null) {
            this.buildIt(this.myfile.getMachineModel().getSmpCpusNumber(),
                    this.myfile.getMachineModel().getSmpCoresNumber(), this.myfile
                    .getMachineModel().getSmpThreadsNumber(), this.myfile
                    .getMachineModel().getSmpSocketsNumber(), this.myfile
                    .getMachineModel().getSmpCpusMaxNumber());
        }
    }

    public void buildIt(String smpCpusNumbers, String smpCoresNumber,
            String smpThreadsNumber, String smpSocketsNumber,
            String smpCpusMaxNumber) {
        StringBuilder result = new StringBuilder("");
        result.append(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()]);
        if (smpCpusNumbers != null) {
            if (!smpCpusNumbers.isEmpty()) {
                result.append("cpus=").append(smpCpusNumbers);
                this.myfile.getMachineModel().setSmpCpusNumber(smpCpusNumbers);
            }
        }
        if (smpCoresNumber != null) {
            if (!smpCoresNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("cores=").append(smpCoresNumber);
                this.myfile.getMachineModel().setSmpCoresNumber(smpCoresNumber);
            }
        }
        if (smpThreadsNumber != null) {
            if (!smpThreadsNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("threads=").append(smpThreadsNumber);
                this.myfile.getMachineModel().setSmpThreadsNumber(smpThreadsNumber);
            }
        }
        if (smpSocketsNumber != null) {
            if (!smpSocketsNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("sockets=").append(smpSocketsNumber);
                this.myfile.getMachineModel().setSmpSocketsNumber(smpSocketsNumber);
            }
        }
        if (smpCpusMaxNumber != null) {
            if (!smpCpusMaxNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("maxcpus=").append(smpCpusMaxNumber);
                this.myfile.getMachineModel().setSmpCpusMaxNumber(smpCpusMaxNumber);
            }
        }
        if (!result.toString().equals(
                VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
            this.setOption(result.toString().substring(result.toString().indexOf(" ") + 1));
        } else {
            this.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()]);
        }

    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.SMPOPTION.getValor());
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setSmpCpusNumber("");
        this.myfile.getMachineModel().setSmpCoresNumber("");
        this.myfile.getMachineModel().setSmpThreadsNumber("");
        this.myfile.getMachineModel().setSmpSocketsNumber("");
        this.myfile.getMachineModel().setSmpCpusMaxNumber("");
    }
}
