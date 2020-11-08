package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class NUMAView extends DeviceBaseView {

	private static final long serialVersionUID = 1L;
	
	public static final int itemNumbers = 10;
	private static final int emptyJLabelsSetSize = 7;

	private JPanel windowContent;

	private GridLayout gridLayout;
	private JLabel numaGeneralDescription;
	private JLabel temp[];
	private JCheckBox enabledOptions[];
	private JLabel memDescription[];
	private JComboBox<String> cpuLeftNumbers[];
	private JComboBox<String> cpuRightNumbers[];
	private JLabel cpuDescription[];
	private JLabel andDescription[];
	private JSpinner memSize[];
	private SpinnerModel spinnerModel[];
	private JSpinner.NumberEditor editor[];
	private DecimalFormat format[];
	private JButton eraseButton;
	private JButton okButton;

	@SuppressWarnings("unchecked")
	public NUMAView(EmulatorQemuMachineControl emc) {
		super(emc);

		windowContent = new JPanel();

		gridLayout = new GridLayout(24, 4);

		windowContent.setLayout(gridLayout);

		numaGeneralDescription = new JLabel("Choose the NUMA option(s):");

		windowContent.add(numaGeneralDescription);

		temp = new JLabel[23];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = new JLabel();
			if (i < emptyJLabelsSetSize) {
				windowContent.add(temp[i]);
			}
		}

		String[] cpuOptions = { "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19" };

		enabledOptions = new JCheckBox[itemNumbers];
		memDescription = new JLabel[itemNumbers];
		this.spinnerModel = new SpinnerNumberModel[itemNumbers];
		this.memSize = new JSpinner[itemNumbers];
		this.editor = new NumberEditor[itemNumbers];
		this.format = new DecimalFormat[itemNumbers];
		this.cpuDescription = new JLabel[itemNumbers];
		this.cpuLeftNumbers = new JComboBox[itemNumbers];
		this.andDescription = new JLabel[itemNumbers];
		this.cpuRightNumbers = new JComboBox[itemNumbers];

		for (int i = 0; i < itemNumbers; i++) {
			enabledOptions[i] = new JCheckBox("Enable this NUMA option:");
			memDescription[i] = new JLabel("Ram Memory = ");
			memDescription[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.spinnerModel[i] = new SpinnerNumberModel(0.0, // initial value
					0.0, // min
					4096.0, // max
					1); // step
			this.memSize[i] = new JSpinner(spinnerModel[i]);
			editor[i] = (JSpinner.NumberEditor) this.memSize[i].getEditor();
			format[i] = editor[i].getFormat();
			format[i].setMinimumFractionDigits(3);
			editor[i].getTextField().setHorizontalAlignment(SwingConstants.CENTER);
			this.cpuDescription[i] = new JLabel("(One or Two) Cpu(s) = ");
			this.cpuDescription[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.cpuLeftNumbers[i] = new JComboBox<String>(cpuOptions);
			this.andDescription[i] = new JLabel("-");
			this.andDescription[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.cpuRightNumbers[i] = new JComboBox<String>(cpuOptions);

			windowContent.add(enabledOptions[i]);
			windowContent.add(memDescription[i]);
			windowContent.add(memSize[i]);
			windowContent.add(temp[emptyJLabelsSetSize + i]);
			windowContent.add(this.cpuDescription[i]);
			windowContent.add(this.cpuLeftNumbers[i]);
			windowContent.add(this.andDescription[i]);
			windowContent.add(this.cpuRightNumbers[i]);
		}

		for (int i = emptyJLabelsSetSize + itemNumbers; i < temp.length; i++) {
			this.windowContent.add(this.temp[i]);
		}

		okButton = new JButton("OK");

		eraseButton = new JButton("Erase");

		windowContent.add(okButton);

		windowContent.add(eraseButton);

		this.add(windowContent);

		this.setTitle("NUMA");
	}

	private void rechecks() {
		this.repaint();
	}

	public void configureListener(ActionListener listener) {
		eraseButton.addActionListener(listener);
		okButton.addActionListener(listener);
	}

	public void configureStandardMode() {
		eraseButton.setActionCommand("eraseButton");
		okButton.setActionCommand("okButton");
	}

	public JCheckBox[] getEnabledOptions() {
		return enabledOptions.clone();
	}

	public JSpinner.NumberEditor getEditor(int position) {
		return editor[position];
	}

	public JComboBox<String>[] getCpuLeftNumbers() {
		return cpuLeftNumbers.clone();
	}

	public JComboBox<String>[] getCpuRightNumbers() {
		return cpuRightNumbers.clone();
	}

	@Override
	public void applyView(IemultorStore store) {
		HashMap<Integer, String> checkingMemFile = new HashMap<Integer, String>();
		checkingMemFile.put(0, eQControl.getMachineModel().getFirstNumaNodeMem());
		checkingMemFile.put(1, eQControl.getMachineModel().getSecondNumaNodeMem());
		checkingMemFile.put(2, eQControl.getMachineModel().getThirdNumaNodeMem());
		checkingMemFile.put(3, eQControl.getMachineModel().getFourthNumaNodeMem());
		checkingMemFile.put(4, eQControl.getMachineModel().getFifthNumaNodeMem());
		checkingMemFile.put(5, eQControl.getMachineModel().getSixthNumaNodeMem());
		checkingMemFile.put(6, eQControl.getMachineModel().getSeventhNumaNodeMem());
		checkingMemFile.put(7, eQControl.getMachineModel().getEighthNumaNodeMem());
		checkingMemFile.put(8, eQControl.getMachineModel().getNinthNumaNodeMem());
		checkingMemFile.put(9, eQControl.getMachineModel().getTenthNumaNodeMem());

		HashMap<Integer, String> checkingCpusFile = new HashMap<Integer, String>();
		checkingCpusFile.put(0, eQControl.getMachineModel().getFirstNumaNodeCpus());
		checkingCpusFile.put(1, eQControl.getMachineModel().getSecondNumaNodeCpus());
		checkingCpusFile.put(2, eQControl.getMachineModel().getThirdNumaNodeCpus());
		checkingCpusFile.put(3, eQControl.getMachineModel().getFourthNumaNodeCpus());
		checkingCpusFile.put(4, eQControl.getMachineModel().getFifthNumaNodeCpus());
		checkingCpusFile.put(5, eQControl.getMachineModel().getSixthNumaNodeCpus());
		checkingCpusFile.put(6, eQControl.getMachineModel().getSeventhNumaNodeCpus());
		checkingCpusFile.put(7, eQControl.getMachineModel().getEighthNumaNodeCpus());
		checkingCpusFile.put(8, eQControl.getMachineModel().getNinthNumaNodeCpus());
		checkingCpusFile.put(9, eQControl.getMachineModel().getTenthNumaNodeCpus());

		for (int i = 0; i < itemNumbers; i++) {
			if (checkingMemFile.get(i) != null || checkingCpusFile.get(i) != null) {
				this.enabledOptions[i].setSelected(true);
			} else {
				this.enabledOptions[i].setSelected(false);
			}

			if (checkingMemFile.get(i) != null) {
				this.editor[i].getTextField().setText(checkingMemFile.get(i));
			} else {
				this.editor[i].getTextField().setText("0,000");
			}

			if (checkingCpusFile.get(i) != null) {
				String[] cpus = checkingCpusFile.get(i).split("-");
				if (cpus.length >= 1) {
					this.cpuLeftNumbers[i].setSelectedItem(cpus[0]);
				}
				if (cpus.length == 2) {
					this.cpuRightNumbers[i].setSelectedItem(cpus[1]);
				}
			}
		}

		this.rechecks();

	}
}
