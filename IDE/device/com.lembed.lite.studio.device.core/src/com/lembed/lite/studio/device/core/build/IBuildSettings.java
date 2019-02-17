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
package com.lembed.lite.studio.device.core.build;

import java.util.Collection;

import com.lembed.lite.studio.device.generic.IAttributes;

/**
 * This interface contains build settings obtained from LITE configuration<br>
 * It provides methods with common types to avoid dependencies on ILiteConfiguration internal details,
 */
public interface IBuildSettings extends IAttributes {

	/**
	 * Retrieves setting value as collection of strings (defines, include paths, etc)
	 * @param type a type of setting to retrieve, see most commonly used type descriptions below 
	 * @return the settings value as collection of strings or <code>null</code> if there is no string list for that type
	 * @see IOption
	 */
	Collection<String> getStringListValue(int type);

	/**
	 * Adds a value string list entry to corresponding collection
	 * @param type a type of setting to add, see most commonly used type descriptions below
	 * @param value value to add
	 */
	public void addStringListValue(int type, String value);

	/**
	 * Retrieves attribute of selected device<br>
	 * See: <a href="http://www.keil.com/pack/doc/CMSIS/Pack/html/pdsc_family_pg.html#element_processor">"http://www.keil.com/pack/doc/CMSIS/Pack/html/pdsc_family_pg.html#element_processor"</a><br>
	 * @param key processor attribute name or one of: "Dname", "Dfamily", "DsubFamily", "Dvariant"
	 * @return device attribute
	 * @note the returned value in most cases cannot be set to an {@link IOption} directly<br>
	 *  it should be converted to toolchain-specific value(s) first
	 */
	String getDeviceAttribute(String key);


	/**
	 * Returns single linker script file (or scatter file)
	 * @return linker script the very first occurrence of the file if any 
	 */
	String getSingleLinkerScriptFile();

	// several build-specific string constants could appear in PDSC file: 	
	static public final String AMISC_TAG		= "AMisc";		//$NON-NLS-1$ 
	static public final String CMISC_TAG		= "CMisc";		//$NON-NLS-1$ 
	static public final String CPPMISC_TAG		= "CPPMisc";	//$NON-NLS-1$
	static public final String LMISC_TAG		= "LMisc";		//$NON-NLS-1$
	static public final String ARMISC_TAG		= "ARMisc";		//$NON-NLS-1$
	static public final String PRE_BUILD_TAG	= "preBuild";	//$NON-NLS-1$
	static public final String POST_BUILD_TAG	= "postBuild";	//$NON-NLS-1$

	// LITE option types most commonly used by getStringListValue(), addStringListValue() and LiteToolChainAdapter.getLiteOptionType()
	// not implemented as enum to be easily extensible
	public static final int UNKNOWN_OPTION 	= -1;
    // all other options start with 100
	public static final int LITE_OPTION		= 100;
	// options provided by LITE configuration, updated every time project loaded or component added/removed  
	public static final int LITE_DEFINES         = LITE_OPTION + 1; // option to hold LITE preprocessor definitions
	public static final int LITE_INCLUDE_PATH   	= LITE_OPTION + 2; // option to hold LITE includes
	public static final int LITE_LIBRARIES    	= LITE_OPTION + 3; // option to hold LITE library's
	public static final int LITE_LIBRARY_PATHS  	= LITE_OPTION + 4; // option to hold LITE library paths
	public static final int LITE_OBJECTS   		= LITE_OPTION + 5; // option to hold LITE additional object files 
	public static final int LITE_LINKER_SCRIPT   = LITE_OPTION + 6; // linker script/scatter file

	// user-defined options (defined in derived LiteConfiguration or IEnvironnmentProvider) should start here
	public static final int LITE_USER_OPTION	= LITE_OPTION + 10;     
	
	// options that could be set during project creation, device change or forced option restore.  
	public static final int LITE_INITIAL_OPTION = LITE_OPTION  + 100;

