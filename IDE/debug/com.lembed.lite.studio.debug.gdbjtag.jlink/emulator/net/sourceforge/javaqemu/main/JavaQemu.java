package net.sourceforge.javaqemu.main;

import net.sourceforge.javaqemu.control.Control;
import net.sourceforge.javaqemu.view.JQemuComponent;
import net.sourceforge.javaqemu.view.JQemuView;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;
import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;

public class JavaQemu extends ViewPart{
	
	
	private Composite parent;
	private ViewPart view;
	private Frame xframe;
	

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
				DevicePlugin.getImageDescriptor(DevicePlugin.icon_refresh)) {
			@Override
			public void run() {
				
			}
			
		};
		
		return actionCollapseAll;
    }
    
	private Action createActionTree() {
		Action actionCollapseAll = new Action("run", //$NON-NLS-1$
				DevicePlugin.getImageDescriptor(DevicePlugin.icon_refresh)) {
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
							JQemuComponent view = new JQemuComponent();
							
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
		
//        JQemuView view = new JQemuView();

		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
	    xframe = SWT_AWT.new_Frame(composite);
        xframe.setLayout(new BorderLayout());
//        xframe.add(new JScrollPane(view));
        xframe.pack();
        xframe.setLocationRelativeTo(null); 
        xframe.setVisible(true);
        
//        Control control = new Control(view);
//        control.starts();
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
