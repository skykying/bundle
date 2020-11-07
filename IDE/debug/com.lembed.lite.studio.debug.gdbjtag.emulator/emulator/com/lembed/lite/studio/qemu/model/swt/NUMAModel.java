package com.lembed.lite.studio.qemu.model.swt;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.NUMAView;

public class NUMAModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;
    private static HashMap<Integer, Integer> gettingOptionsEnumModel;

    static {
        gettingOptionsEnumModel = new HashMap<Integer, Integer>();
        for (int i = 0; i < NUMAView.itemNumbers; i++) {
            gettingOptionsEnumModel.put(i,
                    OptionsEnumModel.NUMA1OPTION.getValor() + i);
        }
    }

    public NUMAModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        HashMap<Integer, String> checkingMemFile = new HashMap<Integer, String>();
        checkingMemFile.put(0, myfile.getMachineModel().getFirstNumaNodeMem());
        checkingMemFile.put(1, myfile.getMachineModel().getSecondNumaNodeMem());
        checkingMemFile.put(2, myfile.getMachineModel().getThirdNumaNodeMem());
        checkingMemFile.put(3, myfile.getMachineModel().getFourthNumaNodeMem());
        checkingMemFile.put(4, myfile.getMachineModel().getFifthNumaNodeMem());
        checkingMemFile.put(5, myfile.getMachineModel().getSixthNumaNodeMem());
        checkingMemFile.put(6, myfile.getMachineModel().getSeventhNumaNodeMem());
        checkingMemFile.put(7, myfile.getMachineModel().getEighthNumaNodeMem());
        checkingMemFile.put(8, myfile.getMachineModel().getNinthNumaNodeMem());
        checkingMemFile.put(9, myfile.getMachineModel().getTenthNumaNodeMem());

        HashMap<Integer, String> checkingCpusFile = new HashMap<Integer, String>();
        checkingCpusFile.put(0, myfile.getMachineModel().getFirstNumaNodeCpus());
        checkingCpusFile.put(1, myfile.getMachineModel().getSecondNumaNodeCpus());
        checkingCpusFile.put(2, myfile.getMachineModel().getThirdNumaNodeCpus());
        checkingCpusFile.put(3, myfile.getMachineModel().getFourthNumaNodeCpus());
        checkingCpusFile.put(4, myfile.getMachineModel().getFifthNumaNodeCpus());
        checkingCpusFile.put(5, myfile.getMachineModel().getSixthNumaNodeCpus());
        checkingCpusFile.put(6, myfile.getMachineModel().getSeventhNumaNodeCpus());
        checkingCpusFile.put(7, myfile.getMachineModel().getEighthNumaNodeCpus());
        checkingCpusFile.put(8, myfile.getMachineModel().getNinthNumaNodeCpus());
        checkingCpusFile.put(9, myfile.getMachineModel().getTenthNumaNodeCpus());

        for (int i = 0; i < NUMAView.itemNumbers; i++) {
            if (checkingMemFile.get(i) != null
                    || checkingCpusFile.get(i) != null) {
                String[] cpus = checkingCpusFile.get(i).split("-");
                if (cpus.length == 2) {
                    this.buildIt(checkingMemFile.get(i), cpus[0], cpus[1], i);
                } else if (cpus.length == 1) {
                    this.buildIt(checkingMemFile.get(i), cpus[0], "", i);
                }
            }
        }

    }

    public void buildIt(String mem, String cpuOne, String cpuTwo, int position) {
        StringBuilder result = new StringBuilder("");
        result.append(VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)]);
        if (mem != null) {
            if (!mem.isEmpty()) {
                result.append("node,mem=").append(mem.replace(",", "."));
                switch (position) {
                    case 0:
                        this.myfile.getMachineModel().setFirstNumaNodeMem(mem);
                        break;
                    case 1:
                        this.myfile.getMachineModel().setSecondNumaNodeMem(mem);
                        break;
                    case 2:
                        this.myfile.getMachineModel().setThirdNumaNodeMem(mem);
                        break;
                    case 3:
                        this.myfile.getMachineModel().setFourthNumaNodeMem(mem);
                        break;
                    case 4:
                        this.myfile.getMachineModel().setFifthNumaNodeMem(mem);
                        break;
                    case 5:
                        this.myfile.getMachineModel().setSixthNumaNodeMem(mem);
                        break;
                    case 6:
                        this.myfile.getMachineModel().setSeventhNumaNodeMem(mem);
                        break;
                    case 7:
                        this.myfile.getMachineModel().setEighthNumaNodeMem(mem);
                        break;
                    case 8:
                        this.myfile.getMachineModel().setNinthNumaNodeMem(mem);
                        break;
                    case 9:
                        this.myfile.getMachineModel().setTenthNumaNodeMem(mem);
                        break;
                    default:
                        break;
                }
            }
        }

        if (cpuOne != null && cpuTwo == null) {
            if (!cpuOne.isEmpty()) {
                if (result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
                    result.append("node");
                }
                result.append(",cpus=").append(cpuOne);
                setCpuFileOption(cpuOne, position);
            }
        }

        if (cpuOne == null && cpuTwo != null) {
            if (!cpuTwo.isEmpty()) {
                if (result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
                    result.append("node");
                }
                result.append(",cpus=").append(cpuTwo);
                setCpuFileOption(cpuTwo, position);
            }
        }

        if (cpuOne != null && cpuTwo != null) {
            if (!cpuOne.isEmpty() && cpuTwo.isEmpty()) {
                if (result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
                    result.append("node");
                }
                result.append(",cpus=").append(cpuOne);
                setCpuFileOption(cpuOne, position);
            } else if (cpuOne.isEmpty() && !cpuTwo.isEmpty()) {
                if (result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
                    result.append("node");
                }
                result.append(",cpus=").append(cpuTwo);
                setCpuFileOption(cpuTwo, position);
            } else if (!cpuOne.isEmpty() && !cpuTwo.isEmpty()) {
                if (result.toString().equals(
                        VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
                    result.append("node");
                }
                result.append(",cpus=").append(cpuOne).append("-")
                        .append(cpuTwo);
                setCpuFileOption(cpuOne + "-" + cpuTwo, position);
            }
        }

        if (!result.toString().equals(
                VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)])) {
            this.setOption(
                    result.toString().substring(
                            result.toString().indexOf(" ") + 1), position);
        } else {
            this.unsetOption(
                    VMConfigurationModel.getTagsOptions()[gettingOptionsEnumModel.get(position)], position);
        }

    }

    public void setOption(String option, int position) {
        super.setOption(option, gettingOptionsEnumModel.get(position));
    }

    public void unsetOption(String option, int position) {
        super.unsetOption(option, gettingOptionsEnumModel.get(position));
        switch (position) {
            case 0:
                myfile.getMachineModel().setFirstNumaNodeMem("");
                myfile.getMachineModel().setFirstNumaNodeCpus("");
                break;
            case 1:
                myfile.getMachineModel().setSecondNumaNodeMem("");
                myfile.getMachineModel().setSecondNumaNodeCpus("");
                break;
            case 2:
                myfile.getMachineModel().setThirdNumaNodeMem("");
                myfile.getMachineModel().setThirdNumaNodeCpus("");
                break;
            case 3:
                myfile.getMachineModel().setFourthNumaNodeMem("");
                myfile.getMachineModel().setFourthNumaNodeCpus("");
                break;
            case 4:
                myfile.getMachineModel().setFifthNumaNodeMem("");
                myfile.getMachineModel().setFifthNumaNodeCpus("");
                break;
            case 5:
                myfile.getMachineModel().setSixthNumaNodeMem("");
                myfile.getMachineModel().setSixthNumaNodeCpus("");
                break;
            case 6:
                myfile.getMachineModel().setSeventhNumaNodeMem("");
                myfile.getMachineModel().setSeventhNumaNodeCpus("");
                break;
            case 7:
                myfile.getMachineModel().setEighthNumaNodeMem("");
                myfile.getMachineModel().setEighthNumaNodeCpus("");
                break;
            case 8:
                myfile.getMachineModel().setNinthNumaNodeMem("");
                myfile.getMachineModel().setNinthNumaNodeCpus("");
                break;
            case 9:
                myfile.getMachineModel().setTenthNumaNodeMem("");
                myfile.getMachineModel().setTenthNumaNodeCpus("");
                break;
            default:
                break;
        }
    }

    private void setCpuFileOption(String option, int position) {
        switch (position) {
            case 0:
                this.myfile.getMachineModel().setFirstNumaNodeCpus(option);
                break;
            case 1:
                this.myfile.getMachineModel().setSecondNumaNodeCpus(option);
                break;
            case 2:
                this.myfile.getMachineModel().setThirdNumaNodeCpus(option);
                break;
            case 3:
                this.myfile.getMachineModel().setFourthNumaNodeCpus(option);
                break;
            case 4:
                this.myfile.getMachineModel().setFifthNumaNodeCpus(option);
                break;
            case 5:
                this.myfile.getMachineModel().setSixthNumaNodeCpus(option);
                break;
            case 6:
                this.myfile.getMachineModel().setSeventhNumaNodeCpus(option);
                break;
            case 7:
                this.myfile.getMachineModel().setEighthNumaNodeCpus(option);
                break;
            case 8:
                this.myfile.getMachineModel().setNinthNumaNodeCpus(option);
                break;
            case 9:
                this.myfile.getMachineModel().setTenthNumaNodeCpus(option);
                break;
            default:
                break;
        }
    }
}
