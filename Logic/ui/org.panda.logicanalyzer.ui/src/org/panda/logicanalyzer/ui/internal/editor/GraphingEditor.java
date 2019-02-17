package org.panda.logicanalyzer.ui.internal.editor;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.panda.logicanalyzer.ui.Activator;
import org.panda.logicanalyzer.ui.IDataPacketEditorInput;

/**
 * Visualizes a {@link IDataPacket} apropriately.
 */
public class GraphingEditor extends EditorPart {

	public GraphingEditor() {
		super();
		System.out.println("GraphingEditor");
	}

	/**
	 * The ID of this editor
	 */
	public static final String EDITOR_ID = "org.panda.logicanalyzer.ui.graphingEditor";

	/**
	 * The visualizer contained by this editor
	 */
	private DataPacketVisualizer dataPacketVisualizer;

	@Override
	public void doSave(IProgressMonitor monitor) {
		// we don't save this editor
	}

	@Override
	public void doSaveAs() {
		// we don't save this editor
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		if (!(input instanceof IDataPacketEditorInput)) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unknown editor input " + input.getClass().getName());
			throw new PartInitException(status);
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		IDataPacket packet = ((IDataPacketEditorInput) getEditorInput()).getPacket();
		try {
			dataPacketVisualizer = new DataPacketVisualizer(parent);
			dataPacketVisualizer.setInput(packet);
		} catch (CoreException e) {
			ErrorDialog.openError(null, null, null, e.getStatus());
		}
	}

	/**
	 * Sets the time scale of this editor in nanoseconds per pixel. Any value less than equal to zero is ignored.
	 *
	 * @param nsPerPixel the new time scale
	 */
	public void setScale(final long nsPerPixel) {
		if (dataPacketVisualizer != null) dataPacketVisualizer.setScale(nsPerPixel);
	}

	/**
	 * Returns the current timescale of this editor or -1 if the editor control has not yet been created.
	 *
	 * @return the scale or -1
	 */
	public long getScale() {
		return dataPacketVisualizer == null ? -1 : dataPacketVisualizer.getScale();
	}

	/**
	 * Calling this method will cause the editor to autoscale. That is setting scale to
	 * <code>editorWidthInPx / frameLengthInNs</code>. If either the packet or {@link #dataPacketVisualizer}
	 * is null, or <code>editorWidthInPx</code> is zero, nothing is done.
	 */
	public void autoScale() {
		if (dataPacketVisualizer == null || dataPacketVisualizer.getBounds().width == 0) return;

		dataPacketVisualizer.autoScale();
	}

	@Override
	public void setFocus() {

	}

	/**
	 * Sets the current selection. If from or to is less than equal to zero, the selection
	 * is removed.
	 *
	 * @param from The start of the selection in nanoseconds
	 * @param to The end of the selection in nanoseconds
	 */
	public void setSelection(long from, long to) {
		dataPacketVisualizer.setSelection(from, to);
	}

	/**
	 * If this flag is true, selection made on one channel is propagated to all the other channels
	 *
	 * @return the all channel selection flag
	 */
	public boolean isAllChannelSelection() {
		return dataPacketVisualizer.isAllChannelSelection();
	}

	/**
	 * If this flag is true, selection made on one channel is propagated to all the other channels
	 *
	 * @param allChannelSelection true if selection propagation should be performed, false if not
	 */
	public void setAllChannelSelection(boolean allChannelSelection) {
		dataPacketVisualizer.setAllChannelSelection(allChannelSelection);
	}

	/**
	 * Computes the currently selected data. This implementation is truely unefficient (something linke O(n^3))
	 *
	 * @return the currently selected data or null if there currently is no selection
	 */
	public IDataPacket getSelectedData() {
		return dataPacketVisualizer.getSelectedData();
	}

	/**
	 * @return the complete data packet this editor is working on
	 */
	public IDataPacket getAllData() {
		return ((IDataPacketEditorInput) getEditorInput()).getPacket();
	}

	/**
	 * @return the tool manager of this editor or null if {@link #createPartControl(Composite)} has not yet been called
	 */
	public SelectionBasedToolManager getToolManager() {
		return dataPacketVisualizer == null ? null : dataPacketVisualizer.getToolManager();
	}

}
