package com.lembed.unit.test.core.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.IArchive;
import org.eclipse.cdt.core.model.IArchiveContainer;
import org.eclipse.cdt.core.model.IBinary;
import org.eclipse.cdt.core.model.IBinaryContainer;
import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ISourceRoot;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.core.settings.model.ICConfigExtensionReference;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.internal.core.pdom.indexer.IndexerPreferences;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * Helper methods to set up a ICProject.
 */
@SuppressWarnings("restriction")
public class CProjectHelper {

	public static ICProject createCProject(final String projectName, String binFolderName) throws CoreException {
		return createCCProject(projectName, binFolderName, null);
	}

	/**
	 * Creates a ICProject.
	 */
	public static ICProject createCProject(final String projectName, String binFolderName, final String indexerID)
			throws CoreException {
		final IWorkspace ws = ResourcesPlugin.getWorkspace();
		final ICProject newProject[] = new ICProject[1];
		ws.run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				IWorkspaceRoot root = ws.getRoot();
				IProject project = root.getProject(projectName);
				if (indexerID != null) {
					IndexerPreferences.set(project, IndexerPreferences.KEY_INDEX_UNUSED_HEADERS_WITH_DEFAULT_LANG,
							"true");
					IndexerPreferences.set(project, IndexerPreferences.KEY_INDEXER_ID, indexerID);
				}
				if (!project.exists()) {
					project.create(null);
				} else {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
				if (!project.isOpen()) {
					project.open(null);
				}

				if (!project.hasNature(CProjectNature.C_NATURE_ID)) {
					String projectId = "org.eclipse.cdt.core.tests.TestProject";
					addNatureToProject(project, CProjectNature.C_NATURE_ID, null);
					CCorePlugin.getDefault().mapCProjectOwner(project, projectId, false);
				}

				addDefaultBinaryParser(project);
				newProject[0] = CCorePlugin.getDefault().getCoreModel().create(project);
			}
		}, null);

		return newProject[0];
	}

	public static ICProject getProjectFromPath(IPath projectDescriptionFilePath) throws CoreException {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IProjectDescription desc = ws.loadProjectDescription(projectDescriptionFilePath);
		String projectName = desc.getName();

		final ICProject newProject[] = new ICProject[1];
		ws.run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				IWorkspaceRoot root = ws.getRoot();
				IProject project = root.getProject(projectName);

				if (!project.exists()) {
					project.create(null);
				} else {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				}

				if (!project.isOpen()) {
					project.open(null);
				}

				if (!project.hasNature(CProjectNature.C_NATURE_ID)) {
					String projectId = "org.eclipse.cdt.core.tests.TestProject";
					addNatureToProject(project, CProjectNature.C_NATURE_ID, null);
					CCorePlugin.getDefault().mapCProjectOwner(project, projectId, false);
				}

				addDefaultBinaryParser(project);
				newProject[0] = CCorePlugin.getDefault().getCoreModel().create(project);
			}
		}, null);

		return newProject[0];
	}

	/**
	 * Add the default binary parser if no binary parser configured.
	 * 
	 * @param project
	 * @throws CoreException
	 */
	public static boolean addDefaultBinaryParser(IProject project) throws CoreException {
		ICConfigExtensionReference[] binaryParsers = CCorePlugin.getDefault().getDefaultBinaryParserExtensions(project);
		if (binaryParsers == null || binaryParsers.length == 0) {
			ICProjectDescription desc = CCorePlugin.getDefault().getProjectDescription(project);
			if (desc == null) {
				return false;
			}

			desc.getDefaultSettingConfiguration().create(CCorePlugin.BINARY_PARSER_UNIQ_ID,
					CCorePlugin.DEFAULT_BINARY_PARSER_UNIQ_ID);
			CCorePlugin.getDefault().setProjectDescription(project, desc);
		}
		return true;
	}

	public static String getMessage(IStatus status) {
		StringBuffer message = new StringBuffer("[");
		message.append(status.getMessage());
		if (status.isMultiStatus()) {
			IStatus children[] = status.getChildren();
			for (int i = 0; i < children.length; i++) {
				message.append(getMessage(children[i]));
			}
		}
		message.append("]");
		return message.toString();
	}

	public static ICProject createCCProject(final String projectName, final String binFolderName) throws CoreException {
		return createCCProject(projectName, binFolderName, null);
	}

	public static ICProject createCCProject(final String projectName, final String binFolderName,
			final String indexerID) throws CoreException {
		final IWorkspace ws = ResourcesPlugin.getWorkspace();
		final ICProject newProject[] = new ICProject[1];
		ws.run(new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				ICProject cproject = createCProject(projectName, binFolderName, indexerID);
				if (!cproject.getProject().hasNature(CCProjectNature.CC_NATURE_ID)) {
					addNatureToProject(cproject.getProject(), CCProjectNature.CC_NATURE_ID, null);
				}
				newProject[0] = cproject;
			}
		}, null);
		return newProject[0];
	}

	/**
	 * Removes a ICProject.
	 */
	public static void delete(ICProject cproject) {
		try {
			cproject.getProject().delete(true, true, null);
		} catch (CoreException e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			} finally {
				try {
					System.gc();
					System.runFinalization();
					cproject.getProject().delete(true, true, null);
				} catch (CoreException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * Adds a folder container to a ICProject.
	 */
	public static ICContainer addCContainer(ICProject cproject, String containerName) throws CoreException {
		IProject project = cproject.getProject();
		ICContainer container = null;
		if (containerName == null || containerName.length() == 0) {
			ICContainer[] conts = cproject.getSourceRoots();
			if (conts.length > 0) {
				container = conts[0];
			}
		} else {
			IFolder folder = project.getFolder(containerName);
			if (!folder.exists()) {
				folder.create(false, true, null);
			}
			container = CoreModel.getDefault().create(folder);
		}
		return container;
	}

	/**
	 * Removes a folder from a ICProject.
	 */
	public static void removeCContainer(ICProject cproject, String containerName) throws CoreException {
		IFolder folder = cproject.getProject().getFolder(containerName);
		folder.delete(true, null);
	}

	/**
	 * Attempts to find an archive with the given name in the workspace
	 */
	public static IArchive findArchive(ICProject testProject, String name) throws CModelException {
		int x;
		IArchive[] myArchives;
		IArchiveContainer archCont;
		/***************************************************************************************************************************
		 * Since ArchiveContainer.getArchives does not wait until all the archives in
		 * the project have been parsed before returning the list, we have to do a sync
		 * ArchiveContainer.getChildren first to make sure we find all the archives.
		 */
		archCont = testProject.getArchiveContainer();
		myArchives = archCont.getArchives();
		if (myArchives.length < 1)
			return (null);
		for (x = 0; x < myArchives.length; x++) {
			if (myArchives[x].getElementName().equals(name))
				return (myArchives[x]);
		}
		return (null);
	}

	/**
	 * Attempts to find a binary with the given name in the workspace
	 */
	public static IBinary findBinary(ICProject testProject, String name) throws CModelException {
		IBinaryContainer binCont;
		int x;
		IBinary[] myBinaries;
		binCont = testProject.getBinaryContainer();
		myBinaries = binCont.getBinaries();
		if (myBinaries.length < 1)
			return (null);
		for (x = 0; x < myBinaries.length; x++) {
			if (myBinaries[x].getElementName().equals(name))
				return (myBinaries[x]);
		}
		return (null);
	}

	public static List<IBinary> findBinary(ICProject testProject) throws CModelException {
		IBinaryContainer binCont;
		int x;
		IBinary[] allBinaries;
		binCont = testProject.getBinaryContainer();
		allBinaries = binCont.getBinaries();
		List<IBinary> blist = new LinkedList<IBinary>();

		if (allBinaries.length < 1)
			return blist;

		for (x = 0; x < allBinaries.length; x++) {
			blist.add(allBinaries[x]);
		}

		return blist;
	}

	/**
	 * Attempts to find an object with the given name in the workspace
	 */
	public static IBinary findObject(ICProject testProject, String name) throws CModelException {
		ICElement[] sourceRoots = testProject.getChildren();
		for (int i = 0; i < sourceRoots.length; i++) {
			ISourceRoot root = (ISourceRoot) sourceRoots[i];
			ICElement[] myElements = root.getChildren();
			for (int x = 0; x < myElements.length; x++) {
				if (myElements[x].getElementName().equals(name)) {
					if (myElements[x] instanceof IBinary) {
						return ((IBinary) myElements[x]);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Attempts to find a TranslationUnit with the given name in the workspace
	 * 
	 * @throws InterruptedException
	 */
	public static ITranslationUnit findTranslationUnit(ICProject testProject, String name)
			throws CModelException, InterruptedException {

		ICElement[] sourceRoots = testProject.getChildren();
		for (int i = 0; i < sourceRoots.length; i++) {
			ISourceRoot root = (ISourceRoot) sourceRoots[i];
			ICElement[] myElements = root.getChildren();
			for (int x = 0; x < myElements.length; x++) {
				if (myElements[x].getElementName().equals(name)) {
					if (myElements[x] instanceof ITranslationUnit) {
						return ((ITranslationUnit) myElements[x]);
					}
				}
			}
		}

		return null;
	}

	public static List<ITranslationUnit> getTranslationUnitsOfProject(ICProject cProject) throws CModelException {
		List<ITranslationUnit> tus = new LinkedList<ITranslationUnit>();
		ISourceRoot[] sourceRoots = cProject.getAllSourceRoots();
		for (int i = 0; i < sourceRoots.length; i++) {
			ISourceRoot root = sourceRoots[i];
			ICElement[] tElements = root.getChildren();
			for (int x = 0; x < tElements.length; x++) {
				if (tElements[x] instanceof ITranslationUnit) {
					tus.add((ITranslationUnit) tElements[x]);
				}
			}
		}

		return tus;
	}

	/**
	 * Attempts to find an element with the given name in the workspace
	 */
	public static ICElement findElement(ICProject testProject, String name) throws CModelException {
		ICElement[] sourceRoots = testProject.getChildren();
		for (int i = 0; i < sourceRoots.length; i++) {
			ISourceRoot root = (ISourceRoot) sourceRoots[i];
			ICElement[] myElements = root.getChildren();
			for (int x = 0; x < myElements.length; x++) {
				if (myElements[x].getElementName().equals(name)) {
					return myElements[x];
				}
			}
		}
		return null;
	}

	public static List<ICElement> findElements(ICProject testProject) throws CModelException {
		List<ICElement> ces = new LinkedList<ICElement>();

		ICElement[] sourceRoots = testProject.getChildren();
		for (int i = 0; i < sourceRoots.length; i++) {
			ICElement root = sourceRoots[i];
			ces.add(root);
		}
		return ces;
	}

	public static void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor)
			throws CoreException {
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = natureId;
		description.setNatureIds(newNatures);
		proj.setDescription(description, monitor);
	}
}