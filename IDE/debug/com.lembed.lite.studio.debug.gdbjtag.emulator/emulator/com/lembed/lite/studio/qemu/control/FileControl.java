package com.lembed.lite.studio.qemu.control;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.model.FileModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.FileView;

public class FileControl {

    private final List<FileModel> fileModelList;
    private final FileView fileView;
    
    private int position;
    private JContainerView view;

    public FileControl(JPanel jpanel, JContainerView view) {
        super();
        fileModelList = new ArrayList<FileModel>();
        fileView = new FileView(jpanel);
        view = view;
    }

    public FileModel getFilemodel() {
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
            fileModelList.set(position, new FileModel());
        }
        return fileModelList.get(position);
    }

    public FileView getFileview() {
        return fileView;
    }
}
