package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.view.IemultorStore;

public class VMCreationViewPart1 extends DeviceBaseView {

	private static final long serialVersionUID = 1L;

	private JPanel windowContent;

	private GridLayout gridLayout;

	private JLabel chooseMachineNameLabel;

	private JTextField choooseMachineNameField;

	private JButton cancelButton;

	private JButton nextButton;

	public VMCreationViewPart1() {
		super();

		windowContent = new JPanel();

		gridLayout = new GridLayout(2, 2);

		windowContent.setLayout(gridLayout);

		chooseMachineNameLabel = new JLabel("Choose the name of your new machine:");

		choooseMachineNameField = new JTextField(0);

		windowContent.add(chooseMachineNameLabel);

		windowContent.add(choooseMachineNameField);

		cancelButton = new JButton("Cancel");

		nextButton = new JButton("Next >");

		windowContent.add(nextButton);

		windowContent.add(cancelButton);
		
		this.add(windowContent);
		this.setTitle("Create a new machine - Part 1");
	}

	public void initialize() {
		this.add(windowContent);
		this.setTitle("Create a new machine - Part 1");
	}

	public void configureListener(ActionListener listener) {
		cancelButton.addActionListener(listener);
		nextButton.addActionListener(listener);
	}

	public void configureStandardMode() {
		cancelButton.setActionCommand("Cancel1");
		nextButton.setActionCommand("Next1");
	}

	public JTextField getFieldChoooseMachineName() {
		return choooseMachineNameField;
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

	@Override
	public void applyView(IemultorStore store) {

	}
}
