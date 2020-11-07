package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;

public class NetworkHubportWorkerView extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JLabel temp;

    private JLabel idDescription;

    private JTextField id;

    private JLabel hubidDescription;

    private JTextField hubid;

    private JButton eraseButton;

    private JButton okButton;

    public NetworkHubportWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super();

        windowContent = new JPanel();

        gridLayout = new GridLayout(4, 2);

        windowContent.setLayout(gridLayout);

        isEnabled = new JCheckBox("Enable this option!");

        windowContent.add(this.isEnabled);

        temp = new JLabel();

        windowContent.add(temp);

        idDescription = new JLabel("Choose the id:");

        windowContent.add(idDescription);

        id = new JTextField();

        windowContent.add(id);

        hubidDescription = new JLabel("Choose the hubid:");

        windowContent.add(hubidDescription);

        hubid = new JTextField();

        windowContent.add(hubid);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

//        this.setContentPane(windowContent);
//
//        this.setTitle("JavaQemu - Network Hub Port Choice");

        switch (position) {
            case 1:
                if (myfile.getMachineModel().getFirstNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getFirstNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getFirstNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 2:
                if (myfile.getMachineModel().getSecondNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getSecondNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getSecondNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 3:
                if (myfile.getMachineModel().getThirdNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getThirdNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getThirdNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 4:
                if (myfile.getMachineModel().getFourthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getFourthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getFourthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 5:
                if (myfile.getMachineModel().getFifthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getFifthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getFifthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 6:
                if (myfile.getMachineModel().getSixthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getSixthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getSixthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 7:
                if (myfile.getMachineModel().getSeventhNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getSeventhNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getSeventhNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 8:
                if (myfile.getMachineModel().getEighthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getEighthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getEighthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 9:
                if (myfile.getMachineModel().getNinthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getNinthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getNinthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 10:
                if (myfile.getMachineModel().getTenthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getTenthNetworkNetdevOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getTenthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            default:
                break;
        }

        this.rechecks();
    }

    private void rechecks() {
//        this.pack();
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

    private void buildMe(String optionString) {
        String options[] = optionString.split(",");

        for (String option : options) {
            if (option.startsWith("id=")) {
                this.id
                        .setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("hubid=")) {
                this.hubid.setText(option.substring(option.indexOf("=") + 1));
            }
        }

    }

    public JCheckBox getIsEnabled() {
        return isEnabled;
    }

    private boolean contains(String option) {
        if (option.contains("hubport")) {
            return true;
        }
        return false;
    }

    public JTextField getId() {
        return id;
    }

    public JTextField getHubid() {
        return hubid;
    }
}
