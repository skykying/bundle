package com.lembed.lite.studio.qemu.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.qemu.control.ProcessControl;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.EmulationView;

public class EmulationModel {

    private List<Process> processList;
    private List<ProcessControl> processesControlList;
    private List<ScriptModel> scriptModelList;
    private JQemuView view;
    private List<JPanelModel> panelModelList;

    private String qemuPath;

    private String qemuPathDir;

    private List<String[]> execQemu;

    private List<String> execute_before_start_qemu;

    private List<String> execute_after_stop_qemu;

    private EmulationView emulationView;

    private List<List<String>> options;

    public EmulationModel(EmulationView myview, JQemuView view) {
        super();
        this.execQemu = new ArrayList<String[]>();

        this.emulationView = myview;

        this.options = new ArrayList<List<String>>();

        this.processList = new ArrayList<Process>();
        this.processesControlList = new ArrayList<ProcessControl>();
        this.scriptModelList = new ArrayList<ScriptModel>();
        this.view = view;
        this.panelModelList = new ArrayList<JPanelModel>();
    }

    public void setExecQemu() {
        int position = getPosition();
        if (this.execQemu.size() <= position) {
            for (int i = this.execQemu.size(); i <= position; i++) {
                this.execQemu.add(i, null);
            }
        }
        if (this.execQemu.get(position) == null) {
            this.execQemu.set(position, new String[7]);
            this.execQemu.get(position)[0] = "";
            this.execQemu.get(position)[1] = "";
            this.execQemu.get(position)[2] = "";
            this.execQemu.get(position)[3] = "";
            this.execQemu.get(position)[4] = "";
            this.execQemu.get(position)[5] = "";
            this.execQemu.get(position)[6] = "";
        }
    }

    public Process getProcesses(int position) {
        return processList.get(position);
    }

    public ProcessControl getProcessesControl(int position) {
        return this.processesControlList.get(position);
    }

    public ScriptModel getScriptModel(int position) {
        return scriptModelList.get(position);
    }

