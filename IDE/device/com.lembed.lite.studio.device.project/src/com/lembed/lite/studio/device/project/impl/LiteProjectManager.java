/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *
 * Resource change listener snippet is taken from:
 * https://www.eclipse.org/articles/Article-Resource-deltas/resource-deltas.html
 * *******************************************************************************/

package com.lembed.lite.studio.device.project.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.commands.ICommandService;

import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.core.data.CpPack;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.events.LiteEventProxy;
import com.lembed.lite.studio.device.core.events.LitePairEvent;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.CpVariableResolver;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.jobs.UpdaterJob;
import com.lembed.lite.studio.device.project.utils.BufferPrinter;
import com.lembed.lite.studio.device.project.utils.ConfigurationUtils;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;
import com.lembed.lite.studio.device.utils.Utils;

/**
 * Class that manages lite projects and their associations to ICproject and
 * IProject
 */
public class LiteProjectManager extends LiteEventProxy implements IResourceChangeListener, IExecutionListener {

	private LiteSetupParticipant liteSetupParticipant = null;
	private Map<String, ILiteProject> liteProjects = Collections.synchronizedMap(new HashMap<>());
	private Map<String, ILiteProject> fUpdateQueue = Collections.synchronizedMap(new HashMap<>());
	private Set<String> fMissingPacks = Collections.synchronizedSet(new HashSet<>());
	boolean fMissingPacksQueryInProgress = false;
	// to prevent multiple dialogs at a time

	private boolean executionListenerRegistered = false;
	boolean postponeRefresh = false;

	private boolean execit = false;

