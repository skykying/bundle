/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 * Liviu Ionescu - review, testing and enhancements
 *******************************************************************************/

package com.lembed.lite.studio.device.toolchain.litestudio;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.core.runtime.Platform;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.build.IBuildSettings;
import com.lembed.lite.studio.device.toolchain.ILinkerScriptGenerator;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapter;

public class LiteStudioToolChainAdapter extends LiteToolChainAdapter {

	static public final String LITE_STUDIO_TOOLCHAIN_PREFIX = "com.lembed.lite.studio.managedbuild.cross"; //$NON-NLS-1$
	static public final String LITE_STUDIO_TOOLCHAIN_ID = LITE_STUDIO_TOOLCHAIN_PREFIX + ".toolchain.base"; //$NON-NLS-1$

	static public final String LITE_STUDIO_OPTION = LITE_STUDIO_TOOLCHAIN_PREFIX + ".option"; //$NON-NLS-1$
	static public final String LITE_STUDIO_ARM_TARGET = LITE_STUDIO_OPTION + ".arm.target"; //$NON-NLS-1$
	static public final String LITE_STUDIO_CPU_OPTION = LITE_STUDIO_ARM_TARGET + ".family"; //$NON-NLS-1$
	static public final String LITE_STUDIO_FPU_OPTION = LITE_STUDIO_ARM_TARGET + ".fpu.unit"; //$NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_ARCHITECTURE = LITE_STUDIO_OPTION + ".architecture"; //$NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_TOOLCHAIN_NAME = LITE_STUDIO_OPTION + ".toolchain.name"; //$NON-NLS-1$

	static public final String LITE_STUDIO_FPU_ABI_OPTION = LITE_STUDIO_ARM_TARGET + ".fpu.abi"; //$NON-NLS-1$
	static public final String LITE_STUDIO_ENDIAN_OPTION = LITE_STUDIO_ARM_TARGET + ".endianness"; //$NON-NLS-1$
	static public final String LITE_STUDIO_INSTR_SET_ARM = LITE_STUDIO_ARM_TARGET + ".instructionset"; //$NON-NLS-1$
	static public final String LITE_STUDIO_LINKER_SCRIPT_OPTION = LITE_STUDIO_OPTION + ".base.linker.scriptfile"; //$NON-NLS-1$
	static public final String LITE_STUDIO_CPU_VALUE_PREFIX = LITE_STUDIO_ARM_TARGET + ".mcpu."; //$NON-NLS-1$
	static public final String LITE_STUDIO_FPU_VALUE_PREFIX = LITE_STUDIO_ARM_TARGET + ".fpu.unit."; //$NON-NLS-1$
	static public final String LITE_STUDIO_FPU_ABI_VALUE_PREFIX = LITE_STUDIO_ARM_TARGET + ".fpu.abi."; //$NON-NLS-1$
	static public final String LITE_STUDIO_ENDIAN_VALUE_PREFIX = LITE_STUDIO_ARM_TARGET + ".endianness."; //$NON-NLS-1$
	static public final String LITE_STUDIO_INSTR_SET_VALUE_PREFIX = LITE_STUDIO_ARM_TARGET + ".instructionset."; //$NON-NLS-1$
	static public final String LITE_STUDIO_CMISC_OPTION = LITE_STUDIO_OPTION + ".base.compiler.other"; //$NON-NLS-1$
	static public final String LITE_STUDIO_AMISC_OPTION = LITE_STUDIO_OPTION + ".assembler.other"; //$NON-NLS-1$
	static public final String LITE_STUDIO_LMISC_OPTION = LITE_STUDIO_OPTION + ".base.linker.other"; //$NON-NLS-1$

