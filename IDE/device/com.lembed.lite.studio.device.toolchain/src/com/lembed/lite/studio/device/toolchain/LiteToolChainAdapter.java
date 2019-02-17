/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * Copyright (c) 2017 LEMBED
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 * LEMBED - adapter for LiteSTUDIO
 *******************************************************************************/
package com.lembed.lite.studio.device.toolchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.runtime.PlatformObject;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.build.IBuildSettings;
import com.lembed.lite.studio.device.utils.Utils;

/**
 * Generic tool chain adapter implementation, uses PlatformObject's
 * implementation of IAdaptable interface
 */
public abstract class LiteToolChainAdapter extends PlatformObject implements ILiteToolChainAdapter {

	protected boolean bInitialUpdate = false;

	@Override
	public void setToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings) {

		if (configuration == null || buildSettings == null) {
			return;
		}

		if (bInitialUpdate) {
			updateBuildSteps(configuration, buildSettings, IBuildSettings.PRE_BUILD_STEPS);
			updateBuildSteps(configuration, buildSettings, IBuildSettings.POST_BUILD_STEPS);
		}

		IToolChain toolchain = configuration.getToolChain();
		if (toolchain == null) {
			return;
		}
		// iterate over tool chain options
		updateOptions(configuration, toolchain, buildSettings);

		// iterate over tools
		ITool[] tools = toolchain.getTools();
		for (ITool t : tools) {
			if (t == null || !t.isEnabled()) {
				continue;
			}
			updateOptions(configuration, t, buildSettings);
		}
	}

	@Override
	public void setInitialToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings) {
		// default updates all options
		bInitialUpdate = true;
		setToolChainOptions(configuration, buildSettings);
		bInitialUpdate = false;
	}

	/**
	 * Updates tool chain/tool options for given configuration
	 * 
	 * @param configuration
	 *            option's parent IConfiguration
	 * @param tool
	 *            IHoldsOptions representing ITool or IToolChain
	 * @param buildSettings
	 *            IBuildSettings containing source LITE information
	 */
	protected void updateOptions(IConfiguration configuration, IHoldsOptions tool, IBuildSettings buildSettings) {
		IOption[] options = tool.getOptions();
		for (IOption o : options) {
			try {
				if (o != null) {
					updateOption(configuration, tool, o, buildSettings);
				}
			} catch (BuildException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates an option that contains preprocessor defines, libraries, include
	 * paths or library paths .<br>
	 * Removes all options after <code>_LITE_</code> and adds new defines.
	 * 
	 * @param configuration
	 *            option's parent IConfiguration
	 * @param tool
	 *            option's parent IHoldsOptions (ITool or IToolChain )
	 * @param option
	 *            IOption to update
	 * @param buildSettings
	 *            IBuildSettings containing source LITE information
	 * @throws BuildException
	 */
	protected void updateOption(IConfiguration configuration, IHoldsOptions tool, IOption option,
			IBuildSettings buildSettings) throws BuildException {
		int oType = getOptionType(option);

		if (!bInitialUpdate && isInitialOption(oType)) {
			return; // initial update only
		}

		if (oType > IBuildSettings.LITE_OPTION) {
			updateLiteOption(oType, configuration, tool, option, buildSettings);
		}
	}

	/**
	 * Updates LITE option value
	 * 
	 * @param oType
	 *            option's type : see getLiteOptionType()
	 * @param configuration
	 *            option's parent IConfiguration
	 * @param tool
	 *            option's parent IHoldsOptions
	 * @param option
	 *            IOption to update
	 * @param buildSettings
	 *            IBuildSettings containing source LITE information
	 * @throws BuildException
	 */
	protected void updateLiteOption(int oType, IConfiguration configuration, IHoldsOptions tool, IOption option,
			IBuildSettings buildSettings) throws BuildException {
		int type = option.getBasicValueType();

		if (type == IOption.STRING_LIST) {
			setStringListOptionValue(oType, configuration, tool, option, buildSettings);
			return;
		}

		String value = getLiteOptionValue(oType, buildSettings, option);
		if (value != null) {
			if (type == IOption.BOOLEAN) {
				boolean bVal = value.equals("1") || value.equalsIgnoreCase("true"); //$NON-NLS-1$ //$NON-NLS-2$
				ManagedBuildManager.setOption(configuration, tool, option, bVal);
			} else {
				ManagedBuildManager.setOption(configuration, tool, option, value);
			}
		}
	}
	
	@Override
	public void resetToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings) throws BuildException {

		if (configuration == null || buildSettings == null) {
			return;
		}

		IToolChain toolchain = configuration.getToolChain();
		if (toolchain == null) {
			return;
		}
		// iterate over tool chain options
		resetLiteOption(configuration, toolchain, buildSettings);
		
		// iterate over tools
		ITool[] tools = toolchain.getTools();
		for (ITool t : tools) {
			if (t == null) {
				continue;
			}
			resetLiteOption(configuration, t, buildSettings);
		}
	}
	
	
	protected void resetLiteOption(IConfiguration configuration, IHoldsOptions tool, IBuildSettings buildSettings)
			throws BuildException {

		IOption[] options = tool.getOptions();
		for (IOption option : options) {
			try {
				if (option != null) {	
					/**
					 * just the string list option
					 */
					int type = option.getBasicValueType();
					
					/**
					 *  Specification type to LITE Build settings,
					 *  get type through option id, or super class id
					 */
					int oType = getOptionType(option);
					if (type == IOption.STRING_LIST) {
						resetStringListOptionValue(oType, configuration, tool, option, buildSettings);
					}
				}
			} catch (BuildException e) {
				e.printStackTrace();
			}
		}

	}
	
	protected void resetStringListOptionValue(int oType, IConfiguration configuration, IHoldsOptions tool, IOption option,
			IBuildSettings buildSettings) throws BuildException {
		Collection<String> qValues = new HashSet<String>();
		
		//log(oType + " = oType = " + option.getValueType() +" " +option.getBaseId());		
		if (option.getBasicValueType() != IOption.STRING_LIST) {
			return;
		}
		
		/**
		 * collect the current string list of the option
		 */
		List<String> currentValues = getCurrentStringListValue(option);
		if (currentValues == null) {
			currentValues = getCurrentStringListValue(option);
		}
		
		if (currentValues == null) {
			currentValues =  new ArrayList<String>();
		}
		
		// remove duplicate item of current values
		List<String> currentValueNew = new ArrayList<String>();
		for(String v : currentValues){
			if(!currentValueNew.contains(v)){
				currentValueNew.add(v);
			}
		}
		
		/**
		 * the buildSettings is old build configuration, get the old settings, which 
		 * need to be cleaned
		 */
		Collection<String> needToRemoveValues = getStringListValue(buildSettings, oType);
		if(needToRemoveValues == null){			
			needToRemoveValues = new HashSet<String>();
		}
		
		//log("current value      size = " +      currentValues.size());
		//log("needToRemoveValues size = " + needToRemoveValues.size());
		// remove duplicate item of current values
		List<String> evalue = new ArrayList<String>();
		for(String v : needToRemoveValues){			
			if(!evalue.contains(v)){				
				evalue.add(v);
			}
		}
		
		needToRemoveValues = evalue;
		for (String s : needToRemoveValues) {			
			if (isToQuoteOption(oType, option)) {
				String qs = Utils.addQuotes(s);
				qValues.add(qs);
			} else {
				qValues.add(s);
			}		
		}
		
		/**
		 * remove duplicate symbols
		 */
		List<String> tmpvalue = new ArrayList<String>();		
		for(String ov : qValues){			
			if(!tmpvalue.contains(ov)){
				tmpvalue.add(ov);				
			}
		}
		
		for(String ovs : tmpvalue){			
			if(currentValueNew.contains(ovs)){
				currentValueNew.remove(ovs);				
			}
		}
		
		// copy to array and add quotes if needed
		String[] arrayValue = new String[currentValueNew.size()];
		int i = 0;
		for (String s : currentValueNew) {
			arrayValue[i] = s;
			i++;
		}

		ManagedBuildManager.setOption(configuration, tool, option, arrayValue);
	}
	
	/**
	 * Returns LITE values for specification type (IBuildSettings.LITE_DEFINES, others)
	 * if the type is IBuildSettings.LITE_DEFINES this function will return HashMap instance
	 * of the symbols of CMSIS
	 * 
	 * @param buildSettings
	 *            IBuildSettings to get value from
	 * @param type
	 *            option type returned by getOptionType(IOption option)
	 * @return collection of strings for the option
	 */
	protected abstract Collection<String> getStringListValue(IBuildSettings buildSettings, int type);

	/**
	 * Updates string list option values
	 * 
	 * @param oType
	 *            option's extended type: see getOptionType()
	 * @param configuration
	 *            option's parent IConfiguration
	 * @param tool
	 *            option's parent IHoldsOptions
	 * @param option
	 *            IOption to update
	 * @throws BuildException
	 */
	protected void setStringListOptionValue(int oType, IConfiguration configuration, IHoldsOptions tool, IOption option,
			IBuildSettings buildSettings) throws BuildException {		
		
		if (option.getBasicValueType() != IOption.STRING_LIST) {
			return;
		}

		List<String> value = getCurrentStringListValue(option);
		if (value == null) {
			return;
		}

		value = cleanStringList(value, oType);
		Collection<String> newValue = getStringListValue(buildSettings, oType);
		if (newValue != null) {
			value.addAll(newValue);
		}
		
		Collection<String> qValues = new HashSet<String>();
		for(String v : value){
			if (isToQuoteOption(oType, option)) {
				/**
				 * this will add include path from pack include and "LITE" directory
				 * to linker options arrayValue[i] = Utils.addQuotes(s);
				 */
				qValues.add(Utils.addQuotes(v));
			} else {
				qValues.add(v);
			}
		}
		
		Collection<String> fValues = new HashSet<String>();
		for(String vs : qValues){
			if(!fValues.contains(vs)){
				fValues.add(vs);
			}
		}
		
		// copy to array and add quotes if needed
		String[] arrayValue = new String[fValues.size()];
		int i = 0;
		for (String s : fValues) {			
			arrayValue[i] = s;			
			i++;
		}
		
		ManagedBuildManager.setOption(configuration, tool, option, arrayValue);
	}

	/**
	 * Checks if specified option type is for initial setting only
	 * 
	 * @param oType
	 *            option type
	 * @return true if option is an initial option
	 */
	protected boolean isInitialOption(int oType) {
		return oType > IBuildSettings.LITE_INITIAL_OPTION;
	}

	/**
	 * Checks if option value shout be quoted
	 * 
	 * @param oType
	 *            option's extended type: see getOptionType()
	 * @param option
	 *            IOption to check
	 * @return true if surround with quotes
	 */
	protected boolean isToQuoteOption(int oType, IOption option) {
		if (oType == IBuildSettings.LITE_LINKER_SCRIPT) {
			return true;
		}
		int valueType;
		try {
			valueType = option.getValueType();
		} catch (BuildException e) {
			e.printStackTrace();
			return false;
		}
		switch (valueType) {
		case IOption.INCLUDE_PATH:
		case IOption.LIBRARY_PATHS:
		case IOption.LIBRARIES:
		case IOption.OBJECTS:
			return true;
		case IOption.PREPROCESSOR_SYMBOLS:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Retrieves current string list value form IOption
	 * 
	 * @param option
	 *            option from which to retrieve string list
	 * @return string list or null if there is no value for this option
	 * @throws BuildException
	 */
	protected List<String> getCurrentStringListValue(IOption option) throws BuildException {
		String[] array = null;
		int type = option.getValueType();
		switch (type) {
		case IOption.PREPROCESSOR_SYMBOLS:
			array = option.getDefinedSymbols();
			break;
		case IOption.INCLUDE_PATH:
			array = option.getIncludePaths();
			break;
		case IOption.LIBRARY_PATHS:
			array = option.getLibraryPaths();
			break;
		case IOption.LIBRARIES:
			array = option.getLibraries();
			break;
		case IOption.OBJECTS:
			array = option.getUserObjects();
			break;
		case IOption.STRING_LIST:
			array = option.getStringListValue();
			break;
		default:
			break;
		}
		if (array == null) {			
			return null;
		}
			
		return new ArrayList<String>(Arrays.asList(array));
	}

	/**
	 * Returns current string value stored by option of STRING base type
	 * 
	 * @param option
	 *            option to get value from
	 * @return option value as String, null if option base type is not STRING
	 */
	public String getCurrentStringValue(IOption option) {
		try {
			int type = option.getBasicValueType();
			if (type == IOption.STRING) {
				return option.getStringValue();
			}
		} catch (BuildException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Removes entries from a string list for given option
	 * 
	 * @param value
	 *            list of strings to clean
	 * @param oType
	 *            option's extended type: see getOptionType()
	 */
	protected List<String> cleanStringList(List<String> value, int oType) {
		switch (oType) {
		case IBuildSettings.LITE_DEFINES:
			value = truncateStringList(value, CmsisConstants._LITE_);
			break;
		case IBuildSettings.LITE_INCLUDE_PATH:
		case IBuildSettings.LITE_LIBRARY_PATHS:
		case IBuildSettings.LITE_LIBRARIES:
		case IBuildSettings.LITE_OBJECTS:
		case IBuildSettings.LITE_LINKER_SCRIPT:
			value = removeLitePathEntries(value);
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * Removes all entries in the list after truncateFrom string (inclusive
	 * truncateFrom entry)
	 * 
	 * @param strings
	 *            list of strings to truncate
	 * @param truncateFrom
	 * @return updated list
	 */
	static public List<String> truncateStringList(List<String> strings, String truncateFrom) {
		if (strings == null) {
			return null;
		}
		int index = strings.indexOf(truncateFrom);
		if (index >= 0) {
			return strings.subList(0, index);
		}
		return strings;
	}

	/**
	 * Removes all entries beginning with LITE or ${cmsis_pack_root} paths from
	 * supplied list
	 * 
	 * @param paths
	 *            list of paths/files to process
	 * @return updated list
	 */
	static public List<String> removeLitePathEntries(List<String> paths) {
		for (Iterator<String> iterator = paths.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			if (s.startsWith(CmsisConstants.PROJECT_LITE_PATH, 1) || s.startsWith(CmsisConstants.CMSIS_PACK_ROOT_VAR, 1)
					|| s.startsWith(CmsisConstants.CMSIS_LITE_VAR, 1)) {
				iterator.remove();
			}
		}
		return paths;
	}

	/**
	 * Returns if CPU can have FPU
	 * 
	 * @param cpu
	 *            core name
	 * @return true
	 */
	public boolean coreHasFpu(String cpu) {
		if (cpu == null) {
			return false;
		}
		switch (cpu) {
		case "SC000": //$NON-NLS-1$
		case "SC300": //$NON-NLS-1$
		case "Cortex-M0": //$NON-NLS-1$
		case "Cortex-M0+": //$NON-NLS-1$
		case "Cortex-M1": //$NON-NLS-1$
		case "Cortex-M3": //$NON-NLS-1$
		case "ARMV8MBL": //$NON-NLS-1$
			return false;
		default:
		case "Cortex-M4": //$NON-NLS-1$
		case "Cortex-M7": //$NON-NLS-1$
		case "Cortex-R4": //$NON-NLS-1$
		case "Cortex-R5": //$NON-NLS-1$
		case "Cortex-A5": //$NON-NLS-1$
		case "Cortex-A7": //$NON-NLS-1$
		case "Cortex-A8": //$NON-NLS-1$
		case "Cortex-A9": //$NON-NLS-1$
		case "Cortex-A15": //$NON-NLS-1$
		case "Cortex-A17": //$NON-NLS-1$
		case "Cortex-A53": //$NON-NLS-1$
		case "Cortex-A57": //$NON-NLS-1$
		case "Cortex-A72": //$NON-NLS-1$
		case "ARMV8MML": //$NON-NLS-1$
			return true;
		}
	}

	/**
	 * Return option type: base or LITE one
	 * 
	 * 
	 * 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.assembler.defs
		* -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.assembler.undefs
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.paths
		* 8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.files
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.assembler.flags
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.defs
		* -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.paths
		* 8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.files
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.defs
		* -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.paths
		* 8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.files
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.c.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.c.linker.paths
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.c.linker.otherobjs
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.paths
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.otherobjs
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.createflash.othersection
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.assembler.defs
		* -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.assembler.undefs
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.paths
		* 8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.files
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.assembler.flags
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.defs
		* -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.files
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.files
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.c.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.c.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.c.linker.otherobjs
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.otherobjs
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.createflash.othersection
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.assembler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.assembler.undefs
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.files
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.assembler.flags
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.files
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.files
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.c.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.c.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.c.linker.otherobjs
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.otherobjs
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.createflash.othersection
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.assembler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.assembler.undefs
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.assembler.include.files
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.assembler.flags
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.c.compiler.include.files
		* 101 = oType = 5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.defs
		*  -5 = oType = -5 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.undef
		* 102 = oType = 4 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.paths
		* 	8 = oType = 8 com.lembed.lite.studio.managedbuild.cross.option.cpp.compiler.include.files
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.c.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.c.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.c.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.c.linker.otherobjs
		* 106 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.scriptfile
		* 103 = oType = 6 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.libs
		* 104 = oType = 9 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.paths
		* 	3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.flags
		* 105 = oType = 7 com.lembed.lite.studio.managedbuild.cross.option.cpp.linker.otherobjs
		* 3 = oType = 3 com.lembed.lite.studio.managedbuild.cross.option.createflash.othersection
	 * 
	 * @param option
	 *            IOption to get type
	 * @return positive integer if it is a known option, -1 otherwise
	 */
	public int getOptionType(IOption option) {
		if (option == null) {
			return IBuildSettings.UNKNOWN_OPTION;
		}

		for (IOption o = option; o != null; o = o.getSuperClass()) {
			String id = o.getId();
			
			// implement by child class
			int liteType = getLiteOptionType(id);
			if (liteType > IBuildSettings.LITE_OPTION) {
				return liteType;
			}
			
			// get the option super class id
			id = o.getBaseId();
			liteType = getLiteOptionType(id);
			if (liteType > IBuildSettings.LITE_OPTION) {
				return liteType;
			}
		}
		
		// get the type by value type
		try {
			return getOptionType(option.getValueType());
		} catch (BuildException e) {
			e.printStackTrace();
		}
		return IBuildSettings.UNKNOWN_OPTION;
	}

	protected abstract int getLiteOptionType(String id);

	protected int getOptionType(int valueType) {
		// default converts some base types to LITE ones
		switch (valueType) {
		case IOption.PREPROCESSOR_SYMBOLS:
			return IBuildSettings.LITE_DEFINES;
		case IOption.INCLUDE_PATH:
			return IBuildSettings.LITE_INCLUDE_PATH;
		case IOption.LIBRARIES:
			return IBuildSettings.LITE_LIBRARIES;
		case IOption.LIBRARY_PATHS:
			return IBuildSettings.LITE_LIBRARY_PATHS;
		case IOption.OBJECTS:
			return IBuildSettings.LITE_OBJECTS;
		default:
			break;
		}
		return valueType;
	}

	/**
	 * Returns option value for given option type
	 * 
	 * @param oType
	 *            LITE option type
	 * @param buildSettings
	 *            IBuildSettings containing source LITE information
	 * @param option
	 *            IOption for which to get new value
	 * @return new option value as a string
	 */
	protected String getLiteOptionValue(int oType, IBuildSettings buildSettings, IOption option) {
		if (oType > IBuildSettings.LITE_OPTION) {
			switch (oType) {
			case IBuildSettings.LITE_LINKER_SCRIPT:
				return getLinkerSrciptOptionValue(buildSettings);
			default:
				// default simply returns device attribute value
				return getDeviceAttribute(oType, buildSettings);
			}
		}
		return null;
	}

	/**
	 * Returns device attribute for given option type
	 * 
	 * @return CPU option string
	 */
	protected String getDeviceAttribute(int oType, IBuildSettings buildSettings) {
		switch (oType) {
		case IBuildSettings.CPU_OPTION:
			return buildSettings.getDeviceAttribute("Dcore"); //$NON-NLS-1$
		case IBuildSettings.FPU_OPTION:
			return buildSettings.getDeviceAttribute("Dfpu"); //$NON-NLS-1$
		case IBuildSettings.ENDIAN_OPTION:
			return buildSettings.getDeviceAttribute("Dendian"); //$NON-NLS-1$
		default:
			break;
		}
		return null;
	}

	/**
	 * Returns single linker script file if it is only one
	 * 
	 * @param buildSettings
	 *            IBuildSettings to get value from
	 * @return single linker script file or null
	 */
	protected String getLinkerSrciptOptionValue(IBuildSettings buildSettings) {
		return buildSettings.getSingleLinkerScriptFile();
	}

	/**
	 * Updates pre- or post-build command for given configuration
	 * 
	 * @param configuration
	 *            destination IConfiguration to set steps to
	 * @param buildSettings
	 *            source IBuildSettings
	 * @param oType
	 *            option type: PRE_BUILD_STEPS or POST_BUILD_STEPS
	 */
	protected void updateBuildSteps(IConfiguration configuration, IBuildSettings buildSettings, int oType) {

		String step;
		if (oType == IBuildSettings.PRE_BUILD_STEPS) {
			step = configuration.getPrebuildStep();
		} else if (oType == IBuildSettings.POST_BUILD_STEPS) {
			step = configuration.getPostbuildStep();
		} else {
			return;
		}

		if (step == null) {
			// actually should not happen, but do not rely on CDT
			step = CmsisConstants.EMPTY_STRING;
		}

		// find begin and end markers
		int beginPos = step.indexOf(CmsisConstants.CMSIS_LITE_BEGIN_VAR);
		if (beginPos < 0) {
			beginPos = step.length();
		}
		int endPos = step.indexOf(CmsisConstants.CMSIS_LITE_END_VAR);
		if (endPos < 0) {
			endPos = step.length();
		} else {
			if (beginPos > endPos) {
				beginPos = endPos; // a marker has been removed by user
			}
			endPos += CmsisConstants.CMSIS_LITE_END_VAR.length();
		}

		String prefix = (beginPos > 0) ? step.substring(0, beginPos) : CmsisConstants.EMPTY_STRING;
		String suffix = (endPos >= 0) ? step.substring(endPos) : CmsisConstants.EMPTY_STRING;
		String liteCommand = getPrePostCommand(buildSettings, oType);
		String newStep = CmsisConstants.EMPTY_STRING;
		if (!prefix.isEmpty()) {
			if (prefix.endsWith(";")) { //$NON-NLS-1$
				prefix = prefix.substring(0, prefix.length() - 1);
			}
			newStep += prefix;
		}

		if (liteCommand != null && !liteCommand.isEmpty()) {
			if (!newStep.isEmpty() && !newStep.endsWith(";")) { //$NON-NLS-1$
				newStep += ';';
			}
			newStep += liteCommand;
		}

		if (!suffix.isEmpty()) {
			if (!newStep.isEmpty() && !newStep.endsWith(";") && !suffix.startsWith(";")) { //$NON-NLS-1$ //$NON-NLS-2$
				newStep += ';';
			}
			newStep += suffix;
		}

		if (step.equals(newStep)) {
			return; // nothing to do
		}

		if (oType == IBuildSettings.PRE_BUILD_STEPS) {
			configuration.setPrebuildStep(newStep);
		} else if (oType == IBuildSettings.POST_BUILD_STEPS) {
			configuration.setPostbuildStep(newStep);
		}
	}

	/**
	 * Returns assembled command of build steps
	 * 
	 * @param buildSettings
	 *            source IBuildSettings
	 * @param oType
	 *            option type: PRE_BUILD_STEPS or POST_BUILD_STEPS
	 * @return String containing assembled command of build steps
	 */
	protected String getPrePostCommand(IBuildSettings buildSettings, int oType) {
		Collection<String> steps = buildSettings.getStringListValue(oType);
		if (steps == null || steps.isEmpty()) {
			return CmsisConstants.EMPTY_STRING;
		}
		String cmd = CmsisConstants.CMSIS_LITE_BEGIN_VAR;
		for (String s : steps) {
			if (cmd.length() > CmsisConstants.CMSIS_LITE_BEGIN_VAR.length()) {
				cmd += ';';
			}
			cmd += s;
		}
		cmd += CmsisConstants.CMSIS_LITE_END_VAR;
		return cmd;
	}

	private static void log(String msg) {
		System.out.println("-------->>> " + msg + "\n");
	}

}
