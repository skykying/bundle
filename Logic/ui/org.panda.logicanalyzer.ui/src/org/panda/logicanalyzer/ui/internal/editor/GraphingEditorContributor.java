package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * This class adds some actions to the editors toolbar. This is mostly the tools
 * provided by third parties using the
 * <code>org.panda.logicanalyzer.ui.selectionBasedTool</code> extension
 * point.
 */
public class GraphingEditorContributor extends EditorActionBarContributor {

	/**
	 * A tool action enables a tool based on its descriptor.
	 */
	private class ToolAction extends Action {

		/**
		 * The tool we represent the action for
		 */
		private final ToolDescriptor descriptor;

		public ToolAction(ToolDescriptor descriptor) {
			super(descriptor.getName(), IAction.AS_RADIO_BUTTON);
			this.descriptor = descriptor;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return descriptor.getIconUrl() == null ? null : ImageDescriptor.createFromURL(descriptor.getIconUrl());
		}

		@Override
		public void run() {
			try {
				activeEditor.getToolManager().enableTool(descriptor.getName());
			} catch (CoreException e) {
				setChecked(false);
				ErrorDialog.openError(null, null, "Error while enabling tool", e.getStatus());
			}
		}

	}

	/**
	 * The currently active editor or null if no editor is currently active.
	 */
	private GraphingEditor activeEditor;



	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		try {
			for (ToolDescriptor descriptor : ToolDescriptor.getAllDescriptors()) {
				ToolAction action = new ToolAction(descriptor);

				// there really must be a better way of initiality selecting
				// this tool. Hardcoding its name is so bad ...
				action.setChecked(descriptor.getName().equals("Serial selection"));

				toolBarManager.add(action);
			}
		} catch (CoreException e) {
			ErrorDialog.openError(null, null, null, e.getStatus());
		}

		toolBarManager.update(true);
	}


	/**
	 * Creates the tools menu for the {@link #activeEditor} or does nothing if
	 * {@link #activeEditor} is null.
	 *
	 * @param manager the menu to create the actions in
	 */
	protected void createToolsMenu(IMenuManager manager) {
		if (activeEditor == null) return;

		for (ToolDescriptor descriptor : activeEditor.getToolManager().getDescriptors())
			manager.add(new ToolAction(descriptor));

		manager.update(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		if (targetEditor instanceof GraphingEditor) {
			activeEditor = (GraphingEditor)targetEditor;
		}
	}

}