	public static final String ARCHITECTURE_AARCH64 = "aarch64"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET = LITE_STUDIO_OPTION + "." + ARCHITECTURE_AARCH64 + ".target."; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_FAMILY = LITE_STUDIO_OPTION_AARCH64_TARGET + "family"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_FEATURE_CRC = LITE_STUDIO_OPTION_AARCH64_TARGET + "feature.crc"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_FEATURE_CRYPTO = LITE_STUDIO_OPTION_AARCH64_TARGET + "feature.crypto"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_FEATURE_FP = LITE_STUDIO_OPTION_AARCH64_TARGET + "feature.fp"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_FEATURE_SIMD = LITE_STUDIO_OPTION_AARCH64_TARGET + "feature.simd"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_CMODEL = LITE_STUDIO_OPTION_AARCH64_TARGET + "cmodel"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_TARGET_STRICTALIGN = LITE_STUDIO_OPTION_AARCH64_TARGET + "strictalign"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_AARCH64_MCPU_GENERIC = LITE_STUDIO_OPTION + ".aarch64.target.mcpu.generic"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_FEATURE_SIMD = LITE_STUDIO_OPTION + ".aarch64.target.feature.simd"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_FEATURE_SIMD_ENABLED = LITE_STUDIO_OPTION_AARCH64_FEATURE_SIMD + ".enabled"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_AARCH64_CMODEL = LITE_STUDIO_OPTION + ".aarch64.target.cmodel"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_AARCH64_CMODEL_SMALL = LITE_STUDIO_OPTION_AARCH64_CMODEL + ".small"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_COMMAND = LITE_STUDIO_OPTION + ".command."; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_PREFIX = LITE_STUDIO_OPTION_COMMAND + "prefix"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_SUFFIX = LITE_STUDIO_OPTION_COMMAND + "suffix"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_C = LITE_STUDIO_OPTION_COMMAND + "c"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_CPP = LITE_STUDIO_OPTION_COMMAND + "cpp"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_AR = LITE_STUDIO_OPTION_COMMAND + "ar"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_OBJCOPY = LITE_STUDIO_OPTION_COMMAND + "objcopy"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_OBJDUMP = LITE_STUDIO_OPTION_COMMAND + "objdump"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_SIZE = LITE_STUDIO_OPTION_COMMAND + "size"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_MAKE = LITE_STUDIO_OPTION_COMMAND + "make"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_COMMAND_RM = LITE_STUDIO_OPTION_COMMAND + "rm"; // $NON-NLS-1$

	private static final String LITE_STUDIO_OPTION_COMMAND_READELF = LITE_STUDIO_OPTION_COMMAND + "readelf"; // $NON-NLS-1$
	private static final String LITE_STUDIO_OPTION_COMMAND_NM = LITE_STUDIO_OPTION_COMMAND + "nm"; // $NON-NLS-1$
	private static final String LITE_STUDIO_OPTION_COMMAND_ADDR2LINE = LITE_STUDIO_OPTION_COMMAND + "addr2line"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_ADDTOOLS = LITE_STUDIO_OPTION + ".addtools."; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_ADDTOOLS_CREATEFLASH = LITE_STUDIO_OPTION_ADDTOOLS + "createflash"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_ADDTOOLS_CREATELISTING = LITE_STUDIO_OPTION_ADDTOOLS + "createlisting"; // $NON-NLS-1$
	public static final String LITE_STUDIO_OPTION_ADDTOOLS_PRINTSIZE = LITE_STUDIO_OPTION_ADDTOOLS + "printsize"; // $NON-NLS-1$

	public static final String LITE_STUDIO_OPTION_CREATEFLASH_CHOICE = LITE_STUDIO_OPTION_ADDTOOLS + ".createflash.choice"; // $NON-NLS-1$

	// These should be in sync with plugin.xml definitions
	public static final boolean LITE_STUDIO_OPTION_ADDTOOLS_CREATEFLASH_DEFAULT = true;
	public static final boolean LITE_STUDIO_OPTION_ADDTOOLS_CREATELISTING_DEFAULT = false;
	public static final boolean LITE_STUDIO_OPTION_ADDTOOLS_PRINTSIZE_DEFAULT = true;

	private static int toolchain_index = 0;


	@Override
	protected int getLiteOptionType(String id) {
		switch (id) {
		case LITE_STUDIO_CPU_OPTION:
			return IBuildSettings.CPU_OPTION;
		case LITE_STUDIO_FPU_OPTION:
			return IBuildSettings.FPU_OPTION;
		case LITE_STUDIO_FPU_ABI_OPTION:
			return IBuildSettings.FLOAT_ABI_OPTION;
		case LITE_STUDIO_ENDIAN_OPTION:
			return IBuildSettings.ENDIAN_OPTION;
		case LITE_STUDIO_INSTR_SET_ARM:
			return IBuildSettings.INSTR_SET_OPTION;
		case LITE_STUDIO_LINKER_SCRIPT_OPTION:
			return IBuildSettings.LITE_LINKER_SCRIPT;
		// misc options are here for completeness
		case LITE_STUDIO_CMISC_OPTION:
			return IBuildSettings.CMISC_OPTION;
		case LITE_STUDIO_AMISC_OPTION:
			return IBuildSettings.AMISC_OPTION;
		case LITE_STUDIO_LMISC_OPTION:
			return IBuildSettings.LMISC_OPTION;

		default:
			break;
		}
		return IBuildSettings.UNKNOWN_OPTION;
	}

