package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;

public class NetworkDnssearchUserWorkerView extends JFrame {

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

    public NetworkDnssearchUserWorkerView(FileControl myfile, int position) {
        super();

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

        this.setContentPane(windowContent);

        this.setTitle("JavaQemu - Network User Choice");

        switch (position) {
            case 1:
                if (myfile.getFilemodel().getFirstNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getFirstNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel()
                                .getFirstNetworkExtraOption());
                    }
                }
                break;
            case 2:
                if (myfile.getFilemodel().getSecondNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel()
                            .getSecondNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel()
                                .getSecondNetworkExtraOption());
                    }
                }
                break;
            case 3:
                if (myfile.getFilemodel().getThirdNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getThirdNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getThirdNetworkExtraOption());
                    }
                }
                break;
            case 4:
                if (myfile.getFilemodel().getFourthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getFourthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getFourthNetworkExtraOption());
                    }
                }
                break;
            case 5:
                if (myfile.getFilemodel().getFifthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getFifthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getFifthNetworkExtraOption());
                    }
                }
                break;
            case 6:
                if (myfile.getFilemodel().getSixthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getSixthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getSixthNetworkExtraOption());
                    }
                }
                break;
            case 7:
                if (myfile.getFilemodel().getSeventhNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getSeventhNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getSeventhNetworkExtraOption());
                    }
                }
                break;
            case 8:
                if (myfile.getFilemodel().getEighthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getEighthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getEighthNetworkExtraOption());
                    }
                }
                break;
            case 9:
                if (myfile.getFilemodel().getNinthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getNinthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getNinthNetworkExtraOption());
                    }
                }
                break;
            case 10:
                if (myfile.getFilemodel().getTenthNetworkExtraOption() != null) {
                    if (this.contains(myfile.getFilemodel().getTenthNetworkExtraOption())) {
                        this.buildTextArea(myfile.getFilemodel().getTenthNetworkExtraOption());
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
}
