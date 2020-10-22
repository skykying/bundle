package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import com.lembed.lite.studio.qemu.model.MachineModel;
import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.internal.MachineOptionsView;
import com.lembed.lite.studio.qemu.view.internal.MachineTypeView;

public class MachineControl implements ActionListener {

    private MachineModel mymodel;
    private MachineTypeView mytype;
    private MachineOptionsView myoptions;
    private FileControl myfile;

    public MachineControl(EmulationControl myemulation, FileControl myfile) {
        this.mymodel = new MachineModel(myemulation);
        myoptions = new MachineOptionsView(myfile);
        mytype = new MachineTypeView(myfile);
        this.myfile = myfile;
    }

    public void starts() {
        this.myoptions.configureStandardMode();
        this.myoptions.configureListener(this);
        this.mytype.configureStandardMode();
        this.mytype.configureListener(this);
        if (this.myoptions.getLoaded() || this.mytype.getLoaded()) {
            if (this.mytype.getMachineTypes().getSelectedItem().toString()
                    .indexOf(":") != -1) {
                this.mymodel.buildIt(
                        this.mytype
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.mytype.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2),
                        this.mymodel.buildAccel((String) this.myoptions
                                .getFirstOption().getSelectedItem(),
                                (String) this.myoptions.getSecondOption()
                                .getSelectedItem(),
                                (String) this.myoptions.getThirdOption()
                                .getSelectedItem()),
                        (String) this.myoptions.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.myoptions.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.myoptions.getDump_guest_core()
                        .getSelectedItem(), (String) this.myoptions
                        .getMem_merge().getSelectedItem());
            } else {
                this.mymodel.buildIt(this.mytype.getMachineTypes()
                        .getSelectedItem().toString().toLowerCase(Locale.ENGLISH),
                        this.mymodel.buildAccel((String) this.myoptions
                                .getFirstOption().getSelectedItem(),
                                (String) this.myoptions.getSecondOption()
                                .getSelectedItem(),
                                (String) this.myoptions.getThirdOption()
                                .getSelectedItem()),
                        (String) this.myoptions.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.myoptions.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.myoptions.getDump_guest_core()
                        .getSelectedItem(), (String) this.myoptions
                        .getMem_merge().getSelectedItem());
            }
        }
    }

    public void change_the_visibility_of_type_view(Boolean value) {
        this.mytype.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.mytype.getMachineTypes().setSelectedIndex(0);
            this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MACHINEOPTION.getValor()]);
            this.mytype.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (this.mytype.getMachineTypes().getSelectedItem().toString()
                    .indexOf(":") != -1) {
                this.mymodel.buildIt(
                        this.mytype
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.mytype.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2),
                        this.mymodel.buildAccel((String) this.myoptions
                                .getFirstOption().getSelectedItem(),
                                (String) this.myoptions.getSecondOption()
                                .getSelectedItem(),
                                (String) this.myoptions.getThirdOption()
                                .getSelectedItem()),
                        (String) this.myoptions.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.myoptions.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.myoptions.getDump_guest_core()
                        .getSelectedItem(), (String) this.myoptions
                        .getMem_merge().getSelectedItem());
                myfile.getFilemodel().setMachineType(
                        this.mytype
                        .getMachineTypes()
                        .getSelectedItem()
                        .toString()
                        .substring(
                                this.mytype.getMachineTypes()
                                .getSelectedItem().toString()
                                .indexOf(":") + 2));
            } else {
                this.mymodel.buildIt(this.mytype.getMachineTypes()
                        .getSelectedItem().toString().toLowerCase(Locale.ENGLISH),
                        this.mymodel.buildAccel((String) this.myoptions
                                .getFirstOption().getSelectedItem(),
                                (String) this.myoptions.getSecondOption()
                                .getSelectedItem(),
                                (String) this.myoptions.getThirdOption()
                                .getSelectedItem()),
                        (String) this.myoptions.getKernel_irqchip()
                        .getSelectedItem(), Double
                        .parseDouble(this.myoptions.getEditor()
                                .getTextField().getText()
                                .replace(",", ".")),
                        (String) this.myoptions.getDump_guest_core()
                        .getSelectedItem(), (String) this.myoptions
                        .getMem_merge().getSelectedItem());
                myfile.getFilemodel().setMachineType(
                        this.mytype.getMachineTypes().getSelectedItem()
                        .toString().toLowerCase(Locale.ENGLISH));
            }
            this.mytype.setVisible(false);
        } else if (e.getActionCommand().equals("eraseButton2")) {
            this.myoptions.getFirstOption().setSelectedIndex(0);
            this.myoptions.getSecondOption().setSelectedIndex(0);
            this.myoptions.getThirdOption().setSelectedIndex(0);
            this.myoptions.getKernel_irqchip().setSelectedIndex(0);
            this.myoptions.getEditor().getTextField().setText("0");
            this.myoptions.getDump_guest_core().setSelectedIndex(0);
            this.myoptions.getMem_merge().setSelectedIndex(0);
            this.myoptions.setVisible(false);
        } else if (e.getActionCommand().equals("okButton2")) {
            myfile.getFilemodel().setMachineAccel1(
                    (String) this.myoptions.getFirstOption().getSelectedItem());
            myfile.getFilemodel()
                    .setMachineAccel2(
                            (String) this.myoptions.getSecondOption()
                            .getSelectedItem());
            myfile.getFilemodel().setMachineAccel3(
                    (String) this.myoptions.getThirdOption().getSelectedItem());
            myfile.getFilemodel().setMachineKernel_irpchip(
                    (String) this.myoptions.getKernel_irqchip()
                    .getSelectedItem());
            myfile.getFilemodel().setMachineKvm_shadow_mem(
                    this.myoptions.getEditor().getTextField().getText());
            myfile.getFilemodel().setMachineDump_guest_core(
                    (String) this.myoptions.getDump_guest_core()
                    .getSelectedItem());
            myfile.getFilemodel().setMachineMem_merge(
                    (String) this.myoptions.getMem_merge().getSelectedItem());
            this.myoptions.setVisible(false);
        } else if (e.getActionCommand().equals("showOptionsButton")) {
            this.myoptions.setVisible(true);
        } else if (e.getActionCommand().equals("firstOption")
                || e.getActionCommand().equals("secondOption")
                || e.getActionCommand().equals("thirdOption")) {
            this.myoptions.resolveAccelOptions();
        }
    }

}
