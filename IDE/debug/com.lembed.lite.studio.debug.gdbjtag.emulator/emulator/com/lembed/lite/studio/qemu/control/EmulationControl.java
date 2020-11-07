package com.lembed.lite.studio.qemu.control;

import java.io.IOException;
import java.util.List;

import com.lembed.lite.studio.qemu.model.EmulationModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.EmulationView;

public class EmulationControl {

    private EmulationModel mymodel;

    private EmulationView myview;

    public EmulationControl(JContainerView view) {
        super();
        this.myview = new EmulationView();
        this.mymodel = new EmulationModel(this.myview, view);
    }

    public boolean stops(int position) {
        if (this.mymodel.getNumberOfProcesses() > position) {
            if (this.mymodel.getProcessesControl(position) != null) {
                if (this.mymodel.getProcessesControl(position).getModel()
                        .getMainThread().isAlive()) {
                    this.mymodel.getProcessesControl(position).getModel()
                            .getMainThread().interrupt();
                }
            }
            if (this.mymodel.getProcesses(position) != null) {
                this.mymodel.getProcesses(position).destroy();
            }
        }
        return true;
    }

    public boolean warns() {
        return this.myview.warns();
    }

    public void setPathQemu(String text) {
        this.mymodel.setQemuPath(text);
    }

    public boolean preruns(int position, String machineName) {
        return this.mymodel.preruns(position, machineName);
    }

    public void setExecute_before_start_qemu(
            List<String> jTextAreaToArrayListOfStrings) {
        this.mymodel
                .setExecute_before_start_qemu(jTextAreaToArrayListOfStrings);
    }

    public void setExecute_after_stop_qemu(
            List<String> jTextAreaToArrayListOfStrings) {
        this.mymodel.setExecute_after_stop_qemu(jTextAreaToArrayListOfStrings);
    }

    public boolean postruns(int position, String machineName) {
        return this.mymodel.postruns(position, machineName);
    }

    public boolean runs(int position, String machineName) throws IOException,
            InterruptedException {
        return this.mymodel.runs(position, machineName);
    }

    public String getFullCommandLine(int position) {
        return this.mymodel.getFullCommandLine(position);
    }

    public void define_first_hard_disk_option(String diskImagePath) {
        this.mymodel.define_first_hard_disk_option(diskImagePath);
    }

    public String getFirstHardDiskOption() {
        return this.mymodel.getFirstHardDiskOption();
    }

    public void define_second_hard_disk_option(String diskImagePath) {
        this.mymodel.define_second_hard_disk_option(diskImagePath);
    }

    public String getSecondHardDiskOption() {
        return this.mymodel.getSecondHardDiskOption();
    }

    public void define_third_hard_disk_option(String diskImagePath) {
        this.mymodel.define_third_hard_disk_option(diskImagePath);
    }

    public String getThirdHardDiskOption() {
        return this.mymodel.getThirdHardDiskOption();
    }

    public void define_fourth_hard_disk_option(String path) {
        this.mymodel.define_fourth_hard_disk_option(path);
    }

    public String getFourthHardDiskOption() {
        return this.mymodel.getFourthHardDiskOption();
    }

    public void setBiosVgaBiosKeymapsPath(String path) {
        this.mymodel.setBiosVgaBiosKeymapsPath(path);
    }

    public void change_options(int option, String value) {
        this.mymodel.change_options(option, value);
    }

    public void appends_options() {
        this.mymodel.appends_options();
    }

    public int getOptionsSize() {
        return this.mymodel.getOptionsSize();
    }

    public void removes_options(String option) {
        this.mymodel.removes_options(option);
    }

    public void removes_options(String option, int position) {
        this.mymodel.removes_options(option, position);
    }

    public void showsMessages() throws IOException {
        StringBuilder result = new StringBuilder("");
        for (int i = 1; i < this.mymodel.getNumberOfProcesses(); i++) {
            if (this.mymodel.isRunning(this.mymodel.getProcesses(i))) {
                if (this.mymodel.getProcessesControl(i) != null) {
                    result.append("The emulation process output of the '")
                            .append(this.mymodel.getProcessesControl(i)
                                    .getMachineName()).append("' VM is:\n");
                    result.append(this.mymodel.getProcessesControl(i)
                            .getModel().getOutputs().getText());
                    if (result.toString().equals(
                            "The emulation process output of the '"
                            + this.mymodel.getProcessesControl(i)
                            .getMachineName() + "' VM is:\n")) {
                        result.append("(empty)\n");
                    } else {
                        result.append("\n");
                    }
                } else if (this.mymodel.getScriptModel(i) != null) {
                    result.append("The emulation process output of the script is:\n");
                    result.append(this.mymodel.getScriptModel(i).getOutputs());
                    result.append(this.mymodel.getScriptModel(i).getOutputs());
                    if (result.toString().equals(
                            "The emulation process output of the script is:\n")) {
                        result.append("(empty)\n");
                    } else {
                        result.append("\n");
                    }
                }
            }
        }
        if (result.toString().isEmpty()) {
            result.append("There is not a running process!");
        }
        this.myview.showMessage(result.toString());

        result = new StringBuilder("");
        for (int i = 1; i < this.mymodel.getNumberOfProcesses(); i++) {
            if (this.mymodel.isRunning(this.mymodel.getProcesses(i))) {
                if (this.mymodel.getProcessesControl(i) != null) {
                    result.append("The emulation process error of the '")
                            .append(this.mymodel.getProcessesControl(i)
                                    .getMachineName()).append("' VM is:\n");
                    result.append(this.mymodel.getProcessesControl(i)
                            .getModel().getErrors().getText());
                    if (result.toString().equals(
                            "The emulation process error of the '"
                            + this.mymodel.getProcessesControl(i)
                            .getMachineName() + "' VM is:\n")) {
                        result.append("(empty)\n");
                    } else {
                        result.append("\n");
                    }
                }
            }
        }
        if (result.toString().isEmpty()) {
            result.append("There is not a running process!");
        }
        this.myview.showMessage(result.toString());
    }

    public void removesAProcess(int position) {
        this.mymodel.removesAProcess(position);
    }

    public void removeAllProcesses() {
        this.mymodel.removeAllProcesses();
    }

    public void setJPanel() {
        this.mymodel.setJPanel();
    }

    public void close_emulation(int position) {
        this.mymodel.close_emulation(position);
    }

    public void closeAllEmulation() {
        this.mymodel.closeAllEmulation();
    }
}
