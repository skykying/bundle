/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.managedbuild.cross.llvm;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;


@SuppressWarnings("javadoc")
public class ClangOption {
 
	/** clang options  ---> https://gi?st.github.com/masuidrive/5231110 */

	public static final String TOOLCHAIN_PREFIX = "com.lembed.lite.studio.managedbuild.cross.llvm.toolchain"; //$NON-NLS-1$

	public static final String OPTION_PREFIX = LlvmUIPlugin.PLUGIN_ID + ".option"; //$NON-NLS-1$

	public static final String OPTION_ARCHITECTURE = OPTION_PREFIX + ".architecture"; //$NON-NLS-1$

	public static final String ARCHITECTURE_ARM = "arm"; //$NON-NLS-1$
	public static final String ARCHITECTURE_AARCH64 = "aarch64"; //$NON-NLS-1$

	public static final String OPTION_ARCHITECTURE_ARM = OPTION_ARCHITECTURE + "." + ARCHITECTURE_ARM; //$NON-NLS-1$
	public static final String OPTION_ARCHITECTURE_AARCH64 = OPTION_ARCHITECTURE + "." + ARCHITECTURE_AARCH64; //$NON-NLS-1$

	public static final String OPTION_TARGET = OPTION_PREFIX + ".target."; //$NON-NLS-1$

	public static final String OPTION_ARM_TARGET = OPTION_PREFIX + "." + ARCHITECTURE_ARM + ".target."; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String OPTION_ARM_TARGET_FAMILY = OPTION_ARM_TARGET + "family"; //$NON-NLS-1$

	public static final String OPTION_ARM_TARGET_ARCHITECTURE = OPTION_ARM_TARGET + "architecture"; //$NON-NLS-1$
	public static final String OPTION_ARM_TARGET_INSTRUCTIONSET = OPTION_ARM_TARGET + "instructionset"; //$NON-NLS-1$

	public static final String OPTION_ARM_TARGET_THUMB_INTERWORK = OPTION_ARM_TARGET + "thumbinterwork"; //$NON-NLS-1$
	public static final String OPTION_ARM_TARGET_ENDIANNESS = OPTION_ARM_TARGET + "endianness"; //$NON-NLS-1$
	public static final String OPTION_ARM_TARGET_FLOAT_ABI = OPTION_ARM_TARGET + "fpu.abi"; //$NON-NLS-1$
	public static final String OPTION_ARM_TARGET_FLOAT_UNIT = OPTION_ARM_TARGET + "fpu.unit"; //$NON-NLS-1$
	public static final String OPTION_ARM_TARGET_UNALIGNEDACCESS = OPTION_ARM_TARGET + "unalignedaccess"; //$NON-NLS-1$

	public static final String OPTION_AARCH64_TARGET = OPTION_PREFIX + "." + ARCHITECTURE_AARCH64 + ".target."; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String OPTION_AARCH64_TARGET_FAMILY = OPTION_AARCH64_TARGET + "family"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_FEATURE_CRC = OPTION_AARCH64_TARGET + "feature.crc"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_FEATURE_CRYPTO = OPTION_AARCH64_TARGET + "feature.crypto"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_FEATURE_FP = OPTION_AARCH64_TARGET + "feature.fp"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_FEATURE_SIMD = OPTION_AARCH64_TARGET + "feature.simd"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_CMODEL = OPTION_AARCH64_TARGET + "cmodel"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_TARGET_STRICTALIGN = OPTION_AARCH64_TARGET + "strictalign"; //$NON-NLS-1$

	public static final String OPTION_TARGET_OTHER = OPTION_TARGET + "other"; //$NON-NLS-1$

	public static final String OPTION_OPTIMIZATION = OPTION_PREFIX + ".optimization."; //$NON-NLS-1$

