/*******************************************************************************
 * Copyright (c) 2014, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Red Hat Inc.
 *******************************************************************************/

package com.lembed.codeanalyzer.profiling.ui;

import org.eclipse.cdt.autotools.core.AutotoolsNewProjectNature;
import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.internal.autotools.core.configure.AutotoolsConfigurationManager;
import org.eclipse.cdt.internal.autotools.core.configure.IAConfiguration;
import org.eclipse.cdt.internal.autotools.core.configure.IConfigureOption;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IResourceInfo;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

/**
 * <h1> C and C++ Project configuration and build helpers. </h1>
 * <p> This class is focused on automating the process of enabling profiling plugins. <br>
 *  Used primarily for gcov gprof. </p>
 *
 * <p> It supports c/c++ in Managed and Autotools projects, <br>
 * by providing the following functionality : <br>
 * - Identify project type and differentiate C from C++ <br>
 * - Check if a flag is set  <br>
 * - Enable a build-flag prog <br>
 * - Rebuild a project </p>
 *
 * <p> Common steps: <br>
 * <ol>
 *      <li> Check project type (Managed/Autotools) (getProjectType) </li>
 *      <li> Find out if it's C/C++, determine option ID to check (isCppType ..) </li>
 *      <li> Check if option (e.g -pg) is checked by the user (iOptionChecked ...) </li>
 *      <li> If not, prompt the user to have it checked. See {@link MessageDialogSyncedRunnable MessageDialogSyncedRunnable} ) </li>
 *      <li> If user agrees, programmatically set the option. (SetOptionIn ..) </li>
 *      <li> Rebuild the project </li>
 *      <li> Continue with launch. </li>
 * </ol>
 * For an example, see <code> com.lembed.codeanalyzer.internal.gprof.launch.GprofLaunchConfigurationDelegate  </code>.
 * @since 3.1
 */
public class CProjectBuildHelpers {

    /**
     *  <h1>Custom Project Type enumerator.</h1>
     *
     *  <p>
     *  Used in conjunction with {@link CProjectBuildHelpers#getProjectType getProjectType} <br>
     *  Check the return value against these constants.<br>
     *  </p>
     *
     *  <p> Can be set to one of the following:
     *  - AUTO_TOOLS <br>
     *  - MANAGED_MAKEFILE <br>
     *  - OTHER <br></p>
     */
    public static enum ProjectBuildType {
        AUTO_TOOLS, MANAGED_MAKEFILE, OTHER
    }

    /**
     * <h1>Finds out the type of the project as defined by {@link ProjectBuildType ProjectBuildType}.</h1>
     *
     * <p> A project can be of different types.<br>
     * Common types are:
     *  - Autotools<br>
     *  - Managed Make project<br>
     *  - Manual Makefiles<br></p>
     *
     *
     * <p>
     * Some dialogues (initially in gCov and gProf) distinguish between these when displaying dialogues. This code is used
     * by these dialogues.
     * </p>
     *
     * <p>
     * The method was written with extensibility in mind. <br>
     * Other routines check for autotools/Managed make, and if those are not present, then it shows generic advice about
     * adding flags. MAKEFILE per-se isn't really checked. I.e this means that it should be safe to add additional
     * project types.
     * </p>
     *
     * @param project  project for which you want to get the type.
     * @return (enum)  projectType : <br>
     *         AUTO_TOOLS, <br>
     *         MANAGED_MAKEFILE, <br>
     *         OTHER <br>
     */
    public static ProjectBuildType getProjectType(IProject project) {
        //       AUTOTOOLS
        // Autotools has an 'Autotools' nature by which we can identify it.
        if (isAutoTools(project)) {
            return ProjectBuildType.AUTO_TOOLS;
        }

        IConfiguration defaultConfiguration = helperGetActiveConfiguration(project);
        IBuilder builder = defaultConfiguration.getBuilder();
        Boolean projIsManaged = builder.isManagedBuildOn();

        //       MANAGED PROJECT
        if (projIsManaged) {
            return ProjectBuildType.MANAGED_MAKEFILE;
        } else {
            return ProjectBuildType.OTHER; //E.g a manual makefile.
        }
    }

    /**
     * <h1>Differentiate C++ from C projects.</h1>
     *
     * <p>
     * Projects can be c or cpp based.<br>
     * Cpp projects have a ccnature as well as a cnature, <br>
     * where as C projects only have a cnature. <br>
     * </p>
     * @param project  the IProject project which will be read to check if it is c or cpp.
     * @return         true if it has a cpp nature.
     */
    public static boolean isCppType(IProject project) {
        try {
            return project.hasNature(CCProjectNature.CC_NATURE_ID);
        } catch (CoreException ex) {
            //This should almost never happen unless the user manually edited the .project file.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetProjectType);
            return false;
        }
    }

