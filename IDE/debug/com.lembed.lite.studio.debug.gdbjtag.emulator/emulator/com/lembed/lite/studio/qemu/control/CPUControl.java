package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.lembed.lite.studio.qemu.model.CPUModel;
import com.lembed.lite.studio.qemu.view.internal.CPUIDFlagView;
import com.lembed.lite.studio.qemu.view.internal.CPUView;
import com.lembed.lite.studio.qemu.view.internal.UtilitiesView;

public class CPUControl implements ActionListener {

    private final CPUModel cpuModel;
    private final CPUView cpuView;
    private final CPUIDFlagView cpuIdFlagView;
    private final FileControl cpuFileControl;

    public CPUControl(EmulationControl myemulation, FileControl fileControl) {
        this.cpuModel = new CPUModel(myemulation);
        this.cpuView = new CPUView(fileControl);
        this.cpuIdFlagView = new CPUIDFlagView(fileControl);
        this.cpuFileControl = fileControl;
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
            cpuFileControl.getFilemodel().setCpuModel(cpuModel.getCpuModel());
            cpuView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            this.cpuModel.buildCPUModel(this.cpuView.getCpuModels());
            this.cpuModel.buildIt();
            this.cpuFileControl.getFilemodel().setCpuModel(this.cpuModel.getCpuModel());
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
                this.cpuFileControl.getFilemodel().setCpuidFlags(this.cpuModel.getCpuidFlags());
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
                this.cpuFileControl.getFilemodel().setCpuidFlags(this.cpuModel.getCpuidFlags());
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
            this.cpuFileControl.getFilemodel().setCpuidFlags(this.cpuModel.getCpuidFlags());
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
