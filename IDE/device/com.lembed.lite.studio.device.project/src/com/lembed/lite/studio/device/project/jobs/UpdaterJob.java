package com.lembed.lite.studio.device.project.jobs;

import java.util.Collection;
import java.util.List;
import com.lembed.lite.studio.device.ui.console.LiteConsole;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.item.ICmsisMapItem;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.CpVariableResolver;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.ui.LiteProjectDecorator;
import com.lembed.lite.studio.device.project.utils.ConfigurationUtils;
import com.lembed.lite.studio.device.ui.CpPlugInUI;

public class UpdaterJob extends WorkspaceJob {
	public static final int UPDATE_ALL = 0x05;

	public static final int UPDATE_RESOURCES_RM = 0x11;
	public static final int UPDATE_RESOURCES_ADD = 0x12;

	protected ILiteProject liteProject;
	protected IProject project;
	protected IProgressMonitor monitor = null;

	protected int uFlag = 0;

	protected LiteConsole liteConsole = null;

	private static Boolean running =  false;

	public UpdaterJob() {
		super("LiteSTUDIO UpdaterJob"); //$NON-NLS-1$

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		setRule(workspace.getRoot()); // ensures synch update

		liteConsole = LiteConsole.openConsole(project);
	}

	public UpdaterJob setLiteProject(ILiteProject liteProject) {
		this.liteProject = liteProject;
		this.project = liteProject.getProject();
		return this;
	}

	public UpdaterJob setFlag(int flag) {
		this.uFlag = flag;
		return this;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) {

		if (project == null || liteProject == null) {
			Status status = new Status(IStatus.ERROR, CpPlugInUI.PLUGIN_ID,
			                           Messages.LiteProjectUpdater_ErrorProjectIsNull);
			return status;
		}

		this.monitor = monitor;
		Status status = null;
		EEvaluationResult res = EEvaluationResult.FULFILLED;
		try {

			String packRoot = CpVariableResolver.getCmsisPackRoot();
			if (packRoot == null || packRoot.isEmpty()) {
				status = new Status(IStatus.WARNING, CpPlugInUI.PLUGIN_ID,
				                    Messages.LiteProjectUpdater_ErrorCmisPackRootNotSet);
				throw new CoreException(status);
			}
			res = checkLiteConfiguration();

		} catch (CoreException e) {
			status = new Status(e.getStatus().getSeverity(), CpPlugInUI.PLUGIN_ID,
			                    Messages.LiteProjectUpdater_ErrorUpdatingLiteProject, e);
			res = EEvaluationResult.FAILED;
		} catch (Exception e) {
			e.printStackTrace();
			status = new Status(IStatus.ERROR, CpPlugInUI.PLUGIN_ID,
			                    Messages.LiteProjectUpdater_ErrorUpdatingLiteProject, e);
			res = EEvaluationResult.FAILED;
		}
		if (res.ordinal() < EEvaluationResult.MISSING.ordinal() || status != null) {
			liteConsole.outputError(Messages.LiteProjectUpdater_Fail);
			if (status != null) {
				liteConsole.outputInfo(status.getMessage());
				IStatus[] statusArray = status.getChildren();
				if (statusArray != null && statusArray.length > 0) {
					for (IStatus s : statusArray) {
						liteConsole.outputInfo(s.getMessage());
					}
				}
			}
		}

		if (EEvaluationResult.FULFILLED == res) {
			doWork(uFlag);
			//printToolChainOptions(liteProject);
		}

		liteConsole.output(CmsisConstants.EMPTY_STRING);
		LiteProjectDecorator.refresh();
		if (status == null) {
			status = new Status(IStatus.OK, CpPlugInUI.PLUGIN_ID, Messages.LiteProjectUpdater_ProjectUpdated);
		}
		CpProjectPlugIn.getLiteProjectManager().updateFinished(liteProject);
		return status;
	}

