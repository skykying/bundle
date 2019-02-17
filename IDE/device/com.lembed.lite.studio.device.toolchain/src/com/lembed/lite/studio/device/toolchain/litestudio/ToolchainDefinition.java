/*******************************************************************************
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.device.toolchain.litestudio;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;

import com.lembed.lite.studio.core.EclipseUtils;

public class ToolchainDefinition {

	// ------------------------------------------------------------------------

	public static final String LEMBED_TOOLCHAIN_NAME = "Lembed ARMv7 bare-metal EABI";
	public static final String DEFAULT_TOOLCHAIN_NAME = LEMBED_TOOLCHAIN_NAME;

	// ------------------------------------------------------------------------

	private String fName;
	private String fPrefix;
	private String fSuffix;
	private String fArchitecture;
	private String fCmdMake;
	private String fCmdRm;
	private String fCmdWinMake;
	private String fCmdWinRm;
	private String fCmdC;
	private String fCmdCpp;
	private String fCmdAr;
	private String fCmdObjcopy;
	private String fCmdObjdump;
	private String fCmdSize;
	private URL fDefaultPath;
	private String fCmdReadElf;
	private String fCmdNM;
	private String fCmdAddr2line;

	private static String fArchitectures[] = { "ARM (AArch32)", "ARM64 (AArch64)" };

	// ------------------------------------------------------------------------

	public ToolchainDefinition(String sName) {
		fName = sName;
		fPrefix = "";
		fSuffix = "";
		fArchitecture = "arm";
		fCmdMake = "make";
		fCmdRm = "rm";
		fCmdC = "gcc";
		fCmdCpp = "g++";
		fCmdAr = "ar";
		fCmdObjcopy = "objcopy";
		fCmdObjdump = "objdump";
		fCmdSize = "size";
		fCmdNM = "nm";
		fCmdReadElf = "readelf";
		fCmdAddr2line = "addr2line";
		fDefaultPath = Platform.getInstallLocation().getURL();
	}

	public ToolchainDefinition(String sName, String sPrefix) {
		this(sName);
		fPrefix = sPrefix;
	}

	public ToolchainDefinition(String sName, String sPrefix, String sArchitecture) {
		this(sName, sPrefix);
		fArchitecture = sArchitecture;
	}

	public ToolchainDefinition(String sName, String sPrefix, String sArchitecture, String cmdMake, String cmdRm) {
		this(sName, sPrefix, sArchitecture);
		fArchitecture = sArchitecture;
		fCmdMake = cmdMake;
		fCmdRm = cmdRm;
	}

	// ------------------------------------------------------------------------

	public void setWin(String cmdMake, String cmdRm) {
		fCmdMake = cmdMake;
		fCmdRm = cmdRm;
	}

	public String getName() {
		return fName;
	}

	public void setName(String name) {
		fName = name;
	}

	public String getPrefix() {
		return fPrefix;
	}

	public void setPrefix(String prefix) {
		fPrefix = prefix;
	}

	public String getSuffix() {
		return fSuffix;
	}

	public void setSuffix(String suffix) {
		fSuffix = suffix;
	}

	public String getArchitecture() {
		return fArchitecture;
	}

	public void setArchitecture(String architecture) {
		fArchitecture = architecture;
	}

	public String getCmdMake() {
		return fCmdMake;
	}
	
	public String getCmdReadElf(){
		return fCmdReadElf;
	}
	
	public void setCmdReadElf(String elf){
		fCmdReadElf = elf;
	}
	
	public String getCmdNM(){
		return fCmdNM;
	}
	
	public void setCmdNM(String nm){
		fCmdNM = nm;
	}
	
	public String getCmdAddr2line(){
		return fCmdAddr2line;
	}
	
	public void setCmdAddr2line(String addr2line){
		fCmdAddr2line = addr2line;
	}
	
	public void setCmdMake(String cmdMake) {
		fCmdMake = cmdMake;
	}

	public String getCmdRm() {
		return fCmdRm;
	}

	public void setCmdRm(String cmdRm) {
		fCmdRm = cmdRm;
	}

	public String getCmdWinMake() {
		return fCmdWinMake;
	}

	public void setCmdWinMake(String cmdWinMake) {
		fCmdWinMake = cmdWinMake;
	}

	public String getCmdWinRm() {
		return fCmdWinRm;
	}

	public void setCmdWinRm(String cmdWinRm) {
		fCmdWinRm = cmdWinRm;
	}

	public String getCmdC() {
		return fCmdC;
	}

	public void setCmdC(String cmdC) {
		fCmdC = cmdC;
	}

	public String getCmdCpp() {
		return fCmdCpp;
	}

	public void setCmdCpp(String cmdCpp) {
		fCmdCpp = cmdCpp;
	}

	public String getCmdAr() {
		return fCmdAr;
	}

	public void setCmdAr(String cmdAr) {
		fCmdAr = cmdAr;
	}

	public String getCmdObjcopy() {
		return fCmdObjcopy;
	}

	public void setCmdObjcopy(String cmdObjcopy) {
		fCmdObjcopy = cmdObjcopy;
	}

	public String getCmdObjdump() {
		return fCmdObjdump;
	}

	public void setCmdObjdump(String cmdObjdump) {
		fCmdObjdump = cmdObjdump;
	}

	public String getCmdSize() {
		return fCmdSize;
	}

	public void setCmdSize(String cmdSize) {
		fCmdSize = cmdSize;
	}

	public String getFullCmdC() {
		return getPrefix() + getCmdC() + getSuffix();
	}

	public String getFullName() {
		return getName() + " (" + getFullCmdC() + ")";
	}

	// Static members
	private static List<ToolchainDefinition> fgList;

	private static final String CUSTOM_TOOLCHAINS_EXT_POTNT_ID = "com.lembed.lite.studio.managedbuild.cross.toolchains";

	public static List<ToolchainDefinition> getList() {
		return fgList;
	}

	public static ToolchainDefinition getToolchain(int index) {
		return fgList.get(index);
	}

	public static ToolchainDefinition getToolchain(String index) {
		return fgList.get(Integer.parseInt(index));
	}

	public static int getSize() {
		return fgList.size();
	}

	/**
	 * Try to identify toolcahin by name. If not possible, throw
	 * IndexOutOfBoundsException().
	 * 
	 * @param sName
	 *            a string with the toolchain name.
	 * @return non-negative index.
	 */
	public static int findToolchainByName(String sName) {

		int i = 0;
		for (ToolchainDefinition td : fgList) {
			if (td.fName.equals(sName))
				return i;
			i++;
		}
		// not found
		throw new IndexOutOfBoundsException();
	}

	public static int findToolchainByFullName(String sName) {

		int i = 0;
		for (ToolchainDefinition td : fgList) {
			String sFullName = td.getFullName();
			if (sFullName.equals(sName))
				return i;
			i++;
		}
		// not found
		return getDefault();
	}

	public static int getDefault() {
		return 0;
	}

	public static String[] getArchitectures() {
		return fArchitectures;
	}

	public static String getArchitecture(int index) {
		return fArchitectures[index];
	}

	// Initialise the list of known toolchains
	static {
		fgList = new ArrayList<ToolchainDefinition>();

		// 0
		fgList.add(new ToolchainDefinition(LEMBED_TOOLCHAIN_NAME, "arm-none-eabi-"));
		// 1
		ToolchainDefinition tc;
		tc = new ToolchainDefinition("Sourcery CodeBench Lite for ARM EABI", "arm-none-eabi-");
		if (EclipseUtils.isWindows()) {
			tc.setWin("cs-make", "cs-rm");
		}
		// fgList.add(tc);

		// 2
		tc = new ToolchainDefinition("Sourcery CodeBench Lite for ARM GNU/Linux", "arm-none-linux-gnueabi-");
		if (EclipseUtils.isWindows()) {
			tc.setWin("cs-make", "cs-rm");
		}
		// fgList.add(tc);

		// 3
		// fgList.add(new ToolchainDefinition("devkitPro ARM EABI",
		// "arm-eabi-"));

		// 4
		// fgList.add(new ToolchainDefinition("Yagarto, Summon, etc. ARM
		// EABI","arm-none-eabi-"));

		// 5
		fgList.add(new ToolchainDefinition("Linaro ARMv7 bare-metal EABI", "arm-none-eabi-"));

		// 6
		fgList.add(new ToolchainDefinition("Linaro ARMv7 big-endian bare-metal EABI", "armeb-none-eabi-"));

		// 7
		fgList.add(new ToolchainDefinition("Linaro ARMv7 Linux GNU EABI HF", "arm-linux-gnueabihf-"));

		// 8
		fgList.add(new ToolchainDefinition("Linaro ARMv7 big-endian Linux GNU EABI HF", "armeb-linux-gnueabihf-"));

		// 64 bit toolchains
		// 9
		// fgList.add(new ToolchainDefinition("Linaro AArch64 bare-metal ELF",
		// "aarch64-elf-", "aarch64"));

		// 10
		// fgList.add(new ToolchainDefinition("Linaro AArch64 big-endian
		// bare-metal ELF","aarch64_be-elf-", "aarch64"));

		// 11
		// fgList.add(new ToolchainDefinition("Linaro AArch64 Linux
		// GNU","aarch64-linux-gnu-", "aarch64"));

		// 12
		// fgList.add(new ToolchainDefinition("Linaro AArch64 big-endian Linux
		// GNU", "aarch64_be-linux-gnu-","aarch64"));

		// 13 - Moved to extension point
		fgList.add(new ToolchainDefinition("Custom", "arm-none-eabi-"));
	}

	// ------------------------------------------------------------------------
}
