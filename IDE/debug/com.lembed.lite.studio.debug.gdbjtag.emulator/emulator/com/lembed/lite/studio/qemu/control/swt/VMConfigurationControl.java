package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.qemu.view.internal.swt.VMConfigurationView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;

public class VMConfigurationControl implements BaseControl {

	private VMConfigurationView cfgView;

	private RAMControl ramCTL;
	private OptionsDisplayControl displayCTL;
	private HardDiskControl hdCTL;
	private AdvancedOptionsControl advancedoptionsCTL;
	private MachineControl machineCTL;
	private CPUControl cpuCTL;
	private CDROMControl cdromCTL;
	private FloppyControl floppyCTL;
	private BootControl bootCTL;
	private KeyboardControl kbCTL;
	private SoundHardwareControl soundHardwareCTL;
	private SMPControl smpCTL;
	private NUMAControl numaCTL;
	private MemoryControl memoryCTL;
	private NetworkManagerControl networkCTL;
	private TimeControl timeCTL;
	private ImageControl imageCTL;
	private MonitorControl monitorCTL;
	private USBControl usbCTL;
	private SpecificBootControl specificbootCTL;
	private NameControl nameCTL;
	private CustomOptionsControl customOptionsCTL;

	public NameControl getMyName() {
		return nameCTL;
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		ListAppend(list,ramCTL.getViews());
		ListAppend(list,displayCTL.getViews());
		ListAppend(list,hdCTL.getViews());
		ListAppend(list,advancedoptionsCTL.getViews());
		
		ListAppend(list,machineCTL.getViews());
		ListAppend(list,cpuCTL.getViews());
		ListAppend(list,cdromCTL.getViews());
		ListAppend(list,floppyCTL.getViews());
		ListAppend(list,bootCTL.getViews());
		
		ListAppend(list,kbCTL.getViews());
		ListAppend(list,soundHardwareCTL.getViews());
		ListAppend(list,smpCTL.getViews());
		ListAppend(list,numaCTL.getViews());
		
		ListAppend(list,memoryCTL.getViews());
		ListAppend(list,networkCTL.getViews());
		ListAppend(list,timeCTL.getViews());
		ListAppend(list,imageCTL.getViews());
		
		ListAppend(list,monitorCTL.getViews());
		ListAppend(list,usbCTL.getViews());
		ListAppend(list,specificbootCTL.getViews());
		ListAppend(list,customOptionsCTL.getViews());

		return list;
	}


	public void ListAppend(List<DeviceBaseView> list, List<DeviceBaseView> list2){

		for(DeviceBaseView e : list2) {
			list.add(e);
		}
	}
	
	public VMConfigurationControl(EmulationControl eCTL,EmulatorQemuMachineControl eQMCTL) {
		ramCTL = new RAMControl(eCTL, eQMCTL);
		cfgView = new VMConfigurationView (ramCTL.getOption());
		displayCTL = new OptionsDisplayControl(eCTL, eQMCTL);
		hdCTL = new HardDiskControl(eCTL, eQMCTL);
		advancedoptionsCTL = new AdvancedOptionsControl(eCTL, eQMCTL);
		machineCTL = new MachineControl(eCTL, eQMCTL);
		cpuCTL = new CPUControl(eCTL, eQMCTL);
		cdromCTL = new CDROMControl(eCTL, eQMCTL);
		floppyCTL = new FloppyControl(eCTL, eQMCTL);
		bootCTL = new BootControl(eCTL, eQMCTL);
		kbCTL = new KeyboardControl(eCTL, eQMCTL);
		soundHardwareCTL = new SoundHardwareControl(eCTL, eQMCTL);
		smpCTL = new SMPControl(eCTL, eQMCTL);
		numaCTL = new NUMAControl(eCTL, eQMCTL);
		memoryCTL = new MemoryControl(eCTL, eQMCTL);
		networkCTL = new NetworkManagerControl(eCTL, eQMCTL);
		timeCTL = new TimeControl(eCTL, eQMCTL);
		imageCTL = new ImageControl(eCTL, eQMCTL);
		monitorCTL = new MonitorControl(eCTL, eQMCTL);
		usbCTL = new USBControl(eCTL, eQMCTL);
		specificbootCTL = new SpecificBootControl(eCTL, eQMCTL);
		nameCTL = new NameControl(eCTL, eQMCTL);
		customOptionsCTL = new CustomOptionsControl(eCTL, eQMCTL);
	}

	public void starts() {
		this.cfgView.setVisible(false);
		this.cfgView.configureStandardMode();
		this.cfgView.configureListener(this);
		this.hdCTL.starts();
		this.machineCTL.init();
		this.cpuCTL.starts();
		this.cdromCTL.starts();
		this.floppyCTL.starts();
		this.bootCTL.starts();
		this.smpCTL.starts();
		this.numaCTL.starts();
		this.memoryCTL.starts();
		this.timeCTL.starts();
		this.imageCTL.init();
	}

	public void restarts() {
		this.cfgView.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("okButton")) {
			this.ramCTL.setOption(this.cfgView.getEditor().getTextField().getText().replace(",", "."));

			this.cfgView.setRamSize(ramCTL.getOption());
			this.cfgView.setVisible(false);
		} else if (e.getActionCommand().equals("resetRamSizeButton")) {
			this.ramCTL.setOption("128.000");
			this.cfgView.getEditor().getTextField().setText("128,000");
			this.cfgView.setRamSize(this.ramCTL.getOption());
			this.cfgView.setVisible(false);
		} else if (e.getActionCommand().equals("showDisplayOptions")) {
			displayCTL.setVisible(true);
		} else if (e.getActionCommand().equals("changeDiskOptionsPaths")) {
			this.hdCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showAdvancedOptions")) {
			this.advancedoptionsCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showMachineOptions")) {
			this.machineCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showCPUOptions")) {
			this.cpuCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showCDROMOptions")) {
			this.cdromCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showFloppyOptions")) {
			this.floppyCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showBootOptions")) {
			this.bootCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showKeyboardOptions")) {
			this.kbCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showSoundHardwareOptions")) {
			this.soundHardwareCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showSMPOptions")) {
			this.smpCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showNUMAOptions")) {
			this.numaCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showOtherMemoryOptions")) {
			this.memoryCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showNetworkOptions")) {
			this.networkCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showRtcOptions")) {
			this.timeCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showImageOptions")) {
			this.imageCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showMonitorOptions")) {
			this.monitorCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showUSBOptions")) {
			this.usbCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showSpecificBootOptions")) {
			this.specificbootCTL.setVisible(true);
		} else if (e.getActionCommand().equals("showCustomOptions")) {
			this.customOptionsCTL.setVisible(true);
		}
	}
}
