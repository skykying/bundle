package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.lembed.lite.studio.qemu.model.swt.CPUModel;
import com.lembed.lite.studio.qemu.view.internal.swt.CPUIDFlagView;
import com.lembed.lite.studio.qemu.view.internal.swt.CPUView;
import com.lembed.lite.studio.qemu.view.internal.swt.UtilitiesView;

public class CPUControl implements BaseControl {

    private final CPUModel cpuModel;
    private final CPUView cpuView;
    private final CPUIDFlagView cpuIdFlagView;
    private final EmulatorQemuMachineControl cpuFileControl;

    public CPUControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
        this.cpuModel = new CPUModel(ec);
        this.cpuView = new CPUView(emc);
        this.cpuIdFlagView = new CPUIDFlagView(emc);
        this.cpuFileControl = emc;
    }

    public void starts() {
        cpuView.configureListener(this);
        cpuView.configureStandardMode();
        cpuIdFlagView.configureListener(this);
        cpuIdFlagView.configureStandardMode();
        
        if (cpuIdFlagView.getLoaded() || cpuView.getLoaded()) {
        	
            cpuModel.buildCPUModel(cpuView.getCpuModels());
            JList<String> clist = cpuIdFlagView.getSelectedList();
            DefaultListModel<String> model = (DefaultListModel<String>) clist.getModel();
            String[] result = new String[model.size()];
            for (int i = 0; i < model.size(); i++) {
                result[i] = model.getElementAt(i);
            }
            cpuModel.buildCPUIDFlags(result);
            cpuModel.buildIt();
        }
    }

    public void change_the_visibility_of_model_view(Boolean value) {
        cpuView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            if (cpuView.getCpuModels().getSelectedIndex() != 0) {
                cpuView.getCpuModels().setSelectedIndex(0);
            }
            cpuModel.buildCPUModel(cpuView.getCpuModels());
            cpuModel.buildIt();
            cpuFileControl.getMachineModel().setCpuModel(cpuModel.getCpuModel());
            cpuView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            this.cpuModel.buildCPUModel(this.cpuView.getCpuModels());
            this.cpuModel.buildIt();
            this.cpuFileControl.getMachineModel().setCpuModel(this.cpuModel.getCpuModel());
            this.cpuView.setVisible(false);
        } else if (e.getActionCommand().equals("showsFlagButton")) {
            this.cpuIdFlagView.setVisible(true);
        } else if (e.getActionCommand().equals("eraseButton2")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cpuIdFlagView.getSelectedList().getModel();
            int selectedIndex = this.cpuIdFlagView.getSelectedList().getSelectedIndex();
            if (selectedIndex != -1) {
                model.remove(selectedIndex);
                String[] result = new String[model.size()];
                for (int i = 0; i < model.size(); i++) {
                    result[i] = model.getElementAt(i);
                }
                this.cpuModel.buildCPUIDFlags(result);
                this.cpuModel.buildIt();
                this.cpuFileControl.getMachineModel().setCpuidFlags(this.cpuModel.getCpuidFlags());
            }
        } else if (e.getActionCommand().equals("okButton2")) {
            this.cpuIdFlagView.setVisible(false);
        } else if (e.getActionCommand().equals("addButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cpuIdFlagView.getSelectedList().getModel();
            if (!model.contains(this.cpuIdFlagView.getAvailableList().getSelectedItem().toString())) {
                model.addElement(this.cpuIdFlagView.getAvailableList().getSelectedItem().toString());
                String[] result = new String[model.size()];
                for (int i = 0; i < model.size(); i++) {
                    result[i] = model.getElementAt(i);
                }
                this.cpuModel.buildCPUIDFlags(result);
                this.cpuModel.buildIt();
                this.cpuFileControl.getMachineModel().setCpuidFlags(this.cpuModel.getCpuidFlags());
            } else {
                UtilitiesView.showMessageAnywhere("This element already is added to the list.");
            }
        } else if (e.getActionCommand().equals("eraseAllButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cpuIdFlagView.getSelectedList().getModel();
            model.removeAllElements();
            String[] result = new String[1];
            result[0] = "";

            this.cpuModel.buildCPUIDFlags(result);
            this.cpuModel.buildIt();
            this.cpuFileControl.getMachineModel().setCpuidFlags(this.cpuModel.getCpuidFlags());
            this.cpuIdFlagView.setVisible(false);
        } else if (e.getActionCommand().equals("findButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cpuIdFlagView.getSelectedList().getModel();
            if (!model.contains(this.cpuIdFlagView.getAvailableList().getSelectedItem().toString())) {
                UtilitiesView.showMessageAnywhere("The list doesn't contain the element: "
                                + this.cpuIdFlagView.getAvailableList().getSelectedItem().toString() + ".");
            } else {
                UtilitiesView.showMessageAnywhere("The list contains the element: "
                                + this.cpuIdFlagView.getAvailableList().getSelectedItem().toString() + ".");
            }
        }
    }
}
