package com.lembed.lite.studio.qemu.view;

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

public class SwtQBaseView extends MultiPageEditorPart {

	/**
	 * The ID of this editor
	 */
	public static final String EDITOR_ID = "com.lembed.lite.studio.qemu.view.SwtQBaseView";

	private Action fRemoveAction;
	private IEditorSite _site;
	private static SwtQBaseView instance;
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

		// TODO Auto-generated method stub
		_site = site;
		fRemoveAction = new Action() {

			public void run() {
				// showMessage("fRemoveAction");
			}
		};

		fRemoveAction.setText("Remove Capture");
		fRemoveAction.setToolTipText("unplugin the logic analyzer instance");
		fRemoveAction.setImageDescriptor(
				EmulatorPlugin.imageDescriptorFromPlugin(EmulatorPlugin.PLUGIN_ID, "icons/obj16/debugger.gif"));

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
	public void createPartControl(Composite parent) {
		// this mark can not move to other place
		isOpened = true;

		contributeToActionBars(_site);
		creatPart(parent);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createPages() {

		// createPage0();
	}

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
		//
		// int index = addPage(parent);
		// setPageText(index, "Messages.ConfigEditor_FirstPageText");
		// setPartName("editor.getTitle()");
		// setTitleImage(EmulatorPlugin.getImage(EmulatorPlugin.ICON_FILE));
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		return super.createSite(editor);
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
		// manager.add(fInstallAction);
		manager.add(new Separator());
		manager.add(fRemoveAction);
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

}
