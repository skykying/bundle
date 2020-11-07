package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkDnssearchUserWorkerView extends DeviceBaseView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel dnssearchDescription;

    private JTextArea dnssearch;

    private JButton add;

    private JTextField text;

    private JTextField option;

    private JButton remove;

    private JButton eraseButton;

    private JButton okButton;

	private int position;

    public NetworkDnssearchUserWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super(myfile);

        this.position = position;
        
        windowContent = new JPanel();

        gridLayout = new GridLayout(4, 2);

        windowContent.setLayout(gridLayout);

        dnssearchDescription = new JLabel("'dnssearch' Option(s):");

        windowContent.add(dnssearchDescription);

        dnssearch = new JTextArea("", 3, 30);
        dnssearch.setLineWrap(true);
        dnssearch.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(this.dnssearch);

        windowContent.add(scrollPane);

        this.option = new JTextField();

        this.add = new JButton("Add the option:");

        windowContent.add(this.add);

        this.text = new JTextField();

        windowContent.add(this.text);

        this.remove = new JButton("Remove the option:");

        windowContent.add(this.remove);

        windowContent.add(this.option);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Network User Choice");

    }

    private void rechecks() {
//        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        add.addActionListener(listener);
        remove.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        add.setActionCommand("add");
        remove.setActionCommand("remove");
    }

    public void buildMe(String option) {
        this.dnssearch.append(option + "\n");
    }

    public void buildTextArea(String optionString) {
        String[] options = optionString.split(",");
        for (String option : options) {
            if (option.startsWith("dnssearch=")) {
                this.dnssearch.append(option.substring(option.indexOf("=") + 1) + "\n");
            }
        }
    }

    public void removeMe(String option) {
        String options[] = dnssearch.getText().split("\n");
        dnssearch.setText("");
        for (String anOption : options) {
            if (!anOption.equals(option)) {
                dnssearch.append(anOption + "\n");
            }
        }
    }

    private boolean contains(String option) {
        if (option.contains("user")) {
            return true;
        }
        return false;
    }

    public JTextArea getDnssearch() {
        return dnssearch;
    }

    public JTextField getOption() {
        return option;
    }

    public JTextField getText() {
        return text;
    }

	@Override
	public void doSave(IemultorStore store) {
		 switch (position) {
         case 1:
             if (fileControl.getMachineModel().getFirstNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel()
                         .getFirstNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel()
                             .getFirstNetworkExtraOption());
                 }
             }
             break;
         case 2:
             if (fileControl.getMachineModel().getSecondNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel()
                         .getSecondNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel()
                             .getSecondNetworkExtraOption());
                 }
             }
             break;
         case 3:
             if (fileControl.getMachineModel().getThirdNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getThirdNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getThirdNetworkExtraOption());
                 }
             }
             break;
         case 4:
             if (fileControl.getMachineModel().getFourthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getFourthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getFourthNetworkExtraOption());
                 }
             }
             break;
         case 5:
             if (fileControl.getMachineModel().getFifthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getFifthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getFifthNetworkExtraOption());
                 }
             }
             break;
         case 6:
             if (fileControl.getMachineModel().getSixthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getSixthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getSixthNetworkExtraOption());
                 }
             }
             break;
         case 7:
             if (fileControl.getMachineModel().getSeventhNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getSeventhNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getSeventhNetworkExtraOption());
                 }
             }
             break;
         case 8:
             if (fileControl.getMachineModel().getEighthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getEighthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getEighthNetworkExtraOption());
                 }
             }
             break;
         case 9:
             if (fileControl.getMachineModel().getNinthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getNinthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getNinthNetworkExtraOption());
                 }
             }
             break;
         case 10:
             if (fileControl.getMachineModel().getTenthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getMachineModel().getTenthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getMachineModel().getTenthNetworkExtraOption());
                 }
             }
             break;
         default:
             break;
     }

     this.rechecks();
		
	}
}
