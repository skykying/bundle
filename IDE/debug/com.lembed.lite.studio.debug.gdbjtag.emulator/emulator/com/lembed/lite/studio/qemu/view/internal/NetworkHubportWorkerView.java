package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;

public class NetworkHubportWorkerView extends JFrame {

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

    public NetworkHubportWorkerView(FileControl myfile, int position) {
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

        this.setContentPane(windowContent);

        this.setTitle("JavaQemu - Network Hub Port Choice");

        switch (position) {
            case 1:
                if (myfile.getFilemodel().getFirstNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getFirstNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getFirstNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 2:
                if (myfile.getFilemodel().getSecondNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getSecondNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getSecondNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 3:
                if (myfile.getFilemodel().getThirdNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getThirdNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getThirdNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 4:
                if (myfile.getFilemodel().getFourthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getFourthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getFourthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 5:
                if (myfile.getFilemodel().getFifthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getFifthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getFifthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 6:
                if (myfile.getFilemodel().getSixthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getSixthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getSixthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 7:
                if (myfile.getFilemodel().getSeventhNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getSeventhNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getSeventhNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 8:
                if (myfile.getFilemodel().getEighthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getEighthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getEighthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 9:
                if (myfile.getFilemodel().getNinthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getNinthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
                                .getNinthNetworkNetdevOption());
                        this.isEnabled.setSelected(true);
                    }
                }
                break;
            case 10:
                if (myfile.getFilemodel().getTenthNetworkNetdevOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getTenthNetworkNetdevOption())) {
                        this.buildMe(myfile.getFilemodel()
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
        this.pack();
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
