package com.lembed.lite.studio.qemu.view;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.debug.gdbjtag.emulator.EmulatorPlugin;
import com.lembed.lite.studio.qemu.control.Control;
import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;
import com.shinyhut.vernacular.VernacularViewer;
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
    
    public static void createVMView(ViewPart cview, Composite composite) {

		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		JScrollPane jscp = new JScrollPane();

		JSwtQemuView view = new JSwtQemuView();

		jscp.setViewportView(view);
		jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscp.revalidate();

		createComponentScroll(cview, composite, jscp);
		com.lembed.lite.studio.qemu.control.swt.Control control = new com.lembed.lite.studio.qemu.control.swt.Control(view);

         control.starts();
	}

	public static void createComponentScroll(ViewPart view, Composite parent, final JScrollPane jp) {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(() -> {
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for (org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}

			org.eclipse.swt.graphics.Rectangle vb = view.getSite().getShell().getBounds();

			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {

				@Override
				public JComponent createSwingComponent() {

					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};

			Platform.createComposite(parent, view.getSite().getShell().getDisplay(), embeddedComposite);

			parent.redraw();
			parent.requestLayout();
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
//				VncView.createVNCViewer(view, parent);
				createVMView(view, parent);
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
