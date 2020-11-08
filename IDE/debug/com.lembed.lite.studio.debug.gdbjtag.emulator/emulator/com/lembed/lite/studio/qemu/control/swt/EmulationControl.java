package com.lembed.lite.studio.qemu.control.swt;

import java.io.IOException;
import java.util.List;

import com.lembed.lite.studio.qemu.model.swt.EmulationModel;
import com.lembed.lite.studio.qemu.view.internal.swt.EmulationView;

public class EmulationControl {

	private EmulationModel emulationModel;

	private EmulationView emulationView;

	public EmulationControl() {
		super();
		this.emulationView = new EmulationView();
		this.emulationModel = new EmulationModel(this.emulationView);
	}

	public boolean stops(int position) {
		if (this.emulationModel.getNumberOfProcesses() > position) {
			if (this.emulationModel.getProcessesControl(position) != null) {
				if (this.emulationModel.getProcessesControl(position).getMyModel().getMainThread().isAlive()) {
					this.emulationModel.getProcessesControl(position).getMyModel().getMainThread().interrupt();
				}
			}
			if (this.emulationModel.getProcesses(position) != null) {
				this.emulationModel.getProcesses(position).destroy();
			}
		}
		return true;
	}

	public boolean warns() {
		return this.emulationView.warns();
	}

	public void setPathQemu(String text) {
		this.emulationModel.setQemuPath(text);
	}

	public boolean preruns(int position, String machineName) {
		return this.emulationModel.preruns(position, machineName);
	}

	public void setExecute_before_start_qemu(List<String> jTextAreaToArrayListOfStrings) {
		this.emulationModel.setExecute_before_start_qemu(jTextAreaToArrayListOfStrings);
	}

	public void setExecute_after_stop_qemu(List<String> jTextAreaToArrayListOfStrings) {
		this.emulationModel.setExecute_after_stop_qemu(jTextAreaToArrayListOfStrings);
	}

	public boolean postruns(int position, String machineName) {
		return this.emulationModel.postruns(position, machineName);
	}

	public boolean runs(int position, String machineName) throws IOException, InterruptedException {
		return this.emulationModel.runs(position, machineName);
	}

	public String getFullCommandLine(int position) {
		return this.emulationModel.getFullCommandLine(position);
	}

	public void define_first_hard_disk_option(String diskImagePath) {
		this.emulationModel.define_first_hard_disk_option(diskImagePath);
	}

	public String getFirstHardDiskOption() {
		return this.emulationModel.getFirstHardDiskOption();
	}

	public void define_second_hard_disk_option(String diskImagePath) {
		this.emulationModel.define_second_hard_disk_option(diskImagePath);
	}

	public String getSecondHardDiskOption() {
		return this.emulationModel.getSecondHardDiskOption();
	}

	public void define_third_hard_disk_option(String diskImagePath) {
		this.emulationModel.define_third_hard_disk_option(diskImagePath);
	}

	public String getThirdHardDiskOption() {
		return this.emulationModel.getThirdHardDiskOption();
	}

	public void define_fourth_hard_disk_option(String path) {
		this.emulationModel.define_fourth_hard_disk_option(path);
	}

	public String getFourthHardDiskOption() {
		return this.emulationModel.getFourthHardDiskOption();
	}

	public void setBiosVgaBiosKeymapsPath(String path) {
		this.emulationModel.setBiosVgaBiosKeymapsPath(path);
	}

	public void change_options(int option, String value) {
		this.emulationModel.change_options(option, value);
	}

	public void appends_options() {
		this.emulationModel.appends_options();
	}

	public int getOptionsSize() {
		return this.emulationModel.getOptionsSize();
	}

	public void removes_options(String option) {
		this.emulationModel.removes_options(option);
	}

	public void removes_options(String option, int position) {
		this.emulationModel.removes_options(option, position);
	}

	public void showsMessages() throws IOException {
		StringBuilder result = new StringBuilder("");
		for (int i = 1; i < this.emulationModel.getNumberOfProcesses(); i++) {
			if (this.emulationModel.isRunning(this.emulationModel.getProcesses(i))) {
				if (this.emulationModel.getProcessesControl(i) != null) {
					result.append("The emulation process output of the '")
							.append(this.emulationModel.getProcessesControl(i).getMachineName()).append("' VM is:\n");
					result.append(this.emulationModel.getProcessesControl(i).getMyModel().getOutputs().getText());
					if (result.toString().equals("The emulation process output of the '"
							+ this.emulationModel.getProcessesControl(i).getMachineName() + "' VM is:\n")) {
						result.append("(empty)\n");
					} else {
						result.append("\n");
					}
				} else if (this.emulationModel.getScriptModel(i) != null) {
					result.append("The emulation process output of the script is:\n");
					result.append(this.emulationModel.getScriptModel(i).getOutputs());
					result.append(this.emulationModel.getScriptModel(i).getOutputs());
					if (result.toString().equals("The emulation process output of the script is:\n")) {
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
		this.emulationView.showMessage(result.toString());

		result = new StringBuilder("");
		for (int i = 1; i < this.emulationModel.getNumberOfProcesses(); i++) {
			if (this.emulationModel.isRunning(this.emulationModel.getProcesses(i))) {
				if (this.emulationModel.getProcessesControl(i) != null) {
					result.append("The emulation process error of the '")
							.append(this.emulationModel.getProcessesControl(i).getMachineName()).append("' VM is:\n");
					result.append(this.emulationModel.getProcessesControl(i).getMyModel().getErrors().getText());
					if (result.toString().equals("The emulation process error of the '"
							+ this.emulationModel.getProcessesControl(i).getMachineName() + "' VM is:\n")) {
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
		this.emulationView.showMessage(result.toString());
	}

	public void removesAProcess(int position) {
		this.emulationModel.removesAProcess(position);
	}

	public void removeAllProcesses() {
		this.emulationModel.removeAllProcesses();
	}

	public void setJPanel() {
		this.emulationModel.setJPanel();
	}

	public void close_emulation(int position) {
		this.emulationModel.close_emulation(position);
	}

	public void closeAllEmulation() {
		this.emulationModel.closeAllEmulation();
	}
}
