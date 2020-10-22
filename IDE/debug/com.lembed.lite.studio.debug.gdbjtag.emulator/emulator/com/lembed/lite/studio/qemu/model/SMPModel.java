package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class SMPModel extends OptionsControl {

    private FileControl myfile;

    public SMPModel(EmulationControl myemulation,
            FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getSmpCpusNumber() != null
                || this.myfile.getFilemodel().getSmpCoresNumber() != null
                || this.myfile.getFilemodel().getSmpThreadsNumber() != null
                || this.myfile.getFilemodel().getSmpSocketsNumber() != null
                || this.myfile.getFilemodel().getSmpCpusMaxNumber() != null) {
            this.buildIt(this.myfile.getFilemodel().getSmpCpusNumber(),
                    this.myfile.getFilemodel().getSmpCoresNumber(), this.myfile
                    .getFilemodel().getSmpThreadsNumber(), this.myfile
                    .getFilemodel().getSmpSocketsNumber(), this.myfile
                    .getFilemodel().getSmpCpusMaxNumber());
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
                this.myfile.getFilemodel().setSmpCpusNumber(smpCpusNumbers);
            }
        }
        if (smpCoresNumber != null) {
            if (!smpCoresNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("cores=").append(smpCoresNumber);
                this.myfile.getFilemodel().setSmpCoresNumber(smpCoresNumber);
            }
        }
        if (smpThreadsNumber != null) {
            if (!smpThreadsNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("threads=").append(smpThreadsNumber);
                this.myfile.getFilemodel().setSmpThreadsNumber(smpThreadsNumber);
            }
        }
        if (smpSocketsNumber != null) {
            if (!smpSocketsNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("sockets=").append(smpSocketsNumber);
                this.myfile.getFilemodel().setSmpSocketsNumber(smpSocketsNumber);
            }
        }
        if (smpCpusMaxNumber != null) {
            if (!smpCpusMaxNumber.isEmpty()) {
                if (!result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SMPOPTION.getValor()])) {
                    result.append(",");
                }
                result.append("maxcpus=").append(smpCpusMaxNumber);
                this.myfile.getFilemodel().setSmpCpusMaxNumber(smpCpusMaxNumber);
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
        this.myfile.getFilemodel().setSmpCpusNumber("");
        this.myfile.getFilemodel().setSmpCoresNumber("");
        this.myfile.getFilemodel().setSmpThreadsNumber("");
        this.myfile.getFilemodel().setSmpSocketsNumber("");
        this.myfile.getFilemodel().setSmpCpusMaxNumber("");
    }
}
