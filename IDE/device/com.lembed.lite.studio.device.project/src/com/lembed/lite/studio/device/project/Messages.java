/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.project;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName(); //$NON-NLS-1$
	public static String CmsisCodeTemplate_Browse;
	public static String CmsisCodeTemplate_Component;
	public static String CmsisCodeTemplate_ContainerNotExist;
	public static String CmsisCodeTemplate_CreatingFile;
	public static String CmsisCodeTemplate_Description;
	public static String CmsisCodeTemplate_Error;
	public static String CmsisCodeTemplate_FileAlreadyExist;
	public static String CmsisCodeTemplate_FileContainerNotExist;
	public static String CmsisCodeTemplate_FileContainerNotSpecified;
	public static String CmsisCodeTemplate_FileExtensionNotConsistent;
	public static String CmsisCodeTemplate_FileName;
	public static String CmsisCodeTemplate_FileNameNotSpecified;
	public static String CmsisCodeTemplate_FileNameNotValid;
	public static String CmsisCodeTemplate_FileUnderLiteFolder;
	public static String CmsisCodeTemplate_Location;
	public static String CmsisCodeTemplate_Name;
	public static String CmsisCodeTemplate_OpeningFileForEditing;
	public static String CmsisCodeTemplate_OverwriteExistingFile;
	public static String CmsisCodeTemplate_ProjectNotWritable;
	public static String CmsisCodeTemplate_RefreshLiteProject;
	public static String CmsisCodeTemplate_SelectFolder;
	public static String CmsisCodeTemplate_Title;
	public static String CmsisCodeTemplate_WindowTitle;
	public static String CmsisCodeTemplate_WizardPage;
	public static String CmsisCodeTemplateNewWizardPage_LocationNotUnderProject;
	public static String CmsisCodeTemplateNewWizardPage_Project;
	public static String CmsisCodeTemplateNewWizardPage_ProjectMustBeSpecified;
	public static String CmsisCodeTemplateNewWizardPage_ProjectMustExist;
	public static String CreateLiteProject_EclipseProjectNotExists;
	public static String CreateLiteProject_ErrorCreatingConfigFile;
	public static String CreateLiteProject_ErrorCreatingLiteProject;
	public static String CreateLiteProject_ToolchainAdapterNotFound;
	public static String MergeConfigFileHandler_Merge;
	public static String ProjectSelectionDialog_NoLiteProjectFound;
	public static String ProjectSelectionDialog_LiteProjectSelectionDialog;
	public static String ProjectSelectionDialog_SelectLiteProject;
	public static String ProjectUtils_CannotCopyFile;
	public static String ProjectUtils_DoesNotExistsOrNotAccessible;
	public static String ProjectUtils_Project;
	public static String ProjectUtils_ProjectfolderMustBeRelative;
	public static String ProjectUtils_TheFile;
	public static String ProjectUtils_to;
	public static String LiteConfigRenameResourceChange_RenamingFile;
	public static String LiteConfiguration_ComponentSelection;
	public static String LiteConfiguration_DeviceHeader;
	public static String LiteProjectManager_ReloadLiteProjectMessage;
	public static String LiteProjectManager_ReloadLiteProjectTitle;
	public static String LiteProjectRenameParticipant_CheckingPreconditions;
	public static String LiteProjectRenameParticipant_CMSIS_Lite_project_rename_handler;
	public static String LiteProjectRenameParticipant_CreatingChange;
	public static String LiteProjectRenameParticipant_RenameIsNotAllowed;
	public static String LiteProjectRenameParticipant_RenameOfLiteFolderIsNotAllowed;
	public static String LiteProjectTemplate_CMSIS_LITE_Project;
	
	public static String LiteProjectTemplate_SelectLibrary;
	public static String LiteProjectTemplate_SetTargetFolder;
	
	public static String LiteProjectUpdater_ErrorLoadinConfigFile;
	public static String LiteProjectUpdater_ErrorConfigFileNotExist;
	public static String LiteProjectUpdater_ErrorParsingFailed;
	public static String LiteProjectUpdater_ErrorCmisPackRootNotSet;
	public static String LiteProjectUpdater_ErrorProjectIsNull;
	public static String LiteProjectUpdater_ErrorUpdatingLiteProject;
	public static String LiteProjectUpdater_LoadingLiteConfiguration;
	public static String LiteProjectUpdater_ProjectUpdated;
	public static String LiteProjectUpdater_UpdatingBuildSettings;
	public static String LiteProjectUpdater_UpdatingProject;
	public static String LiteProjectUpdater_UpdatingResources;
	public static String LiteProjectUpdater_Success;
	public static String LiteProjectUpdater_Fail;
	public static String LiteProjectUpdater_InstallMissinPacksMessage;
	public static String LiteProjectUpdater_InstallMissinPacksTitle;
	public static String LitePropertyPage_Component;
	public static String LitePropertyPage_ComponentNotFound;
	public static String LitePropertyPage_Description;
	public static String LitePropertyPage_Location;
	public static String LitePropertyPage_Path;
	public static String LitePropertyPage_ResolvedVersion;
	public static String LitePropertyPage_SoftwarePack;
	public static String LitePropertyPage_Type;
	public static String LitePropertyPage_URL;
	public static String LitePropertyPage_Version;
	public static String LiteTemplateCmsisProjectPage_NoTollchainAvailable;
	public static String LiteTemplateCmsisProjectPage_Output;
	public static String LiteTemplateCmsisProjectPage_SelectedProjectType;
	public static String LiteTemplateDeviceSelectorPage_NoDevicesAreAvailable;
	public static String LiteTemplateDeviceSelectorPage_NoPackManagerIsAvailble;
	public static String LiteTemplateCmsisProjectPage_CreateDefaultMain;
	public static String LiteTooolChainAdapterSelector_Adapter;
	public static String LiteTooolChainAdapterSelector_Family;
	public static String LiteTooolChainAdapterSelector_FamilyToolTipAndDescription;
	public static String LiteTooolChainAdapterSelector_NoToolchainAdapterAvailable;
	public static String LiteTooolChainAdapterSelector_PassedviaTCompiler;
	public static String LiteTooolChainAdapterSelector_SelectFamily;
	public static String LiteTooolChainAdapterSelector_Toolchain;
	public static String LiteMiddleWareAdapterSelector_Middle;
	public static String LiteTooolChainAdapterSelector_ToolchainAdapter;
	public static String LiteMiddleWareAdapterSelector_MiddleSelecter;
	public static String LiteTooolChainAdapterSelector_ToolChainAdapterIsReponsibleFor;
	public static String UpdateConfigFileHandler_Downgrade;
	public static String UpdateConfigFileHandler_UpdateSelectedFiles;
	public static String UpdateConfigFileHandler_Upgrade;
	public static String LiteMiddleWareAdapterSelector_RTOS;
	public static String LiteMiddleWareAdapterSelector_Net;
	public static String LiteMiddleWareAdapterSelector_NewLib;
	public static String LiteMiddleWareAdapterSelector_RumTime;
	public static String LiteMiddleWareAdapterSelector_DeviceDriver;
	public static String LiteMiddleWareAdapterSelector_Trace;
	public static String LiteTemplateFolder_FolderSetting;
	public static String LiteTemplateFolder_IncludeFolder;
	public static String LiteTemplateFolder_SourceFolder;
	public static String LiteTemplateFolder_SystemFolder;
	public static String LiteTemplateFolder_LinkerScriptFolder;
	public static String LiteTemplateFolder_HeapStackSetting;
	public static String LiteTemplateFolder_MiniStackSize;
	public static String LiteTemplateFolder_MiniStackSizeDefault;
	public static String LiteTemplateFolder_StackSize;
	public static String LiteTemplateFolder_StackSizeDefault;
	public static String LiteTemplateFolder_LinkerScriptFolderDefault;
	public static String LiteTemplateFolder_SystemFolderDefault;
	public static String LiteTemplateFolder_SourceFolderDefault;
	public static String LiteTemplateFolder_IncludeFolderDefault;
	public static String LiteTemplateMemory_ScriptTemplate;
	public static String LiteMiddleWareAdapterSelector_UTILS;
	public static String OPTIZIMATION_TITLES;
	public static String OPTIZIMATION_USE_SOME_WARINGS;
	public static String OPTIMIZATION_USE_MOST_WARINGS;
	public static String OPTIMIZATION_USE_WARING_AS_ERROR;
	public static String OPTIMIZATION_USE_OG_ON_DEBUG;
	public static String OPTIMIZATION_USE_NEWLIB_NANO;
	public static String OPTIZIMATION_EXCLUDE_UNUSED;
	public static String OPTIZIMATION_LINKER;
	public static String OPTIZIMATION_GENERATOR_COVERAGE_INFO;
	
	public static String SysCall_FREESTANDING;
	public static String SysCall_POSIX;
	public static String SysCall_SEMIHOSTING;
	
	public static String TRACE_STDOUT_CHANNEL;
	public static String TRACE_DEBUG_CHANNEL;
	public static String TRACE_NO_CHANNEL;
	
	public static String RTOS_FREERTOS;
	public static String RTOS_MNUTTX;
	public static String DEBUGGER_JLINK;
	public static String DEBUGGER_STLINK;
	public static String UTILS_BUILD_IN;
	
	public static String LiteMiddleWareAdapterSelector_Debugger;
	public static String LiteMiddleWareAdapterSelector_DebuggerTipText;
	public static String LiteMiddleWareAdapterSelector_DebuggerTipText_Combo;
	public static String LiteMiddleWareAdapterSelector_RTOSTipText;
	public static String LiteMiddleWareAdapterSelector_RTOSTipText_Combo;
	public static String LiteMiddleWareAdapterSelector_TraceTipText;
	public static String LiteMiddleWareAdapterSelector_TraceTipText_Combo;
	public static String LiteMiddleWareAdapterSelector_SyscallsTipText;
	public static String LiteMiddleWareAdapterSelector_Syscalls;
	public static String LiteMiddleWareAdapterSelector_SyscallsTipText_Combo;
	public static String LiteMiddleWareAdapterSelector_UTILSTipText;
	public static String LiteMiddleWareAdapterSelector_UTILSTipText_Combo;
	public static String LiteMiddleWareAdapterSelector_NO_Selection;
	public static String TRACE_ITM_CHANNEL;
	public static String DEBUGGER_NULL;
	public static String RTOS_NULL;
	public static String UTILS_NULL;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
