package com.lembed.lite.studio.manager.analysis.memory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.manager.analysis.stack.StackAnalyzerPlugin;
import com.lembed.lite.studio.manager.analysis.stack.BuildMonitor;

/**
 * The Class StackView.
 */
public class MemoryView extends ViewPart{

	private BuildMonitor monitor = null;
	private TableViewer viewer = null;
	
	@Override
	public void createPartControl(Composite parent) {		
		monitor = new BuildMonitor();
		monitor.register();
		viewer = new TableViewer(parent);
		addActionsToMenus(this);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {		
		super.dispose();
		monitor.deRegister();
	}

	
	private void addActionsToMenus(ViewPart view) {

		IContributionManager[] managers = { view.getViewSite().getActionBars().getMenuManager(),
				view.getViewSite().getActionBars().getToolBarManager() };

		for (IContributionManager manager : managers) {
			
			manager.add(createActionSummary());
			manager.add(new Separator());
		}
	}
	
	private Action createActionSummary() {
		Action actionCollapseAll = new Action("Refresh", //$NON-NLS-1$
				StackAnalyzerPlugin.getImageDescriptor(StackAnalyzerPlugin.REFRESH)) {
			@Override
            public void run() {
				//viewer.setInput(StackStore.getEmpty());
				viewer.refresh();
			}
		};

		return actionCollapseAll;
	}
	
}
