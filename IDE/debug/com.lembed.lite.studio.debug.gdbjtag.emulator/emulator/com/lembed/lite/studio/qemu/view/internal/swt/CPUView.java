package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class CPUView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel jpanel;
	private String title;

	private JButton okButton, eraseButton;
	private GridLayout gridLayout;
	private JLabel cpuLabel, temp1;
	private JComboBox<String> cpuModels;
	private JButton showsFlagsButton;
	private Boolean loaded;

	public CPUView(EmulatorQemuMachineControl myfile) {
		super(myfile);

		jpanel = new JPanel();

		gridLayout = new GridLayout(4, 1);

		setTitle("CPU");

		jpanel.setLayout(gridLayout);

		okButton = new JButton("OK");
		eraseButton = new JButton("Erase");

		String[] models = { "", "Qemu 64 bits: qemu64", "AMD Phenom: phenom", "Intel Core 2 Duo: core2duo",
				"KVM 64 bits: kvm64", "Qemu 32 bits: qemu32", "KVM 32 bits: kvm32", "Intel Core Duo: coreduo",
				"486: 486", "Intel Pentium: pentium", "Intel Pentium 2: pentium2", "Intel Pentium 3: pentium3",
				"AMD Athlon: athlon", "Intel n270: n270", "Intel Conroe: Conroe", "Intel Penryn: Penryn",
				"Intel Nehalem: Nehalem", "Intel Westmere: Westmere", "Intel Sandy Bridge: SandyBridge",
				"Intel Haswell: Haswell", "AMD Opteron: Opteron_G1", "AMD Opteron: Opteron_G2",
				"AMD Opteron: Opteron_G3", "AMD Opteron: Opteron_G4", "AMD Opteron: Opteron_G5", "Host: host" };

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
		add(jpanel);

	}

	@Override
	public void applyView(IemultorStore store) {
		if (eQControl.getMachineModel().getCpuModel() != null) {
			if (eQControl.getMachineModel().getCpuModel().isEmpty()) {
				cpuModels.setSelectedItem(eQControl.getMachineModel().getCpuModel());
			} else if (eQControl.getMachineModel().getCpuModel().equals("qemu64")) {
				cpuModels.setSelectedItem("Qemu 64 bits: qemu64");
			} else if (eQControl.getMachineModel().getCpuModel().equals("phenom")) {
				cpuModels.setSelectedItem("AMD Phenom: phenom");
			} else if (eQControl.getMachineModel().getCpuModel().equals("core2duo")) {
				cpuModels.setSelectedItem("Intel Core 2 Duo: core2duo");
			} else if (eQControl.getMachineModel().getCpuModel().equals("kvm64")) {
				cpuModels.setSelectedItem("KVM 64 bits: kvm64");
			} else if (eQControl.getMachineModel().getCpuModel().equals("qemu32")) {
				cpuModels.setSelectedItem("Qemu 32 bits: qemu32");
			} else if (eQControl.getMachineModel().getCpuModel().equals("kvm32")) {
				cpuModels.setSelectedItem("KVM 32 bits: kvm32");
			} else if (eQControl.getMachineModel().getCpuModel().equals("coreduo")) {
				cpuModels.setSelectedItem("Intel Core Duo: coreduo");
			} else if (eQControl.getMachineModel().getCpuModel().equals("486")) {
				cpuModels.setSelectedItem("486: 486");
			} else if (eQControl.getMachineModel().getCpuModel().equals("pentium")) {
				cpuModels.setSelectedItem("Intel Pentium: pentium");
			} else if (eQControl.getMachineModel().getCpuModel().equals("pentium2")) {
				cpuModels.setSelectedItem("Intel Pentium 2: pentium2");
			} else if (eQControl.getMachineModel().getCpuModel().equals("pentium3")) {
				cpuModels.setSelectedItem("Intel Pentium 3: pentium3");
			} else if (eQControl.getMachineModel().getCpuModel().equals("athlon")) {
				cpuModels.setSelectedItem("AMD Athlon: athlon");
			} else if (eQControl.getMachineModel().getCpuModel().equals("n270")) {
				cpuModels.setSelectedItem("Intel n270: n270");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Conroe")) {
				cpuModels.setSelectedItem("Intel Conroe: Conroe");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Penryn")) {
				cpuModels.setSelectedItem("Intel Penryn: Penryn");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Nehalem")) {
				cpuModels.setSelectedItem("Intel Nehalem: Nehalem");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Westmere")) {
				cpuModels.setSelectedItem("Intel Westmere: Westmere");
			} else if (eQControl.getMachineModel().getCpuModel().equals("SandyBridge")) {
				cpuModels.setSelectedItem("Intel Sandy Bridge: SandyBridge");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Haswell")) {
				cpuModels.setSelectedItem("Intel Haswell: Haswell");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Opteron_G1")) {
				cpuModels.setSelectedItem("AMD Opteron: Opteron_G1");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Opteron_G2")) {
				cpuModels.setSelectedItem("AMD Opteron: Opteron_G2");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Opteron_G3")) {
				cpuModels.setSelectedItem("AMD Opteron: Opteron_G3");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Opteron_G4")) {
				cpuModels.setSelectedItem("AMD Opteron: Opteron_G4");
			} else if (eQControl.getMachineModel().getCpuModel().equals("Opteron_G5")) {
				cpuModels.setSelectedItem("AMD Opteron: Opteron_G5");
			} else if (eQControl.getMachineModel().getCpuModel().equals("host")) {
				cpuModels.setSelectedItem("Host: host");
			}
			loaded = true;
		}

	}

	@Override
	public void setTitle(String string) {
		title = string;
	}

	@Override
	public String getTitle() {
		return title;
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
