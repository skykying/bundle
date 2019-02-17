package com.lembed.lite.studio.device.toolchain.litestudio;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.build.IMemorySettings;
import com.lembed.lite.studio.device.core.data.ICpMemory;
import com.lembed.lite.studio.device.toolchain.ILinkerScriptGenerator;
import com.lembed.lite.studio.device.toolchain.Messages;

public class LiteLinkerScriptGenerator implements ILinkerScriptGenerator {

	@Override
	public String generate(IMemorySettings memorySettings) throws CoreException {
		if(memorySettings != null){
			return getMemoryBankInfo(memorySettings);
		}
		return null;
	}

	@Override
	public String getFileExtension() {
		String ext = "ld"; 
		
		return ext;
	}

	
	
	public static String getMemoryBankInfo(IMemorySettings memsetting) {
		
		Map<String, ICpMemory> map = memsetting.getRegions();
		String bundleScript = "";		
		int romIndex = 0;
		int ramIndex = 0;
		
		for (String name : map.keySet()) {

			ICpMemory m = map.get(name);
			long size = m.attributes().getAttributeAsLong(CmsisConstants.SIZE, 0);
			long offset = m.attributes().getAttributeAsLong(CmsisConstants.START, 0);
			String bankName = "  "; //$NON-NLS-1$
			
			if(m.isRAM()){
				ramIndex = ramIndex +1;
				if(ramIndex == 2){
					//the second ram and "ramIndex = 2"
					bankName = bankName + "CCMRAM"+"(rxw)"; //$NON-NLS-1$
				}else{
					//just one ram and "ramIndex =1"
					bankName = bankName + "RAM"+"(rxw)"; //$NON-NLS-1$
				}
			}
			

			if(m.isROM()){
				romIndex = romIndex +1;
				if(romIndex == 2){
					// the second rom and "romIndex = 2"
					bankName = bankName + "FLASHB1" + "(rx)"; //$NON-NLS-1$
				}else{
					// just one rom and one loop "romIndex = 1"
					bankName = bankName + "FLASH" + "(rx)"; //$NON-NLS-1$
				}
			}
			
			String script = NLS.bind(Messages.LiteTemplateMemory_ScriptTemplate, 
				"0x" + Long.toHexString(offset),"0x" +Long.toHexString(size)); //$NON-NLS-1$ //$NON-NLS-2$
			script =  bankName + script + "\n" ;			
			bundleScript = bundleScript + script;
		}

		// just one ram bank, here need more blank memory bank setting to fiter linker script
		if(ramIndex == 1){
			String ascript = NLS.bind(Messages.LiteTemplateMemory_ScriptTemplate, "0x00","0x00"); //$NON-NLS-1$ //$NON-NLS-2$
			ascript = "  CCMRAM(rxw)" + ascript + "\n"; //$NON-NLS-1$
			bundleScript = bundleScript + ascript;
		}
		
		// just one rom bank, here need more blank memory bank setting to fiter linker script
		if(romIndex == 1){
			String oscript = NLS.bind(Messages.LiteTemplateMemory_ScriptTemplate, "0x00","0x00"); //$NON-NLS-1$ //$NON-NLS-2$
			oscript = "  FLASHB1(rx)" + oscript + "\n"; //$NON-NLS-1$
			bundleScript = bundleScript + oscript;
		}
		
		return bundleScript;
	}
}