	protected void doWork(int flag) {

		try {
			switch (flag) {

			case UPDATE_RESOURCES_RM:
				ConfigurationUtils.resetBuildSettings(liteProject, monitor);
				ConfigurationUtils.removeResources(liteProject, false, monitor);
				break;
			case UPDATE_RESOURCES_ADD:
				ConfigurationUtils.addResources(liteProject, monitor);
				ConfigurationUtils.applyBuildSettings(liteProject, true, monitor);
				break;
			case UPDATE_ALL:
				ConfigurationUtils.resetBuildSettings(liteProject, monitor);
				ConfigurationUtils.removeResources(liteProject, false, monitor);
				ConfigurationUtils.addResources(liteProject, monitor);
				ConfigurationUtils.applyBuildSettings(liteProject, true, monitor);
			default:
				break;
			}
			liteProject.save();

			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			ConfigurationUtils.updateIndex(liteProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void expriment(ILiteProject liteProject) {

		ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
		ICpDeviceInfo info = liteConf.getDeviceInfo();
		String deviceName = info.getDeviceName();
		String venderName =  info.getVendor();
		log("deviceName + venderName = " + deviceName + "  " + venderName);

		CpPlugIn.getPackManager().getCmsisPackRootDirectory();
		ICpPackManager manager = CpPlugIn.getPackManager();
		ILiteDeviceItem devices = manager.getDevices();

		ICmsisMapItem<ILiteDeviceItem> root = new CmsisMapItem<>();
		root.addChild(devices);

		ILiteDeviceItem dti = null;
		Collection<? extends ILiteDeviceItem> children = root.getChildren();
		for (ILiteDeviceItem child : children) {
			dti = child.findItem(deviceName, venderName, true);
		}

		if (dti != null) {
			String name = dti.getDevice().getProcessorName();
			ICpDebugConfiguration debugConf = dti.getDevice().getDebugConfiguration(name);
			String svd = debugConf.getSvdFile();
			log(svd);
			IPath path = new Path(svd);
			log(path.toOSString());
		}

		List<String> values = ConfigurationUtils.collectBuildToolIncludePaths(project);
		for (String value : values) {
			log(value);
		}

		values = ConfigurationUtils.collectBuildToolLinkerPaths(project);
		for (String value : values) {
			log(value);
		}

		values = ConfigurationUtils.collectBuildToolSymbols(project);
		for (String value : values) {
			log(value);
		}

	}

	private void printToolChainOptions(ILiteProject liteProject) {
		IProject project = liteProject.getProject();
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject().getConfigurations();
		for (IConfiguration config : projectConfigs) {

			IToolChain toolChain = config.getToolChain();
			ITool[] tools = toolChain.getTools();

			for (ITool tool : tools) {
				IOption[] ots  = tool.getOptions();
				for (IOption op : ots) {

					try {
						log(op.getBaseId() + "  cccccccccccccccc " + op.getValueType());

						List<String> values = ConfigurationUtils.getCurrentStringListValue(op);
						if (values == null) {
							continue;
						}
						for (String path : values) {
							log(path + "        vvvvvvvvvvvvv");
						}

					} catch (BuildException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}



	protected EEvaluationResult checkLiteConfiguration() throws CoreException {
		ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
		Collection<String> errors = liteConf.validate();
		EEvaluationResult res = liteConf.getEvaluationResult();
		if (errors == null || errors.isEmpty()) {
			return res;
		}

		String msg = Messages.LiteProjectUpdater_ErrorLoadinConfigFile + " '" + "':"; //$NON-NLS-1$
		liteConsole.outputError(msg);
		for (String s : errors) {
			liteConsole.output(s);
			msg += System.lineSeparator() + s;
		}

		if (res.ordinal() > EEvaluationResult.FAILED.ordinal()) {
			return res;
		}
		Status status = new Status(IStatus.WARNING, CpPlugInUI.PLUGIN_ID, msg);
		throw new CoreException(status);
	}

	public static void log(String msg) {
		//System.out.println("<<<<<<<  " + msg);
	}
}