	@Override
	protected String getLiteOptionValue(int oType, IBuildSettings buildSettings, IOption option) {
		switch (oType) {
		case IBuildSettings.CPU_OPTION:
			return getCpuOptionValue(buildSettings);
		case IBuildSettings.INSTR_SET_OPTION:
			return LITE_STUDIO_INSTR_SET_VALUE_PREFIX + "thumb"; //$NON-NLS-1$
		case IBuildSettings.ENDIAN_OPTION:
			return getEndianOptionValue(buildSettings);
		case IBuildSettings.FPU_OPTION:
			return getFpuOptionValue(buildSettings);
		case IBuildSettings.FLOAT_ABI_OPTION:
			return getFloatAbiOptionValue(buildSettings);
		case IBuildSettings.LITE_LINKER_SCRIPT:
			return null; // reported via getStringListValue()
		default:
			break;

		}
		return super.getLiteOptionValue(oType, buildSettings, option);
	}

	@Override
	protected Collection<String> getStringListValue(IBuildSettings buildSettings, int type) {
		if (buildSettings == null) {
			return null;
		}

		if (type == IBuildSettings.LITE_LIBRARIES || type == IBuildSettings.LITE_LIBRARY_PATHS) {
			return null;
			// we add libraries as objects => ignore libs and lib paths
		} else if (type == IBuildSettings.LITE_OBJECTS) {
			Collection<String> objs = buildSettings.getStringListValue(IBuildSettings.LITE_OBJECTS);
			List<String> value = new LinkedList<String>();
			if (objs != null && !objs.isEmpty()) {
				value.addAll(objs);
			}
			// add libraries as objects (GCC does not allow to specify libs with
			// absolute paths)
			Collection<String> libs = buildSettings.getStringListValue(IBuildSettings.LITE_LIBRARIES);
			if (libs != null && !libs.isEmpty()) {
				value.addAll(libs);
			}
			return value;
		}
		Collection<String> list = buildSettings.getStringListValue(type);

		return list;
	}

	/**
	 * Returns value for CPU_OPTION
	 *
	 * @param buildSettings
	 *            settings to get required information from
	 * @return CPU_OPTION value string
	 */
	protected String getCpuOptionValue(IBuildSettings buildSettings) {
		String cpu = getDeviceAttribute(IBuildSettings.CPU_OPTION, buildSettings);
		int pos = cpu.indexOf('+');
		if (pos > 0) {
			// Cortex-M0+ -> Cortex-M0plus
			cpu = cpu.substring(0, pos);
			cpu += "plus"; //$NON-NLS-1$
		}
		return LITE_STUDIO_CPU_VALUE_PREFIX + cpu.toLowerCase();
	}

	/**
	 * Returns enum value for FPU_OPTION
	 *
	 * @param buildSettings
	 *            settings to get required information from
	 * @return FPU_OPTION value string
	 */
	public String getFpuOptionValue(IBuildSettings buildSettings) {
		String cpu = getDeviceAttribute(IBuildSettings.CPU_OPTION, buildSettings);
		String fpu = getDeviceAttribute(IBuildSettings.FPU_OPTION, buildSettings);
		String val = "default"; //$NON-NLS-1$
		if (cpu == null || fpu == null || fpu.equals(CmsisConstants.NO_FPU) || !coreHasFpu(cpu)) {
			// default
		} else {
			if (cpu.equals("Cortex-M7")) { //$NON-NLS-1$
				if (fpu.equals(CmsisConstants.SP_FPU)) {
					val = "fpv5spd16"; //$NON-NLS-1$
				}
				if (fpu.equals(CmsisConstants.DP_FPU)) {
					val = "fpv5d16"; //$NON-NLS-1$
				}
			} else if (fpu.equals(CmsisConstants.SP_FPU)) {
				val = "fpv4spd16"; //$NON-NLS-1$
			}
		}
		return LITE_STUDIO_FPU_VALUE_PREFIX + val;
	}

	/**
	 * Returns enum value for FLOAT_ABI_OPTION
	 *
	 * @param buildSettings
	 *            settings to get required information from
	 * @return FPU_OPTION value string
	 */
	public String getFloatAbiOptionValue(IBuildSettings buildSettings) {
		String cpu = getDeviceAttribute(IBuildSettings.CPU_OPTION, buildSettings);
		String fpu = getDeviceAttribute(IBuildSettings.FPU_OPTION, buildSettings);
		String val;
		if (cpu == null || fpu == null || fpu.equals(CmsisConstants.NO_FPU) || !coreHasFpu(cpu)) {
			val = "default"; //$NON-NLS-1$
		} else {
			val = "hard"; //$NON-NLS-1$
		}
		return LITE_STUDIO_FPU_ABI_VALUE_PREFIX + val;
	}

