package org.panda.logicanalyzer.ui.internal.editor;

import java.util.Map;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.panda.logicanalyzer.ui.Activator;

/**
 * The selection data source takes the current editor selection
 * as input.
 */
public class SelectionDataSource implements IDataSource {

	/**
	 * The factory for the selection data source
	 */
	public static class SelectionDataSourceFactory implements IDataSourceFactory {


		public IDataSource createSource(Map<String, Object> configuration) throws CoreException {
			
			IDataPacket selectedData = null;
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			
			IEditorPart activeEditor = window.getActivePage().getActiveEditor();
			if (activeEditor instanceof GraphingEditor) {
				selectedData = ((GraphingEditor)activeEditor).getSelectedData();
			}

			if (selectedData == null) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No selection available");
				throw new CoreException(status);
			}

			return new SelectionDataSource(selectedData);
		}

	}

	/**
	 * The current selection (can be null)
	 */
	private final IDataPacket selectedData;


	public SelectionDataSource(IDataPacket selectedData) {
		this.selectedData = selectedData;
	}


	public IDataPacket run() throws CoreException {
		return selectedData;
	}

}
