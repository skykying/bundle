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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.panda.logicanalyzer.core.pipeline.IPipeline;
import org.panda.logicanalyzer.ui.Activator;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.pipeline.PipelineBuilderWizard;
import org.panda.logicanalyzer.ui.pipeline.ProgressMonitorPipelineExecutor;

/**
 * The tool inspector view serves as a canvas for {@link ISelectionBasedTool}s
 * that need a UI.
 */
public class ToolInspectorView extends ViewPart {

	/**
	 * The ID of this view
	 */
	public static final String VIEW_ID = "org.panda.logicanalyzer.ui.ge.toolInspectorView";

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
		// TODO Auto-generated method stub

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

			}
		};

		fInstallAction.setText("New Capture");
		fInstallAction.setToolTipText("build a new logic analyzer capture");
		fInstallAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/expandall.png"));

		// -----
		fRemoveAction = new Action() {

			public void run() {

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
}
