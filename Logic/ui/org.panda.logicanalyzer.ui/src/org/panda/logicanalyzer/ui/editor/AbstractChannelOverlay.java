package org.panda.logicanalyzer.ui.editor;


/**
 * Base class for implementing overlays. This class takes care of managing
 * the disposal state, so make sure {@link #dispose()} gets called when appropriate.
 */
public abstract class AbstractChannelOverlay implements IChannelOverlay {

	/**
	 * The disposed flag used by {@link #dispose()} and {@link #isDisposed()}
	 */
	private boolean disposed;

	public void dispose() {
		disposed = true;
	}

	public boolean isDisposed() {
		return disposed;
	}

}
