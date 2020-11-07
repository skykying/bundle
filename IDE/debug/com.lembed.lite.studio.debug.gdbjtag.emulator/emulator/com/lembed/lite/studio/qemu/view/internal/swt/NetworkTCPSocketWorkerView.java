package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;

public class NetworkTCPSocketWorkerView extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JLabel temp;

    private JLabel vlanDescription;

    private JComboBox<String> vlan;

    private JLabel nameDescription;

    private JTextField name;

    private JLabel fdDescription;

    private JTextField fd;

    private JLabel hostListenDescription;

    private JTextField hostListen;

    private JLabel portListenDescription;

    private JTextField portListen;

    private JLabel hostConnectDescription;

    private JTextField hostConnect;

    private JLabel portConnectDescription;

    private JTextField portConnect;

    private JButton eraseButton;

    private JButton okButton;

    public NetworkTCPSocketWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super();

        windowContent = new JPanel();

        gridLayout = new GridLayout(9, 2);

        windowContent.setLayout(gridLayout);

        isEnabled = new JCheckBox("Enable this option!");

        windowContent.add(this.isEnabled);

        temp = new JLabel();

        windowContent.add(temp);

        vlanDescription = new JLabel("Choose the VLAN:");

        windowContent.add(vlanDescription);

        String[] numberOptions = {"", "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
            "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58",
            "59", "60", "61", "62", "63", "64", "65", "66", "67", "68",
            "69", "70", "71", "72", "73", "74", "75", "76", "77", "78",
            "79", "80", "81", "82", "83", "84", "85", "86", "87", "88",
            "89", "90", "91", "92", "93", "94", "95", "96", "97", "98",
            "99", "100"};

        this.vlan = new JComboBox<String>(numberOptions);

        windowContent.add(vlan);

        nameDescription = new JLabel("Choose the name or id:");

        windowContent.add(this.nameDescription);

        name = new JTextField();

        windowContent.add(this.name);

        fdDescription = new JLabel("Choose the fd:");

        windowContent.add(fdDescription);

        fd = new JTextField();
        windowContent.add(fd);

        hostListenDescription = new JLabel("Listen incoming connection from another QEMU, on host:");
        windowContent.add(hostListenDescription);

        hostListen = new JTextField();
        windowContent.add(hostListen);

        portListenDescription = new JLabel("Listen incoming connection from another QEMU, on port:");
        windowContent.add(portListenDescription);

        portListen = new JTextField();
        windowContent.add(portListen);

        hostConnectDescription = new JLabel("<html>Connect with another QEMU that is listening<br> incoming connection, on host:</html>");
        windowContent.add(hostConnectDescription);

        hostConnect = new JTextField();
        windowContent.add(hostConnect);

        portConnectDescription = new JLabel("<html>Connect with another QEMU that is listening<br> incoming connection, on port:</html>");
        windowContent.add(portConnectDescription);

        portConnect = new JTextField();
        windowContent.add(portConnect);

        okButton = new JButton("OK");
        eraseButton = new JButton("Erase");
        windowContent.add(okButton);
        windowContent.add(eraseButton);

//        this.setContentPane(windowContent);
//
//        this.setTitle("JavaQemu - Network TCP Socket Choice");

        switch (position) {
            case 1:
                if (myfile.getMachineModel().getFirstNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getFirstNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getFirstNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 2:
                if (myfile.getMachineModel().getSecondNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getSecondNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel()
                                .getSecondNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 3:
                if (myfile.getMachineModel().getThirdNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getThirdNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getThirdNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 4:
                if (myfile.getMachineModel().getFourthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getFourthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getFourthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 5:
                if (myfile.getMachineModel().getFifthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getFifthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getFifthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 6:
                if (myfile.getMachineModel().getSixthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getSixthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getSixthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 7:
                if (myfile.getMachineModel().getSeventhNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getSeventhNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getSeventhNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 8:
                if (myfile.getMachineModel().getEighthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getEighthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getEighthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 9:
                if (myfile.getMachineModel().getNinthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getNinthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getNinthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 10:
                if (myfile.getMachineModel().getTenthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getTenthNetworkExtraOption())) {
                        this.buildMe(myfile.getMachineModel().getTenthNetworkExtraOption());
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
            if (option.startsWith("vlan=")) {
                this.vlan
                        .setSelectedItem(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("name=")) {
                this.name.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("fd=")) {
                this.fd.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("listen=")) {
                this.hostListen.setText(option.substring(option.indexOf("=") + 1, option.indexOf(":")));
                this.portListen.setText(option.substring(option.indexOf(":") + 1));
            } else if (option.startsWith("connect=")) {
                this.hostConnect.setText(option.substring(option.indexOf("=") + 1, option.indexOf(":")));
                this.portConnect.setText(option.substring(option.indexOf(":") + 1));
            }
        }
    }

    public JCheckBox getIsEnabled() {
        return isEnabled;
    }

    public JComboBox<String> getVlan() {
        return vlan;
    }

    public JTextField getNameContents() {
        return name;
    }

    private boolean contains(String option) {
        if (option.contains("socket")) {
            return true;
        }
        return false;
    }

    public JTextField getFd() {
        return fd;
    }

    public JTextField getHostListen() {
        return hostListen;
    }

    public JTextField getPortListen() {
        return portListen;
    }

    public JTextField getHostConnect() {
        return hostConnect;
    }

    public JTextField getPortConnect() {
        return portConnect;
    }
}
