package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class SpecificBootView extends DeviceViewWithFileChooser {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JButton chooseKernel;

    private JTextField kernel;

    private JButton eraseButton;

    private JButton okButton;


    public SpecificBootView(EmulatorQemuMachineControl emc) {
        super(emc,null);

        windowContent = new JPanel();

        gridLayout = new GridLayout(2, 2);

        windowContent.setLayout(gridLayout);

        chooseKernel = new JButton("Choose the kernel image:");

        windowContent.add(chooseKernel);

        kernel = new JTextField();

        windowContent.add(kernel);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.add(windowContent);
        this.setJpanel(windowContent);

        this.setTitle("JavaQemu - Specific Boot Choice");
    }

    private void rechecks() {
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        chooseKernel.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        chooseKernel.setActionCommand("chooseKernel");
    }

    public JTextField getKernel() {
        return kernel;
    }
    


	@Override
	public void applyView(IemultorStore store) {
        if (eQControl.getMachineModel().getKernelBootOption() != null) {
            if (!eQControl.getMachineModel().getKernelBootOption().isEmpty()) {
                this.kernel.setText(eQControl.getMachineModel().getKernelBootOption());
            }
        }

        this.rechecks();
		
	}
}
