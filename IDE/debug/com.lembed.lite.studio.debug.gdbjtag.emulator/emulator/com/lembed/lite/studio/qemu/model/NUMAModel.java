package com.lembed.lite.studio.qemu.model;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;
import com.lembed.lite.studio.qemu.view.internal.NUMAView;

public class NUMAModel extends OptionsControl {

    private FileControl myfile;
    private static HashMap<Integer, Integer> gettingOptionsEnumModel;

    static {
        gettingOptionsEnumModel = new HashMap<Integer, Integer>();
        for (int i = 0; i < NUMAView.itemNumbers; i++) {
            gettingOptionsEnumModel.put(i,
                    OptionsEnumModel.NUMA1OPTION.getValor() + i);
        }
    }

    public NUMAModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        HashMap<Integer, String> checkingMemFile = new HashMap<Integer, String>();
        checkingMemFile.put(0, myfile.getFilemodel().getFirstNumaNodeMem());
        checkingMemFile.put(1, myfile.getFilemodel().getSecondNumaNodeMem());
        checkingMemFile.put(2, myfile.getFilemodel().getThirdNumaNodeMem());
        checkingMemFile.put(3, myfile.getFilemodel().getFourthNumaNodeMem());
        checkingMemFile.put(4, myfile.getFilemodel().getFifthNumaNodeMem());
        checkingMemFile.put(5, myfile.getFilemodel().getSixthNumaNodeMem());
        checkingMemFile.put(6, myfile.getFilemodel().getSeventhNumaNodeMem());
        checkingMemFile.put(7, myfile.getFilemodel().getEighthNumaNodeMem());
        checkingMemFile.put(8, myfile.getFilemodel().getNinthNumaNodeMem());
        checkingMemFile.put(9, myfile.getFilemodel().getTenthNumaNodeMem());

        HashMap<Integer, String> checkingCpusFile = new HashMap<Integer, String>();
        checkingCpusFile.put(0, myfile.getFilemodel().getFirstNumaNodeCpus());
        checkingCpusFile.put(1, myfile.getFilemodel().getSecondNumaNodeCpus());
        checkingCpusFile.put(2, myfile.getFilemodel().getThirdNumaNodeCpus());
        checkingCpusFile.put(3, myfile.getFilemodel().getFourthNumaNodeCpus());
        checkingCpusFile.put(4, myfile.getFilemodel().getFifthNumaNodeCpus());
        checkingCpusFile.put(5, myfile.getFilemodel().getSixthNumaNodeCpus());
        checkingCpusFile.put(6, myfile.getFilemodel().getSeventhNumaNodeCpus());
        checkingCpusFile.put(7, myfile.getFilemodel().getEighthNumaNodeCpus());
        checkingCpusFile.put(8, myfile.getFilemodel().getNinthNumaNodeCpus());
        checkingCpusFile.put(9, myfile.getFilemodel().getTenthNumaNodeCpus());

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
                        this.myfile.getFilemodel().setFirstNumaNodeMem(mem);
                        break;
                    case 1:
                        this.myfile.getFilemodel().setSecondNumaNodeMem(mem);
                        break;
                    case 2:
                        this.myfile.getFilemodel().setThirdNumaNodeMem(mem);
                        break;
                    case 3:
                        this.myfile.getFilemodel().setFourthNumaNodeMem(mem);
                        break;
                    case 4:
                        this.myfile.getFilemodel().setFifthNumaNodeMem(mem);
                        break;
                    case 5:
                        this.myfile.getFilemodel().setSixthNumaNodeMem(mem);
                        break;
                    case 6:
                        this.myfile.getFilemodel().setSeventhNumaNodeMem(mem);
                        break;
                    case 7:
                        this.myfile.getFilemodel().setEighthNumaNodeMem(mem);
                        break;
                    case 8:
                        this.myfile.getFilemodel().setNinthNumaNodeMem(mem);
                        break;
                    case 9:
                        this.myfile.getFilemodel().setTenthNumaNodeMem(mem);
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
                myfile.getFilemodel().setFirstNumaNodeMem("");
                myfile.getFilemodel().setFirstNumaNodeCpus("");
                break;
            case 1:
                myfile.getFilemodel().setSecondNumaNodeMem("");
                myfile.getFilemodel().setSecondNumaNodeCpus("");
                break;
            case 2:
                myfile.getFilemodel().setThirdNumaNodeMem("");
                myfile.getFilemodel().setThirdNumaNodeCpus("");
                break;
            case 3:
                myfile.getFilemodel().setFourthNumaNodeMem("");
                myfile.getFilemodel().setFourthNumaNodeCpus("");
                break;
            case 4:
                myfile.getFilemodel().setFifthNumaNodeMem("");
                myfile.getFilemodel().setFifthNumaNodeCpus("");
                break;
            case 5:
                myfile.getFilemodel().setSixthNumaNodeMem("");
                myfile.getFilemodel().setSixthNumaNodeCpus("");
                break;
            case 6:
                myfile.getFilemodel().setSeventhNumaNodeMem("");
                myfile.getFilemodel().setSeventhNumaNodeCpus("");
                break;
            case 7:
                myfile.getFilemodel().setEighthNumaNodeMem("");
                myfile.getFilemodel().setEighthNumaNodeCpus("");
                break;
            case 8:
                myfile.getFilemodel().setNinthNumaNodeMem("");
                myfile.getFilemodel().setNinthNumaNodeCpus("");
                break;
            case 9:
                myfile.getFilemodel().setTenthNumaNodeMem("");
                myfile.getFilemodel().setTenthNumaNodeCpus("");
                break;
            default:
                break;
        }
    }

    private void setCpuFileOption(String option, int position) {
        switch (position) {
            case 0:
                this.myfile.getFilemodel().setFirstNumaNodeCpus(option);
                break;
            case 1:
                this.myfile.getFilemodel().setSecondNumaNodeCpus(option);
                break;
            case 2:
                this.myfile.getFilemodel().setThirdNumaNodeCpus(option);
                break;
            case 3:
                this.myfile.getFilemodel().setFourthNumaNodeCpus(option);
                break;
            case 4:
                this.myfile.getFilemodel().setFifthNumaNodeCpus(option);
                break;
            case 5:
                this.myfile.getFilemodel().setSixthNumaNodeCpus(option);
                break;
            case 6:
                this.myfile.getFilemodel().setSeventhNumaNodeCpus(option);
                break;
            case 7:
                this.myfile.getFilemodel().setEighthNumaNodeCpus(option);
                break;
            case 8:
                this.myfile.getFilemodel().setNinthNumaNodeCpus(option);
                break;
            case 9:
                this.myfile.getFilemodel().setTenthNumaNodeCpus(option);
                break;
            default:
                break;
        }
    }
}
