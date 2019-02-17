package org.panda.logicanalyzer.ui.editor;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

/**
 * A selection based tool may produce overlays on special occasions
 * (such as a selection change). Such tools are registered using
 * the Eclipse extension point mechanism.
 */
public interface ISelectionBasedTool {

	/**
	 * Initializes this factory.
	 *
	 * @param acceptor The accptor for overlays
	 * @param packet The packet we're working with
	 * @param channelNr The number of the channel this tool is going to work on
	 */
	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet);

	/**
	 * If this tool wants to listen on selections being made on that channel,
	 * it may return a non-null value here. That listener will be notified if a
	 * selection event occurs.
	 *
	 * @return a listener or null
	 */
	public ISelectionListener getSelectionListener();

	/**
	 * <p>
	 * Creates some confiuration content on the given parent. One can specify if
	 * this ability is required during tool registration. None the less, if the
	 * tool has this ability enabled, this method is supposed to create some
	 * content on the parent.
	 * </p>
	 * <p>
	 * <i>Be aware that this method might be called multiple times.</i> In that
	 * case previously created widgets have been disposed. So whenever
	 * interacting with widgets created in this method make sure to check that
	 * they're not already disposed.
	 * </p>
	 *
	 * @param parent
	 *            The parent to create the widgets on
	 */
	public void createContents(Composite parent);

	/**
	 * Called when this tool is enabled. Throwing an exception causes this tool
	 * not to be enabled.
	 *
	 * @throws CoreException
	 *             If the tool is in an invalid state and cannot be enabled.
	 */
	public void enable() throws CoreException;

	/**
	 * Disables this tool. If {@link #enable()} has been called, it's guaranteed
	 * that this is method is called as well. <i>Be aware that it's NOT the
	 * tools responsibility to dispose created overlays because it's
	 * disabled.</i>
	 */
	public void disable();

}
