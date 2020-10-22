package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;

public class CPUView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jpanel;

    private JButton okButton, eraseButton;
    private GridLayout gridLayout;
    private JLabel cpuLabel, temp1;
    private JComboBox<String> cpuModels;
    private JButton showsFlagsButton;
    private Boolean loaded;

    public CPUView(FileControl fileControl) {
        jpanel = new JPanel();

        gridLayout = new GridLayout(3, 2);

        setTitle("JavaQemu - CPU Options");
        setContentPane(jpanel);

        jpanel.setLayout(gridLayout);

        okButton = new JButton("OK");
        eraseButton = new JButton("Erase");

        String[] models = {"", "Qemu 64 bits: qemu64", 
            "AMD Phenom: phenom",
            "Intel Core 2 Duo: core2duo", 
            "KVM 64 bits: kvm64",
            "Qemu 32 bits: qemu32", 
            "KVM 32 bits: kvm32",
            "Intel Core Duo: coreduo", 
            "486: 486",
            "Intel Pentium: pentium", 
            "Intel Pentium 2: pentium2",
            "Intel Pentium 3: pentium3", 
            "AMD Athlon: athlon",
            "Intel n270: n270", 
            "Intel Conroe: Conroe",
            "Intel Penryn: Penryn", 
            "Intel Nehalem: Nehalem",
            "Intel Westmere: Westmere", 
            "Intel Sandy Bridge: SandyBridge",
            "Intel Haswell: Haswell", 
            "AMD Opteron: Opteron_G1",
            "AMD Opteron: Opteron_G2", 
            "AMD Opteron: Opteron_G3",
            "AMD Opteron: Opteron_G4", 
            "AMD Opteron: Opteron_G5",
            "Host: host"};

        cpuModels = new JComboBox<String>(models);
        cpuModels.setSelectedIndex(0);

        showsFlagsButton = new JButton("Show the cpuid flags:");

        cpuLabel = new JLabel("CPU model:");
        jpanel.add(cpuLabel);
        jpanel.add(cpuModels);

        temp1 = new JLabel("");

        jpanel.add(showsFlagsButton);
        jpanel.add(temp1);
        jpanel.add(okButton);
        jpanel.add(eraseButton);

        loaded = false;

        if (fileControl.getFilemodel().getCpuModel() != null) {
            if (fileControl.getFilemodel().getCpuModel().isEmpty()) {
                cpuModels.setSelectedItem(fileControl.getFilemodel()
                        .getCpuModel());
            } else if (fileControl.getFilemodel().getCpuModel().equals("qemu64")) {
                cpuModels.setSelectedItem("Qemu 64 bits: qemu64");
            } else if (fileControl.getFilemodel().getCpuModel().equals("phenom")) {
                cpuModels.setSelectedItem("AMD Phenom: phenom");
            } else if (fileControl.getFilemodel().getCpuModel().equals("core2duo")) {
                cpuModels.setSelectedItem("Intel Core 2 Duo: core2duo");
            } else if (fileControl.getFilemodel().getCpuModel().equals("kvm64")) {
                cpuModels.setSelectedItem("KVM 64 bits: kvm64");
            } else if (fileControl.getFilemodel().getCpuModel().equals("qemu32")) {
                cpuModels.setSelectedItem("Qemu 32 bits: qemu32");
            } else if (fileControl.getFilemodel().getCpuModel().equals("kvm32")) {
                cpuModels.setSelectedItem("KVM 32 bits: kvm32");
            } else if (fileControl.getFilemodel().getCpuModel().equals("coreduo")) {
                cpuModels.setSelectedItem("Intel Core Duo: coreduo");
            } else if (fileControl.getFilemodel().getCpuModel().equals("486")) {
                cpuModels.setSelectedItem("486: 486");
            } else if (fileControl.getFilemodel().getCpuModel().equals("pentium")) {
                cpuModels.setSelectedItem("Intel Pentium: pentium");
            } else if (fileControl.getFilemodel().getCpuModel().equals("pentium2")) {
                cpuModels.setSelectedItem("Intel Pentium 2: pentium2");
            } else if (fileControl.getFilemodel().getCpuModel().equals("pentium3")) {
                cpuModels.setSelectedItem("Intel Pentium 3: pentium3");
            } else if (fileControl.getFilemodel().getCpuModel().equals("athlon")) {
                cpuModels.setSelectedItem("AMD Athlon: athlon");
            } else if (fileControl.getFilemodel().getCpuModel().equals("n270")) {
                cpuModels.setSelectedItem("Intel n270: n270");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Conroe")) {
                cpuModels.setSelectedItem("Intel Conroe: Conroe");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Penryn")) {
                cpuModels.setSelectedItem("Intel Penryn: Penryn");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Nehalem")) {
                cpuModels.setSelectedItem("Intel Nehalem: Nehalem");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Westmere")) {
                cpuModels.setSelectedItem("Intel Westmere: Westmere");
            } else if (fileControl.getFilemodel().getCpuModel().equals("SandyBridge")) {
                cpuModels
                        .setSelectedItem("Intel Sandy Bridge: SandyBridge");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Haswell")) {
                cpuModels.setSelectedItem("Intel Haswell: Haswell");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Opteron_G1")) {
                cpuModels.setSelectedItem("AMD Opteron: Opteron_G1");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Opteron_G2")) {
                cpuModels.setSelectedItem("AMD Opteron: Opteron_G2");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Opteron_G3")) {
                cpuModels.setSelectedItem("AMD Opteron: Opteron_G3");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Opteron_G4")) {
                cpuModels.setSelectedItem("AMD Opteron: Opteron_G4");
            } else if (fileControl.getFilemodel().getCpuModel().equals("Opteron_G5")) {
                cpuModels.setSelectedItem("AMD Opteron: Opteron_G5");
            } else if (fileControl.getFilemodel().getCpuModel().equals("host")) {
                cpuModels.setSelectedItem("Host: host");
            }
            loaded = true;
        }

        rechecks();
    }

    private void rechecks() {
        pack();
        repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        showsFlagsButton.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        showsFlagsButton.setActionCommand("showsFlagButton");
    }

    public JComboBox<String> getCpuModels() {
        return cpuModels;
    }

    public Boolean getLoaded() {
        return loaded;
    }
}
