package com.lembed.lite.studio.qemu.view;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.MultiPageEditorPart;
import com.lembed.lite.studio.qemu.control.swt.ConfigurationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.VMConfigurationControl;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.swt.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.view.internal.swt.CPUView;
import com.lembed.lite.studio.qemu.view.internal.swt.ConfigurationView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.HardDiskView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.PhysicalDriveView;
import com.lembed.lite.studio.qemu.view.internal.swt.USBView;
import com.lembed.lite.studio.qemu.view.internal.swt.VNCDisplayView;
import com.lembed.lite.studio.ui.awt.Platform;
import com.lembed.lite.studio.ui.awt.SwingComponentConstructor;

public class SwtQBaseViewPlugin  {


	
	public ImmutablePair<Integer,String> createPage0(MultiPageEditorPart editor, Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		VNCDisplayView view = new VNCDisplayView(null);
		createComponent(editor,composite,view);
	
		int index = editor.addPage(composite);
		String title = view.getTitle();
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
	}

	public static ImmutablePair<Integer,String> createVMControl(MultiPageEditorPart editor , Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	

		
		EmulationControl emulationControl = new EmulationControl();
		EmulatorQemuMachineControl fileControl = new EmulatorQemuMachineControl();
		VMConfigurationControl vmc = new VMConfigurationControl(emulationControl, fileControl);
		
		for(DeviceBaseView dv : vmc.getViews()) {
			createComponent(editor,composite,dv);
		}
	
		int index = editor.addPage(composite);
		String title = "ggr";
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
	}
	

	
	public ImmutablePair<Integer, String> createNetworkPage(MultiPageEditorPart editor , Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		JScrollPane jscp = new JScrollPane();
		
//		NetworkWorkerView view = new NetworkWorkerView(null,0);
		LastUsedFolderModel lastUsedFolderModel = new LastUsedFolderModel();
		LastUsedFileModel lastUsedFileModel = new LastUsedFileModel();
		ConfigurationControl configurationControl = new ConfigurationControl(lastUsedFolderModel, lastUsedFileModel);
		
		ConfigurationView view = new ConfigurationView();
		view = (ConfigurationView) configurationControl.getViews().get(0);
		
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        
        createComponentScroll(editor,composite,jscp);
	
		int index = editor.addPage(composite);
		String title = view.getTitle();
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
	}
	
	public ImmutablePair<Integer, String> createPhysicalDriveView(MultiPageEditorPart editor , Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		
		JScrollPane jscp = new JScrollPane();
		PhysicalDriveView view = new PhysicalDriveView(null,0);
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(editor,composite,jscp);
	
		int index = editor.addPage(composite);
		String title = view.getTitle();
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
	}
	
	
	public ImmutablePair<Integer, String> createUSBPage(MultiPageEditorPart editor , Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		JScrollPane jscp = new JScrollPane();
	
		
		USBView view = new USBView(null);
		
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(editor,composite,jscp);
	
		int index = editor.addPage(composite);
		String title = view.getTitle();
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
		
	}//HardDiskView
	
	public ImmutablePair<Integer,String> createHardDiskView(MultiPageEditorPart editor , Composite parent ) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));	
		JScrollPane jscp = new JScrollPane();
	
		
		HardDiskView view = new HardDiskView("","","","");
		
        jscp.setViewportView(view);
        jscp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jscp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscp.revalidate();
        
        createComponentScroll(editor,composite,jscp);
	
		int index = editor.addPage(composite);
		String title = view.getTitle();
		
		ImmutablePair <Integer, String> pair = new ImmutablePair <>(index, title);
		return pair;
	}
	
	private static void createComponent(MultiPageEditorPart editor,Composite parent, final JPanel jp) {
		Display.getDefault().asyncExec(()->{
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for(org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}
			
		
			Rectangle vb = editor.getSite().getShell().getBounds();
			
			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {


				@Override
				public JComponent createSwingComponent() {
					
					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};
			
			Platform.createComposite(parent, editor.getSite().getShell().getDisplay(),embeddedComposite);
			
			parent.redraw();
			parent.requestLayout();
		});
	}
	
	private void createComponentScroll(MultiPageEditorPart editor, Composite parent, final JScrollPane jp) {
		Display.getDefault().asyncExec(()->{
			org.eclipse.swt.widgets.Control[] cc = parent.getChildren();
			for(org.eclipse.swt.widgets.Control c : cc) {
				c.dispose();
			}
			
		
			Rectangle vb = editor.getSite().getShell().getBounds();
			
			SwingComponentConstructor embeddedComposite = new SwingComponentConstructor() {


				@Override
				public JComponent createSwingComponent() {
					
					jp.setBounds(vb.x, vb.y, vb.width, vb.height);
					return jp;
				}
			};
			
			Platform.createComposite(parent, editor.getSite().getShell().getDisplay(),embeddedComposite);
			
			parent.redraw();
			parent.requestLayout();
		});
	}
	

}
