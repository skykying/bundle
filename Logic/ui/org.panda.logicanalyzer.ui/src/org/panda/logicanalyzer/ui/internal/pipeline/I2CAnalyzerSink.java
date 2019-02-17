package org.panda.logicanalyzer.ui.internal.pipeline;

import java.util.Map;

import org.panda.logicanalyzer.core.analyzer.AnalysisResultProperty;
import org.panda.logicanalyzer.core.analyzer.IAnalysisResult;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CAnalyzer;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CAnalyzerConfiguration;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CAnalyzerConfigurationFactory;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CAnalyzerResult;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CSuccessfulAnalysisResult;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CUnsuccessfulAnalysisResult;
import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IDataSinkFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.panda.logicanalyzer.ui.Activator;
import org.panda.logicanalyzer.ui.internal.editor.AnalysisResultViewerEditor;

/**
 * This sink feeds its data into the {@link I2CAnalyzer} and opens the
 * {@link AnalysisResultViewerEditor} afterwards to view the results.
 */
public class I2CAnalyzerSink implements IDataSink {

	/**
	 * The configuration for the analyzer
	 */
	private final I2CAnalyzerConfiguration configuration;

	/**
	 * The factory creating the {@link I2CAnalyzerSink}.
	 */
	public static class I2CAnalyzerSinkFactory implements IDataSinkFactory {

		public IDataSink createSink(Map<String, Object> configuration) throws CoreException {
			if (!(configuration.get("channelA") instanceof Integer)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Channel number of line A is mandatory");
				throw new CoreException(status);
			}
			int lineAChannelNr = (Integer) configuration.get("channelA");
			
			if (!(configuration.get("channelB") instanceof Integer)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Channel number of line B is mandatory");
				throw new CoreException(status);
			}
			int lineBChannelNr = (Integer) configuration.get("channelB");
			
			I2CAnalyzerConfigurationFactory factory = I2CAnalyzerConfigurationFactory.applyDefaults();
			if(factory==null){
				
				showMessage("factory is null");
				return null;
			}
			
			return new I2CAnalyzerSink(factory.setLineA(lineAChannelNr).setLineB(lineBChannelNr).apply());
		}

	}

	public I2CAnalyzerSink(I2CAnalyzerConfiguration configuration) {
		this.configuration = configuration;
	}

	public void accept(IDataPacket packet) throws CoreException {

		final I2CAnalyzerResult result = new I2CAnalyzer(configuration).analyze(packet);

		if (result instanceof I2CUnsuccessfulAnalysisResult) {

			String message = "";
			for (AnalysisResultProperty property : result.getProperties()) {
				message += property.getName() + ": " + property.getValue() + ", ";
			}
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, message);

			throw new CoreException(status);
		} else if (result instanceof I2CSuccessfulAnalysisResult) {

			final IEditorInput input = new IEditorInput() {

				@SuppressWarnings("unchecked")
				public Object getAdapter(Class key) {
					Object adapter = null;

					if (key == IAnalysisResult.class) {
						adapter = result;
					}

					return adapter;
				}

				public String getToolTipText() {
					return getName();
				}

				public IPersistableElement getPersistable() {
					return null;
				}

				public String getName() {
					return result.getTitle();
				}

				public ImageDescriptor getImageDescriptor() {
					return null;
				}

				public boolean exists() {
					return true;
				}
			};

			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					try {
						
						IWorkbench workbench = PlatformUI.getWorkbench();
						IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
						IWorkbenchPage page = window.getActivePage();
						
						page.openEditor(input, AnalysisResultViewerEditor.EDITOR_ID);
					} catch (PartInitException e) {
						ErrorDialog.openError(null, null, null, e.getStatus());
					}
				}

			});
		}
	}

	private static void showMessage(String msg) {
		System.out.println(I2CAnalyzerSink.class.getSimpleName() + " # " + msg);
	}

}
