package org.panda.logicanalyzer.ui.internal.editor;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.panda.logicanalyzer.ui.Activator;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;

/**
 * Takes the complete data packet of the currently active editor as input.
 */
public class EditorDataSource implements IDataSource {

	/**
	 * The factory for the selection data source
	 */
	public static class EditorDataSourceFactory implements IDataSourceFactory {


		public IDataSource createSource(Map<String, Object> configuration) throws CoreException {
			IDataPacket selectedData = null;

			IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (activeEditor instanceof GraphingEditor) {
				selectedData = ((GraphingEditor)activeEditor).getAllData();
			}

			if (selectedData == null) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No data available");
				throw new CoreException(status);
			}

			return new SelectionDataSource(selectedData);
		}

	}

	/**
	 * The current selection (can be null)
	 */
	private final IDataPacket data;


	public EditorDataSource(IDataPacket selectedData) {
		this.data = selectedData;
		System.out.println("EditorDataSource");
	}


	public IDataPacket run() throws CoreException {
		return data;
	}

}