	public static final int LITE_CMISC  	= LITE_INITIAL_OPTION + 2; // LITE C compiler miscellaneous
	public static final int LITE_CPPMISC = LITE_INITIAL_OPTION + 3; // LITE CPP compiler miscellaneous
	public static final int LITE_ASMMISC	= LITE_INITIAL_OPTION + 4; // LITE assembler miscellaneous 
	public static final int LITE_ARMISC  = LITE_INITIAL_OPTION + 5; // LITE archiver (librarian) miscellaneous
	public static final int LITE_LMISC	= LITE_INITIAL_OPTION + 6; // LITE linker miscellaneous

	// options that are specific to tool chain integration 
	// most commonly used options are listed here (it is not mandatory to use all/any of them)
	public static final int TOOL_CHAIN_OPTION	= LITE_INITIAL_OPTION + 100; 
	
	public static final int PRE_BUILD_STEPS 	= TOOL_CHAIN_OPTION + 1; // pre-build steps
	public static final int POST_BUILD_STEPS 	= TOOL_CHAIN_OPTION + 2; // post-build steps

	public static final int CDEFINES_OPTION 	= TOOL_CHAIN_OPTION + 3;  // C compiler preprocessor definitions (editable)
	public static final int CPPDEFINES_OPTION 	= TOOL_CHAIN_OPTION + 4;  // CPP compiler preprocessor definitions (editable)
	public static final int ADEFINES_OPTION 	= TOOL_CHAIN_OPTION + 5;  // assembler compiler preprocessor definitions (editable)

	public static final int CINCPATHS_OPTION 	= TOOL_CHAIN_OPTION + 6;  // C compiler include paths (editable)
	public static final int CPPINCPATHS_OPTION 	= TOOL_CHAIN_OPTION + 7;  // CPP compiler include paths (editable)
	public static final int ASMINCPATHS_OPTION 	= TOOL_CHAIN_OPTION + 8;  // assembler include paths (editable)

	public static final int LIBS_OPTION 		= TOOL_CHAIN_OPTION + 9; // libraries for linker
	public static final int LIBPATH_OPTION 		= TOOL_CHAIN_OPTION + 10; // library paths for linker
	public static final int OBJECTS_OPTION 		= TOOL_CHAIN_OPTION + 11; // objects for linker
	
	public static final int CMISC_OPTION 		= TOOL_CHAIN_OPTION + 12; // C compiler miscellaneous
	public static final int CPPMISC_OPTION 		= TOOL_CHAIN_OPTION + 13; // CPP compiler miscellaneous
	public static final int AMISC_OPTION 		= TOOL_CHAIN_OPTION + 14; // assembler miscellaneous
	public static final int ARMISC_OPTION 		= TOOL_CHAIN_OPTION + 15; // archiver (librarian) miscellaneous
	public static final int LMISC_OPTION 		= TOOL_CHAIN_OPTION + 16; // linker miscellaneous

	// options specific to target device   		
	public static final int TOOLCHAIN_DEVICE_OPTION	= TOOL_CHAIN_OPTION  + 40;
	public static final int CPU_OPTION 			= TOOLCHAIN_DEVICE_OPTION + 1; 
	public static final int ARCH_OPTION 		= TOOLCHAIN_DEVICE_OPTION + 2; // architecture 
	public static final int INSTR_SET_OPTION 	= TOOLCHAIN_DEVICE_OPTION + 3; // instruction set
	public static final int THUMB_OPTION 		= TOOLCHAIN_DEVICE_OPTION + 4; // thumb option if separate from instruction set
	public static final int ENDIAN_OPTION 		= TOOLCHAIN_DEVICE_OPTION + 5;
	public static final int FPU_OPTION 			= TOOLCHAIN_DEVICE_OPTION + 6;
	public static final int FLOAT_ABI_OPTION 	= TOOLCHAIN_DEVICE_OPTION + 7;
	
	// initial tool chain-specific options (defined in derived tool chain adapters) should start here
	public static final int TOOLCHAIN_USER_OPTION = TOOL_CHAIN_OPTION  + 100;   


}