	/**
	 * Default constructor
	 */
	public LiteProjectManager() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
		CpPlugIn.addLiteListener(this);
	}

	/**
	 * Clears internal collection of the projects
	 */
	public void destroy() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(this);
		CpPlugIn.removeLiteListener(this);
		if (executionListenerRegistered) {
			ICommandService commandService = CpPlugInUI.getCommandService();
			if (commandService != null) {
				commandService.removeExecutionListener(this);
			}
		}

		synchronized (fUpdateQueue) {
			fUpdateQueue.clear();
		}

		synchronized (liteProjects) { // do it as atomic operation
			for (ILiteProject liteProject : liteProjects.values()) {
				liteProject.destroy();
			}
			liteProjects.clear();
		}
	}

	private void registerExecutionListener() {
		if (executionListenerRegistered) {
			return;
		}
		ICommandService commandService = CpPlugInUI.getCommandService();
		if (commandService != null) {
			commandService.addExecutionListener(this);
			executionListenerRegistered = true;
		}
	}

	/**
	 * Initializes LiteSetupParticipant does nothing if already initialized
	 * used in LiteSetupHook.java
	 */
	public void initLiteSetupParticipant() {
		if (liteSetupParticipant == null) {
			liteSetupParticipant = new LiteSetupParticipant();
		}
	}

	/**
	 * Triggers project index update and notifies that project is updated
	 *
	 * @param project
	 *            IProject associated with an RTE project
	 */
	public void updateIndex(IProject project) {
		if (liteSetupParticipant != null) {
			liteSetupParticipant.updateIndex(project);
		}
		emitLiteEvent(LiteEvent.PROJECT_UPDATED, getLiteProject(project));
	}

	/**
	 * Returns ILiteProject associated for given name
	 *
	 * @param project
	 *            IProject object associated with ILiteProject
	 * @return ILiteProject
	 */
	synchronized public ILiteProject getLiteProject(String name) {
		return liteProjects.get(name);
	}

	/**
	 * Returns IRteProject associated with given IRteProject if any
	 *
	 * @param project
	 *            IProject object associated with IRteProject
	 * @return IRteProject
	 */
	public ILiteProject getLiteProject(IProject project) {
		if (project != null) {
			return getLiteProject(project.getName());
		}
		return null;
	}

	public Collection<ILiteProject> getLiteProjects() {
		return liteProjects.values();
	}

	/**
	 * Creates or returns existing ILiteProject associated with given ILiteProject
	 *
	 * @param project
	 *            IProject object to be associated with ILiteProject
	 * @return existing ILiteProject if exists or new one
	 */
	synchronized public ILiteProject createLiteProject(IProject project) {
		ILiteProject liteProject = getLiteProject(project);
		if (liteProject == null) {
			liteProject = new LiteProject(project);
			addLiteProject(liteProject);
			registerExecutionListener(); // ensure refresh action is attached
		}
		return liteProject;
	}

	/**
	 * Adds lite project to the internal collection
	 *
	 * @param liteProject
	 *            IRteProject to add
	 */
	synchronized public void addLiteProject(ILiteProject liteProject) {
		if (liteProject != null) {
			liteProjects.put(liteProject.getName(), liteProject);
			emitLiteEvent(LiteEvent.PROJECT_ADDED, liteProject);
		}
	}

	/**
	 * Removes lite project from internal collection
	 *
	 * @param liteProject
	 *            ILiteProject to remove
	 */
	synchronized public void deleteLiteProject(ILiteProject liteProject) {
		if (liteProject != null) {
			liteProjects.remove(liteProject.getName());
			liteProject.destroy();
			emitLiteEvent(LiteEvent.PROJECT_REMOVED, liteProject);
		}
	}

	/**
	 * Renames LITE project and updates collection
	 *
	 * @param liteProject
	 *            ILiteProject to remove
	 */
	public void renameLiteProject(String oldName, String newName) {
		ILiteProject liteProject = getLiteProject(oldName);
		if (liteProject != null) {
			synchronized (liteProjects) { // do it as atomic operation
				liteProject.setName(newName);
				liteProjects.remove(oldName);
				liteProjects.put(newName, liteProject);
				emitLiteEvent(LiteEvent.PROJECT_UPDATED, liteProject);
			}
		}
	}

	@Override
	public void handle(LiteEvent event) {
		switch (event.getTopic()) {
		case LiteEvent.PACKS_RELOADED:
		case LiteEvent.PACKS_UPDATED:
			refreshProjects();
			break;
		case LiteEvent.GPDSC_CHANGED:
			refreshGpdscProjects((String) event.getData());
			break;
		case LiteEvent.PRE_IMPORT:
			postponeRefresh = true;
			break;
		case LiteEvent.CONFIG_INFO_REQUEST:
			handleConfiguration(event);
			break;
		case LiteEvent.POST_IMPORT:
			postponeRefresh = false;
			IProject project = (IProject) event.getData();
			if (project != null) {
				ILiteProject liteProject = getLiteProject(project);
				if (liteProject != null) {
					liteProject.refresh();
				}
			}
		default:
			return;
		}
	}

	/**
	 * response the LITE editor request
	 * @param event LiteEvent
	 */
	void handleConfiguration(LiteEvent event) {
		LitePairEvent objs = (LitePairEvent) event.getData();
		IProject project = (IProject) objs.getFirst();
		if (!(project instanceof IProject)) {
			return;
		}
		ICpConfigurationInfo info = (ICpConfigurationInfo) objs.getSecond();
		if (!(info instanceof ICpConfigurationInfo)) {
			return;
		}

		ILiteProject liteProject = getLiteProject(project);
		if (liteProject == null) {
			return;
		}

		log(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 1");
		// remove old project file
		UpdaterJob rJob = new UpdaterJob();
		rJob.setLiteProject(liteProject).setFlag(UpdaterJob.UPDATE_RESOURCES_RM);
		rJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				// TODO Auto-generated method stub
				super.done(event);
			}

		});
		rJob.schedule();

		ILiteConfiguration liteConf =  ConfigurationUtils.updateConfiguration(info);
		if ((liteConf != null) && (liteProject != null)) {
			liteProject.setLiteConfiguration(liteConf);
		}

		// response the LITE configuration file context
		try {
			BufferPrinter print = ConfigurationUtils.generateConfigurationFile(liteProject);
			CpPlugIn.getDefault().emitLiteEvent(LiteEvent.CONFIG_INFO_RESPONSE, print.getBuffer());
			print.close();
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}

		log(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2");
		//add new project files
		UpdaterJob aJob = new UpdaterJob();
		aJob.setLiteProject(liteProject).setFlag(UpdaterJob.UPDATE_RESOURCES_ADD);
		aJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				// TODO Auto-generated method stub
				super.done(event);
			}

		});

		aJob.schedule();
	}

	protected void refreshProjects() {
		synchronized (liteProjects) {
			for (ILiteProject liteProject : liteProjects.values()) {
				if (liteProject.getProject().isOpen()) {
					liteProject.refresh();
				}
			}
		}
	}

	protected void refreshGpdscProjects(String file) {
		synchronized (liteProjects) {
			for (ILiteProject liteProject : liteProjects.values()) {
				if (liteProject.getProject().isOpen()) {
					ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
					if (liteConf != null && liteConf.isGeneratedPackUsed(file)) {
						liteProject.refresh();
					}
				}
			}
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// consider only POST_CHANGE events
		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}

		IResourceDelta resourseDelta = event.getDelta();
		IResourceDeltaVisitor deltaVisitor = new IResourceDeltaVisitor() {
			@Override
			public boolean visit(IResourceDelta delta) {
				IResource resource = delta.getResource();
				int type = resource.getType();
				if (type == IResource.ROOT) {
					// workspace => visit children
					return true;
				}

				IProject project = resource.getProject();
				// findSourceFiles(project);

				// only consider LITE projects
				if (!LiteProjectNature.hasLiteNature(project)) {
					return false; // skip children
				}

				int kind = delta.getKind();
				int flags = delta.getFlags();

				/**
				 * handle project rename and remove action
				 */
				if (type == IResource.PROJECT && kind == IResourceDelta.REMOVED) {
					ILiteProject liteProject = getLiteProject(project);
					if (liteProject == null) {
						return false; // not an LITE project or not loaded => ignore
					}

					if ((flags & IResourceDelta.MOVED_TO) == IResourceDelta.MOVED_TO) {
						// renamed
						IPath newPath = delta.getMovedToPath();
						if (newPath == null) {
							return false;
						}

						// rename project
						String newName = newPath.lastSegment();
						renameLiteProject(project.getName(), newName);
						return false;
					}

					// removed
					deleteLiteProject(liteProject);
					return false;
				}

				if (type == IResource.PROJECT) {
					// is project renamed?
					if (kind == IResourceDelta.REMOVED) {
						if ((flags & IResourceDelta.CHANGED) != 0) {
							return false;
						} else if ((flags & IResourceDelta.MOVED_TO) != 0) {
							return false;
						}
						return true;
					}

				} else if (type == IResource.FILE) {
					// is resource changed?
					if (kind != IResourceDelta.CHANGED) {
						return true;
					}

					// is content changed?
					if ((flags & IResourceDelta.CONTENT) == 0) {
						return true;
					}
				} else if (type == IResource.FOLDER) {

					/**
					 * some symbols and include path actions
					 */
					processResourceActions(delta);
				}
				return true;
			}
		};

		try {
			resourseDelta.accept(deltaVisitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void processResourceActions(IResourceDelta delta) {
		IResource resource = delta.getResource();
		int kind = delta.getKind();
		int flags = delta.getFlags();
		IProject project = resource.getProject();

		if (kind == IResourceDelta.ADDED) {
			//log("add " + delta.getProjectRelativePath());
		}
		if (kind == IResourceDelta.CHANGED) {
			//log("changed "+ delta.getProjectRelativePath());
		}

		if (kind == IResourceDelta.REMOVED) {
			if ((flags & IResourceDelta.MOVED_TO) == IResourceDelta.MOVED_TO) {
				IPath newPath = delta.getMovedToPath();
				//log(" removed  TO " + newPath.toString());
			}
		}

		/**
		 * remove the include paths
		 */
		if ((flags & IResourceDelta.MOVED_FROM) == IResourceDelta.MOVED_FROM) {
			IPath oldPath = delta.getMovedFromPath().makeRelative().removeFirstSegments(1);
			if (!checkExists(project, oldPath)) {
				return;
			}

			Map<String, List<String>> includes = ConfigurationUtils.collectBuildToolOptionValues(project, IOption.INCLUDE_PATH);
			for (List<String> incs : includes.values()) {
				for (String inc : incs) {
					IPath path = new Path(inc).makeRelative().removeFirstSegments(1);
					if (checkExists(project, path)) {
						//log(path.toString());
					}
				}
			}
		}

	}

	public static Boolean checkExists(IProject project, IPath path) {
		if ((path == null) || path.isEmpty()) {
			return false;
		}

		String spath = Utils.removeQuotes(path.toString());
		IPath ps = new Path(spath);
		if (project.exists(ps)) {
			return true;
		}

		return false;
	}

	public static Boolean checkExists(IProject project, String path) {
		if ((path == null) || path.isEmpty()) {
			return false;
		}

		String spath = Utils.removeQuotes(path);
		IPath ps = new Path(spath);

		if (project.exists(ps)) {
			return true;
		}
		return false;
	}

	/**
	 * before the CDT refresh to do LITE project resource refresh
	 * @param commandId UI refresh command id
	 * @param event     UI event, not used
	 */
	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		if (!org.eclipse.ui.IWorkbenchCommandConstants.FILE_REFRESH.equals(commandId)) {
			return;
		}
		ISelectionService selService = CpPlugInUI.getSelectionService();
		if (selService == null) {
			return;
		}
		// refresh LiteSTUDIO project in the selection
		ISelection selection = selService.getSelection();
		Collection<IProject> projects = CpPlugInUI.getProjectsFromSelection(selection);
		if (projects == null || projects.isEmpty()) {
			return;
		}
		for (IProject project : projects) {
			ILiteProject liteProject = getLiteProject(project);
			if (liteProject != null && liteProject.isUpdateCompleted()) {
				liteProject.refresh();
			}
		}
	}

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
		// does nothing
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
		// does nothing
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		// does nothing
	}


	/**
	 * employ the Project Updater class to handler the update job
	 * LiteProject(update) <- LiteProjectManager(updateProject) <- LiteProjectUpdater()
	 * @param liteProject  [child project]
	 * @param updateFlags [parameter flag ]
	 */
	synchronized public void updateProject(LiteProject liteProject, int updateFlags) {
		if (liteProject == null) {
			return;
		}

		IProject project = liteProject.getProject();
		if (project == null || !project.isOpen()) {
			return;
		}

		String name = liteProject.getName();
		if (fUpdateQueue.containsKey(name)) {
			return;
		}

		liteProject.setUpdateCompleted(false);
		fUpdateQueue.put(name, liteProject);
		// LiteProjectUpdater updater = new LiteProjectUpdater(liteProject, updateFlags);

		// //process start
		// updater.schedule();
	}

	synchronized public void updateFinished(ILiteProject liteProject) {
		if (liteProject == null) {
			return;
		}
		String name = liteProject.getName();
		fUpdateQueue.remove(name);

		liteProject.setUpdateCompleted(true);
		collectMissingPacks(liteProject);
		if (!fUpdateQueue.isEmpty()) {
			return;
		}

		queryInstallMissingPacks();
		fMissingPacks.clear();
	}

	private void collectMissingPacks(ILiteProject liteProject) {
		if (liteProject == null || !liteProject.getProject().isOpen()) {
			return;
		}
		ILiteConfiguration conf = liteProject.getLiteConfiguration();
		if (conf == null) {
			return;
		}
		Collection<ICpPackInfo> packs = conf.getMissingPacks();
		if (packs == null || packs.isEmpty()) {
			return;
		}

		for (ICpPackInfo pi : packs) {
			String packId = CpPack.constructPackId(pi.attributes());
			fMissingPacks.add(packId);
		}
	}

	class QueryInstallMissingPacksDlg extends MessageDialog {

		public QueryInstallMissingPacksDlg(Shell parentShell, String dialogTitle, Image dialogTitleImage,
		                                   String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
			super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, 0);
			setShellStyle(getShellStyle() | SWT.SHEET);
			setReturnCode(defaultIndex);
		}
	}

	private void queryInstallMissingPacks() {
		if (fMissingPacksQueryInProgress) {
			return;
		}

		if (fMissingPacks.isEmpty()) {
			return;
		}

		final ICpPackInstaller packInstaller = CpPlugIn.getPackManager().getPackInstaller();
		if (packInstaller == null) {
			return;
		}

		String packRoot = CpVariableResolver.getCmsisPackRoot();
		if (packRoot == null || packRoot.isEmpty()) {
			return;
		}

		Display display = Display.getDefault();
		if (display == null) {
			return;
		}

		final Set<String> missingPacks = new HashSet<>();
		StringBuilder sb = new StringBuilder(System.lineSeparator());
		for (String packId : fMissingPacks) {
			if (packInstaller.isProcessing(packId) || isInstalled(packId)) {
				continue;
			}
			missingPacks.add(packId);
			sb.append(System.lineSeparator());
			sb.append(packId);
		}
		if (missingPacks.isEmpty()) {
			return;
		}
		fMissingPacksQueryInProgress = true;
		sb.append(System.lineSeparator()).append(System.lineSeparator());
		final String message = NLS.bind(Messages.LiteProjectUpdater_InstallMissinPacksMessage, sb.toString());
		display.asyncExec(new Runnable() {
			@Override
			public void run() {

				String[] dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };

				MessageDialog dialog = new QueryInstallMissingPacksDlg(Display.getDefault().getActiveShell(),
				        Messages.LiteProjectUpdater_InstallMissinPacksTitle, null, message, MessageDialog.QUESTION,
				        dialogButtonLabels, 1);

				boolean install = dialog.open() == 0;
				if (install) {
					for (String packId : missingPacks) {
						packInstaller.installPack(packId);
					}
				}
				fMissingPacksQueryInProgress = false;
			}
		});
	}

	/**
	 * Check if the pack manager contains the pack and it is already installed
	 *
	 * @param packAttributes
	 *            pack attributes
	 * @return true if the pack installer has already installed the pack
	 */
	protected boolean isInstalled(String packId) {
		ICpPackCollection installedPacks = CpPlugIn.getPackManager().getInstalledPacks();
		if (installedPacks == null) {
			return false;
		}
		ICpPack pack = installedPacks.getPack(packId);
		return (pack != null);
	}

	@SuppressWarnings("unused")
	private static void log(String msg) {
		System.out.println(msg);
	}
}
