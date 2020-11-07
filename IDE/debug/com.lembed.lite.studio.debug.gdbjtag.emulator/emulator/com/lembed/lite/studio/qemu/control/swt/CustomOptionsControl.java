package com.lembed.lite.studio.qemu.control.swt;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.lembed.lite.studio.qemu.model.swt.CustomOptionsModel;
import com.lembed.lite.studio.qemu.model.swt.Model;
import com.lembed.lite.studio.qemu.view.internal.swt.CustomOptionsView;
import com.lembed.lite.studio.qemu.view.internal.swt.UtilitiesView;

public class CustomOptionsControl implements BaseControl {

    private CustomOptionsView cOptView;
    private CustomOptionsModel cOptModel;

    public CustomOptionsControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.cOptModel = new CustomOptionsModel(myemulation, myfile);
        this.cOptView = new CustomOptionsView(myfile);
        this.cOptView.configureStandardMode();
        this.cOptView.configureListener(this);
    }

    public void setVisible(Boolean value) {
        this.cOptView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            DefaultListModel<String> listModel = (DefaultListModel<String>) this.cOptView.getListBox().getModel();
            listModel.removeAllElements();
            this.cOptView.getTextBox().setText("");
            this.cOptModel.setOption("");
            this.cOptModel.setFileOption("");
            this.cOptView.setVisible(false);
        } else if (e.getActionCommand().equals("eraseSelectedButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cOptView.getListBox().getModel();
            int selectedIndex = this.cOptView.getListBox().getSelectedIndex();
            if (selectedIndex != -1) {
                model.remove(selectedIndex);

                StringBuilder sb = new StringBuilder(1024);
                StringBuilder fileSb = new StringBuilder(1024);

                if (model.getSize() > 0) {
                    sb.append((String) model.getElementAt(0));
                    fileSb.append((String) model.getElementAt(0));

                    for (int i = 1; i < model.getSize(); i++) {
                        sb.append(" ").append((String) model.getElementAt(i));
                        fileSb.append("\n").append((String) model.getElementAt(i));
                    }
                }

                this.cOptModel.setOption(sb.toString());
                this.cOptModel.setFileOption(fileSb.toString());
            }
        } else if (e.getActionCommand().equals("addButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cOptView.getListBox().getModel();
            if (model.contains(this.cOptView.getTextBox().getText())) {
                UtilitiesView.showMessageAnywhere("This element already is added to the list.");
            } else if (Model.containCommonRoot(model, this.cOptView.getTextBox().getText())) {
                UtilitiesView.showMessageAnywhere("Another element already is added to the list, for this option.");
            } else {
                model.addElement(this.cOptView.getTextBox().getText());
                this.cOptView.rechecks();

                StringBuilder sb = new StringBuilder(1024);
                StringBuilder fileSb = new StringBuilder(1024);

                if (model.getSize() > 0) {
                    sb.append((String) model.getElementAt(0));
                    fileSb.append((String) model.getElementAt(0));

                    for (int i = 1; i < model.getSize(); i++) {
                        sb.append(" ").append((String) model.getElementAt(i));
                        fileSb.append("\n").append((String) model.getElementAt(i));
                    }
                }

                this.cOptModel.setOption(sb.toString());
                this.cOptModel.setFileOption(fileSb.toString());
            }
        } else if (e.getActionCommand().equals("okButton")) {
            this.cOptView.getTextBox().setText("");
            this.cOptView.setVisible(false);
        } else if (e.getActionCommand().equals("copyButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cOptView.getListBox().getModel();
            int selectedIndex = this.cOptView.getListBox().getSelectedIndex();
            if (selectedIndex != -1) {
                this.cOptView.getTextBox().setText(model.getElementAt(selectedIndex));
            }
        } else if (e.getActionCommand().equals("modifyButton")) {
            DefaultListModel<String> model = (DefaultListModel<String>) this.cOptView.getListBox().getModel();
            int selectedIndex = this.cOptView.getListBox().getSelectedIndex();
            if (selectedIndex != -1) {
                if (this.cOptView.getTextBox().getText().isEmpty()) {
                    String message = "Sorry! The box text is empty!";
                    System.out.println(message);
                    JScrollPane scrollPane = new JScrollPane();
                    scrollPane.setPreferredSize(new Dimension(500, 500));
                    JTextArea textArea = new JTextArea(message);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setEditable(false);
                    textArea.setMargin(new Insets(5, 5, 5, 5));
                    scrollPane.getViewport().setView(textArea);
                    Object trueMessage = scrollPane;
                    JOptionPane.showMessageDialog(null, trueMessage);
                    
                } else {
                    model.setElementAt(this.cOptView.getTextBox().getText(), selectedIndex);

                    StringBuilder sb = new StringBuilder(1024);
                    StringBuilder fileSb = new StringBuilder(1024);

                    if (model.getSize() > 0) {
                        sb.append((String) model.getElementAt(0));
                        fileSb.append((String) model.getElementAt(0));

                        for (int i = 1; i < model.getSize(); i++) {
                            sb.append(" ").append((String) model.getElementAt(i));
                            fileSb.append("\n").append((String) model.getElementAt(i));
                        }
                    }

                    this.cOptModel.setOption(sb.toString());
                    this.cOptModel.setFileOption(fileSb.toString());
                }
            } else {
                String message = "Sorry! You should to select a line from list above, first!";
                System.out.println(message);
                
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JTextArea textArea = new JTextArea(message);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setMargin(new Insets(5, 5, 5, 5));
                scrollPane.getViewport().setView(textArea);
                Object trueMessage = scrollPane;
                JOptionPane.showMessageDialog(null, trueMessage);
            }
        } else if (e.getActionCommand().equals("findButton")) {
            if (!cOptView.getTextBox().getText().isEmpty()) {
            	
                DefaultListModel<String> model = (DefaultListModel<String>) cOptView.getListBox().getModel();
                if (model.contains(this.cOptView.getTextBox().getText())) {
                	String m = "The list contains the element: " + cOptView.getTextBox().getText() + ".";
                	
                    UtilitiesView.showMessageAnywhere(m);
                } else {
                	String m = "The list doesn't contain the element: "+ cOptView.getTextBox().getText() + ".";
                    UtilitiesView.showMessageAnywhere(m);
                }
            } else {
                UtilitiesView.showMessageAnywhere("Sorry! The box text is empty!");
            }
        }
    }

}
