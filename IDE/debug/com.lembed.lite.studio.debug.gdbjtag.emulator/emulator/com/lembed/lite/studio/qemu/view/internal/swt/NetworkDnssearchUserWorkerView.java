package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;
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

    public NetworkDnssearchUserWorkerView(FileControl myfile, int position) {
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
             if (fileControl.getFilemodel().getFirstNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getFirstNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel()
                             .getFirstNetworkExtraOption());
                 }
             }
             break;
         case 2:
             if (fileControl.getFilemodel().getSecondNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel()
                         .getSecondNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel()
                             .getSecondNetworkExtraOption());
                 }
             }
             break;
         case 3:
             if (fileControl.getFilemodel().getThirdNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getThirdNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getThirdNetworkExtraOption());
                 }
             }
             break;
         case 4:
             if (fileControl.getFilemodel().getFourthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getFourthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getFourthNetworkExtraOption());
                 }
             }
             break;
         case 5:
             if (fileControl.getFilemodel().getFifthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getFifthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getFifthNetworkExtraOption());
                 }
             }
             break;
         case 6:
             if (fileControl.getFilemodel().getSixthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getSixthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getSixthNetworkExtraOption());
                 }
             }
             break;
         case 7:
             if (fileControl.getFilemodel().getSeventhNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getSeventhNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getSeventhNetworkExtraOption());
                 }
             }
             break;
         case 8:
             if (fileControl.getFilemodel().getEighthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getEighthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getEighthNetworkExtraOption());
                 }
             }
             break;
         case 9:
             if (fileControl.getFilemodel().getNinthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getNinthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getNinthNetworkExtraOption());
                 }
             }
             break;
         case 10:
             if (fileControl.getFilemodel().getTenthNetworkExtraOption() != null) {
                 if (this.contains(fileControl.getFilemodel().getTenthNetworkExtraOption())) {
                     this.buildTextArea(fileControl.getFilemodel().getTenthNetworkExtraOption());
                 }
             }
             break;
         default:
             break;
     }

     this.rechecks();
		
	}
}
