package org.panda.logicanalyzer.ui.editor;

/**
 * An overlay acceptor takes overlays and draws them if appropriate.
 */
public interface IOverlayAcceptor {

	/**
	 * Accepts an overlay, thus adding it to the list of overlays to be proccesed.
	 * @param channelNr The number of the channel we're supposed to draw the overlay on
	 * @param overlay The overlay itself
	 */
	public void accept(int channelNr, IChannelOverlay overlay);

}
