package com.lembed.lite.studio.qemu.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.lembed.lite.studio.qemu.view.internal.JPanelCreationView;

public class SwtQemuView extends SwtQBaseView {

	private JPanel jContentPane = null;

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenuItem configureCommand;
	// private JMenuItem exitCommand;

	private JMenu emulationMenu;

	private Action startEmulationAction;
	private JMenuItem startEmulation;

	private Action stopEmulationAction;
	private JMenuItem stopEmulation;

	private JMenu helpMenu;
	private JMenuItem aboutCommand;

	private GridLayout gridLayout;
	private JTabbedPane tabbedPane;
	private List<JPanelCreationView> myPanels;
	private JButton createNewVMOption;
	private JButton openExistingVMOption;

	private int activePanel;

	private ActionListener listener;
	private JPanel myUntitledJPanel;
	private JButton useUtilities;

	public SwtQemuView() {
		super();

		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		configureCommand = new JMenuItem("Configure");
		// exitCommand = new JMenuItem("Quit");

		fileMenu.add(configureCommand);
		// fileMenu.add(exitCommand);
		menuBar.add(fileMenu);

		emulationMenu = new JMenu("Emulation");
		startEmulation = new JMenuItem("Start emulation");

		stopEmulation = new JMenuItem("Stop emulation");

		emulationMenu.add(startEmulation);
		emulationMenu.add(stopEmulation);
		menuBar.add(emulationMenu);

		helpMenu = new JMenu("Help");
		aboutCommand = new JMenuItem("About JavaQemu");

		helpMenu.add(aboutCommand);
		menuBar.add(helpMenu);

		tabbedPane = new JTabbedPane();

		myPanels = new ArrayList<JPanelCreationView>();

		activePanel = 0;

		createNewVMOption = new JButton("Create a new virtual machine");
		openExistingVMOption = new JButton("Open a existing virtual machine");
		useUtilities = new JButton("Use the available utilities from JavaQemu!");

		JPanelCreationView untitledPanel = makeVMPanel("Untitled");
		myUntitledJPanel = untitledPanel;
		myPanels.add(this.myPanels.size(), untitledPanel);

		tabbedPane.addTab("Untitled", untitledPanel);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				processTabChange();
			}
		});

		gridLayout = new GridLayout(1, 1);

		jContentPane.setLayout(gridLayout);
		jContentPane.add(tabbedPane);
	}

	
	public void initComponents() {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT Spinner (o7planning.org)");

		RowLayout rowLayout = new RowLayout();
		rowLayout.marginLeft = 10;
		rowLayout.marginTop = 10;
		rowLayout.spacing = 15;
		shell.setLayout(rowLayout);

		// Label
		Label label = new Label(shell, SWT.NONE);
		label.setText("Select Level:");

		// Spinner
		final Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setMinimum(1);
		spinner.setMaximum(5);
		spinner.setSelection(3);

		Font font = new Font(display, "Courier", 20, SWT.NORMAL);
		spinner.setFont(font);

		// Label
		Label labelMsg = new Label(shell, SWT.NONE);
		labelMsg.setText("?");
		labelMsg.setLayoutData(new RowData(150, SWT.DEFAULT));

		spinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				labelMsg.setText("You select: " + spinner.getSelection());
			}
		});

		shell.setSize(400, 250);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	public  void combox() {
		
		Composite composite = new Composite(getContainer(), SWT.NONE);
		
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems("Alpha", "Bravo", "Charlie");

		Rectangle clientArea = composite.getClientArea();
		combo.setBounds(clientArea.x, clientArea.y, 200, 200);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("widgetSelected");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widgetDefaultSelected");
			}

		});

	}

	public void configureStandardMode() {
		// exitCommand.setActionCommand("ExitCommand");
		configureCommand.setActionCommand("ConfigureCommand");

		{
			startEmulation.setActionCommand("StartEmulation");
			startEmulationAction = new Action() {

				@Override
				public void run() {
					super.run();
				}

			};
			startEmulationAction.setText("Start Emulation");
			startEmulationAction.setToolTipText("start the emulation instance");
		}

		{
			stopEmulation.setActionCommand("StopEmulation");
			stopEmulationAction = new Action() {

				@Override
				public void run() {
					super.run();
				}

			};
			stopEmulationAction.setText("Stop Emulation");
			stopEmulationAction.setToolTipText("stop the emulation instance");
		}

		aboutCommand.setActionCommand("AboutCommand");

		createNewVMOption.setActionCommand("CreateNewVM");

		openExistingVMOption.setActionCommand("OpenExistingVM");

		useUtilities.setActionCommand("useUtilities");
	}

	// public void registerListener(BaseListener listener) {
	// exitCommand.addActionListener(listener);
	// configureCommand.addActionListener(listener);
	//
	// startEmulation.addActionListener(listener);
	// stopEmulation.addActionListener(listener);
	//
	// aboutCommand.addActionListener(listener);
	//
	// createNewVMOption.addActionListener(listener);
	//
	// openExistingVMOption.addActionListener(listener);
	//
	// useUtilities.addActionListener(listener);
	//
	// this.listener = listener;
	// }

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
		panel = new JPanelCreationView(machineName, this.createNewVMOption, this.openExistingVMOption, this.listener,
				this.useUtilities);
		return panel;
	}

	public void addCreationNewJPanel(JPanelCreationView jpanel, String title) {
		myPanels.add(this.myPanels.size(), jpanel);

		tabbedPane.addTab(title, jpanel);

		tabbedPane.setSelectedIndex(myPanels.size() - 1);

		activePanel = tabbedPane.getSelectedIndex();

	}

	private void processTabChange() {
		this.activePanel = tabbedPane.getSelectedIndex();
	}

	public JPanelCreationView getSelectedPanel() {
		return myPanels.get(activePanel);
	}

	public JPanelCreationView getAnyPanel(int position) {
		if (position >= myPanels.size()) {
			showMessage("Illegal Event!View!getAnyPanel!\nSelect another position!");
			return null;
		} else {
			return myPanels.get(position);
		}
	}

	public int getActivePanel() {
		return activePanel;
	}

	public void removeCreationNewJPanel() {
		myPanels.remove(activePanel);

		tabbedPane.remove(activePanel);
	}

	public void removeAllJPanels() {
		for (int i = 0; i < myPanels.size(); i++) {
			myPanels.remove(i);
			tabbedPane.remove(i);
		}
	}

	public void showAboutContents() {
		showMessage("");
	}

	public JPanel getMyUntitledJPanel() {
		return myUntitledJPanel;
	}

	public void changeNameJPanel(String name) {
		tabbedPane.setTitleAt(activePanel, name);
		getSelectedPanel().setTitle(name);
	}

	public String getActiveTitle() {
		return getSelectedPanel().getTitle();
	}

	public int getSizeOfJTabbedPane() {
		return myPanels.size();
	}

//	@Override
//	protected void createPage0() {
//		combox();
//	}
}
