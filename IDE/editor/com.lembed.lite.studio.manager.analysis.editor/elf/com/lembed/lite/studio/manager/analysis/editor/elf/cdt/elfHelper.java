package com.lembed.lite.studio.manager.analysis.editor.elf.cdt;

import java.io.IOException;

import org.eclipse.cdt.utils.elf.ElfHelper;

public class elfHelper extends ElfHelper {
	static String name = null;

	public elfHelper(String filename) throws IOException {
		super(filename);
		name = filename;
	}


	@Override
	public elf.Symbol[] getExternalFunctions() throws IOException {
		
		return super.getExternalFunctions();
	}
}
