package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.control.swt.actions.AboutCommand;
import com.lembed.lite.studio.qemu.control.swt.actions.UseUtilities;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderEnumModel;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.swt.UtilitiesModel;
import com.lembed.lite.studio.qemu.view.BaseEvent;
import com.lembed.lite.studio.qemu.view.BaseListener;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;
import com.lembed.lite.studio.qemu.view.internal.swt.UtilitiesView;

public class Control implements BaseListener {

	private JSwtQemuView view;
	
	private ConfigurationControl configurationControl;
	private EmulationControl emulationControl;
	private VMCreationControl vMCreationControl;
	private DiskCreationControl diskCreationControl;
	private EmulatorQemuMachineControl fileControl;
	private VMClosingControl vMClosingControl;
	private VMOpeningControl vMOpeningControl;
	private List<VMConfigurationControl> vMConfigurationControlist;
	private VMSavingControl vMSavingControl;
	private UtilitiesView utilitiesView;
	private UtilitiesModel utilitiesModel;
	
	private LastUsedFolderModel lastUsedFolderModel;
	private LastUsedFileModel lastUsedFileModel;
	
	private UseUtilities useUtilitiesAction;

	public Control(JSwtQemuView jview) {
		view = jview;
		view.registerListener(this);
		configurationControl = null;
		emulationControl = null;
		vMCreationControl = null;
		diskCreationControl = null;
		fileControl = null;
		vMClosingControl = null;
		vMOpeningControl = null;
		vMConfigurationControlist = new ArrayList<VMConfigurationControl>();
		vMSavingControl = null;
		utilitiesView = null;
		utilitiesModel = null;
		
		lastUsedFolderModel = new LastUsedFolderModel();
		lastUsedFileModel = new LastUsedFileModel();
		
		useUtilitiesAction = new UseUtilities();
	}

	public void starts() {
		view.setVisible(true);
		view.configureStandardMode();
		configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
		emulationControl = new EmulationControl();
		vMClosingControl = new VMClosingControl(view, emulationControl);
		fileControl = new EmulatorQemuMachineControl();
		
		vMOpeningControl = new VMOpeningControl(view, emulationControl, fileControl);
	}

	@Override
	public void actionPerformed(BaseEvent e) {
		doAction(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doAction(e);
	}

	private void doAction(ActionEvent e) {
		if (e.getActionCommand().equals("ExitCommand")) {

			boolean runFurther = view.getSizeOfJTabbedPane() > 1;
			for (int i = 1; i < view.getSizeOfJTabbedPane(); i++) {
				vMConfigurationControlist.remove(i);
			}

			if (runFurther) {
				{
					vMClosingControl.setView(view);
					vMClosingControl.setEmulation(emulationControl);
				}
				vMClosingControl.starts(true);
			}

			view.dispose();

			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				int n = 0;

				@Override
				public void run() {

					if (++n == 2) {
						timer.cancel();
						System.exit(0);
					}
				}
			}, 1000, 1000);
		} else if (e.getActionCommand().equals("AboutCommand")) {
			view.showAboutContents();
			AboutCommand about = new AboutCommand(view);
			about.actionPerformed(e);
			
		} else if (e.getActionCommand().equals("ConfigureCommand")) {
			configurationControl.setVisibleEnable();
		} else if (e.getActionCommand().equals("StartEmulation")) {

			if (configurationControl == null) {
				JSwtQemuView.showMessage(
						"Please, configure the required parameters \nbefore starting the emulation with the qemu!");
			}

			if (configurationControl.getQemu_executable_path() == null) {
				configurationControl.setQemu_executable_path();
			}
			emulationControl.setExecute_before_start_qemu(
					this.JTextAreaToArrayListOfStrings(configurationControl.getExecute_before_start_qemu()));
			if (emulationControl.preruns(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
				JSwtQemuView.showMessage("The pre-run script(s) is(are) gone.");
			}
			emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());

			if (configurationControl.getBios_vga_bios_keymaps_path() == null) {
				configurationControl.setBios_vga_bios_keymaps_path();
			}

			emulationControl.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());

