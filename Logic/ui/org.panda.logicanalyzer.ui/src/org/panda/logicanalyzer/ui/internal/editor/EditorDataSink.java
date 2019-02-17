package org.panda.logicanalyzer.ui.internal.editor;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.panda.logicanalyzer.ui.IDataPacketEditorInput;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IDataSinkFactory;
import org.panda.logicanalyzer.core.pipeline.IPipeline;

/**
 * The editor data sink opens an editor to display the data packet.
 *
 *
 *
 */
public class EditorDataSink implements IDataSink {

	public EditorDataSink() {
		super();
		System.out.println("EditorDataSink");
	}

	/**
	 * Factory for the {@link EditorDataSink}.
	 *
	 *
	 */
	public static class EditorDataSinkFactory implements IDataSinkFactory {

		public IDataSink createSink(Map<String, Object> configuration) throws CoreException {
			return new EditorDataSink();
		}

	}

	public void accept(final IDataPacket packet) throws CoreException {
		final IEditorInput editorInput = new IDataPacketEditorInput() {

			@SuppressWarnings("unchecked")
			public Object getAdapter(Class adapter) {
				return null;
			}

			public String getToolTipText() {
				return getName();
			}


			public IPersistableElement getPersistable() {
				return null;
			}


			public String getName() {
				return "Some Data";
			}


			public ImageDescriptor getImageDescriptor() {
				return null;
			}


			public boolean exists() {
				return true;
			}


			public IPipeline getPipeline() {
				return null;
			}


			public IDataPacket getPacket() {
				return packet;
			}

		};

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				try {
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
					IWorkbenchPage page = window.getActivePage();
					
					page.openEditor(editorInput, GraphingEditor.EDITOR_ID);
				} catch (PartInitException e) {
					ErrorDialog.openError(null, null, null, e.getStatus());
				}
			}

		});
	}
}