	public static final String OPTION_OPTIMIZATION_LEVEL = OPTION_OPTIMIZATION + "level"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_MESSAGELENGTH = OPTION_OPTIMIZATION + "messagelength"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_SIGNEDCHAR = OPTION_OPTIMIZATION + "signedchar"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_FUNCTIONSECTIONS = OPTION_OPTIMIZATION + "functionsections"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_DATASECTIONS = OPTION_OPTIMIZATION + "datasections"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_NOCOMMON = OPTION_OPTIMIZATION + "nocommon"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_NOINLINEFUNCTIONS = OPTION_OPTIMIZATION + "noinlinefunctions"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_FREESTANDING = OPTION_OPTIMIZATION + "freestanding"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_NOBUILTIN = OPTION_OPTIMIZATION + "nobuiltin"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_SPCONSTANT = OPTION_OPTIMIZATION + "spconstant"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_PIC = OPTION_OPTIMIZATION + "PIC"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_LTO = OPTION_OPTIMIZATION + "lto"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_NOMOVELOOPINVARIANTS = OPTION_OPTIMIZATION + "nomoveloopinvariants"; //$NON-NLS-1$
	public static final String OPTION_OPTIMIZATION_OTHER = OPTION_OPTIMIZATION + "other"; //$NON-NLS-1$

	public static final String OPTION_WARNINGS = OPTION_PREFIX + ".warnings."; //$NON-NLS-1$

	public static final String OPTION_WARNINGS_SYNTAXONLY = OPTION_WARNINGS + "syntaxonly"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_PEDANTIC = OPTION_WARNINGS + "pedantic"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_PEDANTICERRORS = OPTION_WARNINGS + "pedanticerrors"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_ALLWARN = OPTION_WARNINGS + "allwarn"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_NOWARN = OPTION_WARNINGS + "nowarn"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_EXTRAWARN = OPTION_WARNINGS + "extrawarn"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_CONVERSION = OPTION_WARNINGS + "conversion"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_UNINITIALIZED = OPTION_WARNINGS + "uninitialized"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_UNUSED = OPTION_WARNINGS + "unused"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_PADDED = OPTION_WARNINGS + "padded"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_FLOATEQUAL = OPTION_WARNINGS + "floatequal"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_SHADOW = OPTION_WARNINGS + "shadow"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_POINTERARITH = OPTION_WARNINGS + "pointerarith"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_LOGICALOP = OPTION_WARNINGS + "logicalop"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_AGREGGATERETURN = OPTION_WARNINGS + "agreggatereturn"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_MISSINGDECLARATION = OPTION_WARNINGS + "missingdeclaration"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_TOERRORS = OPTION_WARNINGS + "toerrors"; //$NON-NLS-1$
	public static final String OPTION_WARNINGS_OTHER = OPTION_WARNINGS + "other"; //$NON-NLS-1$

	public static final String OPTION_DEBUGGING = OPTION_PREFIX + ".debugging."; //$NON-NLS-1$

	public static final String OPTION_DEBUGGING_LEVEL = OPTION_DEBUGGING + "level"; //$NON-NLS-1$
	public static final String OPTION_DEBUGGING_FORMAT = OPTION_DEBUGGING + "format"; //$NON-NLS-1$
	public static final String OPTION_DEBUGGING_PROF = OPTION_DEBUGGING + "prof"; //$NON-NLS-1$
	public static final String OPTION_DEBUGGING_GPROF = OPTION_DEBUGGING + "gprof"; //$NON-NLS-1$
	public static final String OPTION_DEBUGGING_OTHER = OPTION_DEBUGGING + "other"; //$NON-NLS-1$

	// other
	public static final String OPTION_TOOLCHAIN_NAME = OPTION_PREFIX + ".toolchain.name"; //$NON-NLS-1$

	// public static final String OPTION_TOOLCHAIN_PATH = OPTION_PREFIX
	// + ".toolchain.path";
	// public static final String OPTION_TOOLCHAIN_USE_GLOBAL_PATH =
	// OPTION_PREFIX
	// + ".toolchain.useglobalpath";
	// public static final boolean OPTION_TOOLCHAIN_USE_GLOBAL_PATH_DEFAULT =
	// true;