			if (view.getActivePanel() == 0) {
				JSwtQemuView.showMessage("Please, select an open virtual machine tab before starting the emulation.");
			}
			try {
				if (emulationControl.runs(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
					JSwtQemuView.showMessage("If there were no errors so far, so the qemu should be running now!");
				} else if (!configurationControl.getIsConfigured()) {
					configurationControl.setVisibleEnable();
					JSwtQemuView
							.showMessage("The qemu has not been started!\nPlease, check the configuration parameters!"
									+ "\nPlease, configure the path of the qemu executable first!");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("StopEmulation")) {

			if (emulationControl.warns()) {
				if (emulationControl.stops(view.getActivePanel())) {
					JSwtQemuView.showMessage("The Qemu is not running now!");
				}

				if (configurationControl != null) {
					emulationControl.setExecute_after_stop_qemu(
							this.JTextAreaToArrayListOfStrings(configurationControl.getExecute_after_stop_qemu()));
					if (emulationControl.postruns(view.getActivePanel(), view.getSelectedPanel().getTitle())) {
						JSwtQemuView.showMessage("The post-run script(s) is(are) gone.");
					}
				}
			}
		} else if (e.getActionCommand().equals("CreateNewVM")) {
			if (fileControl == null) {
				fileControl = new EmulatorQemuMachineControl();
			}

			if (configurationControl == null) {
				JSwtQemuView.showMessage("Please, configure the required options first!\n"
						+ "Please, configure the path of the qemu-img executable first!");
			} else if (configurationControl.getQemu_img_executable_path() == null
					|| configurationControl.getQemu_img_executable_path().getText().isEmpty()) {
				JSwtQemuView.showMessage("Please, configure the path of the qemu-img executable first!");
				configurationControl.setVisibleEnable();

			} else if (configurationControl.getDefault_virtual_machines_path().isEmpty()) {
				JSwtQemuView.showMessage("Please, configure the path of the default Virtual Machines first!");
				configurationControl.setVisibleEnable();
			} else {
				vMCreationControl = new VMCreationControl(diskCreationControl,
						configurationControl.getQemu_img_executable_path().getText(),
						configurationControl.getDefault_virtual_machines_path(), view, fileControl, emulationControl,
						vMConfigurationControlist);
				vMCreationControl.starts();
			}
		} else if (e.getActionCommand().equals("OpenExistingVM")) {

			if (fileControl == null) {
				fileControl = new EmulatorQemuMachineControl();
			}
			fileControl.getFileview().setFileExtension(".xml");
			fileControl.getFileview().setChoosertitle("Choose an existing JavaQemu VM file!");
			fileControl.getFileview().setFileDescription("XML - JavaQemu VM Files");
			fileControl.getFileview().setCurrentDirectory(
					lastUsedFolderModel.getLastUsedFolder(LastUsedFolderEnumModel.OPENEXISTINGVM.getValor()));
			if (fileControl.getFileview().chooseFile()) {
				if (fileControl.getMachineModel().readXML(fileControl.getFileview().getChoice())) {
					lastUsedFolderModel.setLastUsedFolder(LastUsedFolderEnumModel.OPENEXISTINGVM.getValor(),
							fileControl.getFileview().getChooser().getCurrentDirectory().getAbsolutePath());

					if (vMOpeningControl == null) {
						vMOpeningControl = new VMOpeningControl(view, emulationControl, fileControl);
					}
					vMOpeningControl.starts(fileControl);
					int position;
					if (view.getActivePanel() == 0) {
						position = view.getSizeOfJTabbedPane();
					} else {
						position = view.getActivePanel();
					}
					if (vMConfigurationControlist.size() <= position) {
						for (int i = vMConfigurationControlist.size(); i <= position; i++) {
							vMConfigurationControlist.add(i, null);
						}
					}
					if (vMConfigurationControlist.get(position) == null) {
						vMConfigurationControlist.set(position,
								new VMConfigurationControl(emulationControl, fileControl));
						vMConfigurationControlist.get(position).starts();
						vMConfigurationControlist.get(position).getMyName().updateMe();
					}
				} else {
					JSwtQemuView.showMessage("Please, select a valid file (*.xml)!");
				}
			}
		} else if (e.getActionCommand().equals("CloseVM")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			vMConfigurationControlist.remove(position);
			{
				vMClosingControl.setView(view);
				vMClosingControl.setEmulation(emulationControl);
			}
			vMClosingControl.starts(false);
		} else if (e.getActionCommand().equals("ChangeMachineName")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			String machineName = view.getInputMessage("Please, type a name for VM:");
			if (machineName != null) {
				if (!machineName.isEmpty()) {
					fileControl.getMachineModel().setMachineName(machineName);
					view.changeNameJPanel(machineName);
					vMConfigurationControlist.get(position).getMyName().updateMe();
				} else {
					JSwtQemuView.showMessage("Please, type a valid name for VM!");
				}
			}
		} else if (e.getActionCommand().equals("ChangeMachineConfiguration")) {
			int position;
			if (view.getActivePanel() == 0) {
				position = view.getSizeOfJTabbedPane();
			} else {
				position = view.getActivePanel();
			}
			if (vMConfigurationControlist.get(position) != null) {
				vMConfigurationControlist.get(position).restarts();
			} else {
				if (vMConfigurationControlist.size() <= position) {
					for (int i = vMConfigurationControlist.size(); i <= position; i++) {
						vMConfigurationControlist.add(i, null);
					}
				}
				vMConfigurationControlist.set(position,
						new VMConfigurationControl(emulationControl, fileControl));
				vMConfigurationControlist.get(position).starts();
				vMConfigurationControlist.get(position).restarts();
			}
		} else if (e.getActionCommand().equals("SaveVM")) {
			if (vMSavingControl == null) {
				vMSavingControl = new VMSavingControl(view.getSelectedPanel());
			} else {
				vMSavingControl.setVMSavingViewJPanel(view.getSelectedPanel());
			}

			vMSavingControl.getView().setCurrentDirectory(
					lastUsedFolderModel.getLastUsedFolder(LastUsedFolderEnumModel.SAVEEXISTINGVM.getValor()));

			if (vMSavingControl.chooseFile()) {
				String result = vMSavingControl.getView().getChoice();
				if (result.length() < 5) {
					result += ".xml";
				} else if (!result.substring(result.length() - 4).equals(".xml")) {
					result += ".xml";
				}
				vMSavingControl.setSavedVMPath(result);

				lastUsedFolderModel.setLastUsedFolder(LastUsedFolderEnumModel.SAVEEXISTINGVM.getValor(),
						vMSavingControl.getView().getChooser().getCurrentDirectory().getAbsolutePath());

				fileControl.getMachineModel().saveToXML(vMSavingControl.getSavedVMPath());
			}
		} else if (e.getActionCommand().equals("CreateNewDiskImageFile")) {
			if (diskCreationControl == null) {
				diskCreationControl = new DiskCreationControl("0", this);
			}
			if (configurationControl == null) {
				JSwtQemuView.showMessage("Please, configure the required options first!"
						+ "\nPlease, configure the path of the default Virtual Machines first!");
			} else if (configurationControl.getDefault_virtual_machines_path().isEmpty()) {
				JSwtQemuView.showMessage("Please, configure the path of the default Virtual Machines first!");
				configurationControl.setVisibleEnable();
			} else {
				diskCreationControl
						.setDefault_virtual_machines_path(configurationControl.getDefault_virtual_machines_path());
				if (configurationControl.getQemu_img_executable_path() == null) {
					JSwtQemuView.showMessage("Please, configure the path of the qemu-img executable first!");
					configurationControl.setVisibleEnable();
				} else {
					diskCreationControl.setPathQemu_img(configurationControl.getQemu_img_executable_path().getText());
					diskCreationControl.change_visibility(true);
				}

			}
		} else if (e.getActionCommand().equals("Cancel_CreateNewDiskImageFile")) {
			diskCreationControl.unsetBoxSelections();
			diskCreationControl.change_visibility(false);
		} else if (e.getActionCommand().equals("OK_CreateNewDiskImageFile")) {
			diskCreationControl.setSizeMB(diskCreationControl.getDiskImageSize());
			diskCreationControl.setFileName(diskCreationControl.getDiskName().getText(),
					(String) diskCreationControl.getDiskExtension().getSelectedItem());
			try {
				diskCreationControl.runsThisIfTrue();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				diskCreationControl.showsOutput();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			if (diskCreationControl.getMessage().contains("Formatting")
					|| diskCreationControl.getMessage().contains("Creating")) {
				JSwtQemuView.showMessage("The disk image was created!");
			}

			diskCreationControl.unsetBoxSelections();
			diskCreationControl.change_visibility(false);
		} else if (e.getActionCommand().equals("DiskExtension_CreateNewDiskImageFile")) {
			diskCreationControl.addsComponent((String) diskCreationControl.getDiskExtension().getSelectedItem());
		} else if (e.getActionCommand().equals("seeOutputsFromProcesses")) {
			try {
				emulationControl.showsMessages();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals("useUtilities")) {
			useUtilitiesAction.actionPerformed(e);
		} else if (e.getActionCommand().equals("Hide_Utilities")) {
			useUtilitiesAction.actionPerformed(e);
		} else if (e.getActionCommand().equals("seeQemuVersion")) {
			if (utilitiesModel == null) {
				try {
					if (configurationControl != null) {
						if (configurationControl.getQemu_executable_path() != null) {
							utilitiesModel = new UtilitiesModel(
									Runtime.getRuntime()
											.exec(configurationControl.getQemu_executable_path().getText()
													+ " -version"),
									utilitiesView,
									getQemuPathDir(configurationControl.getQemu_executable_path().getText()));
							if (!utilitiesModel.inheritIO_Output_Qemu()) {
								while (utilitiesModel.isRunning()) {

								}
								if (!utilitiesModel.readsFile()) {
									utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
								}
							}
						} else {
							JSwtQemuView.showMessage("Please, configure the required options first!"
									+ "\nPlease, configure the path of the default Virtual Machines first!");
							configurationControl.setVisibleEnable();
						}
					} else {
						JSwtQemuView.showMessage("Please, configure the required options first!"
								+ "\nPlease, configure the path of the default Virtual Machines first!");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					utilitiesModel.setMyprocess(Runtime.getRuntime()
							.exec(configurationControl.getQemu_executable_path().getText() + " -version"));
					utilitiesModel
							.setQemuPathDir(getQemuPathDir(configurationControl.getQemu_executable_path().getText()));
					if (!utilitiesModel.inheritIO_Output_Qemu()) {
						while (utilitiesModel.isRunning()) {

						}
						if (!utilitiesModel.readsFile()) {
							utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("seeQemuImgVersion")) {
			if (utilitiesModel == null) {
				try {
					if (configurationControl != null) {
						if (configurationControl.getQemu_img_executable_path() != null) {
							utilitiesModel = new UtilitiesModel(
									Runtime.getRuntime()
											.exec(configurationControl.getQemu_img_executable_path().getText()),
									utilitiesView, null);
							if (!utilitiesModel.inheritIO_Output_QemuImg()) {
								while (utilitiesModel.isRunning()) {

								}
								utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
							}
						} else {
							JSwtQemuView.showMessage("Please, configure the required options first!"
									+ "\nPlease, configure the path of the default Virtual Machines first!");
							configurationControl.setVisibleEnable();
						}
					} else {
						JSwtQemuView.showMessage("Please, configure the required options first!"
								+ "\nPlease, configure the path of the default Virtual Machines first!");
						configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					utilitiesModel.setMyprocess(
							Runtime.getRuntime().exec(configurationControl.getQemu_img_executable_path().getText()));
					utilitiesModel.setQemuPathDir(null);
					if (!utilitiesModel.inheritIO_Output_QemuImg()) {
						while (utilitiesModel.isRunning()) {

						}
						utilitiesView.showMessage("Sorry! The requested information can not be obtained.");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("SeeQemuEmulationCommandLine")) {

				if (configurationControl.getQemu_executable_path() != null) {
					emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());
				}

				if (configurationControl.getBios_vga_bios_keymaps_path() != null) {
					emulationControl
							.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());
				}
			} else if (configurationControl != null) {
				if (configurationControl.getQemu_executable_path() != null) {
					emulationControl.setPathQemu(configurationControl.getQemu_executable_path().getText());
				}

				if (configurationControl.getBios_vga_bios_keymaps_path() != null) {
					emulationControl
							.setBiosVgaBiosKeymapsPath(configurationControl.getBios_vga_bios_keymaps_path().getText());
				}
			}

			if (view.getActivePanel() == 0) {
				JSwtQemuView.showMessage(
						"Please, select an open virtual machine tab before seeing the Qemu emulation command line.");
			} else if (!emulationControl.getFullCommandLine(view.getActivePanel()).isEmpty()) {
				JSwtQemuView.showMessage("The Qemu emulation command line is:\n"
						+ emulationControl.getFullCommandLine(view.getActivePanel()) + "\n!");
			} else {
				JSwtQemuView.showMessage("Sorry! There is no Qemu emulation command line, or it is empty!");
			}
		}


	public List<String> JTextAreaToArrayListOfStrings(JTextArea given) {
		List<String> result = new ArrayList<String>();
		if (given != null) {
			String[] helper = given.getText().split("\n");
			for (int i = 0; i < helper.length; i++) {
				result.add(helper[i]);
			}
		}
		return result;
	}

	private String getQemuPathDir(String qemuPath) {
		String extension = checks_extension(qemuPath);
		int position = qemuPath.lastIndexOf(extension);
		return qemuPath.substring(0, position);
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

}
