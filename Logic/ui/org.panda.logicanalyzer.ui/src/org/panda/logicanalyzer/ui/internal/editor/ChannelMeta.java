package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * The channel meta provides the channel name, a hide button,
 * and an invert button.
 */
public class ChannelMeta extends Composite {

	/**
	 * The visualizer this meta field changes the settings for
	 */
	private ChannelVisualizer visualizer;

	/**
	 * The name of this channel
	 */
	private final String channelName;

	public ChannelMeta(Composite parent, String channelName) {
		super(parent, SWT.NONE);
		this.channelName = channelName;

		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 0;
		setLayout(layout);
	}

	private void createHiddenUI() {
		for (Control c : getChildren()) c.dispose();

		Button showButton = new Button(this, SWT.CHECK);
		showButton.setText(channelName);
		showButton.setSelection(true);
		showButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (visualizer == null) return;

				visualizer.setHidden(false);
				createNonHiddenUI();
				pack();
				getParent().pack();
			}

		});
	}

	private void createNonHiddenUI() {
		for (Control c : getChildren()) c.dispose();

		Label label = new Label(this, SWT.NONE);
		label.setText(channelName);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

		new Label(this, SWT.NONE);

		final Button invertButton = new Button(this, SWT.CHECK);
		invertButton.setText("I");
		if (visualizer != null) invertButton.setSelection(visualizer.isInverted());
		invertButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (visualizer == null) return;

				visualizer.setInverted(invertButton.getSelection());
			}

		});

		final Button hideButton = new Button(this, SWT.CHECK);
		hideButton.setText("H");
		hideButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (visualizer == null) return;

				visualizer.setHidden(true);
				createHiddenUI();
				pack();
				getParent().pack();
			}

		});
	}

	/**
	 * Set the visualizer this meta field changes the settings for.
	 *
	 * @param visualizer The visualizer to set
	 */
	public void setVisualizer(ChannelVisualizer visualizer) {
		this.visualizer = visualizer;
		createNonHiddenUI();
	}


}
