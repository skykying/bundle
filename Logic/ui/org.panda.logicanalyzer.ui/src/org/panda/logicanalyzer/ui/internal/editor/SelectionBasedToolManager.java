package org.panda.logicanalyzer.ui.internal.editor;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

/**
 * The selection based tool manager takes care of the selection based tools
 * available.
 *
 *
 *
 */
public class SelectionBasedToolManager implements ISelectionListener {

	/**
	 * Creates a new tool manager for the given visualizer. This method must be called,
	 * after the given visualizer has an input.
	 *
	 * @param visualizer The visualizer to create the manager for
	 * @return The tool manager
	 * @throws CoreException If a registered selection based tool causes trouble
	 */
	public static SelectionBasedToolManager getManager(DataPacketVisualizer visualizer) throws CoreException {
		List<ToolDescriptor> result = ToolDescriptor.getAllDescriptors();

		for (ToolDescriptor descriptor : result) {
			descriptor.initialize(visualizer.getOverlayAcceptor(), visualizer.getInput());
		}

		return new SelectionBasedToolManager(visualizer, result);
	}

	/**
	 * The descriptors this manager takes care of
	 */
	private final List<ToolDescriptor> descriptors;

	/**
	 * The currently enabled tool, or null if no tool is currently enabled.
	 */
	private ToolDescriptor currentTool;

	/**
	 * The selection listener of the current tool, or null if no tool is
	 * currently enabled.
	 */
	private ISelectionListener currentToolsSelectionListener;

	/**
	 * The visualizer we're working on
	 */
	private final DataPacketVisualizer visualizer;

	/**
	 * The tool inspector view. If no tool that needs UI has been enabled yet,
	 * this may very well be <code>null</code>.
	 */
	private ToolInspectorView toolInspectorView;


	private SelectionBasedToolManager(DataPacketVisualizer visualizer, List<ToolDescriptor> descriptors) {
		this.visualizer = visualizer;
		visualizer.addSelectionListener(this);
		this.descriptors = descriptors;
	}

	public void selectionChanged(int channelNr, long from, long to) {
		if (currentToolsSelectionListener != null)
			currentToolsSelectionListener.selectionChanged(channelNr, from, to);
	}

	/**
	 * @return the currently enabled tool or null if no tool is enabled at the
	 *         moment.
	 */
	public ToolDescriptor getCurrentlyEnabledTool() {
		return currentTool;
	}

	/**
	 * Disables the currently enabled tool, or does nothing if there is no
	 * enabled tool.
	 */
	public void disableCurrentTool() {
		if (currentTool != null) {
			currentTool.disable();
			currentTool = null;
			currentToolsSelectionListener = null;
		}
	}

	/**
	 * Enables a tool. If another tool has previously been enabled, it's disabled.
	 * If this denoted tool is already enabled, it's not renabled.
	 *
	 * @param descriptor the tool to enable
	 * @throws CoreException If the tool could not be enabled
	 */
	public void enableTool(ToolDescriptor descriptor) throws CoreException {
		if (descriptor == currentTool) return;

		if (currentTool != null) {
			currentTool.disable();
			currentTool = null;
		}

		if (descriptor.needsUI()) {
			
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if(window==null){
				return;
			}
			
			toolInspectorView = (ToolInspectorView) window.getActivePage().showView(ToolInspectorView.VIEW_ID);
			for (Control control : toolInspectorView.getComposite().getChildren()) {
				control.dispose();
			}

			descriptor.createContents(toolInspectorView.getComposite());
			toolInspectorView.getComposite().pack(true);
		} else {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(toolInspectorView);
		}

		if (visualizer.isAllChannelSelection() != descriptor.isAllChannelSelection()) {
			visualizer.setAllChannelSelection(descriptor.isAllChannelSelection());
		}

		descriptor.enable();
		currentTool = descriptor;
		currentToolsSelectionListener = currentTool.getSelectionListener();
	}

	/**
	 * Enables the tool with the given name. If there are multiple tools with
	 * the same name, the first one's enabled. If no tool with the given name
	 * exists, nothing is done. If another tool has previously been enabled,
	 * it's disabled. If this denoted tool is already enabled, it's not
	 * renabled.
	 *
	 * @param name
	 *            The name of the tool to enable.
	 * @throws CoreException
	 *             If the tool could not be enabled
	 */
	public void enableTool(String name) throws CoreException {
		for (ToolDescriptor descriptor : getDescriptors()) {
			if (descriptor.getName().equals(name)) {
				enableTool(descriptor);
				return;
			}
		}
	}

	/**
	 * @return the descriptors this manager takes care of
	 */
	public List<ToolDescriptor> getDescriptors() {
		return descriptors;
	}

}
