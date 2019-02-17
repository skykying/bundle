package org.panda.logicanalyzer.ui.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The integer bit selector composite lets the user select each individual bit
 * of a 32bit integer.
 */
public class IntegerBitSelector extends Composite {

	/**
	 * Classes implementing this interface can listen for modifications on an
	 * {@link IntegerBitSelector}
	 */
	public static interface IModificationListener {

		/**
		 * The value has been modified
		 *
		 * @param control The control that triggered the event
		 * @param newValue The new value
		 */
		public void valueChanged(IntegerBitSelector control, int newValue);

	}

	/**
	 * Style flag inidicating that a text field should be added to this control
	 * thru which the user may modify the value directly
	 */
	public static final int WITH_TEXT = 1 << 31;

	/**
	 * The checkboxes of this control
	 */
	private final Button[] buttons = new Button[32];

	/**
	 * The selection maintained by this control
	 */
	private int value = 0;

	/**
	 * The modification listeners
	 */
	private List<IModificationListener> modificationListeners = new LinkedList<IModificationListener>();


	public IntegerBitSelector(Composite parent) {
		super(parent, SWT.NONE);

		createContents(false);
	}

	public IntegerBitSelector(Composite parent, int style) {
		super(parent, style);

		createContents((style & WITH_TEXT) == WITH_TEXT);
	}

	/**
	 * Add a new modification listener to this control. If the listener has
	 * already been registered, it is registered again (thus will be called
	 * multiple times on event occurence).
	 *
	 * @param listener
	 *            the listener to add
	 */
	public void addModificationListener(IModificationListener listener) {
		modificationListeners.add(listener);
	}

	/**
	 * Removes a listener from this control. If the listener has never been
	 * registered, nothing changes.
	 *
	 * @param listener The listener to remove
	 */
	public void removeModificationListener(IModificationListener listener) {
		modificationListeners.remove(listener);
	}


	/**
	 * Creates the content of this control
	 *
	 * @param addTextField
	 *            if true, a text field is added thru which the user may modify
	 *            the selection of this control directly
	 * @param parent
	 *            The parent to create the stuff on
	 */
	private void createContents(boolean addTextField) {
		GridLayout layout = new GridLayout(addTextField ? 33 : 32, false);
		layout.horizontalSpacing = 0;
		setLayout(layout);

		Label hiLabel = new Label(this, SWT.NONE);
		GridDataFactory.fillDefaults().span(31, 1).applyTo(hiLabel);
		hiLabel.setText("31");

		Label loLabel = new Label(this, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(loLabel);
		loLabel.setText("00");

		if (addTextField) new Label(this, SWT.NONE);

		for (int i = 31; i >= 0; i--) {
			final int idx = i;

			Button button = new Button(this, SWT.CHECK);
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((Button) e.widget).getSelection()) {
						setValue(getValue() | (1 << idx));
					} else {
						setValue(getValue() & (~(1 << idx)));
					}
				}

			});
			buttons[i] = button;
		}

		if (addTextField) {
			final Text valueText = new Text(this, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(valueText);

			class Listener implements ModifyListener, IModificationListener {
				private boolean selfInducedUpdate = false;

				public void modifyText(ModifyEvent e) {
					String text = valueText.getText();
					selfInducedUpdate = true;
					try {
						if (text.startsWith("0x")) {
							setValue(Integer.parseInt(text.substring(2), 16));
						} else {
							setValue(Integer.parseInt(text));
						}
					} catch (NumberFormatException ex) {
						// who cares? so were simply not doing an update
					} finally {
						selfInducedUpdate = false;
					}
				}

				public void valueChanged(IntegerBitSelector control, int newValue) {
					if (selfInducedUpdate) return;
					valueText.setText("0x" + Integer.toHexString(value));
				}

			}
			Listener listener = new Listener();
			valueText.addModifyListener(listener);
			addModificationListener(listener);
		}
	}

	public void setValue(int value) {
		this.value = value;

		getDisplay().syncExec(new Runnable() {
			
			public void run() {
				for (int i = 0; i < 31; i++) {
					buttons[i].setSelection(((IntegerBitSelector.this.value >> i) & 0x01) > 0);
				}
			}

		});

		Event event = new Event();
		event.widget = this;
		event.display = getDisplay();
		for (IModificationListener listener : modificationListeners)
			listener.valueChanged(this, value);
	}

	public int getValue() {
		return value;
	}

}
