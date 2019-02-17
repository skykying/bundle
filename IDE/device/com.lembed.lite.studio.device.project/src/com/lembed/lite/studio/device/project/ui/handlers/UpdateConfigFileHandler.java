package com.lembed.lite.studio.device.project.ui.handlers;

import java.util.Map;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.enums.EFileCategory;
import com.lembed.lite.studio.device.core.enums.EFileRole;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.impl.LiteProjectStorage;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;

public class UpdateConfigFileHandler extends AbstractHandler implements IElementUpdater {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}

		IStructuredSelection sel = (IStructuredSelection) selection;
		for (Object obj : sel.toArray()) {

			IFile file = ProjectUtils.getLiteFileResource(obj);
			String dstFile = file.getProjectRelativePath().toString();

			ICpFileInfo fi = ProjectUtils.getCpFileInfo(file);
			ICpFile f = fi.getFile();
			String srcFile = fi.getAbsolutePath(f.getName());

			EFileRole role = fi.getRole();
			if (role == EFileRole.CONFIG || role == EFileRole.COPY) {
				int index = -1;
				EFileCategory cat = fi.getCategory();
				if (cat.isHeader() || cat.isSource()) {
					String baseSrc = DeviceUIUtils.extractBaseFileName(srcFile);
					String baseDst = DeviceUIUtils.extractBaseFileName(dstFile);
					int len = baseSrc.length() + 1;
					if (baseDst.length() > len) {
						String instance = baseDst.substring(len);
						try {
							index = Integer.decode(instance);
						} catch (NumberFormatException e) {
							// do nothing, use -1
						}
					}
				}
				try {
					int bCopied = ProjectUtils.copyFile(file.getProject(), srcFile, dstFile, index, null, true);
					if (bCopied == 1) {
						// do the version update and save it in the .cproject
						// file
						fi.setVersion(f.getVersion());
						ILiteProject liteProject = CpProjectPlugIn.getLiteProjectManager().getLiteProject(file.getProject());
						LiteProjectStorage projectStorage = liteProject.getProjectStorage();
						projectStorage.setConfigFileVersion(dstFile, f.getVersion());
						projectStorage.save(CoreModel.getDefault().getProjectDescription(file.getProject()));
						liteProject.save();
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ISelection selection = selectionService.getSelection("org.eclipse.ui.navigator.ProjectExplorer"); //$NON-NLS-1$
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			if (sel.size() == 1) {
				Object obj = sel.getFirstElement();
				IFile file = ProjectUtils.getLiteFileResource(obj);
				ICpFileInfo fi = ProjectUtils.getCpFileInfo(file);
				if (fi == null || fi.getFile() == null) {
					return;
				}
				int versionDiff = fi.getVersionDiff();
				String versionText = " (" + fi.getVersion() + " -> " + fi.getFile().getVersion() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (versionDiff < 0) {
					element.setText(Messages.UpdateConfigFileHandler_Upgrade + file.getName() + versionText);
				} else if (versionDiff > 2) {
					element.setText(Messages.UpdateConfigFileHandler_Downgrade + file.getName() + versionText);
				}
			} else if (sel.size() > 1) {
				element.setText(Messages.UpdateConfigFileHandler_UpdateSelectedFiles);
			}
		}
	}

}
