package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.VMConfigurationView;

public class VMConfigurationControl implements BaseControl {

    private VMConfigurationView cfgView;
    private RAMControl ramCTL;
    private OptionsDisplayControl mydisplay;
    private HardDiskControl myhd;
    private AdvancedOptionsControl myadvancedoptions;
    private MachineControl mymachine;
    private CPUControl cpuCTL;
    private CDROMControl mycdrom;
    private FloppyControl floppyCTL;
    private BootControl bootCTL;
    private KeyboardControl kbCTL;
    private SoundHardwareControl soundHardwareCTL;
    private SMPControl smpCTL;
    private NUMAControl mynuma;
    private MemoryControl mymemory;
    private NetworkManagerControl mynetwork;
    private TimeControl mytime;
    private ImageControl myimage;
    private MonitorControl mymonitor;
    private USBControl myusb;
    private SpecificBootControl myspecificboot;
    private NameControl myName;
    private CustomOptionsControl myCustomOptions;

    public NameControl getMyName() {
        return myName;
    }

    public VMConfigurationControl(EmulationControl eCTL, JContainerView view,
            EmulatorQemuMachineControl eQMCTL) {
        ramCTL = new RAMControl(eCTL, eQMCTL);
        cfgView = new VMConfigurationView(view,
                this.ramCTL.getOption());
        mydisplay = new OptionsDisplayControl(eCTL, eQMCTL);
        myhd = new HardDiskControl(eCTL, eQMCTL);
        myadvancedoptions = new AdvancedOptionsControl(eCTL, eQMCTL, view);
        mymachine = new MachineControl(eCTL, eQMCTL);
        cpuCTL = new CPUControl(eCTL, eQMCTL);
        mycdrom = new CDROMControl(eCTL, eQMCTL);
        floppyCTL = new FloppyControl(eCTL, eQMCTL);
        bootCTL = new BootControl(eCTL, eQMCTL);
        kbCTL = new KeyboardControl(eCTL, eQMCTL);
        soundHardwareCTL = new SoundHardwareControl(eCTL, eQMCTL);
        smpCTL = new SMPControl(eCTL, eQMCTL);
        mynuma = new NUMAControl(eCTL, eQMCTL);
        mymemory = new MemoryControl(eCTL, eQMCTL);
        mynetwork = new NetworkManagerControl(eCTL, eQMCTL);
        mytime = new TimeControl(eCTL, eQMCTL);
        myimage = new ImageControl(eCTL, eQMCTL);
        mymonitor = new MonitorControl(eCTL, eQMCTL);
        myusb = new USBControl(eCTL, eQMCTL);
        myspecificboot = new SpecificBootControl(eCTL, eQMCTL);
        myName = new NameControl(eCTL, eQMCTL, view);
        myCustomOptions = new CustomOptionsControl(eCTL, eQMCTL);
    }

    public void starts() {
        this.cfgView.setVisible(false);
        this.cfgView.configureStandardMode();
        this.cfgView.configureListener(this);
        this.myhd.starts();
        this.mymachine.starts();
        this.cpuCTL.starts();
        this.mycdrom.starts();
        this.floppyCTL.starts();
        this.bootCTL.starts();
        this.smpCTL.starts();
        this.mynuma.starts();
        this.mymemory.starts();
        this.mytime.starts();
        this.myimage.starts();
    }

    public void restarts() {
        this.cfgView.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("okButton")) {
            this.ramCTL.setOption(this.cfgView.getEditor().getTextField()
                    .getText().replace(",", "."));

            this.cfgView.setRamSize(ramCTL.getOption());
            this.cfgView.setVisible(false);
        } else if (e.getActionCommand().equals("resetRamSizeButton")) {
            this.ramCTL.setOption("128.000");
            this.cfgView.getEditor().getTextField().setText("128,000");
            this.cfgView.setRamSize(this.ramCTL.getOption());
            this.cfgView.setVisible(false);
        } else if (e.getActionCommand().equals("showDisplayOptions")) {
            mydisplay.setVisible(true);
        } else if (e.getActionCommand().equals("changeDiskOptionsPaths")) {
            this.myhd.setVisible(true);
        } else if (e.getActionCommand().equals("showAdvancedOptions")) {
            this.myadvancedoptions.setVisible(true);
        } else if (e.getActionCommand().equals("showMachineOptions")) {
            this.mymachine.change_the_visibility_of_type_view(true);
        } else if (e.getActionCommand().equals("showCPUOptions")) {
            this.cpuCTL.change_the_visibility_of_model_view(true);
        } else if (e.getActionCommand().equals("showCDROMOptions")) {
            this.mycdrom.setVisible(true);
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
            this.mynuma.setVisible(true);
        } else if (e.getActionCommand().equals("showOtherMemoryOptions")) {
            this.mymemory.change_the_visibility_of_view(true);
        } else if (e.getActionCommand().equals("showNetworkOptions")) {
            this.mynetwork.setVisible(true);
        } else if (e.getActionCommand().equals("showRtcOptions")) {
            this.mytime.change_the_visibility_of_view(true);
        } else if (e.getActionCommand().equals("showImageOptions")) {
            this.myimage.setVisible(true);
        } else if (e.getActionCommand().equals("showMonitorOptions")) {
            this.mymonitor.setVisible(true);
        } else if (e.getActionCommand().equals("showUSBOptions")) {
            this.myusb.setVisible(true);
        } else if (e.getActionCommand().equals("showSpecificBootOptions")) {
            this.myspecificboot.setVisible(true);
        } else if (e.getActionCommand().equals("showCustomOptions")) {
            this.myCustomOptions.setVisible(true);
        }
    }
}