    /**
     * <h1>Differentiates C from C++ projects.</h1>
     *
     * <p>
     * Projects can be c or cpp based.<br>
     * Cpp projects have a ccnature as well as a cnature, <br>
     * where as C projects only have a cnature.
     * </p>
     * @param project the IProject project which will be read to check if it is c or cpp.
     * @return        true if it has a c nature BUT NOT a cpp nature.
     */
    public static boolean isCType(IProject project) {
        try {
            // has C & has not CPP
            return project.hasNature(CProjectNature.C_NATURE_ID)
                   && !project.hasNature(CCProjectNature.CC_NATURE_ID);
        } catch (CoreException e) {
            // should never really reach this.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetProjectType);
            return false;
        }
    }

    /**
     * <h1>Autotools projects have an Autotools Nature.</h1>
     *
     * @param project IProject that you're dealing with.
     * @return        true if the project has an autotools nature.
     */
    public static boolean isAutoTools(IProject project) {
        try {
            return project.hasNature(AutotoolsNewProjectNature.AUTOTOOLS_NATURE_ID);
        } catch (CoreException e) {
            // should never really reach this unless .cproject is broken.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetProjectType);
            return false;
        }
    }

    /**
     * <h1>Check if an option is set in the CDT settings.</h1>
     *
     * <p>
     * Commonly used when launching a plugin and you want to check if flags are set correctly.
     * </p>
     *
     * <p>
     * An example of an option id is: {@code gnu.cpp.compiler.option.debugging.gprof } To find the option id, check/uncheck it in
     * your project. Then inspect the .cproject file.
     * </p>
     *
     * <p>
     * This method serves as a model to easily add similar methods. find what subclasses the iPrefernce store, it is
     * likeley that your desired option is in one of those stores.
     * </p>
     * @param project         the IProject project which will be read to check if it is c or cpp.
     * @param optionIDString  for example <code> gnu.cpp.compiler.option.debugging.codecov </code>
     * @return                true if the option is set.
     */
    public static boolean isOptionCheckedInCDT(IProject project, String optionIDString) {
        //Set Tool Name.
        String toolSuperclassId = helperGetToolSuperClassId(project);
        if (toolSuperclassId == null) {
            return false;
        }
        return isOptionCheckedInCDTTool(project, optionIDString, toolSuperclassId);
    }

    /**
     * <h1>Check if an option is set</h1>
     * Same as {@link #isOptionCheckedInCDT(IProject project, String optionIDString) isOptionChecked_inCDT },
     * except you specify tool name manually. <br>
     *
     * (e.g you need to check something that's not supported in the implementation above.
     * @param project         the IProject project which will be read to check if it is c or cpp
     * @param optionIDString  for example <code> gnu.cpp.compiler.option.debugging.codecov </code>
     * @param toolSuperClassId  superclass id of the parent tool. see {@link #helperGetToolSuperClassId helper_GetToolSuperClassId}
     * @return                true if the option is set
     */
    private static boolean isOptionCheckedInCDTTool(IProject project, String optionIDString, String toolSuperClassId) {

        IConfiguration activeConf = helperGetActiveConfiguration(project);

        //Get Compiler tool.
        ITool gccCompileriTool = helperGetGccCompilerToolBySuperClass(toolSuperClassId, activeConf);

        //(Get immutable option: This is like a 'template' that we will use to get the actual option)
        IOption optionTemplate = gccCompileriTool.getOptionById(optionIDString);

        //Check that we got a good option.
        if (optionTemplate == null) {
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetOptionTemplate);
            return false;
        }

