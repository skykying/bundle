package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;

public class NetworkBridgeWorkerView extends JFileChooserView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JLabel temp[];

    private JLabel vlanDescription;

    private JComboBox<String> vlan;

    private JLabel nameDescription;

    private JTextField name;

    private JLabel bridgeDeviceDescription;

    private JTextField bridgeDevice;

    private JButton helperChooser;

    private JTextField helper;

    private JButton eraseButton;

    private JButton okButton;

    public NetworkBridgeWorkerView(FileControl myfile, int position) {
        super(null);

        windowContent = new JPanel();

        gridLayout = new GridLayout(6, 2);

        this.setJpanel(windowContent);

        windowContent.setLayout(gridLayout);

        isEnabled = new JCheckBox("Enable this option!");

        windowContent.add(this.isEnabled);

        temp = new JLabel[1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new JLabel();
        }

        windowContent.add(temp[0]);

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

        this.bridgeDeviceDescription = new JLabel("Choose the host bridge device:");

        windowContent.add(this.bridgeDeviceDescription);

        this.bridgeDevice = new JTextField();

        windowContent.add(this.bridgeDevice);

        helperChooser = new JButton("Choose the network helper to configure the TAP interface:");

        windowContent.add(helperChooser);

        helper = new JTextField();

        windowContent.add(helper);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.setContentPane(windowContent);

        this.setTitle("JavaQemu - Network Bridge Choice");

        switch (position) {
            case 1:
                if (myfile.getFilemodel().getFirstNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getFirstNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getFirstNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 2:
                if (myfile.getFilemodel().getSecondNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getSecondNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getSecondNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 3:
                if (myfile.getFilemodel().getThirdNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getThirdNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getThirdNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 4:
                if (myfile.getFilemodel().getFourthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getFourthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getFourthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 5:
                if (myfile.getFilemodel().getFifthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getFifthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getFifthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 6:
                if (myfile.getFilemodel().getSixthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getSixthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getSixthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 7:
                if (myfile.getFilemodel().getSeventhNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getSeventhNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getSeventhNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 8:
                if (myfile.getFilemodel().getEighthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getEighthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getEighthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 9:
                if (myfile.getFilemodel().getNinthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getNinthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getNinthNetworkExtraOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 10:
                if (myfile.getFilemodel().getTenthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getTenthNetworkExtraOption())) {
                        this.buildMe(myfile.getFilemodel().getTenthNetworkExtraOption());
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
        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        helperChooser.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        helperChooser.setActionCommand("helperChooser");
    }

    private void buildMe(String optionString) {
        String options[] = optionString.split(",");
        for (String option : options) {
            if (option.startsWith("vlan=")) {
                this.vlan
                        .setSelectedItem(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("name=")) {
                this.name.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("br=")) {
                this.bridgeDevice.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("helper=")) {
                this.helper
                        .setText(option.substring(option.indexOf("=") + 1));
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
        if (option.contains("bridge")) {
            return true;
        }
        return false;
    }

    public JTextField getBridgeDevice() {
        return bridgeDevice;
    }

    public JTextField getHelper() {
        return helper;
    }
}
