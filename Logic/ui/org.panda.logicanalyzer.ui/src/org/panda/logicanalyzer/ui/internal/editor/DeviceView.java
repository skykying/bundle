package org.panda.logicanalyzer.ui.internal.editor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;



import org.panda.logicanalyzer.core.pipeline.IPipeline;
import org.panda.logicanalyzer.ui.Activator;
import org.panda.logicanalyzer.ui.pipeline.PipelineBuilderWizard;
import org.panda.logicanalyzer.ui.pipeline.ProgressMonitorPipelineExecutor;



public class DeviceView extends ViewPart {

	/**
	 * The ID of this view
	 */
	public static final String VIEW_ID = "org.panda.logicanalyzer.ui.ge.DeviceView";

	/**
	 * The composite we're going to create the tools content on
	 */
	private Composite composite;


	private Action fInstallAction;
	private Action fRemoveAction;

	@Override
	public void createPartControl(Composite parent) {
		this.composite = new Composite(parent, SWT.NONE);

		makeActions();
		contributeToActionBars();
	}

	@Override
	public void setFocus() {

	}

	public Composite getComposite() {
		return composite;
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(fInstallAction);
		manager.add(new Separator());
		manager.add(fRemoveAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fInstallAction);
		manager.add(new Separator());
		manager.add(fRemoveAction);
	}


	private void makeActions() {

		fInstallAction = new Action() {

			@Override
			public void run() {
				PipelineBuilderWizard wizard = new PipelineBuilderWizard();
				WizardDialog dialog = new WizardDialog(null, wizard);
				dialog.open();

				IPipeline pipeline = wizard.getPipeline();
				if (pipeline == null) {
					showMessage("pipeline is null");
					return;
				}

				/**
				 * the pipeline will beused in ProgressMonitorPipelineExecutor's run method, to get and
				 * draw the captured datas
				 */
				ProgressMonitorPipelineExecutor executor = new ProgressMonitorPipelineExecutor(pipeline);
				try {
					/**
					 *  run(boolean fork, boolean cancelable, IRunnableWithProgress runnable)
					 *  throws InvocationTargetException,	InterruptedException
					 */
					new ProgressMonitorDialog(null).run(true, false, executor);
				} catch (InvocationTargetException e) {
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, null, e.getCause());
					ErrorDialog.openError(null, null, "Oops", status);
				} catch (InterruptedException e) {
					// who cares
				}
			}
		};

		fInstallAction.setText("New Capture");
		fInstallAction.setToolTipText("build a new logic analyzer capture");
		fInstallAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/expandall.png"));

		// -----
		fRemoveAction = new Action() {

			public void run() {
				showMessage("fRemoveAction");
			}
		};

		fRemoveAction.setText("Remove Capture");
		fRemoveAction.setToolTipText("unplugin the logic analyzer instance");
		fRemoveAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/collapseall.png"));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	private void showMessage(String msg) {
		System.out.println(DeviceView.class.getSimpleName() + " # " + msg);
	}
}