	public static final String OPTION_COMMAND = OPTION_PREFIX + ".command."; //$NON-NLS-1$
	public static final String OPTION_COMMAND_ADDR2LINE = OPTION_COMMAND + "addr2line"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_PREFIX = OPTION_COMMAND + "prefix"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_SUFFIX = OPTION_COMMAND + "suffix"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_C = OPTION_COMMAND + "c"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_CPP = OPTION_COMMAND + "cpp"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_AR = OPTION_COMMAND + "ar"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_NM = OPTION_COMMAND + "nm"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_READELF = OPTION_COMMAND + "readelf"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_ELFEDIT = OPTION_COMMAND + "elfedit"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_OBJCOPY = OPTION_COMMAND + "objcopy"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_OBJDUMP = OPTION_COMMAND + "objdump"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_SIZE = OPTION_COMMAND + "size"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_MAKE = OPTION_COMMAND + "make"; //$NON-NLS-1$
	public static final String OPTION_COMMAND_RM = OPTION_COMMAND + "rm"; //$NON-NLS-1$

	public static final String OPTION_ADDTOOLS = OPTION_PREFIX + ".addtools."; //$NON-NLS-1$
	public static final String OPTION_ADDTOOLS_CREATEFLASH = OPTION_ADDTOOLS + "createflash"; //$NON-NLS-1$
	public static final String OPTION_ADDTOOLS_CREATELISTING = OPTION_ADDTOOLS + "createlisting"; //$NON-NLS-1$
	public static final String OPTION_ADDTOOLS_PRINTSIZE = OPTION_ADDTOOLS + "printsize"; //$NON-NLS-1$
	public static final String OPTION_CREATEFLASH_CHOICE = OPTION_PREFIX + ".createflash.choice"; //$NON-NLS-1$

	// These should be in sync with plugin.xml definitions
	public static final boolean OPTION_ADDTOOLS_CREATEFLASH_DEFAULT = true;
	public static final boolean OPTION_ADDTOOLS_CREATELISTING_DEFAULT = false;
	public static final boolean OPTION_ADDTOOLS_PRINTSIZE_DEFAULT = true;

	public static final String CHOICE_IHEX = "ihex"; //$NON-NLS-1$
	public static final String CHOICE_SREC = "srec"; //$NON-NLS-1$
	public static final String CHOICE_SYMBOLSREC = "symbolsrec"; //$NON-NLS-1$
	public static final String CHOICE_BINARY = "binary"; //$NON-NLS-1$

	// ARM enumeration values
	public static final String OPTION_ARM_MCPU_CORTEXM3 = OPTION_PREFIX + ".arm.target.mcpu.cortex-m3"; //$NON-NLS-1$
	public static final String OPTION_ARM_MCPU_CORTEXM4 = OPTION_PREFIX + ".arm.target.mcpu.cortex-m4"; //$NON-NLS-1$
	public static final String OPTION_ARM_INSTRUCTIONSET_THUMB = OPTION_PREFIX + ".arm.target.instructionset.thumb"; //$NON-NLS-1$
	public static final String OPTION_ARM_FPU_ABI_HARD = OPTION_PREFIX + ".arm.target.fpu.abi.hard"; //$NON-NLS-1$
	public static final String OPTION_ARM_FPU_ABI_SOFTFP = OPTION_PREFIX + ".arm.target.fpu.abi.softfp"; //$NON-NLS-1$
	public static final String OPTION_ARM_FPU_UNIT_DEFAULT = OPTION_PREFIX + ".arm.target.fpu.unit.default"; //$NON-NLS-1$
	public static final String OPTION_ARM_FPU_UNIT_FPV4SPD16 = OPTION_PREFIX + ".arm.target.fpu.unit.fpv4spd16"; //$NON-NLS-1$

