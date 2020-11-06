package com.lembed.lite.studio.qemu.entry;

import javax.swing.JComponent;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.debug.gdbjtag.emulator.EmulatorPlugin;
import com.lembed.lite.studio.qemu.control.Control;
import com.lembed.lite.studio.qemu.view.DefaultEmulatorStore;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.SwtQBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.VNCDisplayView;
import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;

public class JavaQemuView extends ViewPart{
	
	
	private Composite parent;
	private ViewPart view;
	

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JQemuView view = new JQemuView();
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
				String[] args = {"QEmuRAIDExample",""};
//				try {
//					JQemuMonitor.main(args);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				//main(args);
//				VernacularViewer.main(args);
				
				
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
				Display.getDefault().asyncExec(()->{
					org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
					for(org.eclipse.swt.widgets.Control c : cc) {
						c.dispose();
					}
					
					Rectangle vb = view.getViewSite().getShell().getBounds();
					
					SwingComponentConstructor cxxx = new SwingComponentConstructor() {


						@Override
						public JComponent createSwingComponent() {
							
							VNCDisplayView view = new VNCDisplayView(null);
							//JQemuComponent view = new JQemuComponent();
							
							view.setBounds(vb.x, vb.y, vb.width, vb.height);
							return view;
						}
					};
					
					Platform.createComposite(parent, view.getSite().getShell().getDisplay(),cxxx);
					
					parent.redraw();
					parent.requestLayout();
				});
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
