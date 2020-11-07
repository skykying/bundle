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

public class NetworkGuestfwdUserWorkerView extends DeviceBaseView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel guestfwdDescription;

    private JTextArea guestfwd;

    private JButton add;

    private JLabel ipAddrServerDescription;

    private JTextField ipAddrServer;

    private JLabel portDescription;

    private JTextField port;

    private JLabel secondOptionDescription;

    private JTextField secondOption;

    private JLabel temp;

    private JTextField option;

    private JButton remove;

    private JButton eraseButton;

    private JButton okButton;

	private int position;

    public NetworkGuestfwdUserWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super(myfile);
        this.position = position;

        windowContent = new JPanel();

        gridLayout = new GridLayout(7, 2);

        windowContent.setLayout(gridLayout);

        guestfwdDescription = new JLabel("'guestfwd' Option(s):");

        windowContent.add(guestfwdDescription);

        guestfwd = new JTextArea("", 3, 30);
        guestfwd.setLineWrap(true);
        guestfwd.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(guestfwd);

        windowContent.add(scrollPane);

        this.add = new JButton("Add the following option:");

        windowContent.add(this.add);

        temp = new JLabel();

        windowContent.add(this.temp);

        ipAddrServerDescription = new JLabel("Choose the IP address server:");

        windowContent.add(this.ipAddrServerDescription);

        ipAddrServer = new JTextField();

        windowContent.add(this.ipAddrServer);

        portDescription = new JLabel("Choose the port:");

        windowContent.add(this.portDescription);

        port = new JTextField();

        windowContent.add(port);

        secondOptionDescription = new JLabel("Set the character device 'dev' or the command 'command' to be executed by 'cmd:command':");

        windowContent.add(secondOptionDescription);

        secondOption = new JTextField();

        windowContent.add(this.secondOption);

        this.remove = new JButton("Remove a option:");

        windowContent.add(this.remove);

        this.option = new JTextField();

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
        this.guestfwd.append(option + "\n");
    }

    public void buildTextArea(String optionString) {
        String[] options = optionString.split(",");
        for (String option : options) {
            if (option.startsWith("guestfwd=")) {
                this.guestfwd.append(option.substring(option.indexOf("=") + 1) + "\n");
            }
        }
    }

    public void removeMe(String option) {
        String options[] = guestfwd.getText().split("\n");
        guestfwd.setText("");
        for (String anOption : options) {
            if (!anOption.equals(option)) {
                guestfwd.append(anOption + "\n");
            }
        }
    }

    private boolean contains(String option) {
        if (option.contains("user")) {
            return true;
        }
        return false;
    }

    public JTextArea getGuestfwd() {
        return guestfwd;
    }

    public JTextField getIpAddrServer() {
        return ipAddrServer;
    }

    public JTextField getPort() {
        return port;
    }

    public JTextField getSecondOption() {
        return secondOption;
    }

    public JTextField getOption() {
        return option;
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
