package com.lembed.lite.studio.qemu.view;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.lembed.lite.studio.debug.gdbjtag.emulator.EmulatorPlugin;
import com.lembed.lite.studio.qemu.view.internal.swt.CPUView;
import com.lembed.lite.studio.qemu.view.internal.swt.HardDiskView;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkWorkerView;
import com.lembed.lite.studio.qemu.view.internal.swt.PhysicalDriveView;
import com.lembed.lite.studio.qemu.view.internal.swt.USBView;
import com.lembed.lite.studio.qemu.view.internal.swt.VNCDisplayView;
import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;

public class SwtQBaseView extends MultiPageEditorPart {

	/**
	 * The ID of this editor
	 */
	public static final String EDITOR_ID = "com.lembed.lite.studio.qemu.view.SwtQBaseView";

	private Action fRemoveAction;
	private Action fResetAction;
	private Action fSaveAction;
	private Action fStartAction;
	private Action fStopAction;
	
	
	
	
	private IEditorSite _site;
	private static boolean isOpened = false;

	public static boolean isOpened() {
		return isOpened;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		_site = site;

		initActions();
	}

	
	private void initActions() {
		fRemoveAction = new Action() {
			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fRemoveAction.setText("Remove Capture");
		fRemoveAction.setToolTipText("remove");
		fRemoveAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));
		
		fResetAction = new Action() {
			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fResetAction.setText("Reset");
		fResetAction.setToolTipText("reset all configure");
		fResetAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));
		
		
		fStartAction = new Action() {
			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fStartAction.setText("Start");
		fStartAction.setToolTipText("Start emulator");
		fStartAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));
		
		fStopAction = new Action() {
			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fStopAction.setText("Stop");
		fStopAction.setToolTipText("Stop emulator");
		fStopAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));
		
		fSaveAction = new Action() {
			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fSaveAction.setText("Save");
		fSaveAction.setToolTipText("save configure");
		fSaveAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));
	}
	
	private void contributeToActionBars(IEditorSite site) {
		IActionBars bars = site.getActionBars();
		// registerLocalPullDown(bars.getMenuManager());
		registerLocalToolBar(bars.getToolBarManager());
	}

	protected void registerLocalPullDown(IMenuManager manager) {
		// manager.add(fInstallAction);
		manager.add(new Separator());
		manager.add(fRemoveAction);
	}

	protected void registerLocalToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(fStartAction);
		manager.add(fStopAction);
		manager.add(fSaveAction);
		manager.add(fResetAction);
		manager.add(fRemoveAction);
	}
	
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		isOpened = false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createPages() {
		// this mark can not move to other place
		isOpened = true;
		contributeToActionBars(_site);
		
		 createUSBPage();
		 createPage0();
		 createPage1();
		 
		 createNetworkPage();
		 createPhysicalDriveView();
		 createHardDiskView();
		 setActivePage(0);
	}
	
	
	private void createPage0() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		VNCDisplayView view = new VNCDisplayView(null);
		createComponent(composite,view);
	
		int index = addPage(composite);
		setPageText(index, "VNC");
		setPartName("Messages");
		
	}

	private void createPage1() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		CPUView view = new CPUView(null);
		createComponent(composite,view);
	
		int index = addPage(composite);
		setPageText(index, "CPU");
		setPartName("Messages");
		
	}
	
	private void createNetworkPage() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		JScrollPane jscp = new JScrollPane();
		NetworkWorkerView view = new NetworkWorkerView(null,0);
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(composite,jscp);
	
		int index = addPage(composite);
		setPageText(index, "Network");
		setPartName("Messages");
	}
	
	private void createPhysicalDriveView() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		JScrollPane jscp = new JScrollPane();
		PhysicalDriveView view = new PhysicalDriveView(null,0);
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(composite,jscp);
	
		int index = addPage(composite);
		setPageText(index, "PhysicalDrive");
		setPartName("Messages");
	}
	
	
	private void createUSBPage() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		JScrollPane jscp = new JScrollPane();
	
		
		USBView view = new USBView(null);
		
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(composite,jscp);
	
		int index = addPage(composite);
		setPageText(index, "USB");
		setPartName("Messages");
		
	}//HardDiskView
	
	private void createHardDiskView() {
    	Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		JScrollPane jscp = new JScrollPane();
	
		
		HardDiskView view = new HardDiskView("","","","");
		
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(composite,jscp);
	
		int index = addPage(composite);
		setPageText(index, "HardDisk");
		setPartName("Messages");
		
	}//HardDiskView
	
	protected void creatPart(Composite parent) {

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setItems("Alpha", "Bravo", "Charlie");

		Rectangle clientArea = parent.getClientArea();
		combo.setBounds(clientArea.x, clientArea.y, 200, 200);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("widgetSelected");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widgetDefaultSelected");
			}

		});
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		return super.createSite(editor);
	}



	public static void openEditor(IemultorStore store) {

		final IEditorInput input = new IEditorInput() {

			@SuppressWarnings("unchecked")
			public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
				Object adapter = null;

				if (key == IemultorStore.class) {
					adapter = store;
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
				return store.getTitle();
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

					if (!SwtQBaseView.isOpened()) {
						page.openEditor(input, SwtQBaseView.EDITOR_ID);
					}
				} catch (PartInitException e) {
					ErrorDialog.openError(null, null, null, e.getStatus());
				}
			}

		});
	}
	
	
	private void createComponent(Composite parent, final JPanel jp) {
		Display.getDefault().asyncExec(()->{
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for(org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}
			
		
			Rectangle vb = this.getSite().getShell().getBounds();
			
			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {


				@Override
				public JComponent createSwingComponent() {
					
					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};
			
			Platform.createComposite(parent, this.getSite().getShell().getDisplay(),embeddedComposite);
			
			parent.redraw();
			parent.requestLayout();
		});
	}
	
	private void createComponentScroll(Composite parent, final JScrollPane jp) {
		Display.getDefault().asyncExec(()->{
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for(org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}
			
		
			Rectangle vb = this.getSite().getShell().getBounds();
			
			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {


				@Override
				public JComponent createSwingComponent() {
					
					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};
			
			Platform.createComposite(parent, this.getSite().getShell().getDisplay(),embeddedComposite);
			
			parent.redraw();
			parent.requestLayout();
		});
	}
	
	
	private void te(Composite parent, JPanel jp) {
		Display.getDefault().asyncExec(()->{
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for(org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}
			
		
			Rectangle vb = this.getSite().getShell().getBounds();
			
			SwingComponentConstructor cxxx = new SwingComponentConstructor() {


				@Override
				public JComponent createSwingComponent() {
					
					VNCDisplayView view = new VNCDisplayView(null);
					//JQemuComponent view = new JQemuComponent();
					
					view.setBounds(vb.x, vb.y, vb.width, vb.height);
					return view;
				}
			};
			
			Platform.createComposite(parent, this.getSite().getShell().getDisplay(),cxxx);
			
			parent.redraw();
			parent.requestLayout();
		});
	}
	
	
	private void  closeEditor() {
		Display.getDefault().asyncExec(() -> {
			this.getEditorSite().getPage().closeEditor(this, true);
		});
	}

}
