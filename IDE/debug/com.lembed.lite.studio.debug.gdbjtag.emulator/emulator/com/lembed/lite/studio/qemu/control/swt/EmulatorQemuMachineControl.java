package com.lembed.lite.studio.qemu.control.swt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.model.swt.EmulatorQemuMachineModel;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.swt.FileView;

public class EmulatorQemuMachineControl {

    private final List<EmulatorQemuMachineModel> fileModelList;
    private final FileView fileView;
    
    private int position;
    private JQemuView view;

    public EmulatorQemuMachineControl(JPanel jpanel, JQemuView view) {
        super();
        fileModelList = new ArrayList<EmulatorQemuMachineModel>();
        fileView = new FileView(jpanel);
        this.view = view;
    }

    public EmulatorQemuMachineModel getMachineModel() {
        if (view != null) {
            if (view.getActivePanel() == 0) {
                position = view.getSizeOfJTabbedPane();
            } else {
                position = view.getActivePanel();
            }
        } else {
            position = 0;
        }

        if (fileModelList.size() <= position) {
            for (int i = fileModelList.size(); i <= position; i++) {
                fileModelList.add(i, null);
            }
        }
        if (fileModelList.get(position) == null) {
            fileModelList.set(position, new EmulatorQemuMachineModel());
        }
        return fileModelList.get(position);
    }

    public FileView getFileview() {
        return fileView;
    }
}