	protected String getEndianOptionValue(IBuildSettings buildSettings) {
		String endian = getDeviceAttribute(IBuildSettings.ENDIAN_OPTION, buildSettings);
		String val;
		if (endian == null || endian.isEmpty()) {
			val = "default"; //$NON-NLS-1$
		} else if (endian.equals(CmsisConstants.LITTLENDIAN)) {
			val = "little"; //$NON-NLS-1$
		} else if (endian.equals(CmsisConstants.BIGENDIAN)) {
			val = "big"; //$NON-NLS-1$
		} else {
			val = "default"; //$NON-NLS-1$
		}
		return LITE_STUDIO_ENDIAN_VALUE_PREFIX + val;
	}

	@Override
	public void setUpToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings) {

		if (configuration == null || buildSettings == null) {
			return;
		}

		IToolChain toolchain = configuration.getToolChain();
		if (toolchain == null) {
			return;
		}
		// iterate over toolchain options
		try {
			setOptionsForToolchain(configuration, toolchain, buildSettings);
		} catch (BuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setOptionsForToolchain(IConfiguration config, IToolChain toolchain, IBuildSettings buildSettings) throws BuildException {

		IOption option;
		String val;
		String name = toolchain.getName();
		log(name);

		ToolchainDefinition td = ToolchainDefinition.getToolchain(toolchain_index);

		// Do NOT use ManagedBuildManager.setOption() to avoid sending
		// events to the option. Also do not use option.setValue()
		// since this does not propagate notifications and the
		// values are not saved to .cproject.
		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_TOOLCHAIN_NAME); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getName());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_ARCHITECTURE); // $NON-NLS-1$
		// compose the architecture ID
		String sArchitecture = td.getArchitecture();
		val = LITE_STUDIO_OPTION_ARCHITECTURE + "." + sArchitecture;
		Utils.setOptionForced(config, toolchain, option, val);

		if ("arm".equals(sArchitecture)) {
			option = toolchain.getOptionBySuperClassId(LITE_STUDIO_CPU_OPTION);
			Utils.forceOptionRewrite(config, toolchain, option);

			option = toolchain.getOptionBySuperClassId(LITE_STUDIO_INSTR_SET_ARM);
			Utils.forceOptionRewrite(config, toolchain, option);
		} else if ("aarch64".equals(sArchitecture)) {
			option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_AARCH64_TARGET_FAMILY);
			Utils.setOptionForced(config, toolchain, option, LITE_STUDIO_OPTION_AARCH64_MCPU_GENERIC);

			option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_AARCH64_FEATURE_SIMD);
			Utils.setOptionForced(config, toolchain, option, LITE_STUDIO_OPTION_AARCH64_FEATURE_SIMD_ENABLED);

			option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_AARCH64_CMODEL);
			Utils.setOptionForced(config, toolchain, option, LITE_STUDIO_OPTION_AARCH64_CMODEL_SMALL);
		}

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_PREFIX); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getPrefix());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_SUFFIX); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getSuffix());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_C); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdC());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_CPP); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdCpp());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_AR); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdAr());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_OBJCOPY); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdObjcopy());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_OBJDUMP); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdObjdump());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_SIZE); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdSize());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_MAKE); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdMake());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_RM); // $NON-NLS-1$
		config.setOption(toolchain, option, td.getCmdRm());

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_ADDTOOLS_CREATEFLASH); // $NON-NLS-1$
		config.setOption(toolchain, option, LITE_STUDIO_OPTION_ADDTOOLS_CREATEFLASH_DEFAULT);

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_ADDTOOLS_CREATELISTING); // $NON-NLS-1$
		config.setOption(toolchain, option, LITE_STUDIO_OPTION_ADDTOOLS_CREATELISTING_DEFAULT);

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_ADDTOOLS_PRINTSIZE); // $NON-NLS-1$
		config.setOption(toolchain, option, LITE_STUDIO_OPTION_ADDTOOLS_PRINTSIZE_DEFAULT);

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_READELF); // $NON-NLS-1$
		if (option != null) {
			config.setOption(toolchain, option, td.getCmdReadElf());
		}

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_NM); // $NON-NLS-1$
		if (option != null) {
			config.setOption(toolchain, option, td.getCmdNM());
		}

		option = toolchain.getOptionBySuperClassId(LITE_STUDIO_OPTION_COMMAND_ADDR2LINE); // $NON-NLS-1$
		if (option != null) {
			config.setOption(toolchain, option, td.getCmdAddr2line());
		}

		// do not set the project toolchain path
		log(Platform.getInstallLocation().getURL().toString());
	}

	private static void log(String msg) {
		//System.out.println("<<<< " + msg + "\n");
	}

	@Override
	public void setToolChainIndex(int i) {
		if (i >= 0) {
			toolchain_index = i;
		}
	}

	@Override
	public ILinkerScriptGenerator getLinkerScriptGenerator() {
		return new LiteLinkerScriptGenerator();
	}

}