        // Get Actual Option
        // (Now we acquire the actual option from which we can read the value)
        try {
            IOption mutableOptionToSet = gccCompileriTool.getOptionToSet(optionTemplate, false);
            return (boolean) mutableOptionToSet.getValue();
        } catch (BuildException e) {
            //This is reached if the template that was provided was bad.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetOptionForWriting);
        }
        return false;

    }

    /**
     * <h1> Enable a checkbox in the tools preference store and save to disk.</h1>
     * <p>  The tools prefernce store is where most compiler build flags are stored. <br>
     * More specifically for 'debug' flags like gprof and gCov</p>
     *
     * <p>If you don't know how to get your IProject, see example: <br>
     * <code> com.lembed.codeanalyzer.internal.gprof.launch.GprofLaunchConfigurationDelegate.getProject() </code></p>
     *
     * <p>Related wiki:
     * <a href="https://wiki.eclipse.org/CDT/Developer/Code_Snippets#Programmatically_set_an_option_in_the_project_settings">
     * Programmaticall check option wiki page. </a></p>
     *
     * @param project
     *            I project for which to set the flag
     * @param optionIDString
     *            ID of option as defined in plugin.xml. e.g gnu.cpp.compiler.option.debugging.gprof
     * @param value
     *            true or false
     * @return false if something went wrong. True otherwise
     */
    public static boolean setOptionInCDT(IProject project, String optionIDString, boolean value) {
        // Set Tool Name.
        String toolSuperClassId = helperGetToolSuperClassId(project);
        if (toolSuperClassId == null) {
            return false;
        }
        return setOptionInCDTTool(project, optionIDString, value, toolSuperClassId);
    }

    /**
     * <h1>Set Option in CDT</h1>
     * Same as {@link #setOptionInCDT(IProject project, String optionIDString, boolean value) setOption_in } <br>
     * except you can specify the parent tool manually (in case current implementation does not support what yon need.
     *
     * @param project         an IProject
     * @param optionIDString  ID of option as defined in plugin.xml. e.g gnu.cpp.compiler.option.debugging.gprof
     * @param value           true/false
     * @param toolSuperClassId
     *                   Name of the tool where the option resides. E.g 'GCC C Compiler' or 'GCC C++ Compiler'. <br>
     *                   To find out, check/uncheck an option, inspect the .cproject file, look for the option,<br>
     *                   then see what tool it's under. See the name property
     * @return                true if all went well, false otherwise
     */
    private static boolean setOptionInCDTTool(IProject project, String optionIDString, boolean value, String toolSuperClassId) {

        // Get configuration
        IConfiguration activeConf = helperGetActiveConfiguration(project);

        // Get the ITool the option.
        ITool gccCompileriTool = helperGetGccCompilerToolBySuperClass(toolSuperClassId, activeConf);

        // Get Template Opiton.
        //Get Option ~Immutable. This is like a 'templete' that we will base the actual option on.
        IOption optionTemplate = gccCompileriTool.getOptionById(optionIDString);

        //Check that we got a good option template.
        if (optionTemplate == null) {
            //This could fail if the specified option doesn't exist or is miss-spelled.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetOptionTemplate);
            return false;
        }

        // Get Actual Option
        //
        // Now we acquire an option that can be 'set' to something.
        // In contrast to the immutable option above, if the user never checked/unchecked the option by hand,
        // then the first time 'set' of this option will work correctly. Whereas
        // the immutable option would only work if the user checked/unchecked the option by hand before.
        IOption mutableOptionToSet = null;
        try {
            mutableOptionToSet = gccCompileriTool.getOptionToSet(optionTemplate, false);
            mutableOptionToSet.setValue(value);
        } catch (BuildException e) {
            //This is reached if the template that was provided was bad.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetOptionForWriting);
        }

        // get resource info. (where things are saved to).
        IResourceInfo resourceInfo = activeConf.getResourceInfos()[0];

        // Mark the option as enabled in the build manager.
        ManagedBuildManager.setOption(resourceInfo, gccCompileriTool, mutableOptionToSet,
                true);

        // Save this business to disk.
        ManagedBuildManager.saveBuildInfo(project, true);
        return true;
    }

    /**
     * <h1>Option enabled check</h1>
     * <p> Check to see if an option is enabled in the .autotools configuration.</p>
     *
     * @param project  the IProject project which will be read to check if it is c or cpp.
     * @param optionId copy paste directly from .autotools. pick the 'ID' field value.
     * @return true if it is
     */
    public static boolean isOptionCheckedInAutotoolsPrefStore(final IProject project, final String optionId) {

        //We define a 'final' variable that will be accessible in the runnable object.
        final BooleanWithGetSet userChoiceBool = new BooleanWithGetSet(false);

        // need to run this in the ui thread otherwise get SWT Exceptions
        // based on concurrency issues.
        Display.getDefault().syncExec(() -> {

            //Code copied from private methd: SetAutotoolsStringOptionValue.setOptionValue()
            //Except I added a line to save the configuration to disk as well.
            AutotoolsConfigurationManager.getInstance().syncConfigurations(project);
            ICConfigurationDescription cfgds = CoreModel.getDefault()
            .getProjectDescription(project).getActiveConfiguration();
            if (cfgds != null) {
                IAConfiguration iaConfig = AutotoolsConfigurationManager.getInstance()
                .getConfiguration(project, cfgds.getId());

                //Read option value
                IConfigureOption option = iaConfig.getOption(optionId);
                String optValString = option.getValue();
                boolean optVal = Boolean.parseBoolean(optValString);
                userChoiceBool.setVal(optVal);
            }
        });

        return userChoiceBool.getVal();
    }

    /**
     * <h1>Set Autotools option and write to disk.</h1>
     *
     * <p> Set an option (as well as flags) in the .autotools configuration and update gui. <br>
     * It is oblivious as to whether the option ID is an option or a flag, it just looks at the ID in the xml. </p>
     *
     * <p> It is designed so that it can be ran from a background thread.
     * It syncs with the GUI thread to avoid concurrency exceptions. </p>
     *
     * <p> *this modifies gui checkbox options* <b>as well as</b> *saving the option to disk*. </p>
     *
     * @param project    the IProject project which will be read to check if it is c or cpp.
     * @param optId      Id of option to set. Take directly out of .autotools. a 'flag' is also an option.
     * @param optVal     string value of the option. e.g "true"  "1234";
     */
    public static void setOptionInAutotools(final IProject project, final String optId, final String optVal) {

        // need to run this in the ui thread otherwise get SWT Exceptions
        // based on concurrency issues.
        Display.getDefault().syncExec(() -> {

            //Code copied from private methd: SetAutotoolsStringOptionValue.setOptionValue()
            //Except I added a line to save the configuration to disk as well.
            AutotoolsConfigurationManager.getInstance().syncConfigurations(project);
            ICConfigurationDescription cfgds = CoreModel.getDefault().
            getProjectDescription(project).getActiveConfiguration();

            if (cfgds != null) {
                IAConfiguration iaConfig = AutotoolsConfigurationManager.getInstance()
                .getConfiguration(project, cfgds.getId());

                //Set option value.
                iaConfig.setOption(optId, optVal);

                //Save option to disk.
                AutotoolsConfigurationManager.getInstance().saveConfigs(project);
            }
        });
    }

    /**
     * <h1>Trigger a re-build of the project.</h1>
     *
     * <p> Given a project, it finds the active configuration and rebuilds the project. </p>
     *
     * <p> This works with C/C++ Managed Builds as well as Autotools.</p>
     *
     * <p>Most useful for when you have added a flag to a project programmatically and want to rebuild
     * the project so that the program is rebuild with that flag. The Rebuild triggers an update of the makefile
     * automatically.</p>
     *
     * @param project to rebuild
     */
    public static void rebuildProject(IProject project) {
        //rebuild does not generate an analysis file (e.g gmon.out) But since -pg flag is added, profiling support is added to the binary.
        try {
            IBuildConfiguration buildConfiguration = project.getActiveBuildConfig();

            //force a full rebuild which is usually needed for when you add profiling flags to the build options.
            project.build(buildConfiguration, IncrementalProjectBuilder.FULL_BUILD, null);
        } catch (CoreException e) {

            //This is a very rare occurrence. Usually rebuilds don't fail if they worked the first time.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorRebuilding);
        }
    }

    /*
     *  PRIVATE HELPERS BELOW
     */

    /**
     * <p>Helper to get the active build configuration.</p>
     * @param project IProject for which to get the configuration.
     * @return IConfiguration of that project.
     */
    private static IConfiguration helperGetActiveConfiguration(IProject project) {
        IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
        return buildInfo.getDefaultConfiguration();
    }

    /**
     * <p> For a project, calculate the expected superclass id of the compiler tool we expect the compiler tool to be under.</p>
     *
     * @param project IProject for which to get the tool superclass id.
     * @return superclass id of the parent tool.
     */
    private static String helperGetToolSuperClassId(IProject project) {
        String superClassId = null;
        if (isCType(project)) {
            superClassId = "cdt.managedbuild.tool.gnu.c.compiler"; //$NON-NLS-1$
        } else if (isCppType(project)) {
            superClassId = "cdt.managedbuild.tool.gnu.cpp.compiler"; //$NON-NLS-1$
        } else {
            //This should happen only if project natures are broken. Maybe the .project file is corrupted.
            MessageDialogSyncedRunnable.openErrorSyncedRunnable(ProfilingMessages.errorTitle, ProfilingMessages.errorGetProjectToolname);
            return null;
        }
        return superClassId;
    }

    /**
     * <h1>Get the tool that has the given id at the top of its superclass chain.</h1>
     *
     * @param superClassId a string representing the expected top-most superclass id of the compiler tool.
     * @param activeConf The current active configuration of the project, from which we should be able to find the ITool.
     * @return the 'ITool' instance.
     */
    private static ITool helperGetGccCompilerToolBySuperClass(String superClassId, IConfiguration activeConf) {
        ITool[] tools = activeConf.getTools();
        ITool gccCompileriTool = null;
        for (ITool iTool : tools) {
            ITool tool = iTool;
            while (tool.getSuperClass() != null) {
                tool = tool.getSuperClass();
            }
            if (tool.getId().equals(superClassId)) {
                gccCompileriTool = iTool;
                break;
            }
        }
        return gccCompileriTool;
    }

}