    public void setProcessList(Process myprocess, int position,
            String machineName, Boolean isItAScript) {
        if (this.processList.size() <= position) {
            for (int i = this.processList.size(); i <= position; i++) {
                this.processList.add(i, null);
                this.processesControlList.add(i, null);
                this.scriptModelList.add(i, null);
            }
        }
        this.processList.set(position, myprocess);
        if (!isItAScript) {
            this.processesControlList.set(position, new ProcessControl(myprocess,
                    machineName, emulationView, qemuPathDir));
            try {
                this.processesControlList.get(position).run();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            this.scriptModelList.set(position, new ScriptModel(myprocess, emulationView));
            this.scriptModelList.get(position).start();
        }
    }

    public void removesAProcess(int position) {
        if (this.processList.size() > position) {
            this.processList.set(position, null);
            this.processesControlList.set(position, null);
            this.scriptModelList.set(position, null);
        }
    }

    public void removeAllProcesses() {
        for (int i = 0; i < this.processList.size(); i++) {
            if (this.processList.get(i) != null) {
                this.processList.set(i, null);
                this.processesControlList.set(i, null);
                this.scriptModelList.set(i, null);
            }
        }
    }

    public void setQemuPath(String QemuPath) {
        this.qemuPath = QemuPath;
        this.setExecQemu();
        int position = getPosition();

        this.execQemu.get(position)[0] = this.qemuPath;
        this.setQemuPathDir();
    }

    public String getQemuPath() {
        return qemuPath;
    }

    public boolean preruns(int position, String machineName) {
        if (execute_before_start_qemu != null) {
            for (int i = 0; i < execute_before_start_qemu.size(); i++) {
                try {
                    String given = execute_before_start_qemu.get(i);
                    if (!given.isEmpty()) {
                        this.emulationView.showScriptCommand(given);
                        String[] cmdLine = UsabilityModel.getCmdLine(given);
                        if (cmdLine.length == 0) {
                            this.setProcessList(Runtime.getRuntime().exec(given),
                                    position, machineName, true);
                        } else {
                            this.setProcessList(Runtime.getRuntime().exec(cmdLine),
                                    position, machineName, true);
                        }
                        while (isRunning(this.getProcesses(position))) {
                            this.emulationView
                                    .showMessage("This (pre) script is still running!");
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public boolean checks_if_is_a_valid_file(String path) {
        File file;
        if (path.charAt(0) == '\"' && path.charAt(path.length() - 1) == '\"') {
            file = new File(path.substring(1, path.length() - 1));
        } else {
            file = new File(path);
        }

        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return true;
        }

        return false;
    }

    public void setExecute_before_start_qemu(
            List<String> execute_before_start_qemu) {
        this.execute_before_start_qemu = execute_before_start_qemu;
    }

    public boolean postruns(int position, String machineName) {
        if (execute_after_stop_qemu != null) {
            for (String given : execute_after_stop_qemu) {
                try {
                    if (!given.isEmpty()) {
                        this.emulationView.showScriptCommand(given);
                        String[] cmdLine = UsabilityModel.getCmdLine(given);
                        if (cmdLine.length == 0) {
                            this.setProcessList(Runtime.getRuntime().exec(given),
                                    position, machineName, true);
                        } else {
                            this.setProcessList(Runtime.getRuntime().exec(cmdLine),
                                    position, machineName, true);
                        }
                        while (isRunning(this.getProcesses(position))) {
                            this.emulationView
                                    .showMessage("This (post) script is still running!");
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public void setExecute_after_stop_qemu(
            List<String> execute_after_stop_qemu) {
        this.execute_after_stop_qemu = execute_after_stop_qemu;
    }

    public boolean runs(int position, String machineName) throws IOException,
            InterruptedException {
        // this.myview.showThisBeforeInTheRunProcess(qemuPath, execQemu);
        if (!this.getQemuPath().isEmpty()) {
            if (this.checks_if_is_a_valid_file(this.getQemuPath())) {
                if (this.checks(position)) {
                    if (this.panelModelList.size() > this.view.getActivePanel()) {
                        if (this.panelModelList.get(this.view.getActivePanel()) != null) {
                            this.panelModelList.get(this.view.getActivePanel()).setEmulation(
                                    this.execQemu.get(position));
                            this.emulationView.showThisAfterInTheRunProcess(this.panelModelList.get(
                                    this.view.getActivePanel()).getExecQemu());
                            String[] cmdLine = UsabilityModel.getCmdLine(this.panelModelList.get(
                                    this.view.getActivePanel())
                                    .getEmulation());
                            if (cmdLine.length == 0) {
                                this.setProcessList(
                                        Runtime.getRuntime().exec(
                                                this.panelModelList.get(
                                                        this.view.getActivePanel())
                                                .getEmulation(), null,
                                                new File(this.qemuPathDir)), position,
                                        machineName, false);
                            } else {
                                this.setProcessList(
                                        Runtime.getRuntime().exec(
                                                cmdLine, null,
                                                new File(this.qemuPathDir)), position,
                                        machineName, false);
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    this.emulationView
                            .showMessage("You shouldn�t to use \"-hdc\" and \"-cdrom\" options at the same time!");
                }
            }
        }
        return false;
    }

    public String getFullCommandLine(int position) {
        String result = null;
        if (this.getQemuPath() != null && !this.getQemuPath().isEmpty()) {
            if (this.checks_if_is_a_valid_file(this.getQemuPath())) {
                if (this.checks(position)) {
                    if (this.panelModelList.size() > this.view.getActivePanel()) {
                        if (this.panelModelList.get(this.view.getActivePanel()) != null) {
                            this.panelModelList.get(this.view.getActivePanel()).setEmulation(
                                    this.execQemu.get(position));

                            String[] cmdLine = UsabilityModel.getCmdLine(this.panelModelList.get(
                                    this.view.getActivePanel())
                                    .getEmulation());
                            if (cmdLine.length == 0) {
                                result = this.panelModelList.get(
                                        this.view.getActivePanel())
                                        .getEmulation();
                            } else {
                                StringBuilder builder = new StringBuilder(1024);
                                for (String s : cmdLine) {
                                    builder.append(s).append(" ");
                                }
                                result = builder.toString();
                            }
                            return result;
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                } else {
                    this.emulationView
                            .showMessage("You shouldn�t to use \"-hdc\" and \"-cdrom\" options at the same time!");
                    return "";
                }
            }
        } else if (this.checks(position)) {
            if (this.panelModelList.size() > this.view.getActivePanel()) {
                if (this.panelModelList.get(this.view.getActivePanel()) != null) {
                    this.panelModelList.get(this.view.getActivePanel()).setEmulation(
                            this.execQemu.get(position));

                    String[] cmdLine = UsabilityModel.getCmdLine(this.panelModelList.get(
                            this.view.getActivePanel())
                            .getEmulation());
                    if (cmdLine.length == 0) {
                        result = this.panelModelList.get(
                                this.view.getActivePanel())
                                .getEmulation();
                    } else {
                        StringBuilder builder = new StringBuilder(1024);
                        for (String s : cmdLine) {
                            builder.append(s).append(" ");
                        }
                        result = builder.toString();
                    }
                    return result;
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } else {
            this.emulationView.showMessage("You shouldn�t to use \"-hdc\" and \"-cdrom\" options at the same time!");
            return "";
        }

        return "";
    }

    public boolean checks(int position) {
        if (this.execQemu.get(position) != null) {
            if (this.execQemu.get(position)[4] != null) {
                if (!this.execQemu.get(position)[4].isEmpty()) {
                    if (this.options.get(position).size() > OptionsEnumModel.CDROMOPTION
                            .getValor()) {
                        if (!this.options.get(position).get(OptionsEnumModel.CDROMOPTION.getValor()).isEmpty()) {
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    public void appends_options() {
        int position = getPosition();
        this.execQemu.get(position)[1] = "";
        StringBuilder work = new StringBuilder("");
        for (int i = 0; i < this.options.get(position).size(); i++) {
            if (!this.options.get(position).get(i).isEmpty()) {
                work.append(this.options.get(position).get(i)).append(" ");
            }
        }
        this.execQemu.get(position)[1] = work.toString();
    }

    private String fixPath(String path) {
        if (path.contains(" ")) {
            if (!path.contains("\"")) {
                StringBuilder sb = new StringBuilder("\"");
                sb.append(path).append("\"");
                path = sb.toString();
            }
        }
        return path;
    }

    public void define_first_hard_disk_option(String path) {
        int position = getPosition();
        this.setExecQemu();
        path = fixPath(path);
        if (!path.isEmpty()) {
            this.execQemu.get(position)[2] = "-hda " + path;
        } else {
            this.execQemu.get(position)[2] = "";
        }
    }

    public String getFirstHardDiskOption() {
        int position = getPosition();
        if (this.execQemu.get(position)[2].isEmpty()) {
            return "";
        } else {
            return this.execQemu.get(position)[2].substring(5);
        }
    }

    public void define_second_hard_disk_option(String path) {
        int position = getPosition();
        this.setExecQemu();
        path = fixPath(path);
        if (!path.isEmpty()) {
            this.execQemu.get(position)[3] = "-hdb " + path;
        } else {
            this.execQemu.get(position)[3] = "";
        }
    }

    public String getSecondHardDiskOption() {
        int position = getPosition();
        if (this.execQemu.get(position)[3].isEmpty()) {
            return "";
        } else {
            return this.execQemu.get(position)[3].substring(5);
        }
    }

    public void define_third_hard_disk_option(String path) {
        int position = getPosition();
        this.setExecQemu();
        path = fixPath(path);
        if (!path.isEmpty()) {
            this.execQemu.get(position)[4] = "-hdc " + path;
        } else {
            this.execQemu.get(position)[4] = "";
        }
    }

    public String getThirdHardDiskOption() {
        int position = getPosition();
        if (this.execQemu.get(position)[4].isEmpty()) {
            return "";
        } else {
            return this.execQemu.get(position)[4].substring(5);
        }
    }

    public void define_fourth_hard_disk_option(String path) {
        int position = getPosition();
        this.setExecQemu();
        path = fixPath(path);
        if (!path.isEmpty()) {
            this.execQemu.get(position)[5] = "-hdd " + path;
        } else {
            this.execQemu.get(position)[5] = "";
        }
    }

    public String getFourthHardDiskOption() {
        int position = getPosition();
        if (this.execQemu.get(position)[5].isEmpty()) {
            return "";
        } else {
            return this.execQemu.get(position)[5].substring(5);
        }
    }

    public void setBiosVgaBiosKeymapsPath(String path) {
        int position = getPosition();
        this.setExecQemu();
        path = fixPath(path);
        if (!path.isEmpty()) {
            this.execQemu.get(position)[6] = VMConfigurationModel
                    .getTagsOptions()[OptionsEnumModel.LPATHOPTION.getValor()]
                    + path;
        } else {
            this.execQemu.get(position)[6] = "";
        }
    }

    public void change_options(int option, String value) {
        int position = getPosition();
        this.setExecQemu();
        if (this.options.size() <= position) {
            for (int i = this.options.size(); i <= position; i++) {
                this.options.add(i, new ArrayList<String>());
            }
        }

        if (this.options.get(position).size() <= option) {
            for (int i = this.options.get(position).size(); i < option; i++) {
                this.options.get(position).add(i, "");
            }
            this.options.get(position).add(option, value);
        } else {
            this.options.get(position).set(option, value);
        }

    }

    private void setQemuPathDir() {
        if (qemuPath != null) {
            String extension = this.checks_extension(qemuPath);
            int position = qemuPath.lastIndexOf(extension);
            this.qemuPathDir = this.qemuPath.substring(0, position);
        }
    }

    private String checks_extension(String path) {
        String result = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                result = "/";
                break;
            }
            if (path.charAt(i) == '\\') {
                result = "\\";
                break;
            }
        }
        return result;
    }

    public int getOptionsSize() {
        return this.options.size();
    }

    public void removes_options(String option) {
        if (option.indexOf(" ") == -1) {
            if (option.equals("-nographic")) {
                this.change_options(
                        OptionsEnumModel.NOGRAPHICOPTION.getValor(), "");
            } else if (option.equals("-full-screen")) {
                this.change_options(
                        OptionsEnumModel.FULLSCREENOPTION.getValor(), "");
            } else if (option.equals("-win2k-hack")) {
                this.change_options(
                        OptionsEnumModel.WIN2KHACKOPTION.getValor(), "");
            } else if (option.equals("-no-acpi")) {
                this.change_options(OptionsEnumModel.NOACPIOPTION.getValor(),
                        "");
            } else if (option.equals("-no-frame")) {
                this.change_options(OptionsEnumModel.NOFRAMEOPTION.getValor(), "");
            } else if (option.equals("-mem-prealloc")) {
                this.change_options(OptionsEnumModel.MEMPREALLOC.getValor(), "");
            } else if (option.equals("-snapshot")) {
                this.change_options(OptionsEnumModel.SNAPSHOTOPTION.getValor(), "");
            } else if (option.equals("-no-fd-bootchk")) {
                this.change_options(OptionsEnumModel.NOFDBOOTCHKOPTION.getValor(), "");
            } else if (option.equals("-no-hpet")) {
                this.change_options(OptionsEnumModel.NOHPETOPTION.getValor(), "");
            } else if (option.equals("-usb")) {
                this.change_options(OptionsEnumModel.USBDRIVEROPTION.getValor(), "");
            }
        } else if (option.substring(0, option.indexOf(" ")).equals("-display")) {
            this.change_options(OptionsEnumModel.DISPLAYOPTION.getValor(),
                    "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-vga")) {
            this.change_options(OptionsEnumModel.VGAOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-machine")) {
            this.change_options(OptionsEnumModel.MACHINEOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-cpu")) {
            this.change_options(OptionsEnumModel.CPUOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-cdrom")) {
            this.change_options(OptionsEnumModel.CDROMOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-fda")) {
            this.change_options(OptionsEnumModel.FLOPPYDISKAOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-fdb")) {
            this.change_options(OptionsEnumModel.FLOPPYDISKBOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-boot")) {
            this.change_options(OptionsEnumModel.BOOTOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-k")) {
            this.change_options(OptionsEnumModel.KEYBOARDOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-soundhw")) {
            this.change_options(OptionsEnumModel.SOUNDHARDWAREOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-smp")) {
            this.change_options(OptionsEnumModel.SMPOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-mem-path")) {
            this.change_options(OptionsEnumModel.MEMORYPATHOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-rtc")) {
            this.change_options(OptionsEnumModel.RTCOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-name")) {
            this.change_options(OptionsEnumModel.NAMEOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-mtdblock")) {
            this.change_options(OptionsEnumModel.MTDBLOCKOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-sd")) {
            this.change_options(OptionsEnumModel.SDOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-pflash")) {
            this.change_options(OptionsEnumModel.PFLASHOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-monitor")) {
            this.change_options(OptionsEnumModel.MONITOROPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-qmp")) {
            this.change_options(OptionsEnumModel.QMPOPTION.getValor(), "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-kernel")) {
            this.change_options(OptionsEnumModel.KERNELBOOTOPTION.getValor(), "");
        }
    }

    public void removes_options(String option, int position) {
        if (option.indexOf(" ") == -1) {

        } else if (option.substring(0, option.indexOf(" ")).equals("-numa")) {
            this.change_options(position, "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-net")) {
            this.change_options(position, "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-netdev")) {
            this.change_options(position, "");
        } else if (option.substring(0, option.indexOf(" ")).equals("-usbdevice")) {
            this.change_options(position, "");
        }
    }

    public Integer getNumberOfProcesses() {
        return this.processList.size();
    }

    public boolean isRunning(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void setJPanel() {
        if (this.panelModelList.size() <= this.view.getActivePanel()) {
            for (int i = this.panelModelList.size(); i <= this.view.getActivePanel(); i++) {
                this.panelModelList.add(i, null);
            }
        }
        if (this.panelModelList.get(this.view.getActivePanel()) == null) {
            this.panelModelList.set(this.view.getActivePanel(), new JPanelModel());
        }
        int position = getPosition();
        if (this.execQemu.size() > position) {
            for (int i = 0; i < execQemu.get(position).length; i++) {
                if (!this.execQemu.get(position)[i].isEmpty()) {
                    this.panelModelList.get(this.view.getActivePanel()).setExecQemu(
                            this.execQemu.get(position)[i], i);
                }
            }
        }

    }

    public void close_emulation(int position) {
        this.execQemu.remove(position);
        this.options.remove(position);
        this.panelModelList.remove(position);
    }

    public void closeAllEmulation() {
        for (int i = 0; i < this.execQemu.size(); i++) {
            this.execQemu.remove(i);
            this.options.remove(i);
            this.panelModelList.remove(i);
        }
    }

    public int getPosition() {
        if (this.view.getActivePanel() == 0) {
            return this.view.getSizeOfJTabbedPane();
        } else {
            return this.view.getActivePanel();
        }
    }
}
