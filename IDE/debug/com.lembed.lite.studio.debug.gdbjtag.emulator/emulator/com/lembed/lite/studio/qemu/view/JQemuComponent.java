package com.lembed.lite.studio.qemu.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lembed.lite.studio.qemu.view.internal.JPanelCreationView;

public class JQemuComponent extends JComponent {

    private static final long serialVersionUID = 1L;

    private GridLayout gridLayout;

    private JTabbedPane tabbedPane;

    private List<JPanelCreationView> panelViewList;

    private JButton createNewVMOption;

    private JButton openExistingVMOption;

    private int activePanel;

    private ActionListener listener;

    private JPanel myUntitledJPanel;

    private JButton useUtilities;

    public JQemuComponent() {
        super();


        tabbedPane = new JTabbedPane();

        panelViewList = new ArrayList<JPanelCreationView>();

        activePanel = 0;

        createNewVMOption = new JButton("Create a new virtual machine");
        openExistingVMOption = new JButton("Open a existing virtual machine");
        useUtilities = new JButton("Use the available utilities from JavaQemu!");

        JPanelCreationView untitledPanel = makeVMPanel("Untitled");
        myUntitledJPanel = untitledPanel;
        panelViewList.add(panelViewList.size(), untitledPanel);

        tabbedPane.addTab("Untitled", untitledPanel);

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                processTabChange();
            }
        });

        gridLayout = new GridLayout(1, 1);
        this.add(tabbedPane);
    }


    public void configureStandardMode() {

        createNewVMOption.setActionCommand("CreateNewVM");

        openExistingVMOption.setActionCommand("OpenExistingVM");

        useUtilities.setActionCommand("useUtilities");
    }

    public void configureListener(ActionListener listener) {

        createNewVMOption.addActionListener(listener);

        openExistingVMOption.addActionListener(listener);

        useUtilities.addActionListener(listener);

        this.listener = listener;
    }

    public void showMessage(String message) {
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

    public String getInputMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }


    public JPanelCreationView makeVMPanel(String machineName) {
        JPanelCreationView panel = null;
        panel = new JPanelCreationView(machineName,
                createNewVMOption,
                openExistingVMOption,
                listener,
                useUtilities);
        return panel;
    }

    public void addCreationNewJPanel(JPanelCreationView jpanel, String title) {
        panelViewList.add(this.panelViewList.size(), jpanel);
        tabbedPane.addTab(title, jpanel);
        tabbedPane.setSelectedIndex(this.panelViewList.size() - 1);
        activePanel = tabbedPane.getSelectedIndex();
    }

    private void processTabChange() {
        activePanel = tabbedPane.getSelectedIndex();
    }

    public JPanelCreationView getSelectedPanel() {
        return panelViewList.get(activePanel);
    }

    public JPanelCreationView getAnyPanel(int position) {
        if (position >= this.panelViewList.size()) {
            showMessage("Illegal Event!View!getAnyPanel!\nSelect another position!");
            return null;
        } else {
            return panelViewList.get(position);
        }
    }

    public int getActivePanel() {
        return activePanel;
    }

    public void removeCreationNewJPanel() {
        panelViewList.remove(activePanel);
        tabbedPane.remove(activePanel);
    }

    public void removeAllJPanels() {
        for (int i = 0; i < this.panelViewList.size(); i++) {
            this.panelViewList.remove(i);

            this.tabbedPane.remove(i);
        }
    }

    public void showAboutContents() {
        this.showMessage(/*
                 */"JavaQemu: "
                + "JavaQemu is a graphical user interface for QEMU."
                + /*
                 */ "\nAuthor: " + "Daniel S. F. Bruno.\n"
                + /*
                 */ "JavaQemu License: "
                + "GNU GENERAL PUBLIC LICENSE, VERSION 2." + /*
                 */ "\n"
                + "Version: " + this.getTitle().substring(9) +/*
                 */ "\n");
    }

    private String getTitle() {
		return JQemuComponent.class.getSimpleName();
	}

	public JPanel getMyUntitledJPanel() {
        return this.myUntitledJPanel;
    }

    public void changeNameJPanel(String name) {
        this.tabbedPane.setTitleAt(activePanel, name);
        this.getSelectedPanel().setTitle(name);
    }

    public String getActiveTitle() {
        return this.getSelectedPanel().getTitle();
    }

    public int getSizeOfJTabbedPane() {
        return this.panelViewList.size();
    }
}
