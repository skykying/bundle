package com.lembed.lite.studio.qemu.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.debug.gdbjtag.emulator.EmulatorPlugin;
import com.lembed.lite.studio.qemu.control.Control;
import com.shinyhut.vernacular.VncView;

public class JavaQemuView extends ViewPart{
	
	
	private Composite parent;
	private ViewPart view;
	

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JContainerView view = new JContainerView();
                Control control = new Control(view);

                control.starts();
            }
        });
    }
    
    private Action createActionMonitor() {
		Action actionCollapseAll = new Action("monitor", //$NON-NLS-1$
				EmulatorPlugin.getInstance().getImageDescriptor(EmulatorPlugin.icon_refresh)) {
			@Override
			public void run() {
				SwtQBaseView.openEditor(new DefaultEmulatorStore());
			}
			
		};
		
		return actionCollapseAll;
    }
    
	private Action createActionTree() {
		Action actionCollapseAll = new Action("run", //$NON-NLS-1$
				EmulatorPlugin.getInstance().getImageDescriptor(EmulatorPlugin.icon_refresh)) {
			@Override
			public void run() {
				VncView.createVNCViewer(view, parent);
			}
		};

		return actionCollapseAll;
	}


	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		view = this;
		addActionsToMenus(this);
	}

	
	private void addActionsToMenus(ViewPart view) {

		IContributionManager[] managers = { view.getViewSite().getActionBars().getMenuManager(),
		                                    view.getViewSite().getActionBars().getToolBarManager()
		                                  };

		for (IContributionManager manager : managers) {

			manager.add(createActionTree());
			manager.add(createActionMonitor());
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}
