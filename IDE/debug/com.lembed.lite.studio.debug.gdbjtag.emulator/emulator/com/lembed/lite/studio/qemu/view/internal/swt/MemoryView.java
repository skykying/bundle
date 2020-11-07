package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class MemoryView extends DeviceViewWithFileChooser {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel memoryPathLabel;

    private JTextField memoryPath;

    private JButton memoryPathChooser;

    private JCheckBox memPrealloc;

    private JButton eraseButton;

    private JButton okButton;

    private JLabel temp[];

    public MemoryView(EmulatorQemuMachineControl myfile) {
        super(myfile,null);

        windowContent = new JPanel();
        this.setJpanel(windowContent);

        gridLayout = new GridLayout(3, 3);
        windowContent.setLayout(gridLayout);

        memoryPathLabel = new JLabel("Choose the memory path:");
        windowContent.add(memoryPathLabel);

        memoryPath = new JTextField();
        windowContent.add(memoryPath);

        memoryPathChooser = new JButton("Choose a directory as memory path!");
        windowContent.add(memoryPathChooser);

        memPrealloc = new JCheckBox("-mem-prealloc option!");
        windowContent.add(memPrealloc);

        temp = new JLabel[3];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = new JLabel();
            windowContent.add(temp[i]);
        }

        okButton = new JButton("OK");
        eraseButton = new JButton("Erase");
        windowContent.add(okButton);
        windowContent.add(eraseButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Other Memory Options");

       
    }

    private void rechecks() {
//        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        memoryPathChooser.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        memoryPathChooser.setActionCommand("memoryPathChooser");
    }

    public JTextField getMemoryPath() {
        return memoryPath;
    }

    public JCheckBox getMemPrealloc() {
        return memPrealloc;
    }

	@Override
	public void applyView(IemultorStore store) {
		 if (eQControl.getMachineModel().getMemPathOption() != null) {
	            if (!eQControl.getMachineModel().getMemPathOption().isEmpty()) {
	                this.memoryPath.setText(eQControl.getMachineModel().getMemPathOption());
	            }
	        }

	        if (eQControl.getMachineModel().getMemPreallocOption() != null) {
	            if (!eQControl.getMachineModel().getMemPreallocOption().isEmpty()) {
	                if (eQControl.getMachineModel().getMemPreallocOption().equals("true")) {
	                    this.memPrealloc.setSelected(true);
	                }
	            }
	        }

	        this.rechecks();
		
	}
}
