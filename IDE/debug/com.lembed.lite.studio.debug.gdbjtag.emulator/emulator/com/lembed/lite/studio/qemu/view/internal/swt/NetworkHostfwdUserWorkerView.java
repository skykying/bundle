package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;

public class NetworkHostfwdUserWorkerView extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel hostfwdDescription;

    private JTextArea hostfwd;

    private JButton add;

    private JLabel connectionTypeDescription;

    private JComboBox<String> connectionType;

    private JLabel hostaddrDescription;

    private JTextField hostaddr;

    private JLabel hostportDescription;

    private JTextField hostport;

    private JLabel guestaddrDescription;

    private JTextField guestaddr;

    private JLabel guestportDescription;

    private JTextField guestport;

    private JLabel temp;

    private JTextField option;

    private JButton remove;

    private JButton eraseButton;

    private JButton okButton;

    public NetworkHostfwdUserWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super();

        windowContent = new JPanel();

        gridLayout = new GridLayout(9, 2);

        windowContent.setLayout(gridLayout);

        hostfwdDescription = new JLabel("'hostfwd' Option(s):");

        windowContent.add(hostfwdDescription);

        hostfwd = new JTextArea("", 3, 30);
        hostfwd.setLineWrap(true);
        hostfwd.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(this.hostfwd);

        windowContent.add(scrollPane);

        this.add = new JButton("Add the following option:");

        windowContent.add(this.add);

        this.temp = new JLabel();

        windowContent.add(this.temp);

        connectionTypeDescription = new JLabel("Choose the connection type:");

        windowContent.add(this.connectionTypeDescription);

        String[] connections = {"TCP", "UDP"};

        connectionType = new JComboBox<String>(connections);

        windowContent.add(this.connectionType);

        hostaddrDescription = new JLabel("Choose the host address:");

        windowContent.add(this.hostaddrDescription);

        hostaddr = new JTextField();

        windowContent.add(this.hostaddr);

        hostportDescription = new JLabel("Choose the host port:");

        windowContent.add(this.hostportDescription);

        hostport = new JTextField();

        windowContent.add(hostport);

        guestaddrDescription = new JLabel("Choose the guest address:");

        windowContent.add(guestaddrDescription);

        guestaddr = new JTextField();

        windowContent.add(this.guestaddr);

        guestportDescription = new JLabel("Choose the guest port:");

        windowContent.add(this.guestportDescription);

        guestport = new JTextField();

        windowContent.add(guestport);

        this.remove = new JButton("Remove a option:");

        windowContent.add(this.remove);

        this.option = new JTextField();

        windowContent.add(this.option);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

//        this.setContentPane(windowContent);
//
//        this.setTitle("JavaQemu - Network User Choice");

        switch (position) {
            case 1:
                if (myfile.getMachineModel().getFirstNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getFirstNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel()
                                .getFirstNetworkExtraOption());
                    }
                }
                break;
            case 2:
                if (myfile.getMachineModel().getSecondNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel()
                            .getSecondNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel()
                                .getSecondNetworkExtraOption());
                    }
                }
                break;
            case 3:
                if (myfile.getMachineModel().getThirdNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getThirdNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getThirdNetworkExtraOption());
                    }
                }
                break;
            case 4:
                if (myfile.getMachineModel().getFourthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getFourthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getFourthNetworkExtraOption());
                    }
                }
                break;
            case 5:
                if (myfile.getMachineModel().getFifthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getFifthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getFifthNetworkExtraOption());
                    }
                }
                break;
            case 6:
                if (myfile.getMachineModel().getSixthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getSixthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getSixthNetworkExtraOption());
                    }
                }
                break;
            case 7:
                if (myfile.getMachineModel().getSeventhNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getSeventhNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getSeventhNetworkExtraOption());
                    }
                }
                break;
            case 8:
                if (myfile.getMachineModel().getEighthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getEighthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getEighthNetworkExtraOption());
                    }
                }
                break;
            case 9:
                if (myfile.getMachineModel().getNinthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getNinthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getNinthNetworkExtraOption());
                    }
                }
                break;
            case 10:
                if (myfile.getMachineModel().getTenthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getMachineModel().getTenthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getMachineModel().getTenthNetworkExtraOption());
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
        this.hostfwd.append(option + "\n");
    }

    public void buildTextArea(String optionString) {
        String[] options = optionString.split(",");
        for (String option : options) {
            if (option.startsWith("hostfwd=")) {
                this.hostfwd.append(option.substring(option.indexOf("=") + 1) + "\n");
            }
        }
    }

    public void removeMe(String option) {
        String options[] = hostfwd.getText().split("\n");
        hostfwd.setText("");
        for (String anOption : options) {
            if (!anOption.equals(option)) {
                hostfwd.append(anOption + "\n");
            }
        }
    }

    private boolean contains(String option) {
        if (option.contains("user")) {
            return true;
        }
        return false;
    }

    public JTextArea getHostfwd() {
        return hostfwd;
    }

    public JComboBox<String> getConnectionType() {
        return connectionType;
    }

    public JTextField getHostaddr() {
        return hostaddr;
    }

    public JTextField getHostport() {
        return hostport;
    }

    public JTextField getGuestaddr() {
        return guestaddr;
    }

    public JTextField getGuestport() {
        return guestport;
    }

    public JTextField getOption() {
        return option;
    }
}
