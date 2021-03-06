/*******************************************************************************
 * Copyright (c) 2016 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.pack.jobs;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.osgi.util.NLS;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.pack.Messages;
import com.lembed.lite.studio.device.pack.utils.PackInstallerUtils;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;

/**
 * The Pack Removing Job. This job deletes the pack's folder and
 * deletes the .pack file in the .Download folder if necessary.
 */
public class CpPackRemoveJob extends CpPackJob {

	protected ICpPack fPack;
	protected boolean fDelete;

	/**
	 * @param name The Job's name
	 * @param installer The pack installer
	 * @param pack the pack to remove
	 * @param delete true if the .pack file in the .Download folder should also be deleted
	 */
	public CpPackRemoveJob(String name, ICpPackInstaller installer, ICpPack pack, boolean delete) {
		super(name, installer, pack.getId());
		fPack = pack;
		fDelete = delete;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (fPack == null) {
			return Status.OK_STATUS;
		}

		IPath installedDir;
		if (fPack.getPackState() == PackState.ERROR) {
			fJobId = fPack.getTag();
			installedDir = new Path(fPack.getFileName());
		} else {
			fJobId = fPack.getId();
			installedDir = new Path(fPack.getInstallDir(CpPlugIn.getPackManager().getCmsisPackRootDirectory()));
		}
		monitor.setTaskName(Messages.CpPackRemoveJob_RemovingPack + fJobId);

		IPath downloadPackFilePath = new Path(CpPlugIn.getPackManager().getCmsisPackDownloadDir())
				.append(fJobId + CmsisConstants.EXT_PACK);
		File downloadPackFile = downloadPackFilePath.toFile();
		IPath downloadPdscFilePath = new Path(CpPlugIn.getPackManager().getCmsisPackDownloadDir())
				.append(fJobId + CmsisConstants.EXT_PDSC);
		File downloadPdscFile = downloadPdscFilePath.toFile();

		SubMonitor progress = SubMonitor.convert(monitor, DeviceUIUtils.countFiles(installedDir.toFile()));
		progress.setTaskName(NLS.bind(Messages.CpPackRemoveJob_DeletingFilesFromFolder, installedDir.toOSString()));
		DeviceUIUtils.deleteFolderRecursiveWithProgress(installedDir.toFile(), progress);

		if (fDelete) {
			downloadPackFile.delete();
			downloadPdscFile.delete();
		}

		fResult.setPack(fPack);

		// If the deleted pack is not an error pack, change the pack state
		if (fPack.getPackState() != PackState.ERROR) {
			ICpPack newPack = null;
			if (downloadPackFile.exists() && downloadPdscFile.exists()) {
				fPack.setPackState(PackState.DOWNLOADED);
				fPack.setFileName(downloadPdscFile.getAbsolutePath());
				fResult.setNewPack(fPack);
			} else {
				fPack.setPackState(PackState.AVAILABLE);
				newPack = PackInstallerUtils.loadLatesPack(fPack);
				fResult.setNewPack(newPack);
			}
		}

		fResult.setSuccess(true);

		progress.done();

		if (fDelete || !downloadPackFile.exists() || !downloadPdscFile.exists()) {
			fPackInstaller.jobFinished(fJobId, LiteEvent.PACK_DELETE_JOB_FINISHED, fResult);
		} else {
			fPackInstaller.jobFinished(fJobId, LiteEvent.PACK_REMOVE_JOB_FINISHED, fResult);
		}

		return Status.OK_STATUS;
	}

}
