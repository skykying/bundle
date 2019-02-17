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

package com.lembed.lite.studio.device.pack;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName(); //$NON-NLS-1$
	public static String CopyExampleDialog_AttentionMessage;
	public static String CopyExampleDialog_CopyExampleTitle;
	public static String CopyExampleDialog_Example;
	public static String CopyExampleDialog_Pack;
	public static String CopyExampleDialog_ProjectName;
	public static String CopyExampleDialog_ReplaceExistingProject;
	public static String CopyExampleDialog_ProjectLocation;
	public static String CpPackImportFolderJob_ImportingPack;
	public static String CpPackImportFolderJob_ImportingPacksFrom;
	public static String CpPackImportFolderJob_FolderNotExist;
	public static String CpPackInstaller_Completed;
	public static String CpPackInstaller_CreatingNewProject;
	public static String CpPackInstaller_DeletingPack;
	public static String CpPackInstaller_ErrorWhileCopyingExample;
	public static String CpPackInstaller_ErrorWhileOverwritingExistingProject;
	public static String CpPackInstaller_ErrorWhileReadingProjectDescriptionFile;
	public static String CpPackInstaller_ErrorWhileRefreshingIgnored;
	public static String CpPackInstaller_ExistsQuestion;
	public static String CpPackInstaller_FailedImportFilesFromFolder;
	public static String CpPackInstaller_FileNotFound;
	public static String CpPackInstaller_FinishingOperation;
	public static String CpPackInstaller_ImportingPack;
	public static String CpPackInstaller_ImportingFolderPacks;
	public static String CpPackInstaller_InstallingPack;
	public static String CpPackInstaller_InstallingRequiredPacks;
	public static String CpPackInstaller_JobCancelled;
	public static String CpPackInstaller_NoCompatibleVersionIsFound;
	public static String CpPackInstaller_NoPacksFound;
	public static String CpPackInstaller_NoVersionOfPackFamilyIsFound;
	public static String CpPackInstaller_OpenPackManagerToUpdatePacks;
	public static String CpPackInstaller_PackFamilyNotFound;
	public static String CpPackInstaller_PackUpdatesCompleted;
	public static String CpPackInstaller_Parsing;
	public static String CpPackInstaller_Processing;
	public static String CpPackInstaller_ProjectAlreadyExists;
	public static String CpPackInstaller_ProjectWillBeCreated;
	public static String CpPackInstaller_RefreshAllPacks;
	public static String CpPackInstaller_ReloadPacksAndManagerMessage;
	public static String CpPackInstaller_ReloadPacksAndManagerTitle;
	public static String CpPackInstaller_RemovingPack;
	public static String CpPackInstaller_RepoTypeNotSupported;
	public static String CpPackInstaller_RequiredPackFamilyNotExist;
	public static String CpPackInstaller_RequiredPacksNotResolved;
	public static String CpPackInstaller_SetCmsisPackRootFolderAndTryAgain;
	public static String CpPackInstaller_TimeoutConsoleMessage;
	public static String CpPackInstaller_TimeoutMessage;
	public static String CpPackInstaller_Timout;
	public static String CpPackInstaller_UnpackingPack;
	public static String CpPackInstaller_Updating;
	public static String CpPackInstaller_WasNotSuccessful;

	public static String CpPackJob_CancelledByUser;

	public static String CpPackInstallJob_CannotFindPdscFile;
	public static String CpPackInstallJob_ConnectingTo;
	public static String CpPackInstallJob_DownloadingFrom;
	public static String CpPackInstallJob_FileNotFound;
	public static String CpPackInstallJob_InstallingPack;
	public static String CpPackInstallJob_MalformedURL;
	public static String CpPackInstallJob_TimeoutConsoleMessage;
	public static String CpPackInstallJob_UnknownHostException;
	public static String CpPackInstallJob_UnzippingAndParsing;
	public static String CpPackManager_InstallingRequiredPacks;
	public static String CpPackManager_RequiredPackNotExist;
	public static String CpPackRemoveJob_DeletingFilesFromFolder;
	public static String CpPackRemoveJob_RemovingPack;

	public static String CpPackUnpackJob_FailedToUnzipFile;
	public static String CpPackUnpackJob_InvalidOperation;
	public static String CpPackUnpackJob_OverwriteQuery;
	public static String CpPackUnpackJob_PathAlreadyExists;
	public static String CpPackUnpackJob_FailToParsePdscFile;
	public static String CpPackUnpackJob_PdscFileNotFoundInFolder;
	public static String CpPackUnpackJob_SourceFileCannotBeFound;
	public static String CpPackUnpackJob_Unpacking;

	public static String LicenseDialog_AgreeText;
	public static String LicenseDialog_GuidanceText;
	public static String LicenseDialog_LicenseAgreement;
	public static String LicenseDialog_LicenseDialogTitle;
	public static String OverwriteQuery_ExistsQuestion;
	public static String OverwriteQuery_OverwriteNameAndPathQuestion;
	public static String OverwriteQuery_Question;

	public static String PackInstallerUtils_PleaseAgreeLicenseAgreement;
	public static String BoardsView_AllBoards;
	public static String BoardsView_AvailableInPack;
	public static String BoardsView_Boards;
	public static String PackInstallerView_Help;
	public static String BoardsView_RemoveSelection;
	public static String BoardsView_SearchBoard;
	public static String CollapseAll;
	public static String CollapseAllNodes;
	public static String CollapseSelected;
	public static String CollapseSelectedNode;
	public static String CpInstallerPlugInUI_ExitEclipse;
	public static String CpInstallerPlugInUI_ExitEclipseMessage;
	public static String CpInstallerPlugInUI_OFFLINE;
	public static String CpInstallerPlugInUI_ONLINE;
	public static String DevicesView_1Device;
	public static String DevicesView_AvailableInPack;
	public static String DevicesView_Devices;
	public static String DevicesView_Processor;
	public static String DevicesView_RemoveSelection;
	public static String DevicesView_SearchDevice;
	public static String ExamplesView_Board;
	public static String ExamplesView_CopyExampleInstallPack;
	public static String ExamplesView_Device;
	public static String ExamplesView_OnlyShowInstalledPack;
	public static String ExamplesView_Pack;
	public static String ExamplesView_SearchExample;
	public static String ExpandAll;
	public static String ExpandAllNodes;
	public static String ExpandSelected;
	public static String ExpandSelectedNode;
	public static String Help;
	public static String ImportPacksHandler_DialogText;
	public static String ImportFolderPacksHandler_Message;
	public static String ImportFolderPacksHandler_Title;
	public static String PackPropertyView_CopyAction;
	public static String PackPropertyView_CopyTooltip;
	public static String PackPropertyView_InstallAction;
	public static String PackInstallerView_InstallRequiredPacks;
	public static String PackInstallerView_InstallRequiredPacksToolTip;
	public static String PackPropertyView_InstallTooltip;
	public static String PackPropertyView_UnpackAction;
	public static String PackPropertyView_UnpackTooltip;
	public static String PacksExamplesViewFilter_NoBoards;
	public static String PacksExamplesViewFilter_NoDevices;
	public static String PacksView_1Pack;
	public static String PacksView_CannotLoadPdscFiles;
	public static String PacksView_CheckForUpdate;
	public static String PacksView_CheckForUpdateOnWeb;
	public static String PacksView_Remove;
	public static String PacksView_RemovePlusDelete;
	public static String PacksView_RemoveSelectedPack;
	public static String PacksView_Delete;
	public static String PacksView_Delete_;
	public static String PacksView_DeleteAllTooltip;
	public static String PacksView_DeleteSelectedPack;
	public static String PacksView_DeprecatedOn;
	public static String PacksView_GenericPacksDescription;
	public static String PacksView_InstallSinglePack;
	public static String PacksView_InstallSinglePackTooltip;
	public static String PacksView_Location;
	public static String PacksView_Packs;
	public static String PacksView_PreviousPackVersions;
	public static String PacksView_ReplacedBy;
	public static String PacksView_RequiredPacks;
	public static String PacksView_ResolveRequiredPacks;
	public static String PacksView_SearchPack;
	public static String PacksView_Selected;
	public static String PacksView_ShowPacksOutline;
	public static String PacksView_UnpackSinglePack;
	public static String PacksView_Version;
	public static String ReloadPacksHandler_RefreshPacks;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
