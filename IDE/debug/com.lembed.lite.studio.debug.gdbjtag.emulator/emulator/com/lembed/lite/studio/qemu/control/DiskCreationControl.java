package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.model.DiskCreationModel;
import com.lembed.lite.studio.qemu.model.UsabilityModel;
import com.lembed.lite.studio.qemu.view.internal.DiskCreationView;

public class DiskCreationControl {

    /*TO DO:
	 * put data in model... (MVC)
	 * */
    private DiskCreationModel diskModel;
    private DiskCreationView diskView;
    private String message;

    public String getMessage() {
        return message;
    }

    public DiskCreationControl(String sizeMB, ActionListener listener) {
        diskModel = new DiskCreationModel(sizeMB);
        diskModel.setDefault_virtual_machines_path("");
        diskView = new DiskCreationView();
        diskView.configureListener(listener);
        diskView.configureStandardMode();
        diskView.initialize();
    }

    public void runningRoutine() throws IOException {
        diskView.showThisInTheRunProcess(diskModel.getExecQemu_img());
        StringBuilder sb = new StringBuilder(diskModel.getExecQemu_img()[0]);
        sb.append(" ").append(diskModel.getExecQemu_img()[1]).append(" ");
        if (!diskModel.getExecQemu_img()[2].isEmpty()) {
            sb.append(diskModel.getExecQemu_img()[2]).append(" ");
        }
        sb.append(diskModel.getExecQemu_img()[3]).append(" ").append(diskModel.getExecQemu_img()[4]);
        String[] cmdLine = UsabilityModel.getCmdLine(sb.toString());
        if (cmdLine.length == 0) {
            diskModel.setMyprocess(Runtime.getRuntime().exec(sb.toString(), null));
        } else {
            diskModel.setMyprocess(Runtime.getRuntime().exec(cmdLine, null));
        }
    }

    public boolean runsThisIfTrue() throws IOException, InterruptedException {
        setOptions();
        runningRoutine();
        return true;
    }

    public boolean runsThisIfFalse(String diskExtension, List<String> options)
            throws IOException, InterruptedException {
        setOptionCreationNewVM(diskExtension, options);
        runningRoutine();
        return true;
    }

    public boolean stops() {
        if (diskModel.getMyprocess() != null) {
            diskModel.getMyprocess().destroy();
        }
        return true;
    }