	// AARCH64 enumeration values
	public static final String OPTION_AARCH64_MCPU_GENERIC = OPTION_PREFIX + ".aarch64.target.mcpu.generic"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_FEATURE_SIMD = OPTION_PREFIX + ".aarch64.target.feature.simd"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_FEATURE_SIMD_ENABLED = OPTION_AARCH64_FEATURE_SIMD + ".enabled"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_CMODEL = OPTION_PREFIX + ".aarch64.target.cmodel"; //$NON-NLS-1$
	public static final String OPTION_AARCH64_CMODEL_SMALL = OPTION_AARCH64_CMODEL + ".small"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	public static String getOptionStringValue(IConfiguration config, String sOptionId) {

		IOption option = config.getToolChain().getOptionBySuperClassId(sOptionId);
		String sReturn = null;
		if (option != null) {
			try {
				sReturn = option.getStringValue().trim();
			} catch (BuildException e) {
				LlvmUIPlugin.log(e);
			}
		} else {
			LlvmUIPlugin.log(sOptionId + " not found " + "getOptionStringValue"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return sReturn;
	}

	public static String getOptionEnumCommand(IConfiguration config, String sOptionId) {

		IOption option = config.getToolChain().getOptionBySuperClassId(sOptionId);
		String sReturn = null;
		if (option != null) {
			try {
				String sEnumId = option.getStringValue();
				sReturn = option.getEnumCommand(sEnumId).trim();
			} catch (BuildException e) {
				LlvmUIPlugin.log(e);
			}
		} else {
			LlvmUIPlugin.log(sOptionId + " not found " + "getOptionEnumCommand"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return sReturn;
	}

	public static Boolean getOptionBooleanValue(IConfiguration config, String sOptionId) {

		IOption option = config.getToolChain().getOptionBySuperClassId(sOptionId);
		Boolean bReturn = null;
		if (option != null) {
			try {
				bReturn =  Boolean.valueOf(option.getBooleanValue());
			} catch (BuildException e) {
				LlvmUIPlugin.log(e);
			}
		} else {
			LlvmUIPlugin.log(sOptionId + " not found " + "getOptionBooleanValue"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return bReturn;
	}

	public static Boolean getOptionBooleanValue2(IConfiguration config, String sOptionId) {

		IOption option = config.getToolChain().getOptionBySuperClassId(sOptionId);
		Boolean bReturn = null;
		if (option != null) {
			try {
				IOption option2 = config.getToolChain().getOptionToSet(option, false);
				if (option2 == null) {
					return null;
				}

				bReturn = Boolean.valueOf(option.getBooleanValue());
			} catch (BuildException e) {
				LlvmUIPlugin.log(e);
			}
		} else {
			LlvmUIPlugin.log(sOptionId + " not found "  + "getOptionBooleanValue2"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return bReturn;
	}

	public static String getOptionBooleanCommand(IConfiguration config, String sOptionId) {

		IOption option = config.getToolChain().getOptionBySuperClassId(sOptionId);
		String sReturn = null;
		if (option != null) {
			try {
				if (option.getBooleanValue()) {
					sReturn = option.getCommand().trim();
				}
			} catch (BuildException e) {
				LlvmUIPlugin.log(e);
			}
		} else {
			LlvmUIPlugin.log(sOptionId + " not found " + "getOptionBooleanCommand"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return sReturn;
	}

	private static String getArmTargetFlags(IConfiguration config) {

		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_FAMILY);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_ARCHITECTURE);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_INSTRUCTIONSET);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_ARM_TARGET_THUMB_INTERWORK);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_ENDIANNESS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_FLOAT_ABI);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$

			sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_FLOAT_UNIT);
			if (sValue != null && sValue.length() > 0) {
				sReturn += " " + sValue; //$NON-NLS-1$
			}
		}

		sValue = getOptionEnumCommand(config, OPTION_ARM_TARGET_UNALIGNEDACCESS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sReturn = sReturn.trim();

		return sReturn;
	}

	private static String getAarch64TargetFlags(IConfiguration config) {

		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_FAMILY);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$

			sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_FEATURE_CRC);
			if (sValue != null && sValue.length() > 0) {
				sReturn += "+" + sValue; //$NON-NLS-1$
			}

			sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_FEATURE_CRYPTO);
			if (sValue != null && sValue.length() > 0) {
				sReturn += "+" + sValue; //$NON-NLS-1$
			}

			sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_FEATURE_FP);
			if (sValue != null && sValue.length() > 0) {
				sReturn += "+" + sValue; //$NON-NLS-1$
			}

			sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_FEATURE_SIMD);
			if (sValue != null && sValue.length() > 0) {
				sReturn += "+" + sValue; //$NON-NLS-1$
			}

		}

		sValue = getOptionEnumCommand(config, OPTION_AARCH64_TARGET_CMODEL);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_AARCH64_TARGET_STRICTALIGN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}


		sReturn = sReturn.trim();

		return sReturn;
	}

	private static String getOptimizationFlags(IConfiguration config) {

		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		sReturn = sReturn.trim();

		sValue = getOptionEnumCommand(config, OPTION_OPTIMIZATION_LEVEL);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_MESSAGELENGTH);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_SIGNEDCHAR);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_FUNCTIONSECTIONS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_DATASECTIONS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_NOCOMMON);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_NOINLINEFUNCTIONS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_FREESTANDING);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_NOBUILTIN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_SPCONSTANT);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_PIC);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_LTO);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_OPTIMIZATION_NOMOVELOOPINVARIANTS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionStringValue(config, OPTION_OPTIMIZATION_OTHER);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		if (sReturn != null) {
			sReturn = sReturn.trim();
		}

		return sReturn;
	}

	private static String getWarningFlags(IConfiguration config) {

		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_SYNTAXONLY);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_PEDANTIC);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_PEDANTICERRORS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_NOWARN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_TOERRORS);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_UNUSED);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_UNINITIALIZED);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_ALLWARN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_EXTRAWARN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_MISSINGDECLARATION);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_CONVERSION);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_POINTERARITH);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_PADDED);
		if (sValue != null && sValue.length() > 0)
			sReturn += " " + sValue; //$NON-NLS-1$

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_SHADOW);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_LOGICALOP);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_AGREGGATERETURN);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_WARNINGS_FLOATEQUAL);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionStringValue(config, OPTION_WARNINGS_OTHER);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sReturn = sReturn.trim();

		return sReturn;
	}

	private static String getDebuggingFlags(IConfiguration config) {

		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		sReturn = sReturn.trim();

		sValue = getOptionEnumCommand(config, OPTION_DEBUGGING_LEVEL);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$

			sValue = getOptionEnumCommand(config, OPTION_DEBUGGING_FORMAT);
			if (sValue != null && sValue.length() > 0) {
				sReturn += " " + sValue; //$NON-NLS-1$
			}
		}

		sValue = getOptionStringValue(config, OPTION_DEBUGGING_OTHER);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_DEBUGGING_PROF);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptionBooleanCommand(config, OPTION_DEBUGGING_GPROF);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		return sReturn;
	}

	public static String getToolChainFlags(IConfiguration config) {
		String sArchitectureId = getOptionStringValue(config, OPTION_ARCHITECTURE);
		String sReturn = ""; //$NON-NLS-1$
		String sValue;

		if (sArchitectureId != null) {
			sValue = null;
			if (sArchitectureId.endsWith("." + ARCHITECTURE_ARM)) { //$NON-NLS-1$
				sValue = getArmTargetFlags(config);
			} else if (sArchitectureId.endsWith("." + ARCHITECTURE_AARCH64)) { //$NON-NLS-1$
				sValue = getAarch64TargetFlags(config);
			}

			if (sValue != null && sValue.length() > 0) {
				sReturn += " " + sValue; //$NON-NLS-1$
			}
		}

		sValue = getOptionStringValue(config, OPTION_TARGET_OTHER);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getOptimizationFlags(config);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getWarningFlags(config);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sValue = getDebuggingFlags(config);
		if (sValue != null && sValue.length() > 0) {
			sReturn += " " + sValue; //$NON-NLS-1$
		}

		sReturn = sReturn.trim();

		return sReturn;
	}

}
