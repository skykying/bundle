package com.lembed.lite.studio.qemu.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lembed.lite.studio.qemu.view.internal.swt.SwtPanelCreationView;

public class JSwtQemuView extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null; 


	private GridLayout gridLayout;
	private JTabbedPane tabbedPane;
	private List<SwtPanelCreationView> myPanels;
	private JButton createNewVMOption;
	private JButton openExistingVMOption;

	private int activePanel;

	private ActionListener listener;
	private JPanel myUntitledJPanel;
	private JButton useUtilities;

	public JSwtQemuView() {
		super();


		tabbedPane = new JTabbedPane();

		myPanels = new ArrayList<SwtPanelCreationView>();

		activePanel = 0;

		createNewVMOption = new JButton("Create a new virtual machine");
		openExistingVMOption = new JButton("Open a existing virtual machine");
		useUtilities = new JButton("Use the available utilities from JavaQemu!");

		SwtPanelCreationView untitledPanel = makeVMPanel("Untitled");
		myUntitledJPanel = untitledPanel;
		 myPanels.add(this.myPanels.size(), untitledPanel);

		tabbedPane.addTab("Untitled", untitledPanel);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				processTabChange();
			}
		});

		gridLayout = new GridLayout(1, 1);
		this.initialize();
		this.initialize_window();
		this.rechecks();
	}

	private void initialize() {
		this.add(getJContentPane());


		this.jContentPane.setLayout(this.gridLayout);
		this.repaint();
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
		}
		return jContentPane;
	}

	public void configureStandardMode() {



		createNewVMOption.setActionCommand("CreateNewVM");

		openExistingVMOption.setActionCommand("OpenExistingVM");

		useUtilities.setActionCommand("useUtilities");
	}

	public void registerListener(BaseListener listener) {


		createNewVMOption.addActionListener(listener);

		openExistingVMOption.addActionListener(listener);

		useUtilities.addActionListener(listener);

		this.listener = listener;
	}

	public static void showMessage(String message) {
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

	private void initialize_window() {
		this.jContentPane.add(tabbedPane);
	}

	private void rechecks() {
		this.repaint();
	}

	public SwtPanelCreationView makeVMPanel(String machineName) {
		SwtPanelCreationView panel = null;
		panel = new SwtPanelCreationView(machineName, this.createNewVMOption, this.openExistingVMOption, this.listener,
				this.useUtilities);
		return panel;
	}

	public void addCreationNewJPanel(SwtPanelCreationView jpanel, String title) {
		this.myPanels.add(this.myPanels.size(), jpanel);

		this.tabbedPane.addTab(title, jpanel);

		this.tabbedPane.setSelectedIndex(this.myPanels.size() - 1);

		this.activePanel = tabbedPane.getSelectedIndex();

		this.rechecks();
	}

	private void processTabChange() {
		this.activePanel = tabbedPane.getSelectedIndex();
	}

	public SwtPanelCreationView getSelectedPanel() {
		return this.myPanels.get(activePanel);
	}

	public SwtPanelCreationView getAnyPanel(int position) {
		if (position >= this.myPanels.size()) {
			JSwtQemuView.showMessage("Illegal Event!View!getAnyPanel!\nSelect another position!");
			return null;
		} else {
			return this.myPanels.get(position);
		}
	}

	public int getActivePanel() {
		return activePanel;
	}

	public void removeCreationNewJPanel() {
		this.myPanels.remove(this.activePanel);

		this.tabbedPane.remove(this.activePanel);
	}

	public void removeAllJPanels() {
		for (int i = 0; i < this.myPanels.size(); i++) {
			this.myPanels.remove(i);

			this.tabbedPane.remove(i);
		}
	}

	public void showAboutContents() {
		JSwtQemuView.showMessage("");
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
		return this.myPanels.size();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