    public void showsOutput() throws IOException {
        StringBuilder result = new StringBuilder("The disk image creation process output is:\n");
        StringBuilder output = new StringBuilder();
        InputStreamReader iSReader = null;
        BufferedReader reader = null;
        String line;
        try {
            iSReader = new InputStreamReader(
                    diskModel.getMyprocess().getInputStream(), Charset.forName("UTF-8"));
            reader = new BufferedReader(iSReader);
            line = reader.readLine();
            if (line != null) {
                output.append(line);
            }
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    output.append("\n").append(line);
                }
            }
            if (!output.toString().isEmpty()) {
                message = output.toString();
                result.append(output.toString());
                diskView.showMessage(result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (iSReader != null) {
                    iSReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    public void setPathQemu_img(String pathQemu_img) {
        diskModel.setQemu_imgPath(pathQemu_img);
    }

    public void setFileName(String text, String extension) {
        diskModel.setFileName(text, extension);
    }

    public String getFileName() {
        return diskModel.getFileName();
    }

    public void setDefault_virtual_machines_path(String text) {
        diskModel.setDefault_virtual_machines_path(text);
    }

    public boolean createsAdditionalDirectory(String path) {
        return diskModel.createsAdditionalDirectory(path);
    }

    public void change_visibility(boolean value) {
        diskView.setVisible(value);
    }

    public void setSizeMB(String sizeMB) {
        diskModel.setSizeMB(sizeMB);
    }

    public String getDiskImageSize() {
        return (Double.parseDouble(diskView.getEditor().getTextField()
                .getText().replace(",", ".")) * 1024) + "M";
    }

    public JTextField getDiskName() {
        return diskView.getDiskName();
    }

    public JComboBox<String> getDiskExtension() {
        return diskView.getDiskExtension();
    }

    public String getDiskImageFilePath() {
        return diskModel.getExecQemu_img()[3];
    }

    public void addsComponent(String diskExtension) {
        diskView.addsComponent(diskExtension);
    }

    public void setOptions() {
        if (diskView.getDiskExtension().getSelectedItem().equals(".img")) {
            diskModel.unsetOption();
        } else if (diskView.getDiskExtension().getSelectedItem().equals(".qcow")) {
            if (diskView.getEncryption_box().isSelected()) {
                diskModel.setOption("encryption");
            } else {
                diskModel.unsetOption();
            }
        } else if (diskView.getDiskExtension().getSelectedItem().equals(".qcow2")) {
            if (diskView.getEncryption_box().isSelected()
                    || diskView.getPreallocation_metadata_box().isSelected()
                    || diskView.getCluster_size_box().isSelected()) {
                List<String> options = new ArrayList<String>();
                if (diskView.getEncryption_box().isSelected()) {
                    options.add("encryption");
                }
                if (diskView.getPreallocation_metadata_box().isSelected()) {
                    options.add("preallocation=metadata");
                }
                if (diskView.getCluster_size_box().isSelected()) {
                    if (diskView.getCluster_size_options().getSelectedItem().equals("512k")) {
                        options.add("cluster_size=512k");
                    } else if (diskView.getCluster_size_options().getSelectedItem().equals("1M")) {
                        options.add("cluster_size=1M");
                    } else if (diskView.getCluster_size_options().getSelectedItem().equals("2M")) {
                        options.add("cluster_size=2M");
                    }
                }
                diskModel.setOptions(options);
            } else {
                diskModel.unsetOption();
            }
        } else if (diskView.getDiskExtension().getSelectedItem().equals(".cow")) {
            diskModel.unsetOption();
        } else if (diskView.getDiskExtension().getSelectedItem().equals(".vdi")) {
            if (diskView.getStatic_vdi_box().isSelected()) {
                diskModel.setOption("static");
            } else {
                diskModel.unsetOption();
            }
        } else if (diskView.getDiskExtension().getSelectedItem()
                .equals(".vmdk")) {
            if (diskView.getCompat6_vmdk_box().isSelected()
                    || diskView.getSubformat_vmdk_box().isSelected()) {
                List<String> options = new ArrayList<String>();
                if (diskView.getCompat6_vmdk_box().isSelected()) {
                    options.add("compat6");
                }

                if (diskView.getSubformat_vmdk_box().isSelected()) {
                    if (diskView.getSubformat_vmdk_combo().getSelectedItem()
                            .equals("monolithicSparse")) {
                        options.add("subformat=monolithicSparse");
                    } else if (diskView.getSubformat_vmdk_combo()
                            .getSelectedItem().equals("monolithicFlat")) {
                        options.add("subformat=monolithicFlat");
                    } else if (diskView.getSubformat_vmdk_combo()
                            .getSelectedItem().equals("twoGbMaxExtentSparse")) {
                        options.add("subformat=twoGbMaxExtentSparse");
                    } else if (diskView.getSubformat_vmdk_combo()
                            .getSelectedItem().equals("twoGbMaxExtentFlat")) {
                        options.add("subformat=twoGbMaxExtentFlat");
                    } else if (diskView.getSubformat_vmdk_combo()
                            .getSelectedItem().equals("streamOptimized")) {
                        options.add("subformat=streamOptimized");
                    }
                }
                diskModel.setOptions(options);
            } else {
                diskModel.unsetOption();
            }
        } else if (diskView.getDiskExtension().getSelectedItem()
                .equals(".vpc")) {
            if (diskView.getSubformat_vpc_box().isSelected()) {
                if (diskView.getSubformat_vpc_combo().getSelectedItem()
                        .equals("dynamic")) {
                    diskModel.setOption("subformat=dynamic");
                } else if (diskView.getSubformat_vpc_combo().getSelectedItem()
                        .equals("fixed")) {
                    diskModel.setOption("subformat=fixed");
                }
            } else {
                diskModel.unsetOption();
            }
        }
    }

    public void setOptionCreationNewVM(String diskExtension, List<String> options) {
        if (diskExtension.equals(".img")) {
            diskModel.unsetOption();
        } else if (diskExtension.equals(".qcow")) {
            if (options.contains("encryption")) {
                diskModel.setOption("encryption");
            } else {
                diskModel.unsetOption();
            }
        } else if (diskExtension.equals(".qcow2")) {
            if (options.contains("encryption")
                    || options.contains("preallocation=metadata")
                    || options.contains("cluster_size=512k")
                    || options.contains("cluster_size=1M")
                    || options.contains("cluster_size=2M")) {
                diskModel.setOptions(options);
            } else {
                diskModel.unsetOption();
            }
        } else if (diskExtension.equals(".cow")) {
            diskModel.unsetOption();
        } else if (diskExtension.equals(".vdi")) {
            if (options.contains("static")) {
                diskModel.setOption("static");
            } else {
                diskModel.unsetOption();
            }
        } else if (diskExtension.equals(".vmdk")) {
            if (options.contains("compat6")
                    || options.contains("monolithicSparse")
                    || options.contains("monolithicFlat")
                    || options.contains("twoGbMaxExtentSparse")
                    || options.contains("twoGbMaxExtentFlat")
                    || options.contains("streamOptimized")) {
                diskModel.setOptions(options);
            } else {
                diskModel.unsetOption();
            }
        } else if (diskExtension.equals(".vpc")) {
            if (options.contains("subformat=dynamic")
                    || options.contains("subformat=fixed")) {
                diskModel.setOptions(options);
            } else {
                diskModel.unsetOption();
            }
        }
    }

    public void unsetBoxSelections() {
        diskView.getEncryption_box().setSelected(false);
        diskView.getCluster_size_box().setSelected(false);
        diskView.getCluster_size_options().setSelectedIndex(0);
        diskView.getPreallocation_metadata_box().setSelected(false);
        diskView.getStatic_vdi_box().setSelected(false);
        diskView.getCompat6_vmdk_box().setSelected(false);
        diskView.getSubformat_vmdk_box().setSelected(false);
        diskView.getSubformat_vmdk_combo().setSelectedIndex(0);
        diskView.getSubformat_vpc_box().setSelected(false);
    }
}
