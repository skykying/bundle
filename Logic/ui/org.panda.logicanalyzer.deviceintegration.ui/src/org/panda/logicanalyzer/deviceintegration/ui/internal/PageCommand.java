package org.panda.logicanalyzer.deviceintegration.ui.internal;

import org.eclipse.ui.forms.widgets.ExpandableComposite;

/**
 * This enumeration is used to synchronize the UI and model on the {@link Page}
 */
enum PageCommand {

	/**
	 * Initialize the UI
	 */
	Initialize,

	/**
	 * An {@link ExpandableComposite} has been expanded
	 */
	CompositeExpanded,

	/**
	 * Update the model from the UI
	 */
	UpdateModel,

	/**
	 * The device descriptor has changed
	 */
	DeviceChanged,

	/**
	 * The page must be validated again
	 */
	ValidatePage;

}
