package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkVdeWorkerView extends DeviceViewWithFileChooser {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JLabel temp;

    private JLabel vlanDescription;

    private JComboBox<String> vlan;

    private JLabel nameDescription;

    private JTextField name;

    private JButton socketpathChooser;

    private JTextField socketpath;

    private JLabel portDescription;

    private JTextField port;

    private JLabel groupnameDescription;

    private JTextField groupname;

    private JLabel octalmodeDescription;

    private JTextField octalmode;

    private JButton eraseButton;

    private JButton okButton;

	private int position;

    public NetworkVdeWorkerView(FileControl myfile, int position) {
        super(myfile,null);
        this.position = position;

        windowContent = new JPanel();

        gridLayout = new GridLayout(8, 2);

        this.setJpanel(windowContent);

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

        socketpathChooser = new JButton("Choose the socket path:");

        windowContent.add(this.socketpathChooser);

        socketpath = new JTextField();

        windowContent.add(socketpath);

        portDescription = new JLabel("Choose the port:");

        windowContent.add(portDescription);

        port = new JTextField();

        windowContent.add(port);

        groupnameDescription = new JLabel("Choose the group name:");

        windowContent.add(groupnameDescription);

        groupname = new JTextField();

        windowContent.add(groupname);

        octalmodeDescription = new JLabel("Choose the octal mode:");

        windowContent.add(octalmodeDescription);

        octalmode = new JTextField();

        windowContent.add(octalmode);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Network VDE Choice");

       
    }

    private void rechecks() {
//        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        socketpathChooser.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        socketpathChooser.setActionCommand("socketpathChooser");
    }

    private void buildMe(String optionString) {
        String options[] = optionString.split(",");

        for (String option : options) {
            if (option.startsWith("vlan=")) {
                this.vlan
                        .setSelectedItem(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("name=")) {
                this.name.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("sock=")) {
                this.socketpath.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("port=")) {
                this.port.setText(option.substring(
                        option.indexOf("=") + 1));
            } else if (option.startsWith("group=")) {
                this.groupname
                        .setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("mode=")) {
                this.octalmode.setText(option.substring(option.indexOf("=") + 1));
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
        if (option.contains("vde")) {
            return true;
        }
        return false;
    }

    public JTextField getSocketpath() {
        return socketpath;
    }

    public JTextField getPort() {
        return port;
    }

    public JTextField getGroupname() {
        return groupname;
    }

    public JTextField getOctalmode() {
        return octalmode;
    }

	@Override
	public void doSave(IemultorStore store) {
		 switch (position) {
         case 1:
             if (fileControl.getFilemodel().getFirstNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getFirstNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getFirstNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 2:
             if (fileControl.getFilemodel().getSecondNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getSecondNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getSecondNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 3:
             if (fileControl.getFilemodel().getThirdNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getThirdNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getThirdNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 4:
             if (fileControl.getFilemodel().getFourthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getFourthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getFourthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 5:
             if (fileControl.getFilemodel().getFifthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getFifthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getFifthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 6:
             if (fileControl.getFilemodel().getSixthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getSixthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getSixthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 7:
             if (fileControl.getFilemodel().getSeventhNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getSeventhNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getSeventhNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 8:
             if (fileControl.getFilemodel().getEighthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getEighthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getEighthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 9:
             if (fileControl.getFilemodel().getNinthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getNinthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getNinthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         case 10:
             if (fileControl.getFilemodel().getTenthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getTenthNetworkExtraOption())) {
                     this.buildMe(fileControl.getFilemodel()
                             .getTenthNetworkExtraOption());
                     this.isEnabled.setSelected(true);
                 }
             }
             break;
         default:
             break;
     }

     this.rechecks();
		
	}
}
