package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import com.lembed.lite.studio.qemu.model.swt.MachineModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.MachineOptionsView;
import com.lembed.lite.studio.qemu.view.internal.swt.MachineTypeView;

public class MachineControl implements BaseControl {

    private MachineModel machineModel;
    private MachineTypeView machineTypeView;
    private MachineOptionsView machineOptionsView;
    private EmulatorQemuMachineControl eQctl;

    public MachineControl(EmulationControl myemulation, EmulatorQemuMachineControl emc) {
        this.machineModel = new MachineModel(myemulation);
        machineOptionsView = new MachineOptionsView(emc);
        machineTypeView = new MachineTypeView(emc);
        this.eQctl = emc;
    }

    public void starts() {
        this.machineOptionsView.configureStandardMode();
        this.machineOptionsView.configureListener(this);
        this.machineTypeView.configureStandardMode();
        this.machineTypeView.configureListener(this);
        if (this.machineOptionsView.getLoaded() || this.machineTypeView.getLoaded()) {
            if (this.machineTypeView.getMachineTypes().getSelectedItem().toString()
                    .indexOf(":") != -1) {
                this.machineModel.buildIt(
                        this.machineTypeView
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.machineTypeView.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2),
                        this.machineModel.buildAccel((String) this.machineOptionsView
                                .getFirstOption().getSelectedItem(),
                                (String) this.machineOptionsView.getSecondOption()
                                .getSelectedItem(),
                                (String) this.machineOptionsView.getThirdOption()
                                .getSelectedItem()),
                        (String) this.machineOptionsView.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.machineOptionsView.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.machineOptionsView.getDump_guest_core()
                        .getSelectedItem(), (String) this.machineOptionsView
                        .getMem_merge().getSelectedItem());
            } else {
                this.machineModel.buildIt(this.machineTypeView.getMachineTypes()
                        .getSelectedItem().toString().toLowerCase(Locale.ENGLISH),
                        this.machineModel.buildAccel((String) this.machineOptionsView
                                .getFirstOption().getSelectedItem(),
                                (String) this.machineOptionsView.getSecondOption()
                                .getSelectedItem(),
                                (String) this.machineOptionsView.getThirdOption()
                                .getSelectedItem()),
                        (String) this.machineOptionsView.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.machineOptionsView.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.machineOptionsView.getDump_guest_core()
                        .getSelectedItem(), (String) this.machineOptionsView
                        .getMem_merge().getSelectedItem());
            }
        }
    }

    public void change_the_visibility_of_type_view(Boolean value) {
        this.machineTypeView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.machineTypeView.getMachineTypes().setSelectedIndex(0);
            this.machineModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MACHINEOPTION.getValor()]);
            this.machineTypeView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (this.machineTypeView.getMachineTypes().getSelectedItem().toString()
                    .indexOf(":") != -1) {
                this.machineModel.buildIt(
                        this.machineTypeView
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.machineTypeView.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2),
                        this.machineModel.buildAccel((String) this.machineOptionsView
                                .getFirstOption().getSelectedItem(),
                                (String) this.machineOptionsView.getSecondOption()
                                .getSelectedItem(),
                                (String) this.machineOptionsView.getThirdOption()
                                .getSelectedItem()),
                        (String) this.machineOptionsView.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.machineOptionsView.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.machineOptionsView.getDump_guest_core()
                        .getSelectedItem(), (String) this.machineOptionsView
                        .getMem_merge().getSelectedItem());
                eQctl.getMachineModel().setMachineType(
                        this.machineTypeView
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.machineTypeView.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2));
            } else {
                this.machineModel.buildIt(this.machineTypeView.getMachineTypes()
                        .getSelectedItem().toString().toLowerCase(Locale.ENGLISH),
                        this.machineModel.buildAccel((String) this.machineOptionsView
                                .getFirstOption().getSelectedItem(),
                                (String) this.machineOptionsView.getSecondOption()
                                .getSelectedItem(),
                                (String) this.machineOptionsView.getThirdOption()
                                .getSelectedItem()),
                        (String) this.machineOptionsView.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.machineOptionsView.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.machineOptionsView.getDump_guest_core()
                        .getSelectedItem(), (String) this.machineOptionsView
                        .getMem_merge().getSelectedItem());
                eQctl.getMachineModel().setMachineType(
                        this.machineTypeView.getMachineTypes().getSelectedItem()
                        .toString().toLowerCase(Locale.ENGLISH));
            }
            this.machineTypeView.setVisible(false);
        } else if (e.getActionCommand().equals("eraseButton2")) {
            this.machineOptionsView.getFirstOption().setSelectedIndex(0);
            this.machineOptionsView.getSecondOption().setSelectedIndex(0);
            this.machineOptionsView.getThirdOption().setSelectedIndex(0);
            this.machineOptionsView.getKernel_irqchip().setSelectedIndex(0);
            this.machineOptionsView.getEditor().getTextField().setText("0");
            this.machineOptionsView.getDump_guest_core().setSelectedIndex(0);
            this.machineOptionsView.getMem_merge().setSelectedIndex(0);
            this.machineOptionsView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton2")) {
        	eQctl.getMachineModel().setMachineAccel1(
                    (String) this.machineOptionsView.getFirstOption().getSelectedItem());
        	eQctl.getMachineModel()
                    .setMachineAccel2(
                            (String) this.machineOptionsView.getSecondOption()
                            .getSelectedItem());
        	eQctl.getMachineModel().setMachineAccel3(
                    (String) this.machineOptionsView.getThirdOption().getSelectedItem());
        	eQctl.getMachineModel().setMachineKernel_irpchip(
                    (String) this.machineOptionsView.getKernel_irqchip()
                    .getSelectedItem());
            eQctl.getMachineModel().setMachineKvm_shadow_mem(
                    this.machineOptionsView.getEditor().getTextField().getText());
            eQctl.getMachineModel().setMachineDump_guest_core(
                    (String) this.machineOptionsView.getDump_guest_core()
                    .getSelectedItem());
            eQctl.getMachineModel().setMachineMem_merge(
                    (String) this.machineOptionsView.getMem_merge().getSelectedItem());
            this.machineOptionsView.setVisible(false);
        } else if (e.getActionCommand().equals("showOptionsButton")) {
            this.machineOptionsView.setVisible(true);
        } else if (e.getActionCommand().equals("firstOption")
                || e.getActionCommand().equals("secondOption")
                || e.getActionCommand().equals("thirdOption")) {
            this.machineOptionsView.resolveAccelOptions();
        }
    }

}
