package org.panda.logicanalyzer.ui.editor;

/**
 * Classes implementing this interface may listen for selection events on a
 * channel visualizer.
 */
public interface ISelectionListener {

	/**
	 * Called when the selection has changed. Be aware that the timings supplied
	 * to this method are given in the scale of this visualizer. If from or to
	 * is less than equal to zero, the selection has been removed.
	 *
	 * @param channelNr
	 *            The number of the channel the selection occured at (or -1 if
	 *            on all channels)
	 *
	 * @param from
	 *            The start of the selection or -1
	 * @param to
	 *            The end of the selection or -1
	 */
	public void selectionChanged(int channelNr, long from, long to);

}
