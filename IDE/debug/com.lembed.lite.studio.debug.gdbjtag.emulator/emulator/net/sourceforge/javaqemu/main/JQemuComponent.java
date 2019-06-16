package net.sourceforge.javaqemu.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.javaqemu.model.Model;
import net.sourceforge.javaqemu.view.JPanelCreationView;

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
                this.createNewVMOption,
                this.openExistingVMOption,
                this.listener,
                this.useUtilities);
        return panel;
    }

    public void addCreationNewJPanel(JPanelCreationView jpanel, String title) {
        this.panelViewList.add(this.panelViewList.size(), jpanel);

        this.tabbedPane.addTab(title, jpanel);

        this.tabbedPane.setSelectedIndex(this.panelViewList.size() - 1);

        this.activePanel = tabbedPane.getSelectedIndex();
    }

    private void processTabChange() {
        activePanel = tabbedPane.getSelectedIndex();
    }

    public JPanelCreationView getSelectedPanel() {
        return this.panelViewList.get(activePanel);
    }

    public JPanelCreationView getAnyPanel(int position) {
        if (position >= this.panelViewList.size()) {
            this.showMessage("Illegal Event!View!getAnyPanel!\nSelect another position!");
            return null;
        } else {
            return this.panelViewList.get(position);
        }
    }

    public int getActivePanel() {
        return activePanel;
    }

    public void removeCreationNewJPanel() {
        this.panelViewList.remove(this.activePanel);

        this.tabbedPane.remove(this.activePanel);
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
